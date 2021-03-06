package br.ba.acosta.servicos;

import br.ba.acosta.builders.LocacaoBuilder;
import br.ba.acosta.dao.LocacaoDAO;
import br.ba.acosta.entidades.Filme;
import br.ba.acosta.entidades.Locacao;
import br.ba.acosta.entidades.Usuario;
import br.ba.acosta.exception.FilmeSemEstoqueException;
import br.ba.acosta.exception.LocadoraException;

import static br.ba.acosta.builders.FilmeBuilder.umFilme;
import static br.ba.acosta.builders.LocacaoBuilder.umLocacao;
import static br.ba.acosta.builders.UsuarioBuilder.umUsuario;
import static br.ba.acosta.matchers.MatchersProprios.*;

import br.ba.acosta.matchers.DiaSemanaMatcher;
import br.ba.acosta.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.mockito.Mockito.*;

public class LocacaoServiceTest {

    @InjectMocks
    private LocacaoService service;

    @Mock
    private LocacaoDAO dao;
    @Mock
    private SPCService spcService;
    @Mock
    private EmailService emailService;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        /*
        service = new LocacaoService();

        dao = mock(LocacaoDAO.class);
        service.setLocacaoDAO(dao);

        spcService = mock(SPCService.class);
        service.setSpcService(spcService);

        emailService = mock(EmailService.class);
        service.setEmailService(emailService);
         */
    }

    @Test
    public void deveAlugarFilme() throws Exception {
        assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(
                umFilme().comValor(1.5).agora()
        );

        //acao
        Locacao locacao = service.alugarFilme(usuario,filmes);

        //verificacao - import static
        error.checkThat(locacao.getValor(), is(equalTo(1.5)));

        //obterDataComDiferencaDias
        //error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        error.checkThat(locacao.getDataLocacao(), ehHoje());

        //obterDataComDiferencaDias
        //error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
        error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
    }

    //Exception Elegante
    @Test(expected = FilmeSemEstoqueException.class)
    public void naoDeveAlugarFilmeSemEstoqueElegante() throws Exception {
        //cenario
        Usuario usuario = new Usuario("Ailson");
        List<Filme> filmes = Arrays.asList(
                umFilme().semEstoque().agora()
        );

        //acao
        service.alugarFilme(usuario,filmes);
    }

    //Exception Robusta
    @Test
    public void naoDeveAlugarFilmeSemEstoqueRobusto() {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(
                umFilme().semEstoque().agora()
        );

        //acao
        try {
            service.alugarFilme(usuario,filmes);
            //verificacao
            fail("Deveria ter lancado uma excecao");
        } catch (Exception e) {
            //verificacao
            assertThat(e.getMessage(), is("Filme sem estoque"));
        }
    }

    //Exception New
    @Test
    public void naoDeveAlugarFilmeSemEstoque() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(
                umFilme().semEstoque().agora()
        );

        //verificacao
        exception.expect(Exception.class);
        exception.expectMessage("Filme sem estoque");

        //acao
        service.alugarFilme(usuario,filmes);
    }

    @Test
    public void naoDeveAlugarFilmeSemUsuario() throws Exception {
        //cenario
        List<Filme> filmes = Arrays.asList(
                umFilme().agora()
        );

        //acao
        try {
            service.alugarFilme(null,filmes);
            //verificacao
            fail("Deveria ter lancado uma excecao");
        } catch (LocadoraException e) {
            //verificacao
            assertThat(e.getMessage(), is("Usuario vazio"));
        }
    }

    @Test
    public void naoDeveAlugarFilmeSemFilme() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();

        //verificacao
        exception.expect(LocadoraException.class);
        exception.expectMessage("Filme vazio");

        //acao
        service.alugarFilme(usuario,null);
    }

    @Test
    public void naoDeveDevolverNaSegundaFilmeAlugadonoSabado() throws Exception {
        assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(
                umFilme().agora()
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

    @Test
    public void naoDeveAlugarFilmeparaNegativadoSPC() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(
                umFilme().agora()
        );

        when(spcService.possuiNegativacao(usuario)).thenReturn(true);

        //verificacao
        exception.expect(LocadoraException.class);
        exception.expectMessage("Usuário Negativado");

        //acao
        service.alugarFilme(usuario, filmes);

    }

    @Test
    public void naoDeveAlugarFilmeparaNegativadoSPCRobusto() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(
                umFilme().agora()
        );

        //when(spcService.possuiNegativacao(usuario)).thenReturn(true);
        //verificao generica
        when(spcService.possuiNegativacao(any(Usuario.class))).thenReturn(true);

        //acao
        try {
            service.alugarFilme(usuario, filmes);
            //verificacao
            fail() ;
        } catch (FilmeSemEstoqueException e) {
            e.printStackTrace();
        } catch (LocadoraException e) {
            //verificacao
            assertThat(e.getMessage(), is("Usuário Negativado")) ;
        }

        //verificacao
        verify(spcService).possuiNegativacao(usuario);
    }

    @Test
    public void deveEnviarEmailParaLocacoesAtrasadas() {
        //cenario
        Usuario usuario1 = umUsuario().comNOme("Usuario 1 atrasado").agora();
        Usuario usuario2 = umUsuario().comNOme("Usuario 2 em dia").agora();
        Usuario usuario3 = umUsuario().comNOme("Usuario 3 atrasado").agora();
        List<Locacao> locacaos = Arrays.asList(
                umLocacao().comUsuario(usuario1).atrasado().agora(),
                umLocacao().comUsuario(usuario2).agora(),
                umLocacao().comUsuario(usuario3).atrasado().agora(),
                umLocacao().comUsuario(usuario3).atrasado().agora()
        );

        when(dao.obterLocacoesPendentes()).thenReturn(locacaos);

        //acao
        service.notificarAtrasos();

        //verificacao
        verify(emailService).notificarAtraso(usuario1);
        verify(emailService, never()).notificarAtraso(usuario2);
        verify(emailService, times(2)).notificarAtraso(usuario3);
        verify(emailService, atLeastOnce()).notificarAtraso(usuario3);
        //verificao generica
        verify(emailService, times(3)).notificarAtraso(any(Usuario.class));
        verify(emailService, atLeastOnce()).notificarAtraso(any(Usuario.class));
        verifyNoMoreInteractions(emailService);
    }

    @Test
    public void deveTratarErroNoSPC() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();
        List<Filme> filmes = Arrays.asList(
                umFilme().agora()
        );

        when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catratrófica"));

        //verificacao
        exception.expect(LocadoraException.class);
        exception.expectMessage("Problema com SPC, tente nocamente");

        //acao
        service.alugarFilme(usuario, filmes);
    }

    @Test
    public void deveProrrogarUmaLocacao() {
        //cenario
        Locacao locacao = umLocacao().agora();
        //acao
        service.porrogarLocacao(locacao, 3);
        //verificacao
        ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
        verify(dao).salvar(argCapt.capture());
        Locacao locacaoRetornado = argCapt.getValue();

        error.checkThat(locacaoRetornado.getValor(), is(12.0));
        error.checkThat(locacaoRetornado.getDataLocacao(), ehHoje());
        error.checkThat(locacaoRetornado.getDataRetorno(), ehHojeComDiferencaDias(3));
    }
}
