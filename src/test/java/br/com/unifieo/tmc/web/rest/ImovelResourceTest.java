package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Imovel;
import br.com.unifieo.tmc.repository.ImovelRepository;
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
 * Test class for the ImovelResource REST controller.
 *
 * @see ImovelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ImovelResourceTest {

    private static final String DEFAULT_RUA_BLOCO = "SAMPLE_TEXT";
    private static final String UPDATED_RUA_BLOCO = "UPDATED_TEXT";

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;

    @Inject
    private ImovelRepository imovelRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restImovelMockMvc;

    private Imovel imovel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ImovelResource imovelResource = new ImovelResource();
        ReflectionTestUtils.setField(imovelResource, "imovelRepository", imovelRepository);
        this.restImovelMockMvc = MockMvcBuilders.standaloneSetup(imovelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        imovel = new Imovel();
        imovel.setRuaBloco(DEFAULT_RUA_BLOCO);
        imovel.setNumero(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void createImovel() throws Exception {
        int databaseSizeBeforeCreate = imovelRepository.findAll().size();

        // Create the Imovel

        restImovelMockMvc.perform(post("/api/imovels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imovel)))
                .andExpect(status().isCreated());

        // Validate the Imovel in the database
        List<Imovel> imovels = imovelRepository.findAll();
        assertThat(imovels).hasSize(databaseSizeBeforeCreate + 1);
        Imovel testImovel = imovels.get(imovels.size() - 1);
        assertThat(testImovel.getRuaBloco()).isEqualTo(DEFAULT_RUA_BLOCO);
        assertThat(testImovel.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void checkRuaBlocoIsRequired() throws Exception {
        int databaseSizeBeforeTest = imovelRepository.findAll().size();
        // set the field null
        imovel.setRuaBloco(null);

        // Create the Imovel, which fails.

        restImovelMockMvc.perform(post("/api/imovels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imovel)))
                .andExpect(status().isBadRequest());

        List<Imovel> imovels = imovelRepository.findAll();
        assertThat(imovels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = imovelRepository.findAll().size();
        // set the field null
        imovel.setNumero(null);

        // Create the Imovel, which fails.

        restImovelMockMvc.perform(post("/api/imovels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imovel)))
                .andExpect(status().isBadRequest());

        List<Imovel> imovels = imovelRepository.findAll();
        assertThat(imovels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllImovels() throws Exception {
        // Initialize the database
        imovelRepository.saveAndFlush(imovel);

        // Get all the imovels
        restImovelMockMvc.perform(get("/api/imovels"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(imovel.getId().intValue())))
                .andExpect(jsonPath("$.[*].ruaBloco").value(hasItem(DEFAULT_RUA_BLOCO.toString())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }

    @Test
    @Transactional
    public void getImovel() throws Exception {
        // Initialize the database
        imovelRepository.saveAndFlush(imovel);

        // Get the imovel
        restImovelMockMvc.perform(get("/api/imovels/{id}", imovel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(imovel.getId().intValue()))
            .andExpect(jsonPath("$.ruaBloco").value(DEFAULT_RUA_BLOCO.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO));
    }

    @Test
    @Transactional
    public void getNonExistingImovel() throws Exception {
        // Get the imovel
        restImovelMockMvc.perform(get("/api/imovels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImovel() throws Exception {
        // Initialize the database
        imovelRepository.saveAndFlush(imovel);

		int databaseSizeBeforeUpdate = imovelRepository.findAll().size();

        // Update the imovel
        imovel.setRuaBloco(UPDATED_RUA_BLOCO);
        imovel.setNumero(UPDATED_NUMERO);


        restImovelMockMvc.perform(put("/api/imovels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imovel)))
                .andExpect(status().isOk());

        // Validate the Imovel in the database
        List<Imovel> imovels = imovelRepository.findAll();
        assertThat(imovels).hasSize(databaseSizeBeforeUpdate);
        Imovel testImovel = imovels.get(imovels.size() - 1);
        assertThat(testImovel.getRuaBloco()).isEqualTo(UPDATED_RUA_BLOCO);
        assertThat(testImovel.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void deleteImovel() throws Exception {
        // Initialize the database
        imovelRepository.saveAndFlush(imovel);

		int databaseSizeBeforeDelete = imovelRepository.findAll().size();

        // Get the imovel
        restImovelMockMvc.perform(delete("/api/imovels/{id}", imovel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Imovel> imovels = imovelRepository.findAll();
        assertThat(imovels).hasSize(databaseSizeBeforeDelete - 1);
    }
}
