package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.repository.CondominioRepository;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CondominioResource REST controller.
 *
 * @see CondominioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CondominioResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_RAZAO_SOCIAL = "SAMPLE_TEXT";
    private static final String UPDATED_RAZAO_SOCIAL = "UPDATED_TEXT";
    private static final String DEFAULT_CNPJ = "66.741.263/0001-60";
    private static final String UPDATED_CNPJ = "66.741.263/0001-60";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final DateTime DEFAULT_DATA_CADASTRO = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATA_CADASTRO = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATA_CADASTRO_STR = dateTimeFormatter.print(DEFAULT_DATA_CADASTRO);

    private static final Integer DEFAULT_TELEFONE = 1;
    private static final Integer UPDATED_TELEFONE = 2;

    @Inject
    private CondominioRepository condominioRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCondominioMockMvc;

    private Condominio condominio;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CondominioResource condominioResource = new CondominioResource();
        ReflectionTestUtils.setField(condominioResource, "condominioRepository", condominioRepository);
        this.restCondominioMockMvc = MockMvcBuilders.standaloneSetup(condominioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        condominio = new Condominio();
        condominio.setRazaoSocial(DEFAULT_RAZAO_SOCIAL);
        condominio.setCnpj(DEFAULT_CNPJ);
        condominio.setAtivo(DEFAULT_ATIVO);
        condominio.setDataCadastro(DEFAULT_DATA_CADASTRO);
        condominio.setTelefone(DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    public void createCondominio() throws Exception {
        int databaseSizeBeforeCreate = condominioRepository.findAll().size();

        // Create the Condominio

        restCondominioMockMvc.perform(post("/api/condominios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(condominio)))
                .andExpect(status().isCreated());

        // Validate the Condominio in the database
        List<Condominio> condominios = condominioRepository.findAll();
        assertThat(condominios).hasSize(databaseSizeBeforeCreate + 1);
        Condominio testCondominio = condominios.get(condominios.size() - 1);
        assertThat(testCondominio.getRazaoSocial()).isEqualTo(DEFAULT_RAZAO_SOCIAL);
        assertThat(testCondominio.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testCondominio.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testCondominio.getDataCadastro().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testCondominio.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    public void checkRazaoSocialIsRequired() throws Exception {
        int databaseSizeBeforeTest = condominioRepository.findAll().size();
        // set the field null
        condominio.setRazaoSocial(null);

        // Create the Condominio, which fails.

        restCondominioMockMvc.perform(post("/api/condominios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(condominio)))
                .andExpect(status().isBadRequest());

        List<Condominio> condominios = condominioRepository.findAll();
        assertThat(condominios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCnpjIsRequired() throws Exception {
        int databaseSizeBeforeTest = condominioRepository.findAll().size();
        // set the field null
        condominio.setCnpj(null);

        // Create the Condominio, which fails.

        restCondominioMockMvc.perform(post("/api/condominios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(condominio)))
                .andExpect(status().isBadRequest());

        List<Condominio> condominios = condominioRepository.findAll();
        assertThat(condominios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = condominioRepository.findAll().size();
        // set the field null
        condominio.setDataCadastro(null);

        // Create the Condominio, which fails.

        restCondominioMockMvc.perform(post("/api/condominios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(condominio)))
                .andExpect(status().isBadRequest());

        List<Condominio> condominios = condominioRepository.findAll();
        assertThat(condominios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCondominios() throws Exception {
        // Initialize the database
        condominioRepository.saveAndFlush(condominio);

        // Get all the condominios
        restCondominioMockMvc.perform(get("/api/condominios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(condominio.getId().intValue())))
                .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL.toString())))
                .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ.toString())))
                .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
                .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(DEFAULT_DATA_CADASTRO_STR)))
                .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)));
    }

    @Test
    @Transactional
    public void getCondominio() throws Exception {
        // Initialize the database
        condominioRepository.saveAndFlush(condominio);

        // Get the condominio
        restCondominioMockMvc.perform(get("/api/condominios/{id}", condominio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(condominio.getId().intValue()))
            .andExpect(jsonPath("$.razaoSocial").value(DEFAULT_RAZAO_SOCIAL.toString()))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ.toString()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.dataCadastro").value(DEFAULT_DATA_CADASTRO_STR))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE));
    }

    @Test
    @Transactional
    public void getNonExistingCondominio() throws Exception {
        // Get the condominio
        restCondominioMockMvc.perform(get("/api/condominios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCondominio() throws Exception {
        // Initialize the database
        condominioRepository.saveAndFlush(condominio);

		int databaseSizeBeforeUpdate = condominioRepository.findAll().size();

        // Update the condominio
        condominio.setRazaoSocial(UPDATED_RAZAO_SOCIAL);
        condominio.setCnpj(UPDATED_CNPJ);
        condominio.setAtivo(UPDATED_ATIVO);
        condominio.setDataCadastro(UPDATED_DATA_CADASTRO);
        condominio.setTelefone(UPDATED_TELEFONE);


        restCondominioMockMvc.perform(put("/api/condominios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(condominio)))
                .andExpect(status().isOk());

        // Validate the Condominio in the database
        List<Condominio> condominios = condominioRepository.findAll();
        assertThat(condominios).hasSize(databaseSizeBeforeUpdate);
        Condominio testCondominio = condominios.get(condominios.size() - 1);
        assertThat(testCondominio.getRazaoSocial()).isEqualTo(UPDATED_RAZAO_SOCIAL);
        assertThat(testCondominio.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testCondominio.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testCondominio.getDataCadastro().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testCondominio.getTelefone()).isEqualTo(UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    public void deleteCondominio() throws Exception {
        // Initialize the database
        condominioRepository.saveAndFlush(condominio);

		int databaseSizeBeforeDelete = condominioRepository.findAll().size();

        // Get the condominio
        restCondominioMockMvc.perform(delete("/api/condominios/{id}", condominio.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Condominio> condominios = condominioRepository.findAll();
        assertThat(condominios).hasSize(databaseSizeBeforeDelete - 1);
    }
}