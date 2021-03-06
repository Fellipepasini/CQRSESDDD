package projeto.tcc.aplicacao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import projeto.tcc.aplicacao.ServicoMusicaLeitura;
import projeto.tcc.dominio.entidades.musica.Musica;
import projeto.tcc.infraestrutura.armazenamento.repositorio.impl.RepositorioMusicaImpl;

public class ServicoMusicaLeituraImpl implements ServicoMusicaLeitura, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5992327488947705463L;
	private RepositorioMusicaImpl repositorioMusica = new RepositorioMusicaImpl();

	@Override
	public List<Musica> listarTodasMusicas() {
		return repositorioMusica.listarTodasMusicas();
	}

	public Musica carregaMusicaPorNome(String nome) {
		return repositorioMusica.recuperaMusicaPeloNome(nome);
	}

	@Override
	public Set<Musica> listarMinhasMusicas(String aggregateID) {
		return repositorioMusica.recuperarMinhasMusicas(aggregateID);
	}

	@Override
	public Set<Musica> listarMinhasMusicasFavorito(String aggregateID) {
		return repositorioMusica.recuperarMinhasMusicasFavorito(aggregateID);
	}

}
