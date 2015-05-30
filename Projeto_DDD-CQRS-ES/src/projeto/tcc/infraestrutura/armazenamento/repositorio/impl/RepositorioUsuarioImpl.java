package projeto.tcc.infraestrutura.armazenamento.repositorio.impl;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import projeto.tcc.dominio.Usuario;
import projeto.tcc.dominio.eventos.Evento;
import projeto.tcc.dominio.eventos.usuario.UsuarioCadastradoEvento;
import projeto.tcc.infraestrutura.Conexao;
import projeto.tcc.infraestrutura.armazenamento.ArmazenadorEventos;
import projeto.tcc.infraestrutura.armazenamento.repositorio.RepositorioUsuario;

import com.mysql.jdbc.PreparedStatement;

public class RepositorioUsuarioImpl implements RepositorioUsuario{
	
	private static final Logger LOGGER = Logger.getLogger(RepositorioUsuarioImpl.class.getName());

	
	@Override
	public Usuario getUsuario(String id) {
		return null;
	}
	
	
	public void processarUsuarioCadastradoEvento(UsuarioCadastradoEvento e) throws Exception{
		ArmazenadorEventos.salvarEvento(e);
	}


	@Override
	public Usuario getUsuarioPorCPF(String CPF) {
		Usuario usuario = null;
		try {
			
			PreparedStatement pstmt =  (PreparedStatement)Conexao.getConectionReader().prepareStatement("SELECT * from baseleitura.dadosusuario where CPF = ?");
			pstmt.setString(1, CPF);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				usuario = new Usuario();
				usuario.setCPF(CPF);
				usuario.setDataNascimento(rs.getDate("dataNascimento"));
				usuario.setEmail(rs.getString("email"));
				usuario.setId(rs.getInt("id"));
				usuario.setLogin(rs.getString("login"));
				usuario.setNome(rs.getString("nome"));
				usuario.setSenha(rs.getString("senha"));
				usuario.setSenha(rs.getString("sexo"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return usuario;
	}


	@Override
	public String existeUsuarioComEsseLogin(String login) {
		String aggregateID = null;
		try {
			
			PreparedStatement pstmt =  (PreparedStatement)Conexao.getConectionReader().prepareStatement("SELECT aggregateID from baseleitura.dadosusuario where login = ?");
			pstmt.setString(1, login);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				aggregateID = rs.getString("aggregateID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return aggregateID;
	}

	@Override
	public Usuario getUsuarioPorAggregateID(String aggregateID) {
		Usuario usuario;
		try {
			List<Evento> eventos = ArmazenadorEventos.recuperaEventos(aggregateID);
			if (!eventos.isEmpty()) {
				usuario = new Usuario();
				this.constroiEntidade(eventos, usuario);
				return usuario;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void constroiEntidade(List<Evento> history, Usuario usuario) 	{
		Method method;
		try {
			for (Evento evento : history) {
				method = usuario.getClass().getMethod("aplicaMudanca", evento.getClass());
				method.invoke(usuario, evento);
			}
		} catch (SecurityException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (NoSuchMethodException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (IllegalAccessException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (InvocationTargetException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}


}