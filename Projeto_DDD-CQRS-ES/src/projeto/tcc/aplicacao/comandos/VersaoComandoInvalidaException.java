package projeto.tcc.aplicacao.comandos;

public class VersaoComandoInvalidaException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public VersaoComandoInvalidaException() {
		super("Houveram altera��es durante o processamento de sua requisi��o. Recarrega e tente novamente.");
	}

}
