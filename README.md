## API de controle de Incidentes

### Requisitos para execução da API
- Para execução da aplicação e dos testes é necessário ter o Docker instalado e executando.

### Instruções para execução e testes da API
- Para executar a API, primeiramente é necessário executar o docker-compose.yml, que está na raiz do projeto, com o comando abaixo:
  ```bash
    docker-compose up -d
  ```
- Após a execução do comando acima, a API estará disponível via Swagger no endereço `http://localhost:8080/swagger-ui/index.html`


- Para executar os testes unitários e de integração, basta executar o comando abaixo:
  ```bash
    mvn clean verify
  ```

### Tecnologias utilizadas
- Java 21
  - Utilizei a última versão LTS do Java para garantir a compatibilidade com as versões mais recentes do Spring Boot


- Spring Boot 3.3.5
  - Utilizei o Spring Boot por ser um framework robusto e que facilita a criação de APIs REST


- Maven
  - Utilizei o Maven para gerenciar as dependências, construir e executar os testes do projeto


- Lombok
  - Utilizei o Lombok para reduzir a verbosidade do código e facilitar a leitura além de simplificar algumas funcionalidades (como implementação de logs por exemplo)


- MapStruct
  - Utilizei o MapStruct para facilitar a conversão de DTOs para entidades e vice-versa


- JUnit 5 + Mockito
  - Utilizei o JUnit 5 para implementar os testes unitários e também os testes de integração
  - Utilizei o Mockito para mockar as dependências dos testes unitários


- Testcontainers
  - Utilizei o Testcontainers para facilitar a execução de testes de integração utilizando uma imagem do banco de dados PostgreSQL, garantindo que os testes seriam realizados em um ambiente identico ao ambiente de produção


- Docker
  - Utilizei o Docker para facilitar a execução da aplicação em qualquer ambiente,
    além do Dockerfile que permitiu a criação de uma imagem da aplicação para ser exeutada em qualquer ambiente sem necessidade de instalação de dependências nem configurações adicionais
  - Utilizei o Docker Compose para facilitar a execução de múltiplos containers


- Swagger
  - Utilizei o Swagger UI para disponibilizar a documentação da API de forma visual e interativa


- PostgreSQL
  - Utilizei o PostgreSQL como banco de dados para armazenar os dados dos incidentes por ser um banco de dados relacional e por ser open source


- GitHub Actions
  - Utilizei o Github Actions para automatizar a execução dos testes unitários e de integração a cada pull request feito para a branch master

