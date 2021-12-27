package br.com.ia.david.bsn.gerenciamento.login;

import org.junit.Test;

public class BsnGerenciamentoLoginApplicationTest {

	@Test
	public void assertConfig() {
		new BsnGerenciamentoLoginApplication().main(new String[] { "--spring.profiles.active=test" });
	}
}
