package projeto.tcc.interfaceusuario;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import projeto.tcc.dominio.Usuario;
import projeto.tcc.interfaceusuario.servico.ServicoUsuarioFacade;

@ManagedBean
@ViewScoped
public class LoginUsuarioBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7918764410608856865L;
	@Inject
	private ServicoUsuarioFacade servicoUsuarioFacade;
	private String login;
	private String senha;
	
	
	public void logar(ActionEvent actionEvent){
		servicoUsuarioFacade.logar(getLogin(), getSenha());
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

}
