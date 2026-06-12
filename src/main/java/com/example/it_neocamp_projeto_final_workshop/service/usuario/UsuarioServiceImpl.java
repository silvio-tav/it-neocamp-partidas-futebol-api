package com.example.it_neocamp_projeto_final_workshop.service.usuario;

import com.example.it_neocamp_projeto_final_workshop.dto.auth.RegisterRequest;
import com.example.it_neocamp_projeto_final_workshop.exception.UsuarioJaExisteException;
import com.example.it_neocamp_projeto_final_workshop.model.Role;
import com.example.it_neocamp_projeto_final_workshop.model.Usuario;
import com.example.it_neocamp_projeto_final_workshop.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario cadastrarUsuario(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new UsuarioJaExisteException();
        }

        Usuario usuario = Usuario.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}
