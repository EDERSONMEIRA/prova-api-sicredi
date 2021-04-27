package br.com.sicredi.especificacao;

import br.com.sicredi.servico.CreditoServico;
import br.com.sicredi.util.GeradorDeDados;
import br.com.sicredi.util.ProvedorArgumentos;
import io.qameta.allure.*;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.hamcrest.Matchers.is;

@DisplayName("Restrição de crédito")
@Feature("Restrição de crédito")
public class RestricaoCreditoTeste {

    private final CreditoServico creditoServico = CreditoServico.getInstancia();

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Verificar CPF com restrição")
    @Description("Retorna a mensagem de erro \"O CPF tem problema\" para CPF com restrição")
    public void validarCpfComRestricao() {
        String cpfRestricao = GeradorDeDados.getCpfComRestricao();

        creditoServico
                .getConsultarRestricaoCPF(cpfRestricao).then()
                .body("mensagem", is("O CPF " + cpfRestricao + " tem problema"))
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Verificar CPF sem restrição")
    @Description("Deve retornar status code 204 para o CPF que não possuí restrição")
    public void validarCpfSemRestricao() {
        creditoServico
                .getConsultarRestricaoCPF(GeradorDeDados.gerarCpf()).then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Verificar erro para consulta com CPF vazio")
    @Description("Espera status code 404, para consulta com CPF vazio")
    public void validarRestricaoCpfVazio() {
        creditoServico.getConsultarRestricaoCPF(" ").then().statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @ParameterizedTest(name = "Verificar restrição com \"cpf\" : {0}")
    @ArgumentsSource(ProvedorArgumentos.class)
    @Description("Verificar se ao inserir um CPF inválido, retorna o status code 400")
    @Step("Consultando com \"cpf\" : {0}")
    public void verificarEndPointUrlCpfAceitaQualquerTipoDeValor(String valorPropriedade) {
        creditoServico
                .getConsultarRestricaoCPF(valorPropriedade).then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
