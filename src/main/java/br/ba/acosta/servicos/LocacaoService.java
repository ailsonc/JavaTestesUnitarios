package br.ba.acosta.servicos;

import static br.ba.acosta.utils.DataUtils.adicionarDias;

import java.util.Date;

import br.ba.acosta.entidades.Filme;
import br.ba.acosta.entidades.Locacao;
import br.ba.acosta.entidades.Usuario;
import br.ba.acosta.exception.FilmeSemEstoqueException;
import br.ba.acosta.exception.LocadoraException;
import br.ba.acosta.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) throws FilmeSemEstoqueException, LocadoraException {

	    if(usuario == null) {
	    	throw new LocadoraException("Usuario vazio");
		}

		if(filme == null) {
			throw new LocadoraException("Filme vazio");
		}

		if(filme.getEstoque() == 0) {
			throw new FilmeSemEstoqueException("Filme sem estoque");
		}

		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	public static void main(String[] args) {
		//cenario
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Ailson");
		Filme filme = new Filme("Filme",2, 1.5);

		//acao
        Locacao locacao = null;
        try {
            locacao = service.alugarFilme(usuario,filme);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //verificacao
		System.out.println(locacao.getValor() == 1.5);
		System.out.println(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		System.out.println(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
	}
}
