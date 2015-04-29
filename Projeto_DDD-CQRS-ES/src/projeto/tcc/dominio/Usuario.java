package projeto.tcc.dominio;

import java.io.Serializable;

import projeto.tcc.comandos.CadastrarUsuarioComando;
import projeto.tcc.comandos.FazerLoginComando;
import projeto.tcc.eventos.Evento;
import projeto.tcc.eventos.EventoProcessador;
import projeto.tcc.eventos.usuario.UsuarioCadastradoEvento;
import projeto.tcc.infraestrutura.armazenamento.impl.RepositorioUsuarioImpl;


public class Usuario implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String login;
	private String senha;
	private String nome;
	private String CPF;
	

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCPF() {
		return CPF;
	}

	public void setCPF(String cPF) {
		CPF = cPF;
	}

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public void cuidarCadastro(CadastrarUsuarioComando usuario) throws Exception {
//		if (StringUtils.isNullOrEmpty(usuario.getCpf()) || StringUtils.isNullOrEmpty(usuario.getNome())
//				|| StringUtils.isNullOrEmpty(usuario.getLogin()) || !StringUtils.isNullOrEmpty(usuario.getSenha())) {
//			//throw new erro. Nao pode ser gerado o evento pois nao atendeu a regra de negocio
//		}
		
		
		new EventoProcessador().processar((new UsuarioCadastradoEvento(usuario.aggregateId(),usuario.getLogin(),usuario.getSenha())));
//		new RepositorioUsuarioImpl().processarUsuarioCadastradoEvento((new UsuarioCadastradoEvento(usuario.aggregateId(),usuario.getLogin(),usuario.getSenha())));
	}
	
	
	public Evento logar(FazerLoginComando login) {
		

		//retorna evento
		
		return null;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
		

}
