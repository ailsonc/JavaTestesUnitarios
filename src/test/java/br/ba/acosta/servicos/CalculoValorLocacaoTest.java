package br.ba.acosta.servicos;

import br.ba.acosta.dao.LocacaoDAO;
import br.ba.acosta.dao.LocacaoDAOFake;
import br.ba.acosta.entidades.Filme;
import br.ba.acosta.entidades.Locacao;
import br.ba.acosta.entidades.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static br.ba.acosta.builders.FilmeBuilder.umFilme;
import static br.ba.acosta.builders.UsuarioBuilder.umUsuario;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
    @InjectMocks
    private LocacaoService service;
    @Mock
    private LocacaoDAO dao;
    @Mock
    private SPCService spcService;


    @Parameter
    public List<Filme> filmes;

    @Parameter(value=1)
    public Double valorLocacao;

    @Parameter(value=2)
    public String nomeTest;

    private static Filme filme = umFilme().agora();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        /*
        service = new LocacaoService();
        LocacaoDAO dao = mock(LocacaoDAO.class);
        service.setLocacaoDAO(dao);

        SPCService spcService = mock(SPCService.class);
        service.setSpcService(spcService);
        */
    }

    @Parameters(name="{2}")
    public static Collection<Object[]> getParametros() {
        return Arrays.asList(new Object[][]{
                { Arrays.asList(filme, filme), 8.0, "2 Filmes: Sem Descontos"},
                { Arrays.asList(filme, filme, filme), 11.0, "3 Filmes: 25%"},
                { Arrays.asList(filme, filme, filme, filme), 13.0, "4 Filmes: 50%"},
                { Arrays.asList(filme, filme, filme, filme, filme), 14.0, "5 Filmes: 75%"},
                { Arrays.asList(filme, filme, filme, filme, filme, filme), 14.0, "6 Filmes: 100%"},
                { Arrays.asList(filme, filme, filme, filme, filme, filme, filme), 18.0, "7 Filmes: Sem Descontos"},
        });
    }

    @Test
    public void deveCalcularValorLocacaoConsiderandoDescontos() throws Exception {
        //cenario
        Usuario usuario = umUsuario().agora();

        //acao
        Locacao locacao = service.alugarFilme(usuario, filmes);

        //verificacao
        assertThat(locacao.getValor(), is(valorLocacao));

    }

}
