package projeto.tcc.aplicacao.comandos.processador;


import projeto.tcc.aplicacao.comandos.ProcessadorComandos;
import projeto.tcc.dominio.eventos.EventoProcessador;
import projeto.tcc.dominio.eventos.musica.MusicaTocadaEvento;
import projeto.tcc.infraestrutura.ControladorVersao;
import projeto.tcc.interfaceusuario.comandos.Comando;
import projeto.tcc.interfaceusuario.comandos.TocarMusicaComando;

public class ProcessadorTocarMusicaComando implements ProcessadorComandos{

	@Override
	public void execute(Comando comando) throws Exception {
		TocarMusicaComando tocarMusicaComando = (TocarMusicaComando) comando; 
		Long version = ControladorVersao.getProximaVersao();
		EventoProcessador eventoProcessador = new EventoProcessador();
		eventoProcessador.processarEvento((new MusicaTocadaEvento(tocarMusicaComando.aggregateId(), tocarMusicaComando.getNomeMusica(),tocarMusicaComando.getIdMusica(),version, version)));
		
	}

}
