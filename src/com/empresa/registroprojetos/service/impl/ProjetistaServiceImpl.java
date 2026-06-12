package com.empresa.registroprojetos.service.impl;

import com.empresa.registroprojetos.domain.Projetista;
import com.empresa.registroprojetos.exception.RecursoNaoEncontradoException;
import com.empresa.registroprojetos.exception.ValidacaoException;
import com.empresa.registroprojetos.repository.interfaces.IProjetistaRepository;
import com.empresa.registroprojetos.service.interfaces.IProjetistaService;

import java.sql.SQLException;
import java.util.List;

/**
 * SOLID — SRP: Orquestra regras de negócio de Projetista.
 * SOLID — DIP: Recebe IProjetistaRepository (interface) via construtor.
 *
 * IoC sem framework: nenhum "new" de dependência aqui.
 * O AppContext monta o grafo de objetos e injeta via construtor.
 */
public class ProjetistaServiceImpl implements IProjetistaService {

    // Dependência declarada como INTERFACE — nunca como implementação concreta
    private final IProjetistaRepository repository;

    /** Injeção via construtor — testável com qualquer mock */
    public ProjetistaServiceImpl(IProjetistaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Projetista cadastrar(Projetista p) throws ValidacaoException, SQLException {
        if (p.getCodigoFuncional() == null || p.getCodigoFuncional().isBlank())
            throw new ValidacaoException("codigoFuncional", "Código funcional é obrigatório.");
        if (p.getNome() == null || p.getNome().isBlank())
            throw new ValidacaoException("nome", "Nome é obrigatório.");
        if (p.getDataNascimento() == null)
            throw new ValidacaoException("dataNascimento", "Data de nascimento é obrigatória.");
        if (repository.existePorCodigo(p.getCodigoFuncional()))
            throw new ValidacaoException("codigoFuncional",
                    "Código funcional '" + p.getCodigoFuncional() + "' já cadastrado.");
        return repository.salvar(p);
    }

    @Override
    public Projetista buscarPorId(long id) throws RecursoNaoEncontradoException, SQLException {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Projetista", id));
    }

    @Override
    public List<Projetista> listarTodos() throws SQLException {
        return repository.buscarTodos();
    }

    @Override
    public void remover(long id) throws RecursoNaoEncontradoException, SQLException {
        buscarPorId(id);
        repository.deletar(id);
    }
}