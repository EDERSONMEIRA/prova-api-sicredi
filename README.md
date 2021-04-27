# Sicredi Automação API

## Requisitos

* JDK 8 ou superior
*  <a href="https://maven.apache.org/download.cgi" target="_blank">Apache Maven</a>

**Observação:** depois que fizer o download do Apache Maven, não se esqueça de adicioná-lo como uma variável de ambiente no seu sistema operacional.

## Ferramentas e bibliotecas
* <a href="https://junit.org/junit5/" target="_blank">JUnit5</a>
* <a href="https://rest-assured.io/" target="_blank">Rest Assured</a>
* <a href="https://github.com/DiUS/java-faker" target="_blank">Java Faker</a>
* <a href="http://allure.qatools.ru/" target="_blank">Allure reports</a>

### Comandos CLI para executar os testes

Na raiz do projeto execute oa seguintes comandos:

* Somente testes 
```bash
  mvn test
 ```
  
* Testes e gerar relatório HTML com Allure Reports
```bash
  mvn clean test allure:report
```
  
* Executar o relatório localmente no seu browser
```bash
  mvn allure:serve
 ```

--------------------------------------------

### Observações

Na raiz do repositório há arquivos 
```relatorioDeBugs.pdf,``` 
```relatorioDeEstrategiasDeTeste.pdf,```
```relatorioDeSugestoesDeMelhorias.pdf,```
informando a estratégia abordada, inconsistências e sugestões de melhorias.  
