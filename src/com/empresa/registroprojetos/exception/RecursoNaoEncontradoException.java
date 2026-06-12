package com.empresa.registroprojetos.exception;

public class RecursoNaoEncontradoException extends Exception {
    public RecursoNaoEncontradoException(String recurso, Object id) {
        super(recurso + " com id " + id + " não encontrado.");
    }
}