package com.empresa.registroprojetos.domain;

import java.time.LocalDate;

/**
 * Entidade de domínio: Projetista (R1)
 *
 * SOLID — SRP: Apenas representa dados de um projetista.
 *              Nenhuma lógica de negócio ou acesso a banco aqui.
 */
public class Projetista {

    private Long      id;
    private String    codigoFuncional;  // R1
    private String    nome;             // R1
    private LocalDate dataNascimento;   // R1

    public Projetista() {}

    public Projetista(String codigoFuncional, String nome, LocalDate dataNascimento) {
        this.codigoFuncional = codigoFuncional;
        this.nome            = nome;
        this.dataNascimento  = dataNascimento;
    }

    // Getters e Setters
    public Long      getId()                            { return id; }
    public void      setId(Long id)                     { this.id = id; }
    public String    getCodigoFuncional()               { return codigoFuncional; }
    public void      setCodigoFuncional(String v)       { this.codigoFuncional = v; }
    public String    getNome()                          { return nome; }
    public void      setNome(String nome)               { this.nome = nome; }
    public LocalDate getDataNascimento()                { return dataNascimento; }
    public void      setDataNascimento(LocalDate v)     { this.dataNascimento = v; }

    @Override
    public String toString() {
        return "[" + codigoFuncional + "] " + nome;
    }
}