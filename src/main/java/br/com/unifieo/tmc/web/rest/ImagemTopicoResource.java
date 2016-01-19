package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.ImagemTopico;
import br.com.unifieo.tmc.repository.ImagemTopicoRepository;
import br.com.unifieo.tmc.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ImagemTopico.
 */
@RestController
@RequestMapping("/api")
public class ImagemTopicoResource {

    private final Logger log = LoggerFactory.getLogger(ImagemTopicoResource.class);

    @Inject
    private ImagemTopicoRepository imagemTopicoRepository;

    /**
     * POST  /imagemTopicos -> Create a new imagemTopico.
     */
    @RequestMapping(value = "/imagemTopicos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImagemTopico> createImagemTopico(@RequestBody ImagemTopico imagemTopico) throws URISyntaxException {
        log.debug("REST request to save ImagemTopico : {}", imagemTopico);
        if (imagemTopico.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new imagemTopico cannot already have an ID").body(null);
        }
        ImagemTopico result = imagemTopicoRepository.save(imagemTopico);
        return ResponseEntity.created(new URI("/api/imagemTopicos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("imagemTopico", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /imagemTopicos -> Updates an existing imagemTopico.
     */
    @RequestMapping(value = "/imagemTopicos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImagemTopico> updateImagemTopico(@RequestBody ImagemTopico imagemTopico) throws URISyntaxException {
        log.debug("REST request to update ImagemTopico : {}", imagemTopico);
        if (imagemTopico.getId() == null) {
            return createImagemTopico(imagemTopico);
        }
        ImagemTopico result = imagemTopicoRepository.save(imagemTopico);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("imagemTopico", imagemTopico.getId().toString()))
                .body(result);
    }

    /**
     * GET  /imagemTopicos -> get all the imagemTopicos.
     */
    @RequestMapping(value = "/imagemTopicos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ImagemTopico> getAllImagemTopicos() {
        log.debug("REST request to get all ImagemTopicos");
        return imagemTopicoRepository.findAll();
    }

    /**
     * GET  /imagemTopicos/:id -> get the "id" imagemTopico.
     */
    @RequestMapping(value = "/imagemTopicos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ImagemTopico> getImagemTopico(@PathVariable Long id) {
        log.debug("REST request to get ImagemTopico : {}", id);
        return Optional.ofNullable(imagemTopicoRepository.findOne(id))
            .map(imagemTopico -> new ResponseEntity<>(
                imagemTopico,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /imagemTopicos/:id -> delete the "id" imagemTopico.
     */
    @RequestMapping(value = "/imagemTopicos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteImagemTopico(@PathVariable Long id) {
        log.debug("REST request to delete ImagemTopico : {}", id);
        imagemTopicoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("imagemTopico", id.toString())).build();
    }
}
