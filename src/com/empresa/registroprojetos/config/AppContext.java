package com.empresa.registroprojetos.config;

import com.empresa.registroprojetos.repository.impl.ClienteRepositoryImpl;
import com.empresa.registroprojetos.repository.impl.ProjetistaRepositoryImpl;
import com.empresa.registroprojetos.repository.impl.ProjetoRepositoryImpl;
import com.empresa.registroprojetos.repository.interfaces.IClienteRepository;
import com.empresa.registroprojetos.repository.interfaces.IProjetistaRepository;
import com.empresa.registroprojetos.repository.interfaces.IProjetoRepository;
import com.empresa.registroprojetos.service.impl.ClienteServiceImpl;
import com.empresa.registroprojetos.service.impl.ProjetistaServiceImpl;
import com.empresa.registroprojetos.service.impl.ProjetoServiceImpl;
import com.empresa.registroprojetos.service.interfaces.IClienteService;
import com.empresa.registroprojetos.service.interfaces.IProjetistaService;
import com.empresa.registroprojetos.service.interfaces.IProjetoService;
import com.empresa.registroprojetos.service.validator.DataInicioValidator;
import com.empresa.registroprojetos.service.validator.IDataInicioValidator;

/**
 * ══════════════════════════════════════════════════════════════
 * IoC Container Manual — equivalente ao ApplicationContext do Spring,
 * porém sem nenhuma dependência externa.
 *
 * Princípio de Inversão de Controle (IoC):
 *   As classes não criam suas dependências ("new Algo()").
 *   Este AppContext monta o grafo inteiro e injeta via construtores.
 *
 * Injeção de Dependência (DI):
 *   Cada serviço recebe INTERFACES, nunca implementações concretas.
 *   Para trocar SQLite por MySQL: substituir apenas as linhas de
 *   "new *RepositoryImpl()" — zero impacto nos serviços ou na UI.
 *
 * SOLID — SRP: AppContext tem uma única responsabilidade —
 *              montar e fornecer o grafo de dependências.
 * ══════════════════════════════════════════════════════════════
 */
public class AppContext {

    // ── Repositórios (declarados como INTERFACES) ──────────────────
    private final IProjetistaRepository projetistaRepo   = new ProjetistaRepositoryImpl();
    private final IClienteRepository    clienteRepo      = new ClienteRepositoryImpl();
    private final IProjetoRepository    projetoRepo      = new ProjetoRepositoryImpl();

    // ── Validator isolado (R4) ──────────────────────────────────────
    private final IDataInicioValidator  dataInicioValidator = new DataInicioValidator();

    // ── Serviços — injeção via construtor, dependem de INTERFACES ──
    private final IProjetistaService projetistaService =
            new ProjetistaServiceImpl(projetistaRepo);

    private final IClienteService clienteService =
            new ClienteServiceImpl(clienteRepo);

    private final IProjetoService projetoService =
            new ProjetoServiceImpl(
                    projetoRepo,
                    projetistaService,
                    clienteService,
                    dataInicioValidator   // ← R4 injetado — facilmente substituível em testes
            );

    // ── Getters públicos — entregam apenas INTERFACES ──────────────
    public IProjetistaService getProjetistaService() { return projetistaService; }
    public IClienteService    getClienteService()    { return clienteService; }
    public IProjetoService    getProjetoService()    { return projetoService; }
}