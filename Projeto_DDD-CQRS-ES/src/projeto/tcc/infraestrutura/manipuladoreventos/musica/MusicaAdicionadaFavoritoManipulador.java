
package projeto.tcc.infraestrutura.manipuladoreventos.musica;

import java.sql.Connection;

import projeto.tcc.dominio.eventos.Evento;
import projeto.tcc.dominio.eventos.musica.MusicaAdicionadaFavoritosEvento;
import projeto.tcc.infraestrutura.Conexao;
import projeto.tcc.infraestrutura.Subscriber;

import com.mysql.jdbc.PreparedStatement;

public class MusicaAdicionadaFavoritoManipulador implements Subscriber<Evento>{

	
	public MusicaAdicionadaFavoritoManipulador() {
		// TODO Auto-generated constructor stub
	}
	
	public MusicaAdicionadaFavoritoManipulador(MusicaAdicionadaFavoritosEvento evento) {
		trata(evento);
	}
	
	public void trata(MusicaAdicionadaFavoritosEvento evento) {
		insereViews(evento);
	
	}

	private void insereViews(MusicaAdicionadaFavoritosEvento musicaAdicionadaFavoritoEvento) {
		Connection conexao = Conexao.getConectionReader();
		try {
			PreparedStatement pstmt1 = null;
			pstmt1 = (PreparedStatement) conexao.prepareStatement("insert into " +
					"musicasfavoritousuario(aggregateID,nome, duracao) " +
					"values(?,?,?)", 
					PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt1.setString(1, musicaAdicionadaFavoritoEvento.getAggregateId().toString());
			pstmt1.setString(2, musicaAdicionadaFavoritoEvento.getNomeMusica());
			pstmt1.setString(3, null);
			pstmt1.executeUpdate();
			pstmt1.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}finally{
			Conexao.fechaConexao();
		}
	}

	@Override
	public void getPublication(Evento arg) {
		if (arg instanceof MusicaAdicionadaFavoritosEvento) {
			insereViews((MusicaAdicionadaFavoritosEvento) arg);
		}
		
	}


}
