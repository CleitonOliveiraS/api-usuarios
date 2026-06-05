package br.com.cleiton.api.usuarios.service;

import br.com.cleiton.api.usuarios.dto.UsuarioCadastroRequest;
import br.com.cleiton.api.usuarios.exception.EmailDuplicadoException;
import br.com.cleiton.api.usuarios.model.Usuario;
import br.com.cleiton.api.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Nested
    class Cadastrar {

        @Test
        @DisplayName("Cadastro com dados validos")
        void cadastrarUsuarioC1() {

            UsuarioCadastroRequest usuarioCadastroRequest = new UsuarioCadastroRequest("cleiton@email.com", "123456");

            when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(new Usuario(1L, "cleiton@email.com", encoder.encode("123456")));

            Usuario usuario = usuarioRepository.save(new Usuario(
                    null,
                    usuarioCadastroRequest.email(),
                    encoder.encode(usuarioCadastroRequest.senha())
            ));

            Assertions.assertNotNull(usuario);
            Assertions.assertEquals(1L, usuario.getId());
            Assertions.assertNotEquals(usuarioCadastroRequest.senha(), usuario.getSenha());
            Assertions.assertTrue(encoder.matches("123456", usuario.getSenha()));
        }

        @Test
        @DisplayName("Email ja cadastrado")
        void cadastrarUsuarioC2() {

            UsuarioCadastroRequest usuarioCadastro = new UsuarioCadastroRequest("cleiton@email.com", encoder.encode("123456"));

            when(usuarioRepository.existsByEmail(usuarioCadastro.email())).thenReturn(true);

            assertThrows(EmailDuplicadoException.class, () -> {
                usuarioService.cadastrar(usuarioCadastro);
            });

        }

    }

}