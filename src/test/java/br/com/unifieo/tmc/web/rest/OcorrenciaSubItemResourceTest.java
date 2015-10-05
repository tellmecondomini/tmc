package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.OcorrenciaSubItem;
import br.com.unifieo.tmc.repository.OcorrenciaSubItemRepository;

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
 * Test class for the OcorrenciaSubItemResource REST controller.
 *
 * @see OcorrenciaSubItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OcorrenciaSubItemResourceTest {

    private static final String DEFAULT_DESCRICAO = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRICAO = "UPDATED_TEXT";

    @Inject
    private OcorrenciaSubItemRepository ocorrenciaSubItemRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOcorrenciaSubItemMockMvc;

    private OcorrenciaSubItem ocorrenciaSubItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OcorrenciaSubItemResource ocorrenciaSubItemResource = new OcorrenciaSubItemResource();
        ReflectionTestUtils.setField(ocorrenciaSubItemResource, "ocorrenciaSubItemRepository", ocorrenciaSubItemRepository);
        this.restOcorrenciaSubItemMockMvc = MockMvcBuilders.standaloneSetup(ocorrenciaSubItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        ocorrenciaSubItem = new OcorrenciaSubItem();
        ocorrenciaSubItem.setDescricao(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void createOcorrenciaSubItem() throws Exception {
        int databaseSizeBeforeCreate = ocorrenciaSubItemRepository.findAll().size();

        // Create the OcorrenciaSubItem

        restOcorrenciaSubItemMockMvc.perform(post("/api/ocorrenciaSubItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrenciaSubItem)))
                .andExpect(status().isCreated());

        // Validate the OcorrenciaSubItem in the database
        List<OcorrenciaSubItem> ocorrenciaSubItems = ocorrenciaSubItemRepository.findAll();
        assertThat(ocorrenciaSubItems).hasSize(databaseSizeBeforeCreate + 1);
        OcorrenciaSubItem testOcorrenciaSubItem = ocorrenciaSubItems.get(ocorrenciaSubItems.size() - 1);
        assertThat(testOcorrenciaSubItem.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    public void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ocorrenciaSubItemRepository.findAll().size();
        // set the field null
        ocorrenciaSubItem.setDescricao(null);

        // Create the OcorrenciaSubItem, which fails.

        restOcorrenciaSubItemMockMvc.perform(post("/api/ocorrenciaSubItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrenciaSubItem)))
                .andExpect(status().isBadRequest());

        List<OcorrenciaSubItem> ocorrenciaSubItems = ocorrenciaSubItemRepository.findAll();
        assertThat(ocorrenciaSubItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOcorrenciaSubItems() throws Exception {
        // Initialize the database
        ocorrenciaSubItemRepository.saveAndFlush(ocorrenciaSubItem);

        // Get all the ocorrenciaSubItems
        restOcorrenciaSubItemMockMvc.perform(get("/api/ocorrenciaSubItems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ocorrenciaSubItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getOcorrenciaSubItem() throws Exception {
        // Initialize the database
        ocorrenciaSubItemRepository.saveAndFlush(ocorrenciaSubItem);

        // Get the ocorrenciaSubItem
        restOcorrenciaSubItemMockMvc.perform(get("/api/ocorrenciaSubItems/{id}", ocorrenciaSubItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ocorrenciaSubItem.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOcorrenciaSubItem() throws Exception {
        // Get the ocorrenciaSubItem
        restOcorrenciaSubItemMockMvc.perform(get("/api/ocorrenciaSubItems/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOcorrenciaSubItem() throws Exception {
        // Initialize the database
        ocorrenciaSubItemRepository.saveAndFlush(ocorrenciaSubItem);

		int databaseSizeBeforeUpdate = ocorrenciaSubItemRepository.findAll().size();

        // Update the ocorrenciaSubItem
        ocorrenciaSubItem.setDescricao(UPDATED_DESCRICAO);
        

        restOcorrenciaSubItemMockMvc.perform(put("/api/ocorrenciaSubItems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocorrenciaSubItem)))
                .andExpect(status().isOk());

        // Validate the OcorrenciaSubItem in the database
        List<OcorrenciaSubItem> ocorrenciaSubItems = ocorrenciaSubItemRepository.findAll();
        assertThat(ocorrenciaSubItems).hasSize(databaseSizeBeforeUpdate);
        OcorrenciaSubItem testOcorrenciaSubItem = ocorrenciaSubItems.get(ocorrenciaSubItems.size() - 1);
        assertThat(testOcorrenciaSubItem.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    public void deleteOcorrenciaSubItem() throws Exception {
        // Initialize the database
        ocorrenciaSubItemRepository.saveAndFlush(ocorrenciaSubItem);

		int databaseSizeBeforeDelete = ocorrenciaSubItemRepository.findAll().size();

        // Get the ocorrenciaSubItem
        restOcorrenciaSubItemMockMvc.perform(delete("/api/ocorrenciaSubItems/{id}", ocorrenciaSubItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OcorrenciaSubItem> ocorrenciaSubItems = ocorrenciaSubItemRepository.findAll();
        assertThat(ocorrenciaSubItems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
