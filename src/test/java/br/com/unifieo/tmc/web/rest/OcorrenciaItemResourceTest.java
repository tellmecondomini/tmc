package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.OcorrenciaItem;
import br.com.unifieo.tmc.repository.OcorrenciaItemRepository;

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
 * Test class for the OcorrenciaItemResource REST controller.
 *
 * @see OcorrenciaItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OcorrenciaItemResourceTest {

    private static final String DEFAULT_DESCRICAO = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRICAO = "UPDATED_TEXT";

    @Inject
    private OcorrenciaItemRepository ocorrenciaItemRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOcorrenciaItemMockMvc;

    private OcorrenciaItem ocorrenciaItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OcorrenciaItemResource ocorrenciaItemResource = new OcorrenciaItemResource();
        ReflectionTestUtils.setField(ocorrenciaItemResource, "ocorrenciaItemRepository", ocorrenciaItemRepository);
        this.restOcorrenciaItemMockMvc = MockMvcBuilders.standaloneSetup(ocorrenciaItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ocorrenciaItem = new OcorrenciaItem();
        ocorrenciaItem.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createOcorrenciaItem() throws Exception {
        int databaseSizeBeforeCreate = ocorrenciaItemRepository.findAll().size();

        // Create the OcorrenciaItem

        restOcorrenciaItemMockMvc.perform(post("/api/ocorrenciaItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrenciaItem)))
                .andExpect(status().isCreated());

        // Validate the OcorrenciaItem in the database
        List<OcorrenciaItem> ocorrenciaItems = ocorrenciaItemRepository.findAll();
        assertThat(ocorrenciaItems).hasSize(databaseSizeBeforeCreate + 1);
        OcorrenciaItem testOcorrenciaItem = ocorrenciaItems.get(ocorrenciaItems.size() - 1);
        assertThat(testOcorrenciaItem.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ocorrenciaItemRepository.findAll().size();
        // set the field null
        ocorrenciaItem.setDescricao(null);

        // Create the OcorrenciaItem, which fails.

        restOcorrenciaItemMockMvc.perform(post("/api/ocorrenciaItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrenciaItem)))
                .andExpect(status().isBadRequest());

        List<OcorrenciaItem> ocorrenciaItems = ocorrenciaItemRepository.findAll();
        assertThat(ocorrenciaItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOcorrenciaItems() throws Exception {
        // Initialize the database
        ocorrenciaItemRepository.saveAndFlush(ocorrenciaItem);

        // Get all the ocorrenciaItems
        restOcorrenciaItemMockMvc.perform(get("/api/ocorrenciaItems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ocorrenciaItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getOcorrenciaItem() throws Exception {
        // Initialize the database
        ocorrenciaItemRepository.saveAndFlush(ocorrenciaItem);

        // Get the ocorrenciaItem
        restOcorrenciaItemMockMvc.perform(get("/api/ocorrenciaItems/{id}", ocorrenciaItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ocorrenciaItem.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOcorrenciaItem() throws Exception {
        // Get the ocorrenciaItem
        restOcorrenciaItemMockMvc.perform(get("/api/ocorrenciaItems/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOcorrenciaItem() throws Exception {
        // Initialize the database
        ocorrenciaItemRepository.saveAndFlush(ocorrenciaItem);

		int databaseSizeBeforeUpdate = ocorrenciaItemRepository.findAll().size();

        // Update the ocorrenciaItem
        ocorrenciaItem.setDescricao(UPDATED_DESCRICAO);
        

        restOcorrenciaItemMockMvc.perform(put("/api/ocorrenciaItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrenciaItem)))
                .andExpect(status().isOk());

        // Validate the OcorrenciaItem in the database
        List<OcorrenciaItem> ocorrenciaItems = ocorrenciaItemRepository.findAll();
        assertThat(ocorrenciaItems).hasSize(databaseSizeBeforeUpdate);
        OcorrenciaItem testOcorrenciaItem = ocorrenciaItems.get(ocorrenciaItems.size() - 1);
        assertThat(testOcorrenciaItem.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void deleteOcorrenciaItem() throws Exception {
        // Initialize the database
        ocorrenciaItemRepository.saveAndFlush(ocorrenciaItem);

		int databaseSizeBeforeDelete = ocorrenciaItemRepository.findAll().size();

        // Get the ocorrenciaItem
        restOcorrenciaItemMockMvc.perform(delete("/api/ocorrenciaItems/{id}", ocorrenciaItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OcorrenciaItem> ocorrenciaItems = ocorrenciaItemRepository.findAll();
        assertThat(ocorrenciaItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
