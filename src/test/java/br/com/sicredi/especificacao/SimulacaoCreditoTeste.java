package br.com.sicredi.especificacao;

import br.com.sicredi.requisicao_corpo.CorpoRequisicaoCredito;
import br.com.sicredi.servico.CreditoServico;
import br.com.sicredi.util.GeradorDeDados;
import br.com.sicredi.util.ProvedorArgumentos;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.hamcrest.Matchers.*;

@DisplayName("Simulação de Crédito para CPF")
@Feature("Simular a quantidade de crédito que o CPF pode receber")
public class SimulacaoCreditoTeste {

    private final CreditoServico creditoServico = CreditoServico.getInstancia();

    @Test
    @DisplayName("Consulta de todas as simulações")
    @Description("Espera que retorne todas as simulações contendo: id, nome, cpf, email, valor , parcelas e seguro")
    public void consultarTodasSimulacoesExistentes() {
        creditoServico
                .getConsultarTodasSimulacoes().then()
                .body("[0]",
                        allOf(  hasKey("id"),
                                hasKey("nome"),
                                hasKey("cpf"),
                                hasKey("email"),
                                hasKey("valor"),
                                hasKey("parcelas"),
                                hasKey("seguro"))
                )
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Inserir simulação de CPF com restrição")
    @Description("Espera que ao inserir uma simulação com restrição, retorne status code  400 (BAD REQUEST)")
    public void inserirSimulacaoCpfRestricao() {

        String nomeCompleto = GeradorDeDados.gerarNomeCompleto();
        String cpfRestricao = GeradorDeDados.getCpfComRestricao();
        String email = GeradorDeDados.gerarEmail();
        double valor = GeradorDeDados.gerarDouble(1000, 40000);
        int parcelas = GeradorDeDados.gerarInt(2, 48);
        boolean seguro = GeradorDeDados.gerarBoolean();

        Map<String, Object> requisicao = CorpoRequisicaoCredito
                .inserirSimulacao(nomeCompleto, cpfRestricao, email, valor, parcelas, seguro);

        creditoServico
                .postInserirSimulacao(requisicao).then()
                .body("nome", is(nomeCompleto))
                .body("cpf", is(cpfRestricao))
                .body("email", is(email))
                .body("valor", is((float) valor))
                .body("parcelas", is(parcelas))
                .body("seguro", is(seguro))
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Consultar uma simulação de CPF não cadastrado")
    @Description("Espera que retorne mensagem \"valorCpf\" não encontrado e retorne status code 404 (Not Found)")
    public void consultarSimulacaoCpfNaoCadastrado() {
        String cpfValido = GeradorDeDados.gerarCpf();

        creditoServico
                .getConsultarSimulacaoUnicoCPF(cpfValido).then()
                .body("mensagem", is("CPF " + cpfValido + " não encontrado"))
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }


    @ParameterizedTest(name = "Inserir propriedade \"nome\" : {0} (vazia)")
    @ValueSource(strings = {"", " "})
    @Description("Espera que ao inserir  \"nome\" : (vazio), retorne status code 400 (Bad Request)")
    @Step("Inserindo com  \"nome\" : {0}")
    public void inserirSimulacaoValorNomeVazio(String valorNome) {

        Map<String, Object> requisicao = CorpoRequisicaoCredito.inserirSimulacao
                (
                        valorNome,
                        GeradorDeDados.gerarCpf(),
                        GeradorDeDados.gerarEmail(),
                        GeradorDeDados.gerarDouble(1000, 40000),
                        GeradorDeDados.gerarInt(2, 48),
                        GeradorDeDados.gerarBoolean()
                );
        creditoServico
                .postInserirSimulacao(requisicao).then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @ParameterizedTest(name = "Inserir simulação com CPF inválido - \"cpf\" : {0}")
    @ArgumentsSource(ProvedorArgumentos.class)
    @ValueSource(strings = {" ", ""})
    @Description("Inserir simulação com valor da propriedade \"cpf\" inválido, espera que retorne status cod 400 (Bad Request)")
    @Step("Inserindo com \"cpf\" : {0}")
    public void inserirSimulacaoCpfInvalido(String cpfInvalido) {

        String nomeCompleto = GeradorDeDados.gerarNomeCompleto();
        String email = GeradorDeDados.gerarEmail();
        double valor = GeradorDeDados.gerarDouble(1000, 40000);
        int parcelas = GeradorDeDados.gerarInt(2, 48);
        boolean seguro = GeradorDeDados.gerarBoolean();

        Map<String, Object> requisicao = CorpoRequisicaoCredito
                .inserirSimulacao(nomeCompleto, cpfInvalido, email, valor, parcelas, seguro);

        creditoServico
                .postInserirSimulacao(requisicao).then()
                .body("nome", is(nomeCompleto))
                .body("cpf", anyOf(emptyOrNullString(), blankOrNullString(), is(cpfInvalido)))
                .body("email", is(email))
                .body("valor", is((float) valor))
                .body("parcelas", is(parcelas))
                .body("seguro", is(seguro))
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }


    @Test
    @DisplayName("Inserir simulação com valor de CPF válido")
    @Description("Espera que retorne um objeto com os valores informados anteriormente, com status code 201 (Created)")
    public void inserirSimulacaoCpfValido() {

        String nomeCompleto = GeradorDeDados.gerarNomeCompleto();
        String cpfValido = GeradorDeDados.gerarCpf();
        String email = GeradorDeDados.gerarEmail();
        double valor = GeradorDeDados.gerarDouble(1000, 40000);
        int parcelas = GeradorDeDados.gerarInt(2, 48);
        boolean seguro = GeradorDeDados.gerarBoolean();

        Map<String, Object> requisicao = CorpoRequisicaoCredito.inserirSimulacao(
                nomeCompleto, cpfValido, email, valor, parcelas, seguro
        );

        creditoServico
                .postInserirSimulacao(requisicao).then()
                .body("nome", is(nomeCompleto))
                .body("cpf", is(cpfValido))
                .body("email", is(email))
                .body("valor", is((float) valor))
                .body("parcelas", is(parcelas))
                .body("seguro", is(seguro))
                .statusCode(HttpStatus.SC_CREATED);
    }


    @Test
    @DisplayName("Inserir valor da parcela menor que dois (parcela < 2)")
    @Description("Espera que ao inserir valor da parcela menor que dois (parcela < 2), retorne mensagem de erro: \"Parcelas deve ser igual ou maior que 2\"")
    @Step("Valor informado 1")
    public void inserirParcelasValorMenorQueMinimo() {

        Map<String, Object> requisicao = CorpoRequisicaoCredito.inserirSimulacao
                (
                        GeradorDeDados.gerarNomeCompleto(),
                        GeradorDeDados.gerarCpf(),
                        GeradorDeDados.gerarEmail(),
                        GeradorDeDados.gerarDouble(1000, 40000),
                        1,
                        GeradorDeDados.gerarBoolean()
                );

        creditoServico
                .postInserirSimulacao(requisicao).then()
                .body("erros.parcelas", is("Parcelas deve ser igual ou maior que 2"))
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Inserir valor da parcela maior que 48 (parcela > 48)")
    @Description("Espera que ao inserir valor da parcela maior que 48 (parcela > 48), retorne mensagem de erro: \"Parcelas deve ser igual ou menor que 48\"")
    @Step("Valor informado 49")
    public void inserirParcelasValorMaiorMaximo() {

        Map<String, Object> requisicao = CorpoRequisicaoCredito.inserirSimulacao
                (
                        GeradorDeDados.gerarNomeCompleto(),
                        GeradorDeDados.gerarCpf(),
                        GeradorDeDados.gerarEmail(),
                        GeradorDeDados.gerarDouble(1000, 40000),
                        49,
                        GeradorDeDados.gerarBoolean()
                );

        creditoServico
                .postInserirSimulacao(requisicao).then()
                .body("erros.parcelas", is("Parcelas deve ser igual ou menor que 48"))
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Inserir propriedade \"valor\" maior que 40000.00 (valor > 40000.00)")
    @Description("Espera que ao inserir propriedade \"valor\" maior que 40000.00 (valor > 40000.00), retorne mensagem de erro: \"Valor deve ser menor ou igual a R$ 40.000\"")
    @Step("Valor informado 40000.01")
    public void inserirValorMaiorQueMaximo() {
        Map<String, Object> requisicao = CorpoRequisicaoCredito.inserirSimulacao
                (
                        GeradorDeDados.gerarNomeCompleto(),
                        GeradorDeDados.gerarCpf(),
                        GeradorDeDados.gerarEmail(false),
                        40000.01,
                        GeradorDeDados.gerarInt(2, 48),
                        GeradorDeDados.gerarBoolean()
                );
        creditoServico
                .postInserirSimulacao(requisicao).then()
                .body("erros.valor", is("Valor deve ser menor ou igual a R$ 40.000"))
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Inserir propriedade \"valor\" menor que 1000.00 (valor < 1000.00)")
    @Description("Espera que ao inserir propriedade \"valor\" menor que 1000.00 (valor < 1000.00), retorne mensagem de erro: \"Valor deve ser maior ou igual a R$ 1000.00\"")
    @Step("Valor informado 999.99")
    public void inserirValorMenorQueMinimo() {
        Map<String, Object> requisicao = CorpoRequisicaoCredito.inserirSimulacao
                (
                        GeradorDeDados.gerarNomeCompleto(),
                        GeradorDeDados.gerarCpf(),
                        GeradorDeDados.gerarEmail(false),
                        999.99,
                        GeradorDeDados.gerarInt(2, 48),
                        GeradorDeDados.gerarBoolean()
                );
        creditoServico
                .postInserirSimulacao(requisicao).then()
                .body("erros.valor", is("Valor deve ser maior ou igual a R$ 1000.00"))
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Inserir CPF duplicado na simulação")
    @Description("Espera que quando inserido CPF idêntico ao já cadastrado na simulação, retorne mensagem de erro:  \"CPF duplicado\" e status code 409 (Conflict)")
    public void inserirSimulacaoCpfIdentico() {

        String nomeCompleto = GeradorDeDados.gerarNomeCompleto();
        String cpfValido = GeradorDeDados.gerarCpf();
        String email = GeradorDeDados.gerarEmail();
        double valor = GeradorDeDados.gerarDouble(1000, 40000);
        int parcelas = GeradorDeDados.gerarInt(2, 48);
        boolean seguro = GeradorDeDados.gerarBoolean();

        Map<String, Object> requisicao = CorpoRequisicaoCredito.inserirSimulacao
                (nomeCompleto, cpfValido, email, valor, parcelas, seguro);

        creditoServico.postInserirSimulacao(requisicao);

        creditoServico
                .postInserirSimulacao(requisicao).then()
                .body("mensagem", is("CPF duplicado"))
                .statusCode(HttpStatus.SC_CONFLICT);
    }



    @Test
    @DisplayName("Inserir simulação com e-mail inválido")
    @Description("Espera que ao informar e-mail inválido, retorne mensagem: \"não é um endereço de e-mail\" ou \"E-mail deve ser um e-mail válido\"")
    public void inserirSimulacaoEmailInvalido() {

        Map<String, Object> requisicao = CorpoRequisicaoCredito.inserirSimulacao
                (
                        GeradorDeDados.gerarNomeCompleto(),
                        GeradorDeDados.gerarCpf(),
                        GeradorDeDados.gerarEmail(false),
                        GeradorDeDados.gerarDouble(1000, 40000),
                        GeradorDeDados.gerarInt(2, 48),
                        GeradorDeDados.gerarBoolean()
                );
        creditoServico
                .postInserirSimulacao(requisicao).then()
                .body("erros.email", anyOf(is("não é um endereço de e-mail"), is("E-mail deve ser um e-mail válido")))
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Atualizar as propriedades da simulação informando o CPF")
    @Description("Espera que quando informada propriedades válidas, atualize as mesmas, retornando o objeto com as atualizações")
    public void atualizarSimulacao() {
        String cpfValido = GeradorDeDados.gerarCpf();

        Map<String, Object> requisicao = CorpoRequisicaoCredito.inserirSimulacao
                (
                        GeradorDeDados.gerarNomeCompleto(),
                        cpfValido,
                        GeradorDeDados.gerarEmail(),
                        GeradorDeDados.gerarDouble(1000, 40000),
                        GeradorDeDados.gerarInt(2, 48),
                        GeradorDeDados.gerarBoolean()
                );

        creditoServico.postInserirSimulacao(requisicao);

        String nomeCompleto = GeradorDeDados.gerarNomeCompleto();
        String email = GeradorDeDados.gerarEmail();
        double valor = GeradorDeDados.gerarDouble(1000, 5000);
        int parcelas = GeradorDeDados.gerarInt(2, 48);
        boolean seguro = GeradorDeDados.gerarBoolean();

        Map<String, Object> atualizacao = CorpoRequisicaoCredito.atualizarSimulacao
                (nomeCompleto, cpfValido, email, valor, parcelas, seguro);

        creditoServico
                .putAtualizarSimulacaoCPF(cpfValido, atualizacao).then()
                .body("nome", is(nomeCompleto))
                .body("cpf", is(cpfValido))
                .body("email", is(email))
                .body("valor", is((float) valor))
                .body("parcelas", is(parcelas))
                .body("seguro", is(seguro))
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Atualizar simulação com CPF não cadastrado")
    @Description("Ao tentar atualizar simulação com CPF não cadastrado, retorna mensagem: \"CPF <cpfNaoCadastrado> + \" não encontrado\"")
    public void atualizarSimulacaoCpfNaoCadastrado() {

        String cpfNaoCadastrado = GeradorDeDados.gerarCpf();

        Map<String, Object> requisicao = CorpoRequisicaoCredito.atualizarSimulacao
                (
                        GeradorDeDados.gerarNomeCompleto(),
                        cpfNaoCadastrado,
                        GeradorDeDados.gerarEmail(),
                        GeradorDeDados.gerarDouble(1000, 40000),
                        GeradorDeDados.gerarInt(2, 48),
                        GeradorDeDados.gerarBoolean()
                );

        creditoServico
                .putAtualizarSimulacaoCPF(cpfNaoCadastrado, requisicao).then()
                .body("mensagem", is("CPF " + cpfNaoCadastrado + " não encontrado"))
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Excluir simulação")
    @Description("Espera que ao excluir simulação com \"id\" válido, retorne status code 200 (Ok)")
    public void excluirSimulacao() {
        creditoServico
                .deleteRemoverSimulacaoCPF(String.valueOf(GeradorDeDados.gerarInt(0, 1000)))
                .then()
                .body(is("OK"))
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Excluir simulação com id inválido")
    @Description("Espera que ao excluir simulação com \"id\" inválido, retorne status code 400 (Bad Request)")
    public void excluirSimulacaoValorCpfInValido() {
        creditoServico
                .deleteRemoverSimulacaoCPF(GeradorDeDados.gerarNomeUsuario())
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
