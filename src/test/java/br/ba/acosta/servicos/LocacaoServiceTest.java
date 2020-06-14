package br.ba.acosta.servicos;

import br.ba.acosta.entidades.Filme;
import br.ba.acosta.entidades.Locacao;
import br.ba.acosta.entidades.Usuario;
import br.ba.acosta.exception.FilmeSemEstoqueException;
import br.ba.acosta.exception.LocadoraException;
import br.ba.acosta.utils.DataUtils;
import static br.ba.acosta.utils.DataUtils.isMesmaData;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static br.ba.acosta.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Test
    public void testeLocacao() throws Exception {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Ailson");
        Filme filme = new Filme("Filme",2, 1.5);

        //acao
        Locacao locacao = service.alugarFilme(usuario,filme);

        //verificacao - import static
        error.checkThat(locacao.getValor(), is(equalTo(1.5)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    //Exception Elegante
    @Test(expected = FilmeSemEstoqueException.class)
    public void testLocacao_filmeSemEstoque() throws Exception {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Ailson");
        Filme filme = new Filme("Filme",0, 1.5);

        //acao
        service.alugarFilme(usuario,filme);
    }

    //Exception Robusta
    @Test
    public void testLocacao_filmeSemEstoque_dois() {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Ailson");
        Filme filme = new Filme("Filme",0, 1.5);

        //acao
        try {
            service.alugarFilme(usuario,filme);
            Assert.fail("Deveria ter lancado uma excecao");
        } catch (Exception e) {
            Assert.assertThat(e.getMessage(), is("Filme sem estoque"));
        }
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    //Exception New
    @Test
    public void testLocacao_filmeSemEstoquetres() throws Exception {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Ailson");
        Filme filme = new Filme("Filme",0, 1.5);

        //antes
        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque");

        //acao
        service.alugarFilme(usuario,filme);

    }

    @Test
    public void testLocacao_usuarioVazio() throws Exception {
        //cenario
        LocacaoService service = new LocacaoService();
        Filme filme = new Filme("Filme",2, 1.5);

        //acao
        try {
            service.alugarFilme(null,filme);
            Assert.fail("Deveria ter lancado uma excecao");
        } catch (LocadoraException e) {
            Assert.assertThat(e.getMessage(), is("Usuario vazio"));
        }

        System.out.println("Forma Robusta");
    }

    @Test
    public void testLocacao_filmeVazio() throws Exception {
        //cenario
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Ailson");

        //antes
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        service.alugarFilme(usuario,null);

        System.out.println("Forma Nova");
    }

}
