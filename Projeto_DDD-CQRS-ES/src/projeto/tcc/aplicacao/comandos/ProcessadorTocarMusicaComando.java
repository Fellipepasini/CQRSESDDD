package projeto.tcc.aplicacao.comandos;


import projeto.tcc.dominio.entidades.musica.PlayList;
import projeto.tcc.dominio.eventos.EventoProcessador;
import projeto.tcc.dominio.eventos.musica.MusicaTocadaEvento;
import projeto.tcc.dominio.eventos.musica.PlayListAdicionadaEvento;
import projeto.tcc.interfaceusuario.comandos.Comando;
import projeto.tcc.interfaceusuario.comandos.TocarMusicaComando;

public class ProcessadorTocarMusicaComando implements ProcessadorComandos{

	@Override
	public void execute(Comando comando) throws Exception {
		TocarMusicaComando tocarMusicaComando = (TocarMusicaComando) comando; 
//		PosProcessadorComandos.validaVersaoComando(comando);
		EventoProcessador eventoProcessador = new EventoProcessador();
		eventoProcessador.processarEvento((new MusicaTocadaEvento(tocarMusicaComando.aggregateId(), tocarMusicaComando.getNomeMusica(),  0)));
		eventoProcessador.processarAggregado(tocarMusicaComando.aggregateId(), PlayList.class, tocarMusicaComando.getVersion());
		
	}

}
