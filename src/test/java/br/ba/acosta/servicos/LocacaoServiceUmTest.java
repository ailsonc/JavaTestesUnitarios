package br.ba.acosta.servicos;

import br.ba.acosta.entidades.Filme;
import br.ba.acosta.entidades.Locacao;
import br.ba.acosta.entidades.Usuario;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static br.ba.acosta.utils.DataUtils.isMesmaData;
import static br.ba.acosta.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class LocacaoServiceUmTest {
    @Test
    public void testeLocacao() {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Ailson");
        Filme filme = new Filme("Filme",2, 1.5);

        try {
            //acao
            Locacao locacao = service.alugarFilme(usuario,filme);

            //verificacao - old
            Assert.assertTrue(locacao.getValor() == 1.5);
            Assert.assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
            Assert.assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));

            //verificacao - new
            Assert.assertEquals(1.5, locacao.getValor(), 0.01);
            assertThat(locacao.getValor(), CoreMatchers.is(1.5));

            //verificacao - import static
            assertThat(locacao.getValor(), is(1.5));
            assertThat(locacao.getValor(), is(equalTo(1.5)));
            assertThat(locacao.getValor(), is(not(2.5))); //Negação
            assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
            assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
