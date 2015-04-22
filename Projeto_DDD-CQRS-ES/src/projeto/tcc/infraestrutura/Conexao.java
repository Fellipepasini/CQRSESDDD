package projeto.tcc.infraestrutura;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import projeto.tcc.eventos.Evento;
import projeto.tcc.eventos.usuario.UsuarioCadastradoEvento;

import com.mysql.jdbc.PreparedStatement;

public class Conexao {

	Connection connection;
	String query = "select * from aggregates where aggregate_id = ?";


	public void getConection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		}

		try {
			connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/eventsource", "root", "mysql");


		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (connection != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}

	}

	public void writeJavaObject(Evento evento) throws Exception {

		PreparedStatement pstmt3 = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		pstmt3 = (PreparedStatement) connection
				.prepareStatement(query);
		pstmt3.setInt(1,1);
		
		ResultSet rs = pstmt3.executeQuery();
		
		
		// Se ja existe um agregado com aquele id, entao vc apenas registra o evento apontando para ele
		if(rs.next()){
			pstmt1 = (PreparedStatement) connection
					.prepareStatement(
							"insert into eventstore(aggregate_id,version,evento) values(?,?,?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
		}
		
		
		//se ainda nao existe um agregado correspondente ao id, voce insere na tabela de agregados e registra o evento
		else{
			 pstmt2 = (PreparedStatement) connection
					.prepareStatement(
							"insert into aggregates(aggregate_id,type,version) values(?,?,?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
			 pstmt1 = (PreparedStatement) connection
					.prepareStatement(
							"insert into eventstore(aggregate_id,version,evento) values(?,?,?)",
							PreparedStatement.RETURN_GENERATED_KEYS);
		}

		

		// set input parameters
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(evento);
		oos.flush();
		oos.close();
		bos.close();
		byte[] dadosEvento = bos.toByteArray();
		pstmt1.setInt(1, evento.getIdAgregado());
		pstmt1.setInt(2, 1);
		pstmt1.setObject(3, dadosEvento);
		if(pstmt2 != null){
		pstmt2.setInt(1, evento.getIdAgregado());
		pstmt2.setString(2, null);
		pstmt2.setInt(3, 1);
		pstmt2.executeUpdate();
		}
		pstmt1.executeUpdate();

		// get the generated key for the id
		rs.close();
		pstmt1.close();

		System.out.println("writeJavaObject: done serializing: ");
	}

	public Object readJavaObject(long id) throws Exception {
		PreparedStatement pstmt = (PreparedStatement) connection
				.prepareStatement("select dados_evento from eventstore where id_event_store = ?");
		pstmt.setLong(1, id);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		byte[] buf = rs.getBytes("dados_evento");
		ObjectInputStream objectIn = null;
		if (buf != null)
			objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
		UsuarioCadastradoEvento object = (UsuarioCadastradoEvento) objectIn
				.readObject();
		String className = object.getClass().getName();
		rs.close();
		pstmt.close();
		System.out.println(object.getComentario());
		System.out.println("Deserialization Successful."
				+ "\nDeserialized Class: " + className);
		return object;
	}

}