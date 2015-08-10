import java.util.UUID;

import org.junit.Test;

import projeto.tcc.dominio.entidades.usuario.Usuario;
import projeto.tcc.interfaceusuario.comandos.CadastrarUsuarioComando;
import projeto.tcc.interfaceusuario.dto.CriarUsuarioDTO;
import static org.junit.Assert.*;

public class UsuarioTest {

	Usuario usuario;

	@Test
	public void deveCriarUsuario() {
		try {
			usuario = new Usuario();
			CriarUsuarioDTO criarUsuarioDTO = new CriarUsuarioDTO();
			criarUsuarioDTO.setCpf("9999999999");
			criarUsuarioDTO.setEmail("teste@gmail.com");
			criarUsuarioDTO.setLogin("teste");
			criarUsuarioDTO.setSenha("teste");
			criarUsuarioDTO.setVersion(0);
			usuario.criarCadastro(new CadastrarUsuarioComando(UUID.randomUUID(), criarUsuarioDTO));
			assertTrue(true);
		} catch (Exception e) {
			fail("N�o deveria ter passado aqui");
		}

	}
	
	@Test
	public void deveFalharCriarUsuarioDuplicado() {
		try {
			usuario = new Usuario();
			CriarUsuarioDTO criarUsuarioDTO = new CriarUsuarioDTO();
			criarUsuarioDTO.setCpf("9999999999");
			criarUsuarioDTO.setEmail("teste@gmail.com");
			criarUsuarioDTO.setLogin("teste");
			criarUsuarioDTO.setSenha("teste");
			criarUsuarioDTO.setVersion(0);
			UUID randomUUID = UUID.randomUUID();
			usuario.criarCadastro(new CadastrarUsuarioComando(randomUUID, criarUsuarioDTO));
			usuario.criarCadastro(new CadastrarUsuarioComando(randomUUID, criarUsuarioDTO));
			fail("N�o deveria ter passado aqui");
		} catch (Exception e) {
			if (e.getMessage().contains("J� existe um usu�rio com esse cpf")) {
				assertTrue(true);
			}else{
				fail("N�o deveria ter passado aqui");
			}
		}
	}
	
	
//	@Test
//	public void deveFalharUsuarioEditadoMesmaVersao() {
//	}
}
