package com.example.it_neocamp_projeto_final_workshop.controller;

import com.example.it_neocamp_projeto_final_workshop.dto.auth.AuthResponse;
import com.example.it_neocamp_projeto_final_workshop.dto.auth.LoginRequest;
import com.example.it_neocamp_projeto_final_workshop.dto.auth.RegisterRequest;
import com.example.it_neocamp_projeto_final_workshop.model.Usuario;
import com.example.it_neocamp_projeto_final_workshop.security.JwtService;
import com.example.it_neocamp_projeto_final_workshop.service.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Registro e login de usuários")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @Operation(
        summary = "Registrar novo usuário",
        description = "Cria um novo usuário e retorna o token JWT para uso imediato.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso",
                content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (campo em branco ou senha com menos de 6 caracteres)",
                content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail"))),
            @ApiResponse(responseCode = "409", description = "Username já cadastrado",
                content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail")))
        }
    )
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        Usuario usuario = usuarioService.cadastrarUsuario(request);
        String token = jwtService.generateToken(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token));
    }

    @PostMapping("/login")
    @Operation(
        summary = "Autenticar usuário",
        description = "Valida as credenciais e retorna o token JWT a ser enviado no header `Authorization: Bearer <token>`.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (campo em branco)",
                content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail"))),
            @ApiResponse(responseCode = "401", description = "Usuário ou senha inválidos",
                content = @Content(schema = @Schema(ref = "#/components/schemas/ProblemDetail")))
        }
    )
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        Usuario usuario = usuarioService.buscarPorUsername(request.username());
        String token = jwtService.generateToken(usuario);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
