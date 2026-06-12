# Sistema 4 — Registro de Projetos

Sistema desktop desenvolvido em Java com interface gráfica Swing para registro e manutenção de projetos de uma empresa.

## Requisitos implementados
- R1: Cadastro de projetistas (código funcional, nome e data de nascimento)
- R2: Cadastro de clientes (CPF, nome e data de nascimento)
- R3: Registro de projetos (número, datas, descrição, projetista e cliente)
- R4: Validação — data de início não pode ser anterior à data atual

## Como executar
1. Abra o projeto no IntelliJ IDEA
2. Adicione o driver `sqlite-jdbc.jar` como biblioteca (pasta `lib/`)
3. Execute o arquivo `Main.java`

## Arquitetura
O projeto segue a arquitetura em camadas:
- `domain` — entidades de dados
- `repository` — acesso ao banco via JDBC
- `service` — regras de negócio
- `ui` — interface gráfica Swing
- `config` — montagem das dependências (IoC manual)

