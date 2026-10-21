# Atlas — Controle de Projetos

Aplicação Spring Boot com API REST, persistência H2 em memória e interface web servida pelo próprio backend.

## Executar

No Windows, na pasta `ac2`, execute `mvnw.cmd spring-boot:run`. A tela fica em `http://localhost:8081` e o console H2 em `http://localhost:8081/h2-console`.

Configuração de conexão do H2: JDBC URL `jdbc:h2:mem:controleprojetos`, usuário `sa`, senha vazia. Os dados são apagados quando a aplicação é encerrada.

## API

As datas usam o formato ISO `AAAA-MM-DD`.

- `GET /api/projetos` — lista projetos e funcionários; aceita `?inicio=2026-01-01&fim=2026-12-31` para filtrar pela data de início.
- `POST /api/projetos` — cria projeto. JSON: `{"descricao":"Portal institucional","dataInicio":"2026-01-01","dataFim":"2026-04-30"}`.
- `GET /api/projetos/{id}` — consulta um projeto com os funcionários vinculados.
- `PUT /api/projetos/{idProjeto}/funcionarios/{idFuncionario}` — vincula um funcionário existente a um projeto.
- `POST /api/setores` — cria setor. JSON: `{"nome":"Tecnologia"}`.
- `GET /api/setores` e `GET /api/setores/{idSetor}` — consulta setores com seus funcionários.
- `POST /api/funcionarios` — cria funcionário. JSON: `{"nome":"Ana Souza","setorId":1}`.
- `GET /api/funcionarios/{idFuncionario}/projetos` — consulta os projetos do funcionário.

Erros de validação e de negócio retornam JSON com `timestamp`, `status`, `erro`, `mensagem`, `caminho` e, quando aplicável, os `campos` inválidos.

## Organização

- `domain`: entidades e relacionamentos JPA do diagrama.
- `repository`: CRUD e consultas JPQL com `fetch join` para carregar associações solicitadas.
- `service`: validações, regras de negócio e conversão para DTOs.
- `controller`: endpoints REST.
- `src/main/resources/static`: tela responsiva de projetos; JavaScript consome a API no mesmo host.
