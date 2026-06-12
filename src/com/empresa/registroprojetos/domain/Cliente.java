package com.empresa.registroprojetos.domain;

import java.time.LocalDate;

/**
 * Entidade de domínio: Cliente (R2)
 *
 * SOLID — SRP: Apenas representa dados de um cliente.
 */
public class Cliente {

    private Long      id;
    private String    cpf;            // R2
    private String    nome;           // R2
    private LocalDate dataNascimento; // R2

    public Cliente() {}

    public Cliente(String cpf, String nome, LocalDate dataNascimento) {
        this.cpf            = cpf;
        this.nome           = nome;
        this.dataNascimento = dataNascimento;
    }

    // Getters e Setters
    public Long      getId()                        { return id; }
    public void      setId(Long id)                 { this.id = id; }
    public String    getCpf()                       { return cpf; }
    public void      setCpf(String cpf)             { this.cpf = cpf; }
    public String    getNome()                      { return nome; }
    public void      setNome(String nome)           { this.nome = nome; }
    public LocalDate getDataNascimento()            { return dataNascimento; }
    public void      setDataNascimento(LocalDate v) { this.dataNascimento = v; }

    @Override
    public String toString() {
        return nome + " (CPF: " + cpf + ")";
    }
}