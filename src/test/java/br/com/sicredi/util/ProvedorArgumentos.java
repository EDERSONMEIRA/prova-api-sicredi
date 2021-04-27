package br.com.sicredi.util;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class ProvedorArgumentos implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(GeradorDeDados.gerarCpf(true)),
                Arguments.of(GeradorDeDados.gerarCpfInvalido(true)),
                Arguments.of(GeradorDeDados.gerarCpfInvalido()),
                Arguments.of(GeradorDeDados.gerarNomeCompleto()),
                Arguments.of(GeradorDeDados.gerarEmail())
        );
    }
}
