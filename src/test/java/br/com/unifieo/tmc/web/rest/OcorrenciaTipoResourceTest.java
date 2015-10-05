package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.OcorrenciaTipo;
import br.com.unifieo.tmc.repository.OcorrenciaTipoRepository;

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
 * Test class for the OcorrenciaTipoResource REST controller.
 *
 * @see OcorrenciaTipoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OcorrenciaTipoResourceTest {


    @Inject
    private OcorrenciaTipoRepository ocorrenciaTipoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOcorrenciaTipoMockMvc;

    private OcorrenciaTipo ocorrenciaTipo;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OcorrenciaTipoResource ocorrenciaTipoResource = new OcorrenciaTipoResource();
        ReflectionTestUtils.setField(ocorrenciaTipoResource, "ocorrenciaTipoRepository", ocorrenciaTipoRepository);
        this.restOcorrenciaTipoMockMvc = MockMvcBuilders.standaloneSetup(ocorrenciaTipoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ocorrenciaTipo = new OcorrenciaTipo();
    }

    @Test
    @Transactional
    public void createOcorrenciaTipo() throws Exception {
        int databaseSizeBeforeCreate = ocorrenciaTipoRepository.findAll().size();

        // Create the OcorrenciaTipo

        restOcorrenciaTipoMockMvc.perform(post("/api/ocorrenciaTipos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrenciaTipo)))
                .andExpect(status().isCreated());

        // Validate the OcorrenciaTipo in the database
        List<OcorrenciaTipo> ocorrenciaTipos = ocorrenciaTipoRepository.findAll();
        assertThat(ocorrenciaTipos).hasSize(databaseSizeBeforeCreate + 1);
        OcorrenciaTipo testOcorrenciaTipo = ocorrenciaTipos.get(ocorrenciaTipos.size() - 1);
    }

    @Test
    @Transactional
    public void getAllOcorrenciaTipos() throws Exception {
        // Initialize the database
        ocorrenciaTipoRepository.saveAndFlush(ocorrenciaTipo);

        // Get all the ocorrenciaTipos
        restOcorrenciaTipoMockMvc.perform(get("/api/ocorrenciaTipos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ocorrenciaTipo.getId().intValue())));
    }

    @Test
    @Transactional
    public void getOcorrenciaTipo() throws Exception {
        // Initialize the database
        ocorrenciaTipoRepository.saveAndFlush(ocorrenciaTipo);

        // Get the ocorrenciaTipo
        restOcorrenciaTipoMockMvc.perform(get("/api/ocorrenciaTipos/{id}", ocorrenciaTipo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ocorrenciaTipo.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOcorrenciaTipo() throws Exception {
        // Get the ocorrenciaTipo
        restOcorrenciaTipoMockMvc.perform(get("/api/ocorrenciaTipos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOcorrenciaTipo() throws Exception {
        // Initialize the database
        ocorrenciaTipoRepository.saveAndFlush(ocorrenciaTipo);

		int databaseSizeBeforeUpdate = ocorrenciaTipoRepository.findAll().size();

        // Update the ocorrenciaTipo
        

        restOcorrenciaTipoMockMvc.perform(put("/api/ocorrenciaTipos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrenciaTipo)))
                .andExpect(status().isOk());

        // Validate the OcorrenciaTipo in the database
        List<OcorrenciaTipo> ocorrenciaTipos = ocorrenciaTipoRepository.findAll();
        assertThat(ocorrenciaTipos).hasSize(databaseSizeBeforeUpdate);
        OcorrenciaTipo testOcorrenciaTipo = ocorrenciaTipos.get(ocorrenciaTipos.size() - 1);
    }

    @Test
    @Transactional
    public void deleteOcorrenciaTipo() throws Exception {
        // Initialize the database
        ocorrenciaTipoRepository.saveAndFlush(ocorrenciaTipo);

		int databaseSizeBeforeDelete = ocorrenciaTipoRepository.findAll().size();

        // Get the ocorrenciaTipo
        restOcorrenciaTipoMockMvc.perform(delete("/api/ocorrenciaTipos/{id}", ocorrenciaTipo.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OcorrenciaTipo> ocorrenciaTipos = ocorrenciaTipoRepository.findAll();
        assertThat(ocorrenciaTipos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
