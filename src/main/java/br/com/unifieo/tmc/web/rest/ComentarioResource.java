package br.com.unifieo.tmc.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.Comentario;
import br.com.unifieo.tmc.repository.ComentarioRepository;
import br.com.unifieo.tmc.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Comentario.
 */
@RestController
@RequestMapping("/api")
public class ComentarioResource {

    private final Logger log = LoggerFactory.getLogger(ComentarioResource.class);

    @Inject
    private ComentarioRepository comentarioRepository;

    /**
     * POST  /comentarios -> Create a new comentario.
     */
    @RequestMapping(value = "/comentarios",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Comentario> createComentario(@Valid @RequestBody Comentario comentario) throws URISyntaxException {
        log.debug("REST request to save Comentario : {}", comentario);
        if (comentario.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new comentario cannot already have an ID").body(null);
        }
        Comentario result = comentarioRepository.save(comentario);
        return ResponseEntity.created(new URI("/api/comentarios/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("comentario", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /comentarios -> Updates an existing comentario.
     */
    @RequestMapping(value = "/comentarios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Comentario> updateComentario(@Valid @RequestBody Comentario comentario) throws URISyntaxException {
        log.debug("REST request to update Comentario : {}", comentario);
        if (comentario.getId() == null) {
            return createComentario(comentario);
        }
        Comentario result = comentarioRepository.save(comentario);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("comentario", comentario.getId().toString()))
                .body(result);
    }

    /**
     * GET  /comentarios -> get all the comentarios.
     */
    @RequestMapping(value = "/comentarios",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Comentario> getAllComentarios() {
        log.debug("REST request to get all Comentarios");
        return comentarioRepository.findAll();
    }

    /**
     * GET  /comentarios/:id -> get the "id" comentario.
     */
    @RequestMapping(value = "/comentarios/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Comentario> getComentario(@PathVariable Long id) {
        log.debug("REST request to get Comentario : {}", id);
        return Optional.ofNullable(comentarioRepository.findOne(id))
            .map(comentario -> new ResponseEntity<>(
                comentario,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /comentarios/:id -> delete the "id" comentario.
     */
    @RequestMapping(value = "/comentarios/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteComentario(@PathVariable Long id) {
        log.debug("REST request to delete Comentario : {}", id);
        comentarioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("comentario", id.toString())).build();
    }
}
