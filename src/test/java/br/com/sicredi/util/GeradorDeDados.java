package br.com.sicredi.util;

import com.github.javafaker.Faker;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class GeradorDeDados {

    private GeradorDeDados() {
    }

    private final static Faker FAKER = new Faker(new Locale("pt-br"));
    private final static DecimalFormat FORMATO_DECIMAL = new DecimalFormat("#.##");

    private final static List<String> CPF_COM_RESTRICAO = Arrays.asList(
            "97093236014", "60094146012", "84809766080", "62648716050", "26276298085",
            "01317496094", "55856777050", "19626829001", "24094592008", "58063164083"
    );

    public static String gerarNomeCompleto() {
        return FAKER.name().fullName();
    }

    public static String gerarEmail() {
        return gerarNomeUsuario() + "@email.com";
    }

    public static String gerarEmail(boolean valido) {
        return valido ? gerarEmail() : gerarNomeUsuario() + "email.com";
    }

    public static String gerarNomeUsuario() {
        return FAKER.name().username()
                .replaceAll("[âãàáä]", "a").replaceAll("[êèéë]", "e")
                .replaceAll("[îíìï]", "i").replaceAll("[ôõòóö]", "o")
                .replaceAll("[ûúùü]", "u");
    }

    public static boolean gerarBoolean() {
        return FAKER.bool().bool();
    }

    public static String getCpfComRestricao() {
        int numeroAleatorio = new Random().nextInt(CPF_COM_RESTRICAO.size());
        return CPF_COM_RESTRICAO.get(numeroAleatorio);
    }

    public static String gerarCpfInvalido() {
        return FAKER.numerify("###########");
    }

    public static String gerarCpfInvalido(boolean caracteresEspeciais) {
        String cpfInvalido = gerarCpfInvalido();
        return caracteresEspeciais ? adicionarCaracteresEspeciaisCpf(cpfInvalido) : cpfInvalido;
    }

    public static String gerarCpf() {
        int d1;
        int d2;
        int divisao;
        Random random = new Random();

        int n1 = random.nextInt(10);
        int n2 = random.nextInt(10);
        int n3 = random.nextInt(10);
        int n4 = random.nextInt(10);
        int n5 = random.nextInt(10);
        int n6 = random.nextInt(10);
        int n7 = random.nextInt(10);
        int n8 = random.nextInt(10);
        int n9 = random.nextInt(10);
        int soma = n9 * 2 + n8 * 3 + n7 * 4 + n6 * 5 + n5 * 6 + n4 * 7 + n3 * 8 + n2 * 9 + n1 * 10;
        int valor = (soma / 11) * 11;

        d1 = soma - valor;
        divisao = d1 % 11;
        d1 = d1 < 2 ? 0 : 11 - divisao;

        int soma2 = d1 * 2 + n9 * 3 + n8 * 4 + n7 * 5 + n6 * 6 + n5 * 7 + n4 * 8 + n3 * 9 + n2 * 10 + n1 * 11;
        int valor2 = (soma2 / 11) * 11;

        d2 = soma2 - valor2;
        divisao = d2 % 11;
        d2 = d1 < 2 ? 0 : 11 - divisao;

        String textoNumeroConcatenado = String.valueOf(n1) + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9;
        String resultadoDigitos = String.valueOf(d1) + d2;
        return textoNumeroConcatenado + resultadoDigitos;
    }

    public static String gerarCpf(boolean caracteresEspeciais) {
        String cpfGerado = gerarCpf();

        while (CPF_COM_RESTRICAO.contains(cpfGerado))
            cpfGerado = gerarCpf();

        return caracteresEspeciais ? adicionarCaracteresEspeciaisCpf(cpfGerado) : cpfGerado;
    }

    public static double gerarDouble(double valorMinimo, double valorMaximo) {
        return inserirFormatoDecimal(ThreadLocalRandom.current().nextDouble(valorMinimo, valorMaximo));
    }

    public static int gerarInt(int valorMinimo, int valorMaximo) {
        return ThreadLocalRandom.current().nextInt(valorMinimo, valorMaximo);
    }
    private static String adicionarCaracteresEspeciaisCpf(String cpf) {
        return new StringBuilder(cpf).insert(3, ".").insert(7, ".").insert(11, "-").toString();
    }


    private static double inserirFormatoDecimal(double valor) {
        return Double.parseDouble(FORMATO_DECIMAL.format(valor).replace(",", "."));
    }
}
