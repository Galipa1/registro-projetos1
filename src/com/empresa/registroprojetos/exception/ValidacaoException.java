package com.empresa.registroprojetos.exception;

/**
 * SOLID — SRP: Transporta erros de regra de negócio
 *              (ex: data inválida, duplicidade).
 */
public class ValidacaoException extends Exception {

    private final String campo;

    public ValidacaoException(String campo, String mensagem) {
        super(mensagem);
        this.campo = campo;
    }

    public String getCampo() { return campo; }
}