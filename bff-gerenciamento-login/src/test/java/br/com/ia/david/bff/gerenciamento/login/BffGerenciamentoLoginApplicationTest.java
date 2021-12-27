package br.com.ia.david.bff.gerenciamento.login;

import org.junit.Test;

public class BffGerenciamentoLoginApplicationTest {

	@Test
	public void assertConfig() {
		new BffGerenciamentoLoginApplication().main(new String[] { "--spring.profiles.active=test" });
	}
}
