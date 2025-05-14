# Sistema de Processamento de Pedidos - LuizaLabs

Este projeto é uma solução para o Desafio Técnico da Vertical de Logística da LuizaLabs. Ele processa arquivos de pedidos com largura fixa para o formato JSON normalizado.

## Arquitetura

Esta aplicação segue os princípios da Clean Architecture e os padrões de design SOLID:

- **Camada de Controller**: Responsável por lidar com requisições e respostas HTTP
- **Camada de Serviço**: Contém a lógica de negócio para o processamento de pedidos
- **Camada de Repositório**: Responsável pela persistência dos dados
- **Camada de Parser**: Processa arquivos de entrada de acordo com as especificações

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.4.5
- Project Lombok
- JUnit 5 com Mockito
- Spring Web

## Implementação dos Princípios SOLID

1. **Princípio da Responsabilidade Única (SRP)**  
   - Cada classe tem uma única responsabilidade  
   - Exemplo: `FixedWidthOrderFileParser` lida apenas com a leitura do formato do arquivo

2. **Princípio Aberto/Fechado (OCP)**  
   - Componentes estão abertos para extensão, mas fechados para modificação  
   - Interfaces permitem diferentes implementações sem alterar o código do cliente

3. **Princípio da Substituição de Liskov (LSP)**  
   - Interfaces são utilizadas para os principais componentes, garantindo que as implementações possam ser substituídas sem impactar a aplicação  
   - Implementações de repositórios e parsers seguem os contratos das interfaces

4. **Princípio da Segregação de Interfaces (ISP)**  
   - Interfaces pequenas e focadas, como `OrderFileParser` e `OrderRepository`  
   - Nenhuma implementação é forçada a implementar métodos desnecessários

5. **Princípio da Inversão de Dependência (DIP)**  
   - Módulos de alto nível dependem de abstrações  
   - Services dependem de interfaces de repositório e parser, não de implementações concretas

## Funcionalidades Principais

- Processamento de arquivos via API REST
- Armazenamento em memória dos pedidos processados
- Filtro por ID do pedido
- Filtro por intervalo de datas
- Tratamento completo de erros
- Cobertura total de testes

## Endpoints da API

- `POST /api/orders/upload` - Upload e processamento de arquivo de pedidos
- `GET /api/orders` - Recupera todos os pedidos processados
- `GET /api/orders/filter` - Filtra pedidos por ID ou intervalo de datas

## Executando a Aplicação

### Pré-requisitos

- Java 17 ou superior
- Maven

### Compilar e Executar

```bash
# Compilar o projeto
mvn clean install

# Executar a aplicação
mvn spring-boot:run
