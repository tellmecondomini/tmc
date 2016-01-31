package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Funcionario;
import br.com.unifieo.tmc.domain.enumeration.Sexo;
import br.com.unifieo.tmc.repository.FuncionarioRepository;
import br.com.unifieo.tmc.service.FuncionarioService;
import br.com.unifieo.tmc.web.rest.dto.FuncionarioDTO;
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
    private static final String DEFAULT_CPF = "SAMPLE_TEXT";
    private static final String UPDATED_CPF = "UPDATED_TEXT";

    private static final Sexo DEFAULT_SEXO = Sexo.M;
    private static final Sexo UPDATED_SEXO = Sexo.F;

    private static final DateTime DEFAULT_DATA_NASCIMENTO = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATA_NASCIMENTO = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATA_NASCIMENTO_STR = dateTimeFormatter.print(DEFAULT_DATA_NASCIMENTO);
    private static final String DEFAULT_EMAIL = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL = "UPDATED_TEXT";
    private static final String DEFAULT_SENHA = "SAMPLE_TEXT";
    private static final String UPDATED_SENHA = "UPDATED_TEXT";

    private static final Boolean DEFAULT_ATIVO = true;
    private static final Boolean UPDATED_ATIVO = true;

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;
    private static final String DEFAULT_COMPLEMENTO = "SAMPLE_TEXT";
    private static final String UPDATED_COMPLEMENTO = "UPDATED_TEXT";

    private static final Boolean DEFAULT_RESPONSAVEL = false;
    private static final Boolean UPDATED_RESPONSAVEL = false;

    @Inject
    private FuncionarioRepository funcionarioRepository;

    @Inject
    private FuncionarioService funcionarioService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFuncionarioMockMvc;

    private FuncionarioDTO funcionarioDTO;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FuncionarioResource funcionarioResource = new FuncionarioResource();
        ReflectionTestUtils.setField(funcionarioResource, "funcionarioRepository", funcionarioRepository);
        ReflectionTestUtils.setField(funcionarioResource, "funcionarioService", funcionarioService);
        this.restFuncionarioMockMvc = MockMvcBuilders.standaloneSetup(funcionarioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        funcionarioDTO = new FuncionarioDTO();
        funcionarioDTO.setNome(DEFAULT_NOME);
        funcionarioDTO.setCpf(DEFAULT_CPF);
        funcionarioDTO.setSexo(DEFAULT_SEXO);
        funcionarioDTO.setDataNascimento(DEFAULT_DATA_NASCIMENTO);
        funcionarioDTO.setEmail(DEFAULT_EMAIL);
        funcionarioDTO.setSenha(DEFAULT_SENHA);
        funcionarioDTO.setAtivo(DEFAULT_ATIVO);
        funcionarioDTO.setNumero(DEFAULT_NUMERO);
        funcionarioDTO.setComplemento(DEFAULT_COMPLEMENTO);
        funcionarioDTO.setCep("123");
        funcionarioDTO.setLogradouro("Logradouro ");
        funcionarioDTO.setBairro("Bairro ");
        funcionarioDTO.setCidade("Cidade ");
        // funcionarioDTO.setUf(Uf.SP);
        funcionarioDTO.setNumero(DEFAULT_NUMERO);
        funcionarioDTO.setComplemento(DEFAULT_COMPLEMENTO);
    }

    @Test
    @Transactional
    public void createFuncionario() throws Exception {
        int databaseSizeBeforeCreate = funcionarioRepository.findAll().size();

        // Create the Funcionario
        restFuncionarioMockMvc.perform(post("/api/funcionarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionarioDTO)))
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
        assertThat(testFuncionario.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testFuncionario.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testFuncionario.getResponsavel()).isEqualTo(DEFAULT_RESPONSAVEL);
    }

    @Test
    @Transactional
    public void getAllFuncionarios() throws Exception {
        // Initialize the database
        Funcionario funcionario = funcionarioService.save(funcionarioDTO, "");
        funcionarioDTO.setId(funcionario.getId());

        // Get all the funcionarios
        restFuncionarioMockMvc.perform(get("/api/funcionarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(funcionarioDTO.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF.toString())))
                .andExpect(jsonPath("$.[*].sexo").value(hasItem(DEFAULT_SEXO.toString())))
                .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO_STR)))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].senha").value(hasItem(DEFAULT_SENHA.toString())))
                .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
                .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO.toString())))
                .andExpect(jsonPath("$.[*].responsavel").value(hasItem(DEFAULT_RESPONSAVEL.booleanValue())));
    }

    @Test
    @Transactional
    public void getFuncionario() throws Exception {
        // Initialize the database
        funcionarioService.save(funcionarioDTO, "");

        // Get the funcionarioDTO
        restFuncionarioMockMvc.perform(get("/api/funcionarios/{id}", funcionarioDTO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(funcionarioDTO.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF.toString()))
            .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO.toString()))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO_STR))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.senha").value(DEFAULT_SENHA.toString()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO.toString()))
            .andExpect(jsonPath("$.responsavel").value(DEFAULT_RESPONSAVEL.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFuncionario() throws Exception {
        // Get the funcionarioDTO
        restFuncionarioMockMvc.perform(get("/api/funcionarios/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFuncionario() throws Exception {
        // Initialize the database
        Funcionario funcionario = funcionarioService.save(funcionarioDTO, "");
        funcionarioDTO.setId(funcionario.getId());

        int databaseSizeBeforeUpdate = funcionarioRepository.findAll().size();

        // Update the funcionarioDTO
        funcionarioDTO.setNome(UPDATED_NOME);
        funcionarioDTO.setCpf(UPDATED_CPF);
        funcionarioDTO.setSexo(UPDATED_SEXO);
        funcionarioDTO.setDataNascimento(UPDATED_DATA_NASCIMENTO);
        funcionarioDTO.setEmail(UPDATED_EMAIL);
        funcionarioDTO.setSenha(UPDATED_SENHA);
        funcionarioDTO.setAtivo(UPDATED_ATIVO);
        funcionarioDTO.setNumero(UPDATED_NUMERO);
        funcionarioDTO.setComplemento(UPDATED_COMPLEMENTO);

        restFuncionarioMockMvc.perform(put("/api/funcionarios")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionarioDTO)))
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
        assertThat(testFuncionario.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testFuncionario.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testFuncionario.getResponsavel()).isEqualTo(UPDATED_RESPONSAVEL);
    }

    @Test
    @Transactional
    public void deleteFuncionario() throws Exception {
        // Initialize the database
        funcionarioService.save(funcionarioDTO, "");

		int databaseSizeBeforeDelete = funcionarioRepository.findAll().size();

        // Get the funcionarioDTO
        restFuncionarioMockMvc.perform(delete("/api/funcionarios/{id}", funcionarioDTO.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Funcionario> funcionarios = funcionarioRepository.findAll();
        assertThat(funcionarios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
