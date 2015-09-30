package projeto.tcc.aplicacao;

import projeto.tcc.interfaceusuario.comandos.CadastrarUsuarioComando;
import projeto.tcc.interfaceusuario.comandos.DeslogarComando;
import projeto.tcc.interfaceusuario.comandos.EditarUsuarioComando;
import projeto.tcc.interfaceusuario.comandos.FazerLoginComando;

public interface ServicoUsuarioEscrita {

//	String logarUsuario(FazerLoginComando fazerLoginComando) throws Exception;
	
	//outros m�todos omitidos
	void cadastrarUsuario(CadastrarUsuarioComando cadastrarUsuarioComando);

	void editarInformacoesUsuario(EditarUsuarioComando editarUsuarioComando);
	
//	boolean deslogarUsuario(DeslogarComando deslogarComando);
	
	void logarUsuario(FazerLoginComando fazerLoginComando) throws Exception;
	String existeUsuarioComEsseLogin(FazerLoginComando fazerLoginComando)throws Exception;

}
