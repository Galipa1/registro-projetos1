package com.empresa.registroprojetos.service.impl;

import com.empresa.registroprojetos.domain.Cliente;
import com.empresa.registroprojetos.domain.Projetista;
import com.empresa.registroprojetos.domain.Projeto;
import com.empresa.registroprojetos.exception.RecursoNaoEncontradoException;
import com.empresa.registroprojetos.exception.ValidacaoException;
import com.empresa.registroprojetos.repository.interfaces.IProjetoRepository;
import com.empresa.registroprojetos.service.interfaces.IClienteService;
import com.empresa.registroprojetos.service.interfaces.IProjetistaService;
import com.empresa.registroprojetos.service.interfaces.IProjetoService;
import com.empresa.registroprojetos.service.validator.IDataInicioValidator;

import java.sql.SQLException;
import java.util.List;

/**
 * ══════════════════════════════════════════════════════════════
 * SOLID — Todos os 5 princípios aplicados aqui:
 *
 * S — SRP: Orquestra registro de projetos. Validação de data
 *          DELEGADA a IDataInicioValidator (classe separada).
 *
 * O — OCP: Novas regras de validação? Crie nova implementação
 *          de IDataInicioValidator. Esta classe não muda.
 *
 * L — LSP: Qualquer impl. de IProjetistaService/IClienteService
 *          pode ser substituída sem quebrar este serviço.
 *
 * I — ISP: Depende de interfaces mínimas e coesas por domínio.
 *          Não existe uma interface "Deus" com tudo misturado.
 *
 * D — DIP: TODAS as dependências são INTERFACES — nunca
 *          implementações concretas. O AppContext injeta.
 *
 * IoC sem framework:
 *   Grafo de dependências montado externamente em AppContext.
 *   Este serviço não sabe como suas dependências são criadas.
 * ══════════════════════════════════════════════════════════════
 */
public class ProjetoServiceImpl implements IProjetoService {

    // ── Todas declaradas como INTERFACES — nunca como classes concretas ──
    private final IProjetoRepository   projetoRepo;
    private final IProjetistaService   projetistaService;
    private final IClienteService      clienteService;
    private final IDataInicioValidator dataInicioValidator; // ← regra R4 isolada

    /**
     * IoC — Injeção via construtor.
     * AppContext passa as implementações concretas aqui.
     * Em testes, passamos mocks — sem modificar esta classe.
     */
    public ProjetoServiceImpl(
            IProjetoRepository   projetoRepo,
            IProjetistaService   projetistaService,
            IClienteService      clienteService,
            IDataInicioValidator dataInicioValidator) {

        this.projetoRepo         = projetoRepo;
        this.projetistaService   = projetistaService;
        this.clienteService      = clienteService;
        this.dataInicioValidator = dataInicioValidator;
    }

    @Override
    public Projeto registrar(Projeto p)
            throws ValidacaoException, RecursoNaoEncontradoException, SQLException {

        // ── 1. Validação R4 — delegada ao validator isolado (SRP) ───────
        dataInicioValidator.validar(p.getDataInicio());

        // ── 2. Validação de data de término ──────────────────────────────
        if (p.getDataTermino() == null)
            throw new ValidacaoException("dataTermino", "Data de término é obrigatória.");
        if (p.getDataTermino().isBefore(p.getDataInicio()))
            throw new ValidacaoException("dataTermino",
                    "Data de término não pode ser anterior à data de início.");

        // ── 3. Unicidade do número ────────────────────────────────────────
        if (p.getNumero() == null || p.getNumero() <= 0)
            throw new ValidacaoException("numero", "Número do projeto deve ser positivo.");
        if (projetoRepo.existePorNumero(p.getNumero()))
            throw new ValidacaoException("numero",
                    "Número de projeto '" + p.getNumero() + "' já registrado.");

        // ── 4. Verificar existência do Projetista e Cliente ───────────────
        //       Delega a outros services — não acessa banco diretamente (DIP)
        Projetista projetista = projetistaService.buscarPorId(p.getProjetista().getId());
        Cliente    cliente    = clienteService.buscarPorId(p.getCliente().getId());
        p.setProjetista(projetista);
        p.setCliente(cliente);

        if (p.getDescricao() == null || p.getDescricao().isBlank())
            throw new ValidacaoException("descricao", "Descrição é obrigatória.");

        return projetoRepo.salvar(p);
    }

    @Override
    public Projeto buscarPorId(long id) throws RecursoNaoEncontradoException, SQLException {
        return projetoRepo.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto", id));
    }

    @Override
    public List<Projeto> listarTodos() throws SQLException {
        return projetoRepo.buscarTodos();
    }

    @Override
    public void remover(long id) throws RecursoNaoEncontradoException, SQLException {
        buscarPorId(id);
        projetoRepo.deletar(id);
    }
}