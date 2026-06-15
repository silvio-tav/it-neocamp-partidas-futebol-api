# API de Partidas de Futebol

![Java](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white)
![H2](https://img.shields.io/badge/H2-Database-1021FF?style=for-the-badge)
![MySQL](https://img.shields.io/badge/MySQL-8.4-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

Projeto de estudos desenvolvido com Java e Spring Boot para praticar a criação de uma API REST completa, com autenticação JWT, CRUD, validações, filtros dinâmicos, paginação, migrations de banco, consultas agregadas, ranking e retrospecto de clubes de futebol.

> Objetivo principal: consolidar conceitos de backend com Spring Boot em um domínio simples, mas com regras de negócio reais o bastante para exercitar arquitetura em camadas, incluindo segurança com Spring Security e tokens JWT.

---

## Sumário

- [Sobre o projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Modelo de dados](#modelo-de-dados)
- [Como executar](#como-executar)
- [Autenticação](#autenticação)
- [Documentação da API](#documentação-da-api)
- [Endpoints](#endpoints)
- [Exemplos de requisição](#exemplos-de-requisição)
- [Regras de negócio](#regras-de-negócio)
- [Banco de dados e migrations](#banco-de-dados-e-migrations)
- [Testes](#testes)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Aprendizados praticados](#aprendizados-praticados)

---

## Sobre o projeto

A aplicação permite gerenciar clubes, estádios e partidas de futebol. Além do CRUD básico, a API oferece consultas de retrospecto, confrontos diretos entre clubes e rankings por diferentes critérios. Todos os endpoints (exceto os de autenticação) são protegidos por JWT.

Este projeto foi criado para estudos, por isso o README também serve como guia de consulta para entender a estrutura do código, os fluxos principais e os recursos do Spring utilizados.

---

## Funcionalidades

| Área | Recursos |
| --- | --- |
| Autenticação | Registro de usuário, login com JWT, proteção de rotas com Bearer token |
| Clubes | Cadastro, atualização parcial, busca por ID, listagem paginada, filtros por nome, estado e status, inativação lógica |
| Estádios | Cadastro, atualização, busca por ID, listagem paginada, filtro por nome, exclusão |
| Partidas | Cadastro, atualização parcial, busca por ID, listagem paginada, filtros por clube, estádio e goleada, exclusão |
| Retrospecto | Estatísticas gerais do clube, por atuação e por adversário |
| Ranking | Ranking por pontos, gols, vitórias ou jogos |
| Banco de dados | Migrations com Flyway, carga inicial e índices para melhorar consultas |
| Documentação | Swagger/OpenAPI e collection Postman |

---

## Tecnologias

| Tecnologia | Uso no projeto |
| --- | --- |
| Java 17 | Linguagem principal |
| Spring Boot 4.0.6 | Base da aplicação |
| Spring Web MVC | Criação dos endpoints REST |
| Spring Security | Controle de autenticação e autorização |
| JJWT 0.12.6 | Geração e validação de tokens JWT |
| Spring Data JPA | Persistência e repositories |
| Hibernate | Implementação JPA |
| Bean Validation | Validações com annotations |
| H2 Database | Banco em memória para desenvolvimento e estudos |
| MySQL | Banco relacional para execução persistente via Docker |
| Docker Compose | Subida do banco MySQL local |
| Flyway | Versionamento do schema e carga de dados |
| Springdoc OpenAPI | Swagger UI e contrato da API |
| Lombok | Redução de boilerplate em DTOs e entidades |
| Maven | Gerenciamento de dependências e build |

---

## Arquitetura

O projeto segue uma arquitetura em camadas simples, comum em APIs Spring Boot:

```mermaid
flowchart LR
    Client[Cliente HTTP] --> Filter[JWT Filter]
    Filter --> Controller[Controllers REST]
    Controller --> Service[Services]
    Service --> Repository[Repositories]
    Repository --> Database[(H2 ou MySQL)]

    Service --> Specification[Specifications]
    Database --> Flyway[Flyway Migrations]
```

### Responsabilidades por camada

| Camada | Responsabilidade |
| --- | --- |
| `security` | Filtra requisições, valida JWT e autentica o usuário no contexto do Spring |
| `controller` | Recebe requisições HTTP, valida entrada e retorna respostas |
| `service` | Centraliza regras de negócio |
| `repository` | Acessa o banco usando Spring Data JPA |
| `specification` | Monta filtros dinâmicos para listagens |
| `model` | Representa entidades persistidas no banco |
| `dto` | Define contratos de entrada e saída da API |
| `mapper` | Converte entidades em DTOs e DTOs em entidades |
| `exception` / `handler` | Trata erros de domínio e padroniza respostas |

---

## Modelo de dados

```mermaid
erDiagram
    USUARIO {
        UUID usuario_id PK
        string username
        string password
        string role
    }

    CLUBE ||--o{ PARTIDA : clube_casa
    CLUBE ||--o{ PARTIDA : clube_visitante
    ESTADIO ||--o{ PARTIDA : sedia

    CLUBE {
        UUID clube_id PK
        string nome
        string sigla_estado
        date data_criacao
        boolean ativo
    }

    ESTADIO {
        UUID estadio_id PK
        string nome
    }

    PARTIDA {
        UUID partida_id PK
        UUID clube_casa_id FK
        UUID clube_visitante_id FK
        UUID estadio_id FK
        timestamp data_hora_partida
        integer gols_casa
        integer gols_visitante
    }
```

---

## Como executar

### Pré-requisitos

- Java 17 ou superior
- Maven instalado
- Docker e Docker Compose, caso queira executar com MySQL
- Git, caso queira clonar o repositório

### Execução padrão com H2

1. Clone o repositório:

```bash
git clone <url-do-repositorio>
cd it-neocamp-projeto-final-workshop
```

2. Execute a aplicação:

```bash
mvn spring-boot:run
```

3. Acesse a API em:

```text
http://localhost:8080
```

### Execução com MySQL no Docker

O arquivo [docker-compose.yml](docker-compose.yml) sobe um container MySQL 8.4 com volume persistente para manter os dados entre reinicializações.

1. Suba o banco MySQL:

```bash
docker compose up -d mysql
```

2. Execute a aplicação usando o profile `mysql`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

3. Acesse a API em:

```text
http://localhost:8080
```

O Flyway cria as tabelas, índices e dados iniciais automaticamente quando a aplicação inicia.

### Comandos úteis do Docker

| Ação | Comando |
| --- | --- |
| Verificar se o Docker Compose está disponível | `docker compose version` |
| Subir o MySQL em segundo plano | `docker compose up -d mysql` |
| Ver containers rodando | `docker ps` |
| Ver todos os containers | `docker ps -a` |
| Ver logs do MySQL | `docker compose logs mysql` |
| Acompanhar logs em tempo real | `docker compose logs -f mysql` |
| Parar o MySQL | `docker compose stop mysql` |
| Iniciar novamente o MySQL parado | `docker compose start mysql` |
| Reiniciar o MySQL | `docker compose restart mysql` |
| Remover container e rede, mantendo os dados | `docker compose down` |
| Remover container, rede e apagar os dados | `docker compose down -v` |
| Acessar o banco pelo terminal | `docker exec -it futebol-mysql mysql -ufutebol -pfutebol futeboldb` |

Se o seu ambiente usar o Compose antigo, troque `docker compose` por `docker-compose`.

### Observação sobre o Maven Wrapper

O projeto possui arquivos `mvnw` e `mvnw.cmd`, mas para usar o wrapper é necessário que a pasta `.mvn/wrapper` esteja completa. Se o wrapper estiver configurado no seu ambiente, também será possível executar:

```bash
./mvnw spring-boot:run
```

---

## Autenticação

A API utiliza autenticação stateless com **JWT (JSON Web Token)**. Todos os endpoints, exceto `/auth/**`, `/swagger-ui/**` e `/v3/api-docs/**`, exigem um token válido no header da requisição.

### Fluxo de autenticação

```mermaid
sequenceDiagram
    participant C as Cliente
    participant A as POST /auth/register ou /auth/login
    participant S as Spring Security
    participant DB as Banco de dados

    C->>A: { username, password }
    A->>DB: busca ou salva usuário
    DB-->>A: usuário encontrado
    A->>S: gera JWT assinado (24h)
    S-->>C: { "token": "eyJ..." }

    C->>S: GET /clubes (Authorization: Bearer eyJ...)
    S->>S: valida assinatura e expiração
    S-->>C: 200 OK com dados
```

### Endpoints de autenticação

| Método | Endpoint | Descrição | Auth |
| --- | --- | --- | --- |
| `POST` | `/auth/register` | Cria um novo usuário e retorna o token JWT | Não |
| `POST` | `/auth/login` | Autentica e retorna o token JWT | Não |

#### Registrar usuário

```http
POST /auth/register
Content-Type: application/json
```

```json
{
  "username": "admin",
  "password": "senha123"
}
```

**Resposta `201 Created`:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

#### Login

```http
POST /auth/login
Content-Type: application/json
```

```json
{
  "username": "admin",
  "password": "senha123"
}
```

**Resposta `200 OK`:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### Usando o token nas requisições

Inclua o token no header `Authorization` de todas as chamadas protegidas:

```http
GET /clubes
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Rotas públicas (sem token)

| Rota | Motivo |
| --- | --- |
| `POST /auth/register` | Criação de conta |
| `POST /auth/login` | Autenticação |
| `GET /swagger-ui/**` | Documentação |
| `GET /v3/api-docs/**` | Contrato OpenAPI |
| `GET /h2-console/**` | Console H2 (apenas no profile padrão) |

### Configuração JWT

As propriedades do token ficam no `application.yaml`:

```yaml
jwt:
  secret: 404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
  expiration-ms: 86400000  # 24 horas
```

> **Importante:** troque o `jwt.secret` por uma chave Base64 de 256 bits gerada de forma segura antes de usar em produção.

### Postman

A collection [partidas_futebol.postman_collection.json](partidas_futebol.postman_collection.json) já está configurada para autenticação:

1. Execute `POST /auth/register` ou `POST /auth/login`
2. O token é salvo automaticamente na variável `{{token}}` da collection via script
3. Todos os outros requests já enviam o header `Authorization: Bearer {{token}}` automaticamente

---

## Documentação da API

Com a aplicação em execução, acesse:

| Recurso | URL |
| --- | --- |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| H2 Console, apenas no profile padrão | `http://localhost:8080/h2-console` |

### Credenciais do H2

| Campo | Valor |
| --- | --- |
| JDBC URL | `jdbc:h2:mem:futeboldb` |
| User Name | `sa` |
| Password | vazio |

### Credenciais do MySQL local

| Campo | Valor |
| --- | --- |
| Host | `localhost` |
| Porta | `3306` |
| Database | `futeboldb` |
| User | `futebol` |
| Password | `futebol` |
| Root password | `root` |

---

## Endpoints

> Todos os endpoints abaixo exigem o header `Authorization: Bearer <token>`.

### Autenticação

| Método | Endpoint | Descrição | Auth |
| --- | --- | --- | --- |
| `POST` | `/auth/register` | Registra um novo usuário | Não |
| `POST` | `/auth/login` | Realiza login e retorna o token | Não |

### Clubes

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/clubes` | Cadastra um clube |
| `PUT` | `/clubes/{clubeId}` | Atualiza parcialmente um clube |
| `DELETE` | `/clubes/{clubeId}` | Inativa um clube |
| `GET` | `/clubes/{clubeId}` | Busca um clube por ID |
| `GET` | `/clubes` | Lista clubes com filtros e paginação |
| `GET` | `/clubes/{clubeId}/retrospecto` | Retorna retrospecto geral do clube |
| `GET` | `/clubes/{clubeId}/retrospecto/adversarios` | Retorna retrospecto contra adversários |
| `GET` | `/clubes/{clubeId}/retrospecto/adversarios/{adversarioId}` | Retorna confrontos diretos contra um adversário |
| `GET` | `/clubes/ranking` | Retorna ranking de clubes |

#### Filtros de clubes

```text
GET /clubes?nome=Fla&estado=RJ&ativo=true&page=0&size=10
```

| Parâmetro | Tipo | Descrição |
| --- | --- | --- |
| `nome` | `String` | Filtra por trecho do nome |
| `estado` | `EstadoBrasileiro` | Filtra pela sigla do estado |
| `ativo` | `Boolean` | Filtra clubes ativos ou inativos |
| `page` | `Integer` | Página desejada |
| `size` | `Integer` | Quantidade de itens por página |
| `sort` | `String` | Ordenação, exemplo: `nome,asc` |

#### Parâmetros de retrospecto

```text
GET /clubes/{clubeId}/retrospecto?atuacao=MANDANTE
```

| Parâmetro | Valores | Descrição |
| --- | --- | --- |
| `atuacao` | `MANDANTE`, `VISITANTE` | Opcional. Quando omitido, considera todas as partidas |

#### Parâmetros de ranking

```text
GET /clubes/ranking?tipo=PONTOS
```

| Parâmetro | Valores |
| --- | --- |
| `tipo` | `PONTOS`, `GOLS`, `VITORIAS`, `JOGOS` |

### Estádios

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/estadios` | Cadastra um estádio |
| `PUT` | `/estadios/{estadioId}` | Atualiza um estádio |
| `GET` | `/estadios` | Lista estádios com filtro e paginação |
| `GET` | `/estadios/{estadioId}` | Busca um estádio por ID |
| `DELETE` | `/estadios/{estadioId}` | Remove um estádio |

#### Filtros de estádios

```text
GET /estadios?nomeEstadio=Maracana&page=0&size=10
```

| Parâmetro | Tipo | Descrição |
| --- | --- | --- |
| `nomeEstadio` | `String` | Filtra por trecho do nome |
| `page` | `Integer` | Página desejada |
| `size` | `Integer` | Quantidade de itens por página |
| `sort` | `String` | Ordenação, exemplo: `nome,asc` |

### Partidas

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/partidas` | Cadastra uma partida |
| `PUT` | `/partidas/{partidaId}` | Atualiza parcialmente uma partida |
| `DELETE` | `/partidas/{partidaId}` | Remove uma partida |
| `GET` | `/partidas/{partidaId}` | Busca uma partida por ID |
| `GET` | `/partidas` | Lista partidas com filtros e paginação |

#### Filtros de partidas

```text
GET /partidas?nomeClube=Fla&nomeEstadio=Maracana&goleada=true&page=0&size=10
```

| Parâmetro | Tipo | Descrição |
| --- | --- | --- |
| `nomeClube` | `String` | Filtra por trecho do nome do clube mandante ou visitante |
| `nomeEstadio` | `String` | Filtra por trecho do nome do estádio |
| `goleada` | `Boolean` | Quando `true`, retorna partidas com diferença de 3 ou mais gols |
| `page` | `Integer` | Página desejada |
| `size` | `Integer` | Quantidade de itens por página |
| `sort` | `String` | Ordenação, exemplo: `dataHoraPartida,desc` |

---

## Exemplos de requisição

### Registrar e autenticar

```http
POST /auth/register
Content-Type: application/json
```

```json
{
  "username": "admin",
  "password": "senha123"
}
```

```http
POST /auth/login
Content-Type: application/json
```

```json
{
  "username": "admin",
  "password": "senha123"
}
```

### Cadastrar clube

```http
POST /clubes
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

```json
{
  "nome": "Flamengo",
  "estado": "RJ",
  "dataCriacao": "1895-11-15"
}
```

### Cadastrar estádio

```http
POST /estadios
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

```json
{
  "nome": "Maracana"
}
```

### Cadastrar partida

```http
POST /partidas
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

```json
{
  "clubeCasaId": "b1b2c3d4-0001-0000-0000-000000000001",
  "clubeVisitanteId": "b1b2c3d4-0002-0000-0000-000000000002",
  "estadioId": "a1b2c3d4-0001-0000-0000-000000000001",
  "dataHoraPartida": "2024-12-20T16:00:00",
  "golsCasa": 2,
  "golsVisitante": 1
}
```

### Consultar ranking

```http
GET /clubes/ranking?tipo=PONTOS
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### Consultar retrospecto de um clube

```http
GET /clubes/b1b2c3d4-0001-0000-0000-000000000001/retrospecto
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## Regras de negócio

### Autenticação

| Regra | Comportamento |
| --- | --- |
| Username único | Não permite cadastrar username já existente — retorna `409 Conflict` |
| Senha mínima | A senha deve ter ao menos 6 caracteres |
| Senha criptografada | A senha é armazenada com BCrypt, nunca em texto plano |
| Token de 24h | O JWT expira em 24 horas |
| Rotas protegidas | Qualquer rota fora de `/auth/**` exige `Authorization: Bearer <token>` |

### Clubes

| Regra | Comportamento |
| --- | --- |
| Nome obrigatório | Deve ter pelo menos 2 caracteres |
| Estado obrigatório | Deve ser uma sigla válida do enum `EstadoBrasileiro` |
| Data de criação | Deve ser hoje ou uma data passada |
| Duplicidade | Não permite clube com mesmo nome e mesmo estado |
| Inativação | `DELETE /clubes/{id}` altera `ativo` para `false` |

### Estádios

| Regra | Comportamento |
| --- | --- |
| Nome obrigatório | Deve ter pelo menos 3 caracteres |
| Duplicidade | Não permite estádio com mesmo nome |
| Exclusão | `DELETE /estadios/{id}` remove o registro |

### Partidas

| Regra | Comportamento |
| --- | --- |
| Clubes diferentes | Mandante e visitante não podem ser o mesmo clube |
| Clubes existentes | Ambos os clubes precisam existir |
| Clubes ativos | Não é permitido cadastrar partida com clube inativo |
| Estádio existente | O estádio informado precisa existir |
| Conflito de horário | Um clube não pode ter outra partida em intervalo menor que 48 horas |
| Estádio ocupado | Um estádio não pode receber mais de uma partida no mesmo dia |
| Gols | Devem ser números maiores ou iguais a zero |
| Data da partida | Deve ser hoje ou uma data passada |

---

## Banco de dados e migrations

O projeto usa Flyway para criar e popular o banco automaticamente. Por padrão, a aplicação roda com H2 em memória. Para usar MySQL, suba o container com Docker Compose e inicie a aplicação com o profile `mysql`.

| Profile | Banco | Uso |
| --- | --- | --- |
| padrão | H2 em memória | Execução rápida sem dependência externa |
| `mysql` | MySQL 8.4 via Docker | Execução persistente para migração e testes locais |

| Migration | Descrição |
| --- | --- |
| `V1__create_tables.sql` | Cria as tabelas `clube`, `estadio` e `partida` |
| `V2__insert_initial_data.sql` | Insere clubes, estádios e partidas iniciais |
| `V3__insert_test_data.sql` | Insere dados adicionais para testes de ranking, goleadas e retrospecto |
| `V4__create_indexes.sql` | Cria índices para otimizar consultas frequentes |
| `V5__create_users.sql` | Cria a tabela `usuario` para autenticação JWT |

### Índices criados

| Índice | Objetivo |
| --- | --- |
| `idx_clube_sigla_estado_nome` | Melhorar validação e busca por estado/nome |
| `idx_clube_ativo_sigla_estado` | Melhorar filtros e ranking de clubes ativos |
| `idx_estadio_nome` | Melhorar busca e validação por nome do estádio |
| `idx_partida_clube_casa_data` | Melhorar consultas por mandante e data |
| `idx_partida_clube_visitante_data` | Melhorar consultas por visitante e data |
| `idx_partida_estadio_data` | Melhorar validação de estádio ocupado por dia |
| `idx_partida_clubes_casa_visitante` | Melhorar confrontos diretos mandante x visitante |
| `idx_partida_clubes_visitante_casa` | Melhorar confrontos diretos visitante x mandante |

---

## Testes

Para executar os testes:

```bash
mvn test
```

A suíte atual possui testes unitários para services, testes de controllers com `MockMvc`, testes do handler global de exceções e teste de contexto da aplicação. O teste de contexto também valida que as migrations do Flyway são aplicadas corretamente no banco H2 em memória.

---

## Estrutura do projeto

```text
src
├── main
│   ├── java
│   │   └── com/example/it_neocamp_projeto_final_workshop
│   │       ├── config         # SecurityConfig, OpenApiConfig
│   │       ├── controller     # AuthController, ClubeController, EstadioController, PartidaController
│   │       ├── dto            # DTOs de entrada e saída (auth, clube, estádio, partida, retrospecto)
│   │       ├── enums          # Atuacao, EstadoBrasileiro, RankingTipo, Role
│   │       ├── exception      # Exceções de domínio
│   │       ├── handler        # GlobalExceptionHandler
│   │       ├── mapper         # Conversores entidade <-> DTO
│   │       ├── model          # Usuario, Clube, Estadio, Partida
│   │       ├── repository     # UsuarioRepository, ClubeRepository, EstadioRepository, PartidaRepository
│   │       ├── security       # JwtService, JwtAuthenticationFilter, UsuarioDetailsService
│   │       ├── service        # Implementações dos serviços de negócio
│   │       └── specification  # Filtros dinâmicos com Specification
│   └── resources
│       ├── application.yaml
│       └── db/migration       # V1 a V5
└── test
    └── java
```

---

## Aprendizados praticados

- Criação de APIs REST com Spring Boot
- Separação de responsabilidades em camadas
- Autenticação e autorização com Spring Security
- Geração e validação de tokens JWT com JJWT
- Proteção de rotas com filtros stateless
- Hash de senhas com BCrypt
- Uso de DTOs para entrada e saída de dados
- Validação com Bean Validation
- Tratamento centralizado de exceções
- Persistência com Spring Data JPA
- Filtros dinâmicos com `Specification`
- Paginação e ordenação com `Pageable`
- Consultas JPQL com agregações
- Relacionamentos `ManyToOne`
- Migrations com Flyway
- Banco H2 em memória
- Banco MySQL com Docker Compose
- Documentação com Swagger/OpenAPI
- Otimização básica de consultas com índices

---

## Possíveis evoluções

- Adicionar testes de integração com MySQL usando Testcontainers
- Separar dados de exemplo/teste em um profile próprio para evitar carga automática em ambientes persistentes
- Criar um Dockerfile para a aplicação e um Compose completo com API + MySQL
- Criar pipeline de CI para rodar testes e validar migrations
- Adicionar refresh token para renovar o JWT sem novo login
- Implementar roles de acesso (ex: apenas `ADMIN` pode deletar clubes)
- Adicionar observabilidade com Actuator, health checks e logs estruturados
- Melhorar buscas textuais com recursos específicos do banco escolhido
- Criar profile para outros bancos, como PostgreSQL, se houver necessidade de portabilidade

---

## Autor

Projeto desenvolvido para fins de estudo e prática com Java, Spring Boot e desenvolvimento de APIs REST.
