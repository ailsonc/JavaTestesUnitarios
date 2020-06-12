package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class LocacaoServiceTest extends TestCase {

    @Test
    public void alugarFilme() {
        LocacaoService service = new LocacaoService();
        Usuario usuario = new Usuario("Ailson");
        Filme filme = new Filme("Filme",2, 1.5);

        //acao
        Locacao locacao = service.alugarFilme(usuario,filme);

        //verificacao
        Assert.assertTrue(locacao.getValor() == 1.5);
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
        Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
    }
}
