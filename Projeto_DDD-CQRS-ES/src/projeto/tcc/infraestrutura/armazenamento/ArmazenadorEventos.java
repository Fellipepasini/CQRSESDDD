package projeto.tcc.infraestrutura.armazenamento;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import projeto.tcc.dominio.eventos.Evento;
import projeto.tcc.infraestrutura.Conexao;
import projeto.tcc.infraestrutura.ControladorVersao;
import projeto.tcc.infraestrutura.EventoPublicador;
import projeto.tcc.infraestrutura.manipuladoreventos.musica.MusicaAdicionadaFavoritoManipulador;
import projeto.tcc.infraestrutura.manipuladoreventos.musica.MusicaAdicionadaManipulador;
import projeto.tcc.infraestrutura.manipuladoreventos.musica.PlayListAdicionadaManipulador;
import projeto.tcc.infraestrutura.manipuladoreventos.usuario.UsuarioCadastradoManipulador;
import projeto.tcc.infraestrutura.manipuladoreventos.usuario.UsuarioEditadoManipulador;
import projeto.tcc.infraestrutura.manipuladoreventos.usuario.UsuarioLogadoManipulador;

import com.mysql.jdbc.PreparedStatement;

public class ArmazenadorEventos {
	
	private static EventoPublicador publicador;
	private static Connection connection;

	  static {
	      publicador = new EventoPublicador();
	      carregaAssinantes();
	  }
	  
	
	private static void carregaAssinantes() {
		publicador.assina(new MusicaAdicionadaManipulador());
		publicador.assina(new MusicaAdicionadaFavoritoManipulador());
		publicador.assina(new UsuarioCadastradoManipulador());
		publicador.assina(new UsuarioEditadoManipulador());
		publicador.assina(new MusicaAdicionadaManipulador());
		publicador.assina(new UsuarioLogadoManipulador());
		publicador.assina(new PlayListAdicionadaManipulador());
	}


	public static void salvarEvento(Evento evento) {
		connection = Conexao.getConectionEventSource();
		if (evento.getAggregateId() != null && evento.getVersion() != null) {

			try {
				PreparedStatement pstmt1 = null;
				pstmt1 = (PreparedStatement) connection
						.prepareStatement("insert into eventstore(aggregate_id,events, version, groupVersion) values(?,?,?,?)",
								PreparedStatement.RETURN_GENERATED_KEYS);
				insereValoresInsert(pstmt1, evento);
				pstmt1.executeUpdate();
				pstmt1.close();
				
				salvaOuAtualizaAgregado(evento.getAggregateId(),evento.getClazz(), evento.getVersion());
				List<Evento> eventos = new ArrayList<>();
				eventos.add(evento);
				publicador.publicaEventos(eventos);
				
				ControladorVersao.removeIDAgregadoCache(evento.getAggregateId().toString());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				Conexao.fechaConexao();
			}
		}
	}

	
	public static void salvaOuAtualizaAgregado(UUID aggregateID, Class<?> clazz, Long version) throws SQLException {
		boolean jaExisteAgregado = jaExisteAgregado(aggregateID.toString());
		if (jaExisteAgregado) {
			atualizaUltimaVersaoAgregado(aggregateID, version);
		}else{
			insereAgregado(aggregateID, clazz, version);
		}
	}


	private static void insereAgregado(UUID aggregateID, Class<?> clazz,
			Long version) throws SQLException {
		PreparedStatement pstmt2 = null;
		pstmt2 = (PreparedStatement) connection.prepareStatement(
				"insert into aggregates(aggregate_id,type, version) values(?,?,?)",
				PreparedStatement.RETURN_GENERATED_KEYS);

		pstmt2.setString(1, aggregateID.toString());
		pstmt2.setString(2, clazz.getName());
		pstmt2.setLong(3, version);
		pstmt2.executeUpdate();
	}
	
	private static boolean jaExisteAgregado(String aggregateID) throws SQLException{
		PreparedStatement pstmt2 = null;
			pstmt2 = (PreparedStatement) connection.prepareStatement(
					"SELECT 1 from aggregates WHERE  aggregate_id = ?");
			pstmt2.setString(1, aggregateID);
			ResultSet rs = pstmt2.executeQuery();
			if (rs.next()) {
				return true;
			}
		return false;
	}
	
	
	public static void atualizaUltimaVersaoAgregado(UUID aggregateID, Long version) {
		PreparedStatement pstmt2 = null;
		try {
			pstmt2 = (PreparedStatement) connection.prepareStatement(
					"UPDATE aggregates SET version = ? WHERE aggregate_id = ? ");
			pstmt2.setLong(1, version);
			pstmt2.setString(2, aggregateID.toString());
			pstmt2.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static List<Evento> recuperaEventos(String id) {
		List<Evento> eventos = new ArrayList<>();
		Evento evento = null;
		ObjectInputStream objectIn = null;
		connection = Conexao.getConectionEventSource();
		try {
			PreparedStatement pstmt = (PreparedStatement) connection.
			prepareStatement("select events from eventstore where aggregate_id = ? order by version");
			
			pstmt.setString(1, id);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				byte[] buf = rs.getBytes("events");
				if (buf != null) {
					objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
					evento = (Evento) objectIn.readObject();
					eventos.add(evento);
				}
			}
			return eventos;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			Conexao.fechaConexao();
		}
		return null;
	}
	
	
	
	public static Long getUltimaVersaoAgregado(String aggregateID) {
		connection = Conexao.getConectionEventSource();
		try {
			PreparedStatement pstmt = (PreparedStatement) connection
					.prepareStatement("select version from aggregates where aggregate_id = ?");
			pstmt.setString(1, aggregateID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getLong("version");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			Conexao.fechaConexao();
		}
		return 0L;
	}

	
	
	
	
	public static void salvarEventos(List<Evento> evs) throws EventosNulosExceptions {
		validaListaEventosNulasOuVazias(evs);
		
		connection = Conexao.getConectionEventSource();
		PreparedStatement pstmt1 = null;
		try {
			connection.setAutoCommit(false);
			pstmt1 = (PreparedStatement) connection
					.prepareStatement("insert into eventstore(aggregate_id,events, version, groupVersion) values(?,?,?,?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
			processaEventos(evs, pstmt1);
			
			connection.commit();
			pstmt1.close();
			publicador.publicaEventos(evs);
			ControladorVersao.removeIDAgregadoCache(evs.get(0).getAggregateId().toString());
		} catch (Exception e) {
			  trataExcecao();
		} finally {
			Conexao.fechaConexao();
		}
	}


	private static void processaEventos(List<Evento> evs,
			PreparedStatement pstmt1) throws IOException, SQLException {
		for (Evento evento : evs) {
			if (evento.getAggregateId() != null && evento.getVersion() != null) {
				insereValoresInsert(pstmt1, evento);
				pstmt1.executeUpdate();
				salvaOuAtualizaAgregado(evento.getAggregateId(), evento.getClazz(), evento.getVersion());

			}
		}
	}
	


	private static void validaListaEventosNulasOuVazias(List<Evento> evs)
			throws EventosNulosExceptions {
		if (evs == null || evs.isEmpty()) {
			throw new EventosNulosExceptions();
		}
	}


	private static void insereValoresInsert(PreparedStatement pstmt1,
			Evento evento) throws IOException, SQLException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(evento);
		oos.flush();
		oos.close();
		bos.close();
		byte[] dadosEvento = bos.toByteArray();
		pstmt1.setString(1, evento.getAggregateId().toString());
		pstmt1.setObject(2, dadosEvento);
		pstmt1.setLong(3, evento.getVersion());
		pstmt1.setLong(4, evento.getGroupVersion());
	}


	private static void trataExcecao() {
		try{
			 if(connection!=null){
				 connection.rollback();
			 }	
		  }catch(SQLException se2){
		         se2.printStackTrace();
		  }
	}
	
}
