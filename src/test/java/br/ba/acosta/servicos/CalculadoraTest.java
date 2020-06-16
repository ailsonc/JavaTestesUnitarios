package br.ba.acosta.servicos;

import br.ba.acosta.exception.NaoPodeDividirPorZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculadoraTest {

    private Calculadora calc;

    @Before
    public void setup() {
        calc = new Calculadora();
    }

    @Test
    public void deveSomarDoisValores() {
        //cenario
        int a = 5;
        int b = 3;

        //acao
        int resultado = calc.somar(a,b);

        //verificacao
        Assert.assertEquals(8, resultado);
    }

    @Test
    public void deveSubtraiDoisValores() {
        //cenario
        int a = 5;
        int b = 3;

        //acao
        int resultado = calc.subtrair(a,b);

        //verificacao
        Assert.assertEquals(2, resultado);
    }

    @Test
    public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
        //cenario
        int a = 6;
        int b = 3;

        //acao
        int resultado = calc.divide(a,b);

        //verificacao
        Assert.assertEquals(2, resultado);
    }

    @Test(expected = NaoPodeDividirPorZeroException.class)
    public void deveLancarExcecaoAoDividirDoisValores() throws NaoPodeDividirPorZeroException {
        //cenario
        int a = 10;
        int b = 0;

        //acao
        calc.divide(a,b);
    }
}
