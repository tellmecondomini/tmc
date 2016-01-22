package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Comentario;
import br.com.unifieo.tmc.domain.Topico;
import br.com.unifieo.tmc.repository.ComentarioRepository;
import br.com.unifieo.tmc.repository.TopicoRepository;
import br.com.unifieo.tmc.service.TopicoService;
import br.com.unifieo.tmc.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Collectors;

/**
 * REST controller for managing Topico.
 */
@RestController
@RequestMapping("/api")
public class TopicoResource {

    private final Logger log = LoggerFactory.getLogger(TopicoResource.class);

    @Inject
    private TopicoRepository topicoRepository;

    @Inject
    private TopicoService topicoService;

    @Inject
    private ComentarioRepository comentarioRepository;

    /**
     * POST  /topicos -> Create a new topico.
     */
    @RequestMapping(value = "/topicos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Topico> createTopico(@RequestBody Topico topico) throws URISyntaxException {
        log.debug("REST request to save Topico : {}", topico);
        if (topico.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new topico cannot already have an ID").body(null);
        }
        Topico result = topicoService.save(topico);
        return ResponseEntity.created(new URI("/api/topicos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("topico", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /topicos -> Updates an existing topico.
     */
    @RequestMapping(value = "/topicos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Topico> updateTopico(@Valid @RequestBody Topico topico) throws URISyntaxException {
        log.debug("REST request to update Topico : {}", topico);
        if (topico.getId() == null) {
            return createTopico(topico);
        }
        Topico result = topicoService.save(topico);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("topico", topico.getId().toString()))
            .body(result);
    }

    /**
     * GET  /topicos -> get all the topicos.
     */
    @RequestMapping(value = "/topicos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Topico> getAllTopicos() {
        log.debug("REST request to get all Topicos");
        return topicoRepository.findAll().stream().sorted((t1, t2) -> t2.getData().compareTo(t1.getData())).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{id}/comentarios",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Comentario> getComentariosByTopico(@PathVariable Long id) {
        Topico topico = this.topicoRepository.findOne(id);
        return comentarioRepository.findAllByTopico(topico).stream().sorted((c1, c2) -> c2.getData().compareTo(c1.getData())).collect(Collectors.toList());
    }

    /**
     * GET  /topicos/:id -> get the "id" topico.
     */
    @RequestMapping(value = "/topicos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Topico> getTopico(@PathVariable Long id) {
        log.debug("REST request to get Topico : {}", id);
        return Optional.ofNullable(topicoRepository.findOne(id))
            .map(topico -> new ResponseEntity<>(
                topico,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /topicos/:id -> delete the "id" topico.
     */
    @RequestMapping(value = "/topicos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTopico(@PathVariable Long id) {
        log.debug("REST request to delete Topico : {}", id);
        topicoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("topico", id.toString())).build();
    }
}
