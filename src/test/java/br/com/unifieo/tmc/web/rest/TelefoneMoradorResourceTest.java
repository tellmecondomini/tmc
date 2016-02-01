package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.TelefoneMorador;
import br.com.unifieo.tmc.repository.TelefoneMoradorRepository;
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
 * Test class for the TelefoneMoradorResource REST controller.
 *
 * @see TelefoneMoradorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TelefoneMoradorResourceTest {


    private static final Long DEFAULT_NUMERO = 1L;
    private static final Long UPDATED_NUMERO = 2L;

    @Inject
    private TelefoneMoradorRepository telefoneMoradorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTelefoneMoradorMockMvc;

    private TelefoneMorador telefoneMorador;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TelefoneMoradorResource telefoneMoradorResource = new TelefoneMoradorResource();
        ReflectionTestUtils.setField(telefoneMoradorResource, "telefoneMoradorRepository", telefoneMoradorRepository);
        this.restTelefoneMoradorMockMvc = MockMvcBuilders.standaloneSetup(telefoneMoradorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        telefoneMorador = new TelefoneMorador();
        telefoneMorador.setNumero(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void createTelefoneMorador() throws Exception {
        int databaseSizeBeforeCreate = telefoneMoradorRepository.findAll().size();

        // Create the TelefoneMorador

        restTelefoneMoradorMockMvc.perform(post("/api/telefoneMoradors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefoneMorador)))
            .andExpect(status().isCreated());

        // Validate the TelefoneMorador in the database
        List<TelefoneMorador> telefoneMoradors = telefoneMoradorRepository.findAll();
        assertThat(telefoneMoradors).hasSize(databaseSizeBeforeCreate + 1);
        TelefoneMorador testTelefoneMorador = telefoneMoradors.get(telefoneMoradors.size() - 1);
        assertThat(testTelefoneMorador.getNumero()).isEqualTo(DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefoneMoradorRepository.findAll().size();
        // set the field null
        telefoneMorador.setNumero(null);

        // Create the TelefoneMorador, which fails.

        restTelefoneMoradorMockMvc.perform(post("/api/telefoneMoradors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefoneMorador)))
            .andExpect(status().isBadRequest());

        List<TelefoneMorador> telefoneMoradors = telefoneMoradorRepository.findAll();
        assertThat(telefoneMoradors).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTelefoneMoradors() throws Exception {
        // Initialize the database
        telefoneMoradorRepository.saveAndFlush(telefoneMorador);

        // Get all the telefoneMoradors
        restTelefoneMoradorMockMvc.perform(get("/api/telefoneMoradors"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telefoneMorador.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)));
    }

    @Test
    @Transactional
    public void getTelefoneMorador() throws Exception {
        // Initialize the database
        telefoneMoradorRepository.saveAndFlush(telefoneMorador);

        // Get the telefoneMorador
        restTelefoneMoradorMockMvc.perform(get("/api/telefoneMoradors/{id}", telefoneMorador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(telefoneMorador.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO));
    }

    @Test
    @Transactional
    public void getNonExistingTelefoneMorador() throws Exception {
        // Get the telefoneMorador
        restTelefoneMoradorMockMvc.perform(get("/api/telefoneMoradors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelefoneMorador() throws Exception {
        // Initialize the database
        telefoneMoradorRepository.saveAndFlush(telefoneMorador);

        int databaseSizeBeforeUpdate = telefoneMoradorRepository.findAll().size();

        // Update the telefoneMorador
        telefoneMorador.setNumero(UPDATED_NUMERO);


        restTelefoneMoradorMockMvc.perform(put("/api/telefoneMoradors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(telefoneMorador)))
            .andExpect(status().isOk());

        // Validate the TelefoneMorador in the database
        List<TelefoneMorador> telefoneMoradors = telefoneMoradorRepository.findAll();
        assertThat(telefoneMoradors).hasSize(databaseSizeBeforeUpdate);
        TelefoneMorador testTelefoneMorador = telefoneMoradors.get(telefoneMoradors.size() - 1);
        assertThat(testTelefoneMorador.getNumero()).isEqualTo(UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void deleteTelefoneMorador() throws Exception {
        // Initialize the database
        telefoneMoradorRepository.saveAndFlush(telefoneMorador);

        int databaseSizeBeforeDelete = telefoneMoradorRepository.findAll().size();

        // Get the telefoneMorador
        restTelefoneMoradorMockMvc.perform(delete("/api/telefoneMoradors/{id}", telefoneMorador.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<TelefoneMorador> telefoneMoradors = telefoneMoradorRepository.findAll();
        assertThat(telefoneMoradors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
