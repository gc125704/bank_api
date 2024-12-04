package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.edu.utfpr.bankapi.service.InterestCalculator;

public class InterestCalculatorTest {
    @Test
    void deveriaCalcularJurosCompostos(){
        //arrange -> configurações para o teste
        float valor = 1000;
        float taxa = 1.5f;
        int prazo = 6; // o prazo esta em meses

        //act -> executar a funcionalidade a ser testada

        var res = InterestCalculator.calcularJuros(valor, taxa, prazo);

        //assert -> verificar o resultado com o esperado

        Assertions.assertEquals(93.44,res);
    }

    @Test
    void deveriaCalcularJurosCompostosPorDia(){
        //arrange -> configurações para o teste
        float valor = 1000;
        float taxa = 0.05f;
        int prazo = 30; // o prazo esta em meses

        //act -> executar a funcionalidade a ser testada

        var res = InterestCalculator.calcularJuros(valor, taxa, prazo);

        //assert -> verificar o resultado com o esperado

        Assertions.assertEquals(15.11,res);
    }

    void deveriaCalcularJurosSimples(){
        // nao vai ser implementado
    }
}
