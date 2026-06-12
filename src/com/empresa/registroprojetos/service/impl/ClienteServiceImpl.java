package com.empresa.registroprojetos.service.impl;

import com.empresa.registroprojetos.domain.Cliente;
import com.empresa.registroprojetos.exception.RecursoNaoEncontradoException;
import com.empresa.registroprojetos.exception.ValidacaoException;
import com.empresa.registroprojetos.repository.interfaces.IClienteRepository;
import com.empresa.registroprojetos.service.interfaces.IClienteService;

import java.sql.SQLException;
import java.util.List;

/**
 * SOLID — SRP + DIP: serviço de Cliente com dependência injetada via construtor.
 */
public class ClienteServiceImpl implements IClienteService {

    private final IClienteRepository repository;

    public ClienteServiceImpl(IClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cliente cadastrar(Cliente c) throws ValidacaoException, SQLException {
        if (c.getCpf() == null || !c.getCpf().matches("\\d{11}"))
            throw new ValidacaoException("cpf", "CPF deve conter exatamente 11 dígitos numéricos.");
        if (c.getNome() == null || c.getNome().isBlank())
            throw new ValidacaoException("nome", "Nome é obrigatório.");
        if (c.getDataNascimento() == null)
            throw new ValidacaoException("dataNascimento", "Data de nascimento é obrigatória.");
        if (repository.existePorCpf(c.getCpf()))
            throw new ValidacaoException("cpf", "CPF '" + c.getCpf() + "' já cadastrado.");
        return repository.salvar(c);
    }

    @Override
    public Cliente buscarPorId(long id) throws RecursoNaoEncontradoException, SQLException {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", id));
    }

    @Override
    public List<Cliente> listarTodos() throws SQLException {
        return repository.buscarTodos();
    }

    @Override
    public void remover(long id) throws RecursoNaoEncontradoException, SQLException {
        buscarPorId(id);
        repository.deletar(id);
    }
}