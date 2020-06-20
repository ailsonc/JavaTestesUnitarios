package br.ba.acosta.servicos;

import br.ba.acosta.exception.NaoPodeDividirPorZeroException;

public class Calculadora {
    public int somar(int a, int b) {
        System.out.println("Método Somar");
        return a + b;
    }

    public int subtrair(int a, int b) {
        return a - b;
    }

    public int divide(int a, int b) throws NaoPodeDividirPorZeroException {
        if( b == 0) {
            throw new NaoPodeDividirPorZeroException();
        }
        return a / b;
    }

    public void imprime() {
        System.out.println("Passei aqui");
    }
}
