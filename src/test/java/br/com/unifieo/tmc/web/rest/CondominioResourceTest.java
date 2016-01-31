package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.domain.enumeration.Disposicao;
import br.com.unifieo.tmc.domain.enumeration.Sexo;
import br.com.unifieo.tmc.domain.enumeration.Uf;
import br.com.unifieo.tmc.repository.CondominioRepository;
import br.com.unifieo.tmc.service.CondominioService;
import br.com.unifieo.tmc.web.rest.dto.CondominioDTO;
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
    private static final String DEFAULT_CNPJ = "SAMPLE_TEXT";
    private static final String UPDATED_CNPJ = "UPDATED_TEXT";

    private static final Boolean DEFAULT_ATIVO = true;
    private static final Boolean UPDATED_ATIVO = true;

    private static final DateTime DEFAULT_DATA_CADASTRO = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_DATA_CADASTRO = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_DATA_CADASTRO_STR = dateTimeFormatter.print(DEFAULT_DATA_CADASTRO);

    private static final Disposicao DEFAULT_DISPOSICAO = Disposicao.VERTICAL;
    private static final Disposicao UPDATED_DISPOSICAO = Disposicao.HORIZONTAL;

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;
    private static final String DEFAULT_COMPLEMENTO = "SAMPLE_TEXT";
    private static final String UPDATED_COMPLEMENTO = "UPDATED_TEXT";

    @Inject
    private CondominioRepository condominioRepository;

    @Inject
    private CondominioService condominioService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCondominioMockMvc;

    private CondominioDTO condominioDTO;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CondominioResource condominioResource = new CondominioResource();
        ReflectionTestUtils.setField(condominioResource, "condominioRepository", condominioRepository);
        ReflectionTestUtils.setField(condominioResource, "condominioService", condominioService);
        this.restCondominioMockMvc = MockMvcBuilders.standaloneSetup(condominioResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        condominioDTO = new CondominioDTO();
        condominioDTO.setId(null);
        condominioDTO.setRazaoSocial(DEFAULT_RAZAO_SOCIAL);
        condominioDTO.setCnpj(DEFAULT_CNPJ);
        condominioDTO.setDisposicao(DEFAULT_DISPOSICAO);
        condominioDTO.setCondominioCep("123");
        condominioDTO.setCondominioLogradouro("Logradouro Condominio");
        condominioDTO.setCondominioBairro("Bairro Condominio");
        condominioDTO.setCondominioCidade("Cidade Condominio");
        // condominioDTO.setCondominioUf(Uf.SP);
        condominioDTO.setCondominioNumero(DEFAULT_NUMERO);
        condominioDTO.setCondominioComplemento(DEFAULT_COMPLEMENTO);
        condominioDTO.setResponsavelNome("Responsavel");
        condominioDTO.setResponsavelCpf("123");
        condominioDTO.setResponsavelSexo(Sexo.M);
        condominioDTO.setResponsavelDataNascimento(new DateTime());
        condominioDTO.setResponsavelSenha("123");
        condominioDTO.setResponsavelEmail("@responsavel.com.br");
        condominioDTO.setResponsavelCep("123");
        condominioDTO.setResponsavelLogradouro("Logradouro Responsavel");
        condominioDTO.setResponsavelBairro("Bairro Responsavel");
        condominioDTO.setResponsavelCidade("Cidade Responsavel");
        // condominioDTO.setResponsavelUf(Uf.SP);
        condominioDTO.setResponsavelNumero(DEFAULT_NUMERO);
        condominioDTO.setResponsavelComplemento(DEFAULT_COMPLEMENTO);
    }

    @Test
    @Transactional
    public void createCondominio() throws Exception {
        int databaseSizeBeforeCreate = condominioRepository.findAll().size();

        // Create the Condominio

        restCondominioMockMvc.perform(post("/api/condominios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(condominioDTO)))
            .andExpect(status().isCreated());

        // Validate the Condominio in the database
        List<Condominio> condominios = condominioRepository.findAll();
        assertThat(condominios).hasSize(databaseSizeBeforeCreate + 1);
        Condominio testCondominio = condominios.get(condominios.size() - 1);
        assertThat(testCondominio.getRazaoSocial()).isEqualTo(DEFAULT_RAZAO_SOCIAL);
        assertThat(testCondominio.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testCondominio.getAtivo()).isEqualTo(DEFAULT_ATIVO);
        assertThat(testCondominio.getDisposicao()).isEqualTo(DEFAULT_DISPOSICAO);
        assertThat(testCondominio.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testCondominio.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
    }

    @Test
    @Transactional
    public void getAllCondominios() throws Exception {
        // Initialize the database
        condominioService.save(condominioDTO);

        // Get all the condominios
        restCondominioMockMvc.perform(get("/api/condominios"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(condominioDTO.getId().intValue())))
            .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ.toString())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())))
            .andExpect(jsonPath("$.[*].disposicao").value(hasItem(DEFAULT_DISPOSICAO.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO.toString())));
    }

    @Test
    @Transactional
    public void getCondominio() throws Exception {
        // Initialize the database
        condominioService.save(condominioDTO);

        // Get the condominioDTO
        restCondominioMockMvc.perform(get("/api/condominios/{id}", condominioDTO.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(condominioDTO.getId().intValue()))
            .andExpect(jsonPath("$.razaoSocial").value(DEFAULT_RAZAO_SOCIAL.toString()))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ.toString()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()))
            .andExpect(jsonPath("$.disposicao").value(DEFAULT_DISPOSICAO.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCondominio() throws Exception {
        // Get the condominioDTO
        restCondominioMockMvc.perform(get("/api/condominios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCondominio() throws Exception {
        // Initialize the database
        Condominio condominio = condominioService.save(condominioDTO);
        condominioDTO.setId(condominio.getId());

        int databaseSizeBeforeUpdate = condominioRepository.findAll().size();

        // Update the condominioDTO
        condominioDTO.setRazaoSocial(UPDATED_RAZAO_SOCIAL);
        condominioDTO.setCnpj(UPDATED_CNPJ);
        condominioDTO.setDisposicao(UPDATED_DISPOSICAO);
        condominioDTO.setCondominioNumero(UPDATED_NUMERO);
        condominioDTO.setCondominioComplemento(UPDATED_COMPLEMENTO);

        restCondominioMockMvc.perform(put("/api/condominios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(condominioDTO)))
            .andExpect(status().isOk());

        // Validate the Condominio in the database
        List<Condominio> condominios = condominioRepository.findAll();
        assertThat(condominios).hasSize(databaseSizeBeforeUpdate);
        Condominio testCondominio = condominios.get(condominios.size() - 1);
        assertThat(testCondominio.getRazaoSocial()).isEqualTo(UPDATED_RAZAO_SOCIAL);
        assertThat(testCondominio.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testCondominio.getAtivo()).isEqualTo(UPDATED_ATIVO);
        assertThat(testCondominio.getDisposicao()).isEqualTo(UPDATED_DISPOSICAO);
        assertThat(testCondominio.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testCondominio.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
    }

    @Test
    @Transactional
    public void deleteCondominio() throws Exception {
        // Initialize the database
        condominioService.save(condominioDTO);

        int databaseSizeBeforeDelete = condominioRepository.findAll().size();

        // Get the condominioDTO
        restCondominioMockMvc.perform(delete("/api/condominios/{id}", condominioDTO.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Condominio> condominios = condominioRepository.findAll();
        assertThat(condominios).hasSize(databaseSizeBeforeDelete - 1);
    }
}
