package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Convidado;
import br.com.unifieo.tmc.repository.ConvidadoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ConvidadoResource REST controller.
 *
 * @see ConvidadoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ConvidadoResourceTest {

    private static final String DEFAULT_NOME = "SAMPLE_TEXT";
    private static final String UPDATED_NOME = "UPDATED_TEXT";
    private static final String DEFAULT_CPF = "SAMPLE_TEXT";
    private static final String UPDATED_CPF = "UPDATED_TEXT";
    private static final String DEFAULT_EMAIL = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL = "UPDATED_TEXT";

    @Inject
    private ConvidadoRepository convidadoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restConvidadoMockMvc;

    private Convidado convidado;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConvidadoResource convidadoResource = new ConvidadoResource();
        ReflectionTestUtils.setField(convidadoResource, "convidadoRepository", convidadoRepository);
        this.restConvidadoMockMvc = MockMvcBuilders.standaloneSetup(convidadoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        convidado = new Convidado();
        convidado.setNome(DEFAULT_NOME);
        convidado.setCpf(DEFAULT_CPF);
        convidado.setEmail(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createConvidado() throws Exception {
        int databaseSizeBeforeCreate = convidadoRepository.findAll().size();

        // Create the Convidado

        restConvidadoMockMvc.perform(post("/api/convidados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(convidado)))
                .andExpect(status().isCreated());

        // Validate the Convidado in the database
        List<Convidado> convidados = convidadoRepository.findAll();
        assertThat(convidados).hasSize(databaseSizeBeforeCreate + 1);
        Convidado testConvidado = convidados.get(convidados.size() - 1);
        assertThat(testConvidado.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testConvidado.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testConvidado.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = convidadoRepository.findAll().size();
        // set the field null
        convidado.setNome(null);

        // Create the Convidado, which fails.

        restConvidadoMockMvc.perform(post("/api/convidados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(convidado)))
                .andExpect(status().isBadRequest());

        List<Convidado> convidados = convidadoRepository.findAll();
        assertThat(convidados).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConvidados() throws Exception {
        // Initialize the database
        convidadoRepository.saveAndFlush(convidado);

        // Get all the convidados
        restConvidadoMockMvc.perform(get("/api/convidados"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(convidado.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }

    @Test
    @Transactional
    public void getConvidado() throws Exception {
        // Initialize the database
        convidadoRepository.saveAndFlush(convidado);

        // Get the convidado
        restConvidadoMockMvc.perform(get("/api/convidados/{id}", convidado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(convidado.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConvidado() throws Exception {
        // Get the convidado
        restConvidadoMockMvc.perform(get("/api/convidados/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConvidado() throws Exception {
        // Initialize the database
        convidadoRepository.saveAndFlush(convidado);

		int databaseSizeBeforeUpdate = convidadoRepository.findAll().size();

        // Update the convidado
        convidado.setNome(UPDATED_NOME);
        convidado.setCpf(UPDATED_CPF);
        convidado.setEmail(UPDATED_EMAIL);
        

        restConvidadoMockMvc.perform(put("/api/convidados")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(convidado)))
                .andExpect(status().isOk());

        // Validate the Convidado in the database
        List<Convidado> convidados = convidadoRepository.findAll();
        assertThat(convidados).hasSize(databaseSizeBeforeUpdate);
        Convidado testConvidado = convidados.get(convidados.size() - 1);
        assertThat(testConvidado.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testConvidado.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testConvidado.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void deleteConvidado() throws Exception {
        // Initialize the database
        convidadoRepository.saveAndFlush(convidado);

		int databaseSizeBeforeDelete = convidadoRepository.findAll().size();

        // Get the convidado
        restConvidadoMockMvc.perform(delete("/api/convidados/{id}", convidado.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Convidado> convidados = convidadoRepository.findAll();
        assertThat(convidados).hasSize(databaseSizeBeforeDelete - 1);
    }
}
