package br.ba.acosta.servicos;

import static br.ba.acosta.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ba.acosta.entidades.Filme;
import br.ba.acosta.entidades.Locacao;
import br.ba.acosta.entidades.Usuario;
import br.ba.acosta.exception.FilmeSemEstoqueException;
import br.ba.acosta.exception.LocadoraException;
import br.ba.acosta.utils.DataUtils;
import org.junit.Assert;
import org.junit.Test;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {

	    if(usuario == null) {
	    	throw new LocadoraException("Usuario vazio");
		}

		if(filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}

		for(Filme filme: filmes) {
			if(filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException("Filme sem estoque");
			}
		}

		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());

		Double valorTotal = 0d;
		for(int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			switch (i) {
				case 2: valorFilme = valorFilme * 0.75; break;
				case 3: valorFilme = valorFilme * 0.5; break;
				case 4: valorFilme = valorFilme * 0.25; break;
				case 5: valorFilme = 0d; break;
			}

			valorTotal += valorFilme;

		}
		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);

		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}

		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	public static void main(String[] args) {
		/*
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
		*/
	}
}
