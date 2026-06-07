package br.com.cleiton.api.usuarios.service;

import br.com.cleiton.api.usuarios.dto.UsuarioCadastroRequest;
import br.com.cleiton.api.usuarios.dto.UsuarioResponse;
import br.com.cleiton.api.usuarios.exception.EmailDuplicadoException;
import br.com.cleiton.api.usuarios.model.Usuario;
import br.com.cleiton.api.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        @DisplayName("Cadastro com dados válidos")
        void cadastrarUsuarioC1() {

            UsuarioCadastroRequest usuarioCadastroRequest = new UsuarioCadastroRequest("cleiton@email.com", "123456");

            when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(new Usuario(1L, "cleiton@email.com", encoder.encode("123456")));

            Usuario usuario = usuarioRepository.save(new Usuario(
                    null,
                    usuarioCadastroRequest.email(),
                    encoder.encode(usuarioCadastroRequest.senha())
            ));

            assertNotNull(usuario);
            assertEquals(1L, usuario.getId());
            assertNotEquals(usuarioCadastroRequest.senha(), usuario.getSenha());
            assertTrue(encoder.matches("123456", usuario.getSenha()));
        }

        @Test
        @DisplayName("E-mail já cadastrado")
        void cadastrarUsuarioC2() {

            UsuarioCadastroRequest usuarioCadastro = new UsuarioCadastroRequest("cleiton@email.com", encoder.encode("123456"));

            when(usuarioRepository.existsByEmail(usuarioCadastro.email())).thenReturn(true);

            assertThrows(EmailDuplicadoException.class, () -> {
                usuarioService.cadastrar(usuarioCadastro);
            });

        }

    }

    @Nested
    class BuscarPorId {

        @Test
        @DisplayName("Buscar por id existente")
        void buscarPorIdC1() {

            Usuario usuario = new Usuario(1L, "cleiton@email.com", encoder.encode("123456"));

            when(usuarioRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(usuario));

            UsuarioResponse usuarioResponse = usuarioService.buscarPorId(1L);

            assertNotNull(usuarioResponse);
            assertEquals(usuario.getId(), usuarioResponse.id());
            assertEquals(usuario.getEmail(), usuarioResponse.email());

        }

        @Test
        @DisplayName("Buscar por id não existe")
        void buscarPorIdC2() {

            when(usuarioRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

            UsuarioResponse usuarioResponse = usuarioService.buscarPorId(999L);

            assertNull(usuarioResponse);

        }

    }

}