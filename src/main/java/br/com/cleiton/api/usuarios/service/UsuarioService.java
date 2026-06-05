package br.com.cleiton.api.usuarios.service;

import br.com.cleiton.api.usuarios.dto.UsuarioCadastroRequest;
import br.com.cleiton.api.usuarios.dto.UsuarioResponse;
import br.com.cleiton.api.usuarios.exception.EmailDuplicadoException;
import br.com.cleiton.api.usuarios.model.Usuario;
import br.com.cleiton.api.usuarios.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder encoder) {
        this.usuarioRepository = usuarioRepository;
        this.encoder = encoder;
    }

    public UsuarioResponse cadastrar(UsuarioCadastroRequest usuarioCadastroRequest) {

        if (usuarioRepository.existsByEmail(usuarioCadastroRequest.email())) {
            throw new EmailDuplicadoException("Email já existe.");
        }

        Usuario usuario = usuarioRepository.save(
                new Usuario(
                        null,
                        usuarioCadastroRequest.email(),
                        encoder.encode(usuarioCadastroRequest.senha())
                ));

        return new UsuarioResponse(usuario.getId(), usuario.getEmail());
    }
}
