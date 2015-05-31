package projeto.tcc.infraestrutura;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;


import projeto.tcc.dominio.eventos.Evento;
import projeto.tcc.infraestrutura.manipuladoreventos.ManipuladorEventos;

@ApplicationScoped
public class Publicador {

	private static Queue<Evento> filaEventos = new LinkedList<>();
	private final ExecutorService pool = Executors.newFixedThreadPool(10);
	private Calendar dataUltimaPublicacao;
	private ManipuladorEventos manipuladorEventos= new ManipuladorEventos();

	
	public Publicador(Evento evento) {
		adicionaEvento(evento);
		init();
	}
	
	@PostConstruct
	public void init(){
//		dataUltimaPublicacao = Calendar.getInstance();
//		TimerTask task = new TimerTask() {
//			@Override
//			public void run() {
//				if (filaEventos.size() == 5
//						|| (!filaEventos.isEmpty() && tempoUltPublicaoMaiorQue30Segundos())) {
//					publicar();
//				}
//
//			}
//		};
//		Timer timer = new Timer();
//		long delay = 0;
//		long intevalPeriod = 1 * 1000;
//		timer.scheduleAtFixedRate(task, delay, intevalPeriod);
	teste(); // esta em faze de testes usando essa tecnologia FUTURE
		
	}
	

	public boolean tempoUltPublicaoMaiorQue30Segundos() {
		return (((Calendar.getInstance().getTimeInMillis() - dataUltimaPublicacao.getTimeInMillis()) / 1000) > 30);
	}
	
	public Future<String> teste(){
		
		 return pool.submit(new Callable<String>() {
		        @Override
		        public String call() throws Exception {
		                publicar2(filaEventos);
						return "Processado";
		        }
		    });
		
	}
	
	public void publicar2(Queue<Evento> filaEventos){
		
		for (Evento evento : filaEventos) {
			manipuladorEventos.trata(evento);
		}
		filaEventos.clear();

	}

	public void publicar() {
		dataUltimaPublicacao = Calendar.getInstance();
		for (Evento evento : filaEventos) {
			manipuladorEventos.trata(evento);
		}
		filaEventos.clear();

	}

	public static void adicionaEventos(List<Evento> eventos) {
		for (Evento evento : eventos) {
			filaEventos.add(evento);
		}
	}

	public static void adicionaEvento(Evento evento) {
		filaEventos.add(evento);
	}


}
