package br.com.sicredi.servico;


import com.google.common.collect.ImmutableMap;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.util.Map;

abstract class ServicoBase {

    protected RestAssured cliente;
    public static String URL_BASE = "http://localhost:8080/api/v1/";
    protected static final Map<String, Object> CABECALHO = ImmutableMap.of("Content-Type", ContentType.JSON);


    public ServicoBase(String localhost) {
    }
}
