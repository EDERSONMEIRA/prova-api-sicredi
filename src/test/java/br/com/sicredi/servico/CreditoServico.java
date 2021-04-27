package br.com.sicredi.servico;
import io.restassured.response.Response;

import java.util.Map;

public class CreditoServico extends ServicoBase {

    private static CreditoServico instancia;
    private final String SIMULCAO_RECURSO = "simulacoes/";

    private CreditoServico() {
        super("localhost");
    }

    public static CreditoServico getInstancia() {
        if (instancia == null)
            instancia = new CreditoServico();
        return instancia;
    }

    public Response getConsultarRestricaoCPF(String cpf) {
        return cliente.get(URL_BASE + "restricoes/" + cpf);
    }

    public Response getConsultarTodasSimulacoes() {
        return cliente.get(URL_BASE + SIMULCAO_RECURSO);
    }

    public Response getConsultarSimulacaoUnicoCPF(String cpf) {
        return   cliente.given()
                .contentType("application/Json")
                .when()
                .get(URL_BASE + SIMULCAO_RECURSO + cpf)
                .then()
                .extract()
                .response();
    }

    public Response postInserirSimulacao(Map<String, Object> corpoRequisicao) {
        return   cliente.given()
                .contentType("application/Json")
                .body(corpoRequisicao)
                .when()
                .post(URL_BASE+ SIMULCAO_RECURSO)
                .then()
                .extract()
                .response();
    }

    public Response putAtualizarSimulacaoCPF(String cpf, Map<String, Object> corpoRequisicao) {
        return   cliente.given()
                .contentType("application/Json")
                .body(corpoRequisicao)
                .when()
                .put(URL_BASE + SIMULCAO_RECURSO + cpf)
                .then()
                .extract()
                .response();
    }

    public Response deleteRemoverSimulacaoCPF(String id) {

       return cliente.given()
                .when()
                .delete(URL_BASE + SIMULCAO_RECURSO + id)
                .then()
                .extract()
                .response();
    }

}
