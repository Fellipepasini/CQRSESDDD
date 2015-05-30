package projeto.tcc.interfaceusuario.controle;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import projeto.tcc.aplicacao.ServicoUsuarioEscrita;
import projeto.tcc.aplicacao.ServicoUsuarioLeitura;
import projeto.tcc.interfaceusuario.comandos.FazerLoginComando;
import projeto.tcc.interfaceusuario.dto.FazerLoginDTO;

@Named
@SessionScoped
public class LoginUsuarioBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7918764410608856865L;
	@Inject
	private ServicoUsuarioEscrita servicoUsuarioEscrita;
	@Inject
	private FazerLoginDTO fazerLoginDTO;
	
	
	public String logar(){
		try {
			servicoUsuarioEscrita.logarUsuario(new FazerLoginComando(fazerLoginDTO));
			return "ouvirMusica.xhtml?faces-redirect=true";
		} catch (Exception e) {
			FacesContext fc = FacesContext.getCurrentInstance();
			fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
		} 
		return "";
	}


	public FazerLoginDTO getFazerLoginDTO() {
		return fazerLoginDTO;
	}


	public void setFazerLoginDTO(FazerLoginDTO fazerLoginDTO) {
		this.fazerLoginDTO = fazerLoginDTO;
	}



}