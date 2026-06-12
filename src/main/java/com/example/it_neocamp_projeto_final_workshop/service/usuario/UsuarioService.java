package com.example.it_neocamp_projeto_final_workshop.service.usuario;

import com.example.it_neocamp_projeto_final_workshop.dto.auth.RegisterRequest;
import com.example.it_neocamp_projeto_final_workshop.model.Usuario;

public interface UsuarioService {
    Usuario cadastrarUsuario(RegisterRequest request);

    Usuario buscarPorUsername(String username);
}
