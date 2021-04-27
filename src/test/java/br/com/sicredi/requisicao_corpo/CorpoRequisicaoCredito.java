package br.com.sicredi.requisicao_corpo;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CorpoRequisicaoCredito {

    public static Map<String, Object> inserirSimulacao(@Nullable String nome, @Nullable String cpf, @Nullable String email,
                                                       Double valor, Integer parcelas, Boolean seguro) {
        return new HashMap<String, Object>() {
            {
                put("nome", nome);
                put("cpf", cpf);
                put("email", email);
                put("valor", valor);
                put("parcelas", parcelas);
                put("seguro", seguro);
            }
        };
    }

    public static Map<String, Object> atualizarSimulacao(@Nullable String nome, @Nullable String cpf, @Nullable String email,
                                                         Double valor, Integer parcelas, Boolean seguro) {
        return inserirSimulacao(nome, cpf, email, valor, parcelas, seguro);
    }
}
