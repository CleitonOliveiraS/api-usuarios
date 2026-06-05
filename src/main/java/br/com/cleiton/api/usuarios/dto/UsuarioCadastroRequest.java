package br.com.cleiton.api.usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioCadastroRequest(
        @NotBlank(message = "O campo e-mail não deve estar vazio ou nulo.")
        @Email(message = "O e-mail deve estar no formato correto.")
        String email,

        @NotBlank(message = "O campo senha não deve estar vazio ou nulo.")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres.")
        String senha
) {
}
