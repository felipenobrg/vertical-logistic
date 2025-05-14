# 📦 Sistema de Processamento de Pedidos - LuizaLabs

Este projeto é uma solução para o **Desafio Técnico da Vertical de Logística da LuizaLabs**. Ele processa arquivos de pedidos com largura fixa e os converte para o formato **JSON normalizado**, acessível via API REST.

---

## 🧠 Objetivo

Receber um arquivo de pedidos no formato legado via API REST, processá-lo e expor os dados normalizados também por API REST.

---

## 🏛 Arquitetura

A aplicação segue os princípios da **Clean Architecture** e os padrões de design **SOLID**:

- **Controller**: Lida com requisições e respostas HTTP
- **Service**: Contém a lógica de negócio para processamento dos pedidos
- **Repository**: Responsável pela persistência dos dados (em memória)
- **Parser**: Converte o formato fixo do arquivo para objetos da aplicação

---

## 🛠 Tecnologias Utilizadas

- ☕ Java 17  
- 🚀 Spring Boot 3.4.5  
- 🧰 Lombok  
- ✅ JUnit 5 + Mockito  
- 🌐 Spring Web  
- 📘 Swagger (OpenAPI) para documentação interativa

---

## 🔍 Implementação dos Princípios SOLID

### 1. **SRP - Responsabilidade Única**  
Cada classe tem uma única responsabilidade.  
Exemplo: `FixedWidthOrderFileParser` lida apenas com o parsing do arquivo.

### 2. **OCP - Aberto/Fechado**  
Componentes estão abertos para extensão, fechados para modificação via uso de interfaces.

### 3. **LSP - Substituição de Liskov**  
Classes seguem os contratos definidos pelas interfaces, permitindo substituição segura.

### 4. **ISP - Segregação de Interfaces**  
Interfaces pequenas e específicas, como `OrderFileParser` e `OrderRepository`.

### 5. **DIP - Inversão de Dependência**  
Camadas de alto nível dependem de abstrações e não de implementações concretas.

---

## ⚙️ Funcionalidades

- 📥 Upload de arquivos via API REST
- 📂 Armazenamento dos pedidos em memória
- 🔎 Filtros por **ID do pedido** e por **intervalo de datas**
- 🚫 Tratamento de erros com mensagens claras
- 🧪 Testes unitários completos

---

## 🔗 Endpoints da API

| Método | Endpoint                 | Descrição                                |
|--------|--------------------------|------------------------------------------|
| POST   | `/api/orders/upload`     | Upload e processamento do arquivo        |
| GET    | `/api/orders`            | Recupera todos os pedidos processados    |
| GET    | `/api/orders/filter`     | Filtra pedidos por ID e/ou intervalo de datas |

---

## ▶️ Como Executar

### ✅ Pré-requisitos

- Java 17 ou superior
- Maven

### 🧪 Compilar e Rodar

```bash

# Compilar o projeto
mvn clean install

# Rodar a aplicação
mvn spring-boot:run


## 📑 Documentação Swagger

Após iniciar a aplicação, acesse a documentação interativa:

👉 http://localhost:8080/swagger-ui/index.html

---

## 🧪 Rodando Testes

```bash
mvn test


