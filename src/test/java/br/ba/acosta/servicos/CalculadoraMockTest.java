package br.ba.acosta.servicos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CalculadoraMockTest {

    @Mock
    private Calculadora calcMock;

    @Spy
    private Calculadora calSpy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void devoMostrarDiferencaEntreMockSpy() {
        //expectativa
        when(calcMock.somar(1,2)).thenReturn(5);
        //when(calSpy.somar(1,2)).thenReturn(5);
        doReturn(5).when(calSpy).somar(1,2);
        //when(calcMock.somar(2,2)).thenCallRealMethod();
        doCallRealMethod().when(calcMock).imprime();
        //doNothing().when(calSpy).imprime();

        System.out.println("Mock: " + calcMock.somar(1,2));
        System.out.println("Mock: " + calcMock.somar(1,5));
        System.out.println("Mock: " + calcMock.somar(2,2));

        //Spy não funciona com interfaces, so com classes
        System.out.println("Spy: " + calSpy.somar(1,2));
        //System.out.println("Spy: " + calSpy.somar(1,5));

        System.out.println("Mock: ");
        calcMock.imprime();
        System.out.println("Spy: ");
        calSpy.imprime();
    }

    @Test
    public void testMatchers() {
        Calculadora calc = mock(Calculadora.class);
        when(calc.somar(eq(1),anyInt())).thenReturn(5);
        assertEquals(5,calc.somar(1,5) );
    }

    @Test
    public void testArgCapt() {
        Calculadora calc = mock(Calculadora.class);
        ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
        when(calc.somar(argCapt.capture(),argCapt.capture())).thenReturn(5);
        assertEquals(5,calc.somar(1,5) );
        //System.out.println(argCapt.getAllValues());
    }

}
