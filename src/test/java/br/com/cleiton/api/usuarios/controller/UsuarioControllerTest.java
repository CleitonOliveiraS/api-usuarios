package br.com.cleiton.api.usuarios.controller;

import br.com.cleiton.api.usuarios.config.SecurityConfig;
import br.com.cleiton.api.usuarios.dto.UsuarioCadastroRequest;
import br.com.cleiton.api.usuarios.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({UsuarioController.class, SecurityConfig.class})
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123"})
    void deveRetornarBadRequestQuandoSenhaForCurtaNulaOuVazia(String input) throws Exception {

        UsuarioCadastroRequest request = new UsuarioCadastroRequest("cleiton@email.com", input);

        MvcResult result = mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        assertThat(jsonResponse).containsAnyOf("Senha deve ter no mínimo 6 caracteres.", "O campo senha não deve estar vazio ou nulo.");

    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"cleitonemail.com"})
    void deveRetornarBadRequestQuandoEmailNaoTemArrobaNuloOuVazio(String input) throws Exception {

        UsuarioCadastroRequest request = new UsuarioCadastroRequest(input, "123456");

        MvcResult result = mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        assertThat(jsonResponse).containsAnyOf("O e-mail deve estar no formato correto.", "O campo e-mail não deve estar vazio ou nulo.");

    }

}