package br.com.cleiton.api.usuarios.exception;

public class EmailDuplicadoException extends RuntimeException {

    public EmailDuplicadoException(String message) {
        super(message);
    }

}
