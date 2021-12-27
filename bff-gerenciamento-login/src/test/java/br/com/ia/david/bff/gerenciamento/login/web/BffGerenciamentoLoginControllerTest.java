package br.com.ia.david.bff.gerenciamento.login.web;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@WebMvcTest(BffGerenciamentoLoginControllerTest.class)
public class BffGerenciamentoLoginControllerTest {

    private ObjectMapper mapper;
	private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext context;

	@Before
	public void setup() {

		mockMvc = webAppContextSetup(context)
				.build();

		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
	}

	@Test
    public void get_parametro() throws Exception {
		final String url = String.format("/%s", UUID.randomUUID());
		final MockHttpServletRequestBuilder getBuilder = MockMvcRequestBuilders.get(url);

		mockMvc.perform(getBuilder.contentType(MediaType.APPLICATION_JSON));
    }
}
