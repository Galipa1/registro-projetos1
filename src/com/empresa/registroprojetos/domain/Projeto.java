package com.empresa.registroprojetos.domain;

import java.time.LocalDate;

/**
 * Entidade de domínio: Projeto (R3)
 *
 * SOLID — SRP: Apenas representa os dados de um projeto.
 *              A validação de data (R4) fica em DataInicioValidator.
 */
public class Projeto {

    private Long       id;
    private Integer    numero;       // R3
    private LocalDate  dataInicio;   // R3 — regra R4 validada externamente
    private LocalDate  dataTermino;  // R3
    private String     descricao;    // R3
    private Projetista projetista;   // R3
    private Cliente    cliente;      // R3

    public Projeto() {}

    public Projeto(Integer numero, LocalDate dataInicio, LocalDate dataTermino,
                   String descricao, Projetista projetista, Cliente cliente) {
        this.numero     = numero;
        this.dataInicio  = dataInicio;
        this.dataTermino = dataTermino;
        this.descricao   = descricao;
        this.projetista  = projetista;
        this.cliente     = cliente;
    }

    // Getters e Setters
    public Long       getId()                        { return id; }
    public void       setId(Long id)                 { this.id = id; }
    public Integer    getNumero()                    { return numero; }
    public void       setNumero(Integer numero)      { this.numero = numero; }
    public LocalDate  getDataInicio()                { return dataInicio; }
    public void       setDataInicio(LocalDate v)     { this.dataInicio = v; }
    public LocalDate  getDataTermino()               { return dataTermino; }
    public void       setDataTermino(LocalDate v)    { this.dataTermino = v; }
    public String     getDescricao()                 { return descricao; }
    public void       setDescricao(String descricao) { this.descricao = descricao; }
    public Projetista getProjetista()                { return projetista; }
    public void       setProjetista(Projetista v)    { this.projetista = v; }
    public Cliente    getCliente()                   { return cliente; }
    public void       setCliente(Cliente cliente)    { this.cliente = cliente; }
}