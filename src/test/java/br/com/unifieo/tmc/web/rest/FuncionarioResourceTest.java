package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Funcionario;
import br.com.unifieo.tmc.domain.enumeration.Sexo;
import br.com.unifieo.tmc.repository.FuncionarioRepository;
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
 * Test class for the FuncionarioResource REST controller.
 *
 * @see FuncionarioResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FuncionarioResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_NOME = "SAMPLE_TEXT";
    private static final String UPDATED_NOME = "UPDATED_TEXT";
    private static final String DEFAULT_CPF = "123.543.057-02";
    private static final String UPDATED_CPF = "123.543.057-02";

    private static final Sexo DEFAULT_SEXO = Sexo.M;
    private static final Sexo UPDATED_SEXO = Sexo.F;

    private static final DateTime DEFAULT_DATA_NASCIMENTO = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATA_NASCIMENTO = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATA_NASCIMENTO_STR = dateTimeFormatter.print(DEFAULT_DATA_NASCIMENTO);
    private static final String DEFAULT_EMAIL = "tellme@condominium.com.br";
    private static final String UPDATED_EMAIL = "tellme@condominium.com.br";
    private static final String DEFAULT_SENHA = "SAMPLE_TEXT";
    private static final String UPDATED_SENHA = "UPDATED_TEXT";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final DateTime DEFAULT_DATA_CADASTRO = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATA_CADASTRO = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATA_CADASTRO_STR = dateTimeFormatter.print(DEFAULT_DATA_CADASTRO);

    private static final Integer DEFAULT_TELEFONE = 1;
    private static final Integer UPDATED_TELEFONE = 2;

    @Inject
    private FuncionarioRepository funcionarioRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFuncionarioMockMvc;

    private Funcionario funcionario;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FuncionarioResource funcionarioResource = new FuncionarioResource();
        ReflectionTestUtils.setField(funcionarioResource, "funcionarioRepository", funcionarioRepository);
        this.restFuncionarioMockMvc = MockMvcBuilders.standaloneSetup(funcionarioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        funcionario = new Funcionario();
        funcionario.setNome(DEFAULT_NOME);
        funcionario.setCpf(DEFAULT_CPF);
        funcionario.setSexo(DEFAULT_SEXO);
        funcionario.setDataNascimento(DEFAULT_DATA_NASCIMENTO);
        funcionario.setEmail(DEFAULT_EMAIL);
        funcionario.setSenha(DEFAULT_SENHA);
        funcionario.setAtivo(DEFAULT_ATIVO);
        funcionario.setDataCadastro(DEFAULT_DATA_CADASTRO);
        funcionario.setTelefone(DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    public void createFuncionario() throws Exception {
        int databaseSizeBeforeCreate = funcionarioRepository.findAll().size();

        // Create the Funcionario

        restFuncionarioMockMvc.perform(post("/api/funcionarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionario)))
                .andExpect(status().isCreated());

        // Validate the Funcionario in the database
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        assertThat(funcionarios).hasSize(databaseSizeBeforeCreate + 1);
        Funcionario testFuncionario = funcionarios.get(funcionarios.size() - 1);
        assertThat(testFuncionario.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFuncionario.getCpf()).isEqualTo(DEFAULT_CPF);
        assertThat(testFuncionario.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testFuncionario.getDataNascimento().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testFuncionario.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFuncionario.getSenha()).isEqualTo(DEFAULT_SENHA);
        assertThat(testFuncionario.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testFuncionario.getDataCadastro().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_DATA_CADASTRO);
        assertThat(testFuncionario.getTelefone()).isEqualTo(DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionarioRepository.findAll().size();
        // set the field null
        funcionario.setNome(null);

        // Create the Funcionario, which fails.

        restFuncionarioMockMvc.perform(post("/api/funcionarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionario)))
                .andExpect(status().isBadRequest());

        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        assertThat(funcionarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCpfIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionarioRepository.findAll().size();
        // set the field null
        funcionario.setCpf(null);

        // Create the Funcionario, which fails.

        restFuncionarioMockMvc.perform(post("/api/funcionarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionario)))
                .andExpect(status().isBadRequest());

        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        assertThat(funcionarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataNascimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionarioRepository.findAll().size();
        // set the field null
        funcionario.setDataNascimento(null);

        // Create the Funcionario, which fails.

        restFuncionarioMockMvc.perform(post("/api/funcionarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionario)))
                .andExpect(status().isBadRequest());

        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        assertThat(funcionarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionarioRepository.findAll().size();
        // set the field null
        funcionario.setEmail(null);

        // Create the Funcionario, which fails.

        restFuncionarioMockMvc.perform(post("/api/funcionarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionario)))
                .andExpect(status().isBadRequest());

        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        assertThat(funcionarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSenhaIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionarioRepository.findAll().size();
        // set the field null
        funcionario.setSenha(null);

        // Create the Funcionario, which fails.

        restFuncionarioMockMvc.perform(post("/api/funcionarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionario)))
                .andExpect(status().isBadRequest());

        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        assertThat(funcionarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataCadastroIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionarioRepository.findAll().size();
        // set the field null
        funcionario.setDataCadastro(null);

        // Create the Funcionario, which fails.

        restFuncionarioMockMvc.perform(post("/api/funcionarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionario)))
                .andExpect(status().isBadRequest());

        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        assertThat(funcionarios).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFuncionarios() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get all the funcionarios
        restFuncionarioMockMvc.perform(get("/api/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(funcionario.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF.toString())))
                .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())))
                .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO_STR)))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].senha").value(hasItem(DEFAULT_SENHA.toString())))
                .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
                .andExpect(jsonPath("$.[*].dataCadastro").value(hasItem(DEFAULT_DATA_CADASTRO_STR)))
                .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)));
    }

    @Test
    @Transactional
    public void getFuncionario() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

        // Get the funcionario
        restFuncionarioMockMvc.perform(get("/api/funcionarios/{id}", funcionario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(funcionario.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF.toString()))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO.toString()))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO_STR))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.senha").value(DEFAULT_SENHA.toString()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.dataCadastro").value(DEFAULT_DATA_CADASTRO_STR))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE));
    }

    @Test
    @Transactional
    public void getNonExistingFuncionario() throws Exception {
        // Get the funcionario
        restFuncionarioMockMvc.perform(get("/api/funcionarios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFuncionario() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

		int databaseSizeBeforeUpdate = funcionarioRepository.findAll().size();

        // Update the funcionario
        funcionario.setNome(UPDATED_NOME);
        funcionario.setCpf(UPDATED_CPF);
        funcionario.setSexo(UPDATED_SEXO);
        funcionario.setDataNascimento(UPDATED_DATA_NASCIMENTO);
        funcionario.setEmail(UPDATED_EMAIL);
        funcionario.setSenha(UPDATED_SENHA);
        funcionario.setAtivo(UPDATED_ATIVO);
        funcionario.setDataCadastro(UPDATED_DATA_CADASTRO);
        funcionario.setTelefone(UPDATED_TELEFONE);


        restFuncionarioMockMvc.perform(put("/api/funcionarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionario)))
                .andExpect(status().isOk());

        // Validate the Funcionario in the database
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        assertThat(funcionarios).hasSize(databaseSizeBeforeUpdate);
        Funcionario testFuncionario = funcionarios.get(funcionarios.size() - 1);
        assertThat(testFuncionario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFuncionario.getCpf()).isEqualTo(UPDATED_CPF);
        assertThat(testFuncionario.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testFuncionario.getDataNascimento().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testFuncionario.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFuncionario.getSenha()).isEqualTo(UPDATED_SENHA);
        assertThat(testFuncionario.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testFuncionario.getDataCadastro().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_DATA_CADASTRO);
        assertThat(testFuncionario.getTelefone()).isEqualTo(UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    public void deleteFuncionario() throws Exception {
        // Initialize the database
        funcionarioRepository.saveAndFlush(funcionario);

		int databaseSizeBeforeDelete = funcionarioRepository.findAll().size();

        // Get the funcionario
        restFuncionarioMockMvc.perform(delete("/api/funcionarios/{id}", funcionario.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        assertThat(funcionarios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
