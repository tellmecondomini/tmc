package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Assunto;
import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.repository.AssuntoRepository;
import br.com.unifieo.tmc.service.CondominioService;
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

/**
 * REST controller for managing Assunto.
 */
@RestController
@RequestMapping("/api")
public class AssuntoResource {

    private final Logger log = LoggerFactory.getLogger(AssuntoResource.class);

    @Inject
    private AssuntoRepository assuntoRepository;

    @Inject
    private CondominioService condominioService;

    /**
     * POST  /assuntos -> Create a new assunto.
     */
    @RequestMapping(value = "/assuntos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assunto> createAssunto(@Valid @RequestBody Assunto assunto) throws URISyntaxException {
        log.debug("REST request to save Assunto : {}", assunto);
        if (assunto.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new assunto cannot already have an ID").body(null);
        }
        Condominio condominio = condominioService.getCurrentCondominio();
        assunto.setCondominio(condominio);
        Assunto result = assuntoRepository.save(assunto);
        return ResponseEntity.created(new URI("/api/assuntos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("assunto", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /assuntos -> Updates an existing assunto.
     */
    @RequestMapping(value = "/assuntos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assunto> updateAssunto(@Valid @RequestBody Assunto assunto) throws URISyntaxException {
        log.debug("REST request to update Assunto : {}", assunto);
        if (assunto.getId() == null) {
            return createAssunto(assunto);
        }
        Assunto result = assuntoRepository.save(assunto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("assunto", assunto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /assuntos -> get all the assuntos.
     */
    @RequestMapping(value = "/assuntos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Assunto> getAllAssuntos() {
        log.debug("REST request to get all Assuntos");
        Condominio condominio = condominioService.getCurrentCondominio();
        return assuntoRepository.findAllByCondominio(condominio);
    }

    /**
     * GET  /assuntos/:id -> get the "id" assunto.
     */
    @RequestMapping(value = "/assuntos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assunto> getAssunto(@PathVariable Long id) {
        log.debug("REST request to get Assunto : {}", id);
        return Optional.ofNullable(assuntoRepository.findOne(id))
            .map(assunto -> new ResponseEntity<>(
                assunto,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /assuntos/:id -> delete the "id" assunto.
     */
    @RequestMapping(value = "/assuntos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAssunto(@PathVariable Long id) {
        log.debug("REST request to delete Assunto : {}", id);
        assuntoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("assunto", id.toString())).build();
    }
}
