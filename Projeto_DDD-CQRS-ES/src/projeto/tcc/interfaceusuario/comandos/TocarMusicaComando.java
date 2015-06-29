package projeto.tcc.interfaceusuario.comandos;

import java.util.UUID;


public class TocarMusicaComando implements Comando {
	
	private UUID usuarioUID;
	private String nomeMusica;
	
	public TocarMusicaComando(UUID usuarioUID, String nomeMusica) {
		this.usuarioUID = usuarioUID;
		this.nomeMusica = nomeMusica;
	}
	

	@Override
	public UUID aggregateId() {
		return usuarioUID;
	}

	
	//getters e setter omitidos

	public UUID getLoginID() {
		return usuarioUID;
	}


	public void setLoginID(UUID usuarioUID) {
		this.usuarioUID = usuarioUID;
	}


	public String getNomeMusica() {
		return nomeMusica;
	}


	public void setNomeMusica(String nomeMusica) {
		this.nomeMusica = nomeMusica;
	}


	@Override
	public Integer getVersion() {
		// TODO Auto-generated method stub
		return null;
	}



}
