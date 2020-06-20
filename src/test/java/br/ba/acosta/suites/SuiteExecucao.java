package br.ba.acosta.suites;

import br.ba.acosta.servicos.CalculadoraTest;
import br.ba.acosta.servicos.CalculoValorLocacaoTest;
import br.ba.acosta.servicos.LocacaoServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//Exemplo de Suite
//@RunWith(Suite.class)
@SuiteClasses({
        CalculadoraTest.class,
        CalculoValorLocacaoTest.class,
        LocacaoServiceTest.class
})
public class SuiteExecucao {

    @BeforeClass
    public static void before() {
        System.out.println("before");
    }

    @AfterClass
    public static void after() {
        System.out.println("after");
    }

}
