# Finance API

## Descrição

Finance API é uma aplicação backend desenvolvida em Java usando o framework Spring Boot. Esta API gerencia cartões de crédito e transações financeiras, oferecendo endpoints para operações CRUD e consultas avançadas sobre os dados.

## Funcionalidades

- **Gerenciamento de Cartões**: 
  - Criação, visualização, atualização e exclusão de cartões de crédito.
- **Gerenciamento de Transações**:
  - Criação, visualização, atualização e exclusão de transações financeiras associadas aos cartões.
- **Consultas e Relatórios**:
  - Consultas sobre transações por ID de cartão, nome do banco e data.
  - Relatórios de totais de transações por cartão, banco e período.

## Endpoints da API

### Cartões

- **Criar Cartão**
  - `POST /banks`
  - Corpo da requisição:
    ```json
    {
      "name": "Nome do Banco",
      "creditLimit": "1000.00"
    }
    ```
  - Resposta: Detalhes do cartão criado.

- **Obter Todos os Cartões**
  - `GET /banks/list`
  - Resposta: Lista de todos os cartões.

- **Obter Cartão por ID**
  - `GET /banks/{id}`
  - Parâmetros de URL:
    - `id`: ID do cartão
  - Resposta: Detalhes do cartão.

- **Atualizar Cartão**
  - `PUT /banks/{id}`
  - Corpo da requisição:
    ```json
    {
      "name": "Novo Nome do Banco",
      "creditLimit": "1200.00"
    }
    ```
  - Parâmetros de URL:
    - `id`: ID do cartão
  - Resposta: Detalhes do cartão atualizado.

- **Excluir Cartão**
  - `DELETE /banks/{id}`
  - Parâmetros de URL:
    - `id`: ID do cartão
  - Resposta: Confirmação da exclusão.

### Transações

- **Criar Transação**
  - `POST /transactions`
  - Corpo da requisição:
    ```json
    {
      "date": "2024-08-06",
      "amount": "50.00",
      "bank": {
        "id": "1"
      }
    }
    ```
  - Resposta: Detalhes da transação criada.

- **Obter Todas as Transações**
  - `GET /transactions/list`
  - Resposta: Lista de todas as transações.

- **Obter Transação por ID**
  - `GET /transactions/{id}`
  - Parâmetros de URL:
    - `id`: ID da transação
  - Resposta: Detalhes da transação.

- **Atualizar Transação**
  - `PUT /transactions/{id}`
  - Corpo da requisição:
    ```json
    {
      "date": "2024-08-06",
      "amount": "100.00",
      "bank": {
        "id": "1"
      }
    }
    ```
  - Parâmetros de URL:
    - `id`: ID da transação
  - Resposta: Detalhes da transação atualizada.

- **Excluir Transação**
  - `DELETE /transactions/{id}`
  - Parâmetros de URL:
    - `id`: ID da transação
  - Resposta: Confirmação da exclusão.

### Consultas Avançadas

- **Obter Transações por ID do Cartão**
  - `GET /transactions/bank-id/{bankId}`
  - Parâmetros de URL:
    - `bankId`: ID do cartão
  - Resposta: Lista de transações para o cartão especificado.

- **Obter Transações por Nome do Banco**
  - `GET /transactions/bank/{name}`
  - Parâmetros de URL:
    - `name`: Nome do banco
  - Resposta: Lista de transações para o banco especificado.

- **Obter Total por ID do Cartão**
  - `GET /transactions/total/bank-id/{bankId}`
  - Parâmetros de URL:
    - `bankId`: ID do cartão
  - Resposta: Total das transações para o cartão especificado.

- **Obter Total por Nome do Banco**
  - `GET /transactions/total/bank/{name}`
  - Parâmetros de URL:
    - `name`: Nome do banco
  - Resposta: Total das transações para o banco especificado.

- **Obter Transações por Data**
  - `GET /transactions/date/{month}-{year}`
  - Parâmetros de URL:
    - `month`: Mês (MM)
    - `year`: Ano (YYYY)
  - Resposta: Lista de transações para o período especificado.

- **Obter Total por Data**
  - `GET /transactions/total/date/{month}-{year}`
  - Parâmetros de URL:
    - `month`: Mês (MM)
    - `year`: Ano (YYYY)
  - Resposta: Total das transações para o período especificado.

## Configuração do Projeto

### Requisitos

- Java 17 ou superior
- Maven ou Gradle
- IDE (IntelliJ IDEA, Eclipse, etc.)

### Instalação e Execução

1. **Clone o Repositório**
   ```bash
   git clone https://github.com/seu-usuario/finance-api.git
