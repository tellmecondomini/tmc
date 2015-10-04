package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Morador;
import br.com.unifieo.tmc.repository.MoradorRepository;

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
import org.joda.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.unifieo.tmc.domain.enumeration.Sexo;
import br.com.unifieo.tmc.domain.enumeration.TipoMorador;

/**
 * Test class for the MoradorResource REST controller.
 *
 * @see MoradorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MoradorResourceTest {

    private static final String DEFAULT_NOME = "SAMPLE_TEXT";
    private static final String UPDATED_NOME = "UPDATED_TEXT";
    private static final String DEFAULT_CPF = "827.755.763-97";
    private static final String UPDATED_CPF = "827.755.763-97";

    private static final Sexo DEFAULT_SEXO = Sexo.M;
    private static final Sexo UPDATED_SEXO = Sexo.F;
    private static final String DEFAULT_EMAIL = "morador@email.com.br";
    private static final String UPDATED_EMAIL = "morador@gmail.com.br";
    private static final String DEFAULT_SENHA = "SAMPLE_TEXT";
    private static final String UPDATED_SENHA = "UPDATED_TEXT";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = new LocalDate(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = new LocalDate();

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final Boolean DEFAULT_BLOQUEIA_AGENDAMENTO = false;
    private static final Boolean UPDATED_BLOQUEIA_AGENDAMENTO = true;

    private static final TipoMorador DEFAULT_TIPO = TipoMorador.PROPRIETARIO;
    private static final TipoMorador UPDATED_TIPO = TipoMorador.MORADOR;

    @Inject
    private MoradorRepository moradorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMoradorMockMvc;

    private Morador morador;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MoradorResource moradorResource = new MoradorResource();
        ReflectionTestUtils.setField(moradorResource, "moradorRepository", moradorRepository);
        this.restMoradorMockMvc = MockMvcBuilders.standaloneSetup(moradorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        morador = new Morador();
        morador.setNome(DEFAULT_NOME);
        morador.setCpf(DEFAULT_CPF);
        morador.setSexo(DEFAULT_SEXO);
        morador.setEmail(DEFAULT_EMAIL);
        morador.setSenha(DEFAULT_SENHA);
        morador.setDataNascimento(DEFAULT_DATA_NASCIMENTO);
        morador.setAtivo(DEFAULT_ATIVO);
        morador.setBloqueiaAgendamento(DEFAULT_BLOQUEIA_AGENDAMENTO);
        morador.setTipo(DEFAULT_TIPO);
    }

    @Test
    @Transactional
    public void createMorador() throws Exception {
        int databaseSizeBeforeCreate = moradorRepository.findAll().size();

        // Create the Morador

        restMoradorMockMvc.perform(post("/api/moradors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(morador)))
                .andExpect(status().isCreated());

        // Validate the Morador in the database
        List<Morador> moradors = moradorRepository.findAll();
        assertThat(moradors).hasSize(databaseSizeBeforeCreate + 1);
        Morador testMorador = moradors.get(moradors.size() - 1);
        assertThat(testMorador.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMorador.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testMorador.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testMorador.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testMorador.getSenha()).isEqualTo(DEFAULT_SENHA);
        assertThat(testMorador.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testMorador.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testMorador.getBloqueiaAgendamento()).isEqualTo(DEFAULT_BLOQUEIA_AGENDAMENTO);
        assertThat(testMorador.getTipo()).isEqualTo(DEFAULT_TIPO);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = moradorRepository.findAll().size();
        // set the field null
        morador.setNome(null);

        // Create the Morador, which fails.

        restMoradorMockMvc.perform(post("/api/moradors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(morador)))
                .andExpect(status().isBadRequest());

        List<Morador> moradors = moradorRepository.findAll();
        assertThat(moradors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCpfIsRequired() throws Exception {
        int databaseSizeBeforeTest = moradorRepository.findAll().size();
        // set the field null
        morador.setCpf(null);

        // Create the Morador, which fails.

        restMoradorMockMvc.perform(post("/api/moradors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(morador)))
                .andExpect(status().isBadRequest());

        List<Morador> moradors = moradorRepository.findAll();
        assertThat(moradors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = moradorRepository.findAll().size();
        // set the field null
        morador.setEmail(null);

        // Create the Morador, which fails.

        restMoradorMockMvc.perform(post("/api/moradors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(morador)))
                .andExpect(status().isBadRequest());

        List<Morador> moradors = moradorRepository.findAll();
        assertThat(moradors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSenhaIsRequired() throws Exception {
        int databaseSizeBeforeTest = moradorRepository.findAll().size();
        // set the field null
        morador.setSenha(null);

        // Create the Morador, which fails.

        restMoradorMockMvc.perform(post("/api/moradors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(morador)))
                .andExpect(status().isBadRequest());

        List<Morador> moradors = moradorRepository.findAll();
        assertThat(moradors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataNascimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = moradorRepository.findAll().size();
        // set the field null
        morador.setDataNascimento(null);

        // Create the Morador, which fails.

        restMoradorMockMvc.perform(post("/api/moradors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(morador)))
                .andExpect(status().isBadRequest());

        List<Morador> moradors = moradorRepository.findAll();
        assertThat(moradors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMoradors() throws Exception {
        // Initialize the database
        moradorRepository.saveAndFlush(morador);

        // Get all the moradors
        restMoradorMockMvc.perform(get("/api/moradors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(morador.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF.toString())))
                .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].senha").value(hasItem(DEFAULT_SENHA.toString())))
                .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
                .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
                .andExpect(jsonPath("$.[*].bloqueiaAgendamento").value(hasItem(DEFAULT_BLOQUEIA_AGENDAMENTO.booleanValue())))
                .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())));
    }

    @Test
    @Transactional
    public void getMorador() throws Exception {
        // Initialize the database
        moradorRepository.saveAndFlush(morador);

        // Get the morador
        restMoradorMockMvc.perform(get("/api/moradors/{id}", morador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(morador.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF.toString()))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.senha").value(DEFAULT_SENHA.toString()))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.bloqueiaAgendamento").value(DEFAULT_BLOQUEIA_AGENDAMENTO.booleanValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMorador() throws Exception {
        // Get the morador
        restMoradorMockMvc.perform(get("/api/moradors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMorador() throws Exception {
        // Initialize the database
        moradorRepository.saveAndFlush(morador);

		int databaseSizeBeforeUpdate = moradorRepository.findAll().size();

        // Update the morador
        morador.setNome(UPDATED_NOME);
        morador.setCpf(UPDATED_CPF);
        morador.setSexo(UPDATED_SEXO);
        morador.setEmail(UPDATED_EMAIL);
        morador.setSenha(UPDATED_SENHA);
        morador.setDataNascimento(UPDATED_DATA_NASCIMENTO);
        morador.setAtivo(UPDATED_ATIVO);
        morador.setBloqueiaAgendamento(UPDATED_BLOQUEIA_AGENDAMENTO);
        morador.setTipo(UPDATED_TIPO);
        

        restMoradorMockMvc.perform(put("/api/moradors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(morador)))
                .andExpect(status().isOk());

        // Validate the Morador in the database
        List<Morador> moradors = moradorRepository.findAll();
        assertThat(moradors).hasSize(databaseSizeBeforeUpdate);
        Morador testMorador = moradors.get(moradors.size() - 1);
        assertThat(testMorador.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMorador.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testMorador.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testMorador.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testMorador.getSenha()).isEqualTo(UPDATED_SENHA);
        assertThat(testMorador.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testMorador.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testMorador.getBloqueiaAgendamento()).isEqualTo(UPDATED_BLOQUEIA_AGENDAMENTO);
        assertThat(testMorador.getTipo()).isEqualTo(UPDATED_TIPO);
    }

    @Test
    @Transactional
    public void deleteMorador() throws Exception {
        // Initialize the database
        moradorRepository.saveAndFlush(morador);

		int databaseSizeBeforeDelete = moradorRepository.findAll().size();

        // Get the morador
        restMoradorMockMvc.perform(delete("/api/moradors/{id}", morador.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Morador> moradors = moradorRepository.findAll();
        assertThat(moradors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
