package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.ImagemTopico;
import br.com.unifieo.tmc.repository.ImagemTopicoRepository;
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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ImagemTopicoResource REST controller.
 *
 * @see ImagemTopicoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ImagemTopicoResourceTest {


    private static final byte[] DEFAULT_IMAGEM = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGEM = TestUtil.createByteArray(2, "1");

    @Inject
    private ImagemTopicoRepository imagemTopicoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restImagemTopicoMockMvc;

    private ImagemTopico imagemTopico;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ImagemTopicoResource imagemTopicoResource = new ImagemTopicoResource();
        ReflectionTestUtils.setField(imagemTopicoResource, "imagemTopicoRepository", imagemTopicoRepository);
        this.restImagemTopicoMockMvc = MockMvcBuilders.standaloneSetup(imagemTopicoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        imagemTopico = new ImagemTopico();
        imagemTopico.setImagem(DEFAULT_IMAGEM);
    }

    @Test
    @Transactional
    public void createImagemTopico() throws Exception {
        int databaseSizeBeforeCreate = imagemTopicoRepository.findAll().size();

        // Create the ImagemTopico

        restImagemTopicoMockMvc.perform(post("/api/imagemTopicos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imagemTopico)))
                .andExpect(status().isCreated());

        // Validate the ImagemTopico in the database
        List<ImagemTopico> imagemTopicos = imagemTopicoRepository.findAll();
        assertThat(imagemTopicos).hasSize(databaseSizeBeforeCreate + 1);
        ImagemTopico testImagemTopico = imagemTopicos.get(imagemTopicos.size() - 1);
        assertThat(testImagemTopico.getImagem()).isEqualTo(DEFAULT_IMAGEM);
    }

    @Test
    @Transactional
    public void getAllImagemTopicos() throws Exception {
        // Initialize the database
        imagemTopicoRepository.saveAndFlush(imagemTopico);

        // Get all the imagemTopicos
        restImagemTopicoMockMvc.perform(get("/api/imagemTopicos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(imagemTopico.getId().intValue())))
                .andExpect(jsonPath("$.[*].imagem").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGEM))));
    }

    @Test
    @Transactional
    public void getImagemTopico() throws Exception {
        // Initialize the database
        imagemTopicoRepository.saveAndFlush(imagemTopico);

        // Get the imagemTopico
        restImagemTopicoMockMvc.perform(get("/api/imagemTopicos/{id}", imagemTopico.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(imagemTopico.getId().intValue()))
            .andExpect(jsonPath("$.imagem").value(Base64Utils.encodeToString(DEFAULT_IMAGEM)));
    }

    @Test
    @Transactional
    public void getNonExistingImagemTopico() throws Exception {
        // Get the imagemTopico
        restImagemTopicoMockMvc.perform(get("/api/imagemTopicos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImagemTopico() throws Exception {
        // Initialize the database
        imagemTopicoRepository.saveAndFlush(imagemTopico);

		int databaseSizeBeforeUpdate = imagemTopicoRepository.findAll().size();

        // Update the imagemTopico
        imagemTopico.setImagem(UPDATED_IMAGEM);


        restImagemTopicoMockMvc.perform(put("/api/imagemTopicos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imagemTopico)))
                .andExpect(status().isOk());

        // Validate the ImagemTopico in the database
        List<ImagemTopico> imagemTopicos = imagemTopicoRepository.findAll();
        assertThat(imagemTopicos).hasSize(databaseSizeBeforeUpdate);
        ImagemTopico testImagemTopico = imagemTopicos.get(imagemTopicos.size() - 1);
        assertThat(testImagemTopico.getImagem()).isEqualTo(UPDATED_IMAGEM);
    }

    @Test
    @Transactional
    public void deleteImagemTopico() throws Exception {
        // Initialize the database
        imagemTopicoRepository.saveAndFlush(imagemTopico);

		int databaseSizeBeforeDelete = imagemTopicoRepository.findAll().size();

        // Get the imagemTopico
        restImagemTopicoMockMvc.perform(delete("/api/imagemTopicos/{id}", imagemTopico.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ImagemTopico> imagemTopicos = imagemTopicoRepository.findAll();
        assertThat(imagemTopicos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
