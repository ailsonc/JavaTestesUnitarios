package br.ba.acosta.servicos;

import br.ba.acosta.entidades.Filme;
import br.ba.acosta.entidades.Locacao;
import br.ba.acosta.entidades.Usuario;
import br.ba.acosta.exception.FilmeSemEstoqueException;
import br.ba.acosta.exception.LocadoraException;

import static br.ba.acosta.matchers.MatchersProprios.caiEm;
import static br.ba.acosta.matchers.MatchersProprios.caiNumaSegunda;
import static br.ba.acosta.utils.DataUtils.isMesmaData;

import br.ba.acosta.matchers.DiaSemanaMatcher;
import br.ba.acosta.matchers.MatchersProprios;
import br.ba.acosta.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static br.ba.acosta.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

public class LocacaoServiceTest {

    private LocacaoService service;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        service = new LocacaoService();
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Ailson");
        List<Filme> filmes = Arrays.asList(
           new Filme("Filme",2, 1.5)
        );

        //acao
        Locacao locacao = service.alugarFilme(usuario,filmes);

        //verificacao - import static
        error.checkThat(locacao.getValor(), is(equalTo(1.5)));
        error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
    }

    //Exception Elegante
    @Test(expected = FilmeSemEstoqueException.class)
    public void naoDeveAlugarFilmeSemEstoqueElegante() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Ailson");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme",0, 1.5)
        );

        //acao
        service.alugarFilme(usuario,filmes);
    }

    //Exception Robusta
    @Test
    public void naoDeveAlugarFilmeSemEstoqueRobusto() {
        //cenario
        Usuario usuario = new Usuario("Ailson");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme",0, 1.5)
        );

        //acao
        try {
            service.alugarFilme(usuario,filmes);
            Assert.fail("Deveria ter lancado uma excecao");
        } catch (Exception e) {
            assertThat(e.getMessage(), is("Filme sem estoque"));
        }
    }

    //Exception New
    @Test
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Ailson");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme",0, 1.5)
        );

        //antes
        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque");

        //acao
        service.alugarFilme(usuario,filmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws Exception {
        //cenario
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme",2, 1.5)
        );

        //acao
        try {
            service.alugarFilme(null,filmes);
            Assert.fail("Deveria ter lancado uma excecao");
        } catch (LocadoraException e) {
            assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Ailson");

        //antes
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        service.alugarFilme(usuario,null);
    }

    @Test
    public void naoDeveDevolverNaSegundaFilmeAlugadonoSabado() throws Exception {
        assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = new Usuario("Ailson");
        List<Filme> filmes = Arrays.asList(
                new Filme("Filme 1", 1, 5.0)
        );

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        // Exemplo 1
        boolean isSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
        Assert.assertTrue(isSegunda);

        // Exemplo 2
        assertThat(locacao.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));

        // Exemplo 3
        assertThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY));

        // Exemplo 4
        assertThat(locacao.getDataRetorno(), caiNumaSegunda());
    }
}
