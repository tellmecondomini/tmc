package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Dependencia;
import br.com.unifieo.tmc.repository.DependenciaRepository;

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
 * Test class for the DependenciaResource REST controller.
 *
 * @see DependenciaResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DependenciaResourceTest {

    private static final String DEFAULT_NOME = "SAMPLE_TEXT";
    private static final String UPDATED_NOME = "UPDATED_TEXT";

    private static final Boolean DEFAULT_DISPONIVEL = false;
    private static final Boolean UPDATED_DISPONIVEL = true;

    private static final Integer DEFAULT_CAPACIDADE = 1;
    private static final Integer UPDATED_CAPACIDADE = 2;

    private static final Double DEFAULT_CUSTO_ADICIONAL = 0D;
    private static final Double UPDATED_CUSTO_ADICIONAL = 1D;
    private static final String DEFAULT_REGRA_USO = "SAMPLE_TEXT";
    private static final String UPDATED_REGRA_USO = "UPDATED_TEXT";

    @Inject
    private DependenciaRepository dependenciaRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDependenciaMockMvc;

    private Dependencia dependencia;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DependenciaResource dependenciaResource = new DependenciaResource();
        ReflectionTestUtils.setField(dependenciaResource, "dependenciaRepository", dependenciaRepository);
        this.restDependenciaMockMvc = MockMvcBuilders.standaloneSetup(dependenciaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dependencia = new Dependencia();
        dependencia.setNome(DEFAULT_NOME);
        dependencia.setDisponivel(DEFAULT_DISPONIVEL);
        dependencia.setCapacidade(DEFAULT_CAPACIDADE);
        dependencia.setCustoAdicional(DEFAULT_CUSTO_ADICIONAL);
        dependencia.setRegraUso(DEFAULT_REGRA_USO);
    }

    @Test
    @Transactional
    public void createDependencia() throws Exception {
        int databaseSizeBeforeCreate = dependenciaRepository.findAll().size();

        // Create the Dependencia

        restDependenciaMockMvc.perform(post("/api/dependencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dependencia)))
                .andExpect(status().isCreated());

        // Validate the Dependencia in the database
        List<Dependencia> dependencias = dependenciaRepository.findAll();
        assertThat(dependencias).hasSize(databaseSizeBeforeCreate + 1);
        Dependencia testDependencia = dependencias.get(dependencias.size() - 1);
        assertThat(testDependencia.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testDependencia.getDisponivel()).isEqualTo(DEFAULT_DISPONIVEL);
        assertThat(testDependencia.getCapacidade()).isEqualTo(DEFAULT_CAPACIDADE);
        assertThat(testDependencia.getCustoAdicional()).isEqualTo(DEFAULT_CUSTO_ADICIONAL);
        assertThat(testDependencia.getRegraUso()).isEqualTo(DEFAULT_REGRA_USO);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependenciaRepository.findAll().size();
        // set the field null
        dependencia.setNome(null);

        // Create the Dependencia, which fails.

        restDependenciaMockMvc.perform(post("/api/dependencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dependencia)))
                .andExpect(status().isBadRequest());

        List<Dependencia> dependencias = dependenciaRepository.findAll();
        assertThat(dependencias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCapacidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependenciaRepository.findAll().size();
        // set the field null
        dependencia.setCapacidade(null);

        // Create the Dependencia, which fails.

        restDependenciaMockMvc.perform(post("/api/dependencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dependencia)))
                .andExpect(status().isBadRequest());

        List<Dependencia> dependencias = dependenciaRepository.findAll();
        assertThat(dependencias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCustoAdicionalIsRequired() throws Exception {
        int databaseSizeBeforeTest = dependenciaRepository.findAll().size();
        // set the field null
        dependencia.setCustoAdicional(null);

        // Create the Dependencia, which fails.

        restDependenciaMockMvc.perform(post("/api/dependencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dependencia)))
                .andExpect(status().isBadRequest());

        List<Dependencia> dependencias = dependenciaRepository.findAll();
        assertThat(dependencias).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDependencias() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

        // Get all the dependencias
        restDependenciaMockMvc.perform(get("/api/dependencias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dependencia.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].disponivel").value(hasItem(DEFAULT_DISPONIVEL.booleanValue())))
                .andExpect(jsonPath("$.[*].capacidade").value(hasItem(DEFAULT_CAPACIDADE)))
                .andExpect(jsonPath("$.[*].custoAdicional").value(hasItem(DEFAULT_CUSTO_ADICIONAL.doubleValue())))
                .andExpect(jsonPath("$.[*].regraUso").value(hasItem(DEFAULT_REGRA_USO.toString())));
    }

    @Test
    @Transactional
    public void getDependencia() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

        // Get the dependencia
        restDependenciaMockMvc.perform(get("/api/dependencias/{id}", dependencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dependencia.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.disponivel").value(DEFAULT_DISPONIVEL.booleanValue()))
            .andExpect(jsonPath("$.capacidade").value(DEFAULT_CAPACIDADE))
            .andExpect(jsonPath("$.custoAdicional").value(DEFAULT_CUSTO_ADICIONAL.doubleValue()))
            .andExpect(jsonPath("$.regraUso").value(DEFAULT_REGRA_USO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDependencia() throws Exception {
        // Get the dependencia
        restDependenciaMockMvc.perform(get("/api/dependencias/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDependencia() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

		int databaseSizeBeforeUpdate = dependenciaRepository.findAll().size();

        // Update the dependencia
        dependencia.setNome(UPDATED_NOME);
        dependencia.setDisponivel(UPDATED_DISPONIVEL);
        dependencia.setCapacidade(UPDATED_CAPACIDADE);
        dependencia.setCustoAdicional(UPDATED_CUSTO_ADICIONAL);
        dependencia.setRegraUso(UPDATED_REGRA_USO);
        

        restDependenciaMockMvc.perform(put("/api/dependencias")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dependencia)))
                .andExpect(status().isOk());

        // Validate the Dependencia in the database
        List<Dependencia> dependencias = dependenciaRepository.findAll();
        assertThat(dependencias).hasSize(databaseSizeBeforeUpdate);
        Dependencia testDependencia = dependencias.get(dependencias.size() - 1);
        assertThat(testDependencia.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testDependencia.getDisponivel()).isEqualTo(UPDATED_DISPONIVEL);
        assertThat(testDependencia.getCapacidade()).isEqualTo(UPDATED_CAPACIDADE);
        assertThat(testDependencia.getCustoAdicional()).isEqualTo(UPDATED_CUSTO_ADICIONAL);
        assertThat(testDependencia.getRegraUso()).isEqualTo(UPDATED_REGRA_USO);
    }

    @Test
    @Transactional
    public void deleteDependencia() throws Exception {
        // Initialize the database
        dependenciaRepository.saveAndFlush(dependencia);

		int databaseSizeBeforeDelete = dependenciaRepository.findAll().size();

        // Get the dependencia
        restDependenciaMockMvc.perform(delete("/api/dependencias/{id}", dependencia.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Dependencia> dependencias = dependenciaRepository.findAll();
        assertThat(dependencias).hasSize(databaseSizeBeforeDelete - 1);
    }
}
