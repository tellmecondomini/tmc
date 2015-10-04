package br.com.unifieo.tmc.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.TelefoneFuncionario;
import br.com.unifieo.tmc.repository.TelefoneFuncionarioRepository;
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
 * REST controller for managing TelefoneFuncionario.
 */
@RestController
@RequestMapping("/api")
public class TelefoneFuncionarioResource {

    private final Logger log = LoggerFactory.getLogger(TelefoneFuncionarioResource.class);

    @Inject
    private TelefoneFuncionarioRepository telefoneFuncionarioRepository;

    /**
     * POST  /telefoneFuncionarios -> Create a new telefoneFuncionario.
     */
    @RequestMapping(value = "/telefoneFuncionarios",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TelefoneFuncionario> createTelefoneFuncionario(@Valid @RequestBody TelefoneFuncionario telefoneFuncionario) throws URISyntaxException {
        log.debug("REST request to save TelefoneFuncionario : {}", telefoneFuncionario);
        if (telefoneFuncionario.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new telefoneFuncionario cannot already have an ID").body(null);
        }
        TelefoneFuncionario result = telefoneFuncionarioRepository.save(telefoneFuncionario);
        return ResponseEntity.created(new URI("/api/telefoneFuncionarios/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("telefoneFuncionario", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /telefoneFuncionarios -> Updates an existing telefoneFuncionario.
     */
    @RequestMapping(value = "/telefoneFuncionarios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TelefoneFuncionario> updateTelefoneFuncionario(@Valid @RequestBody TelefoneFuncionario telefoneFuncionario) throws URISyntaxException {
        log.debug("REST request to update TelefoneFuncionario : {}", telefoneFuncionario);
        if (telefoneFuncionario.getId() == null) {
            return createTelefoneFuncionario(telefoneFuncionario);
        }
        TelefoneFuncionario result = telefoneFuncionarioRepository.save(telefoneFuncionario);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("telefoneFuncionario", telefoneFuncionario.getId().toString()))
                .body(result);
    }

    /**
     * GET  /telefoneFuncionarios -> get all the telefoneFuncionarios.
     */
    @RequestMapping(value = "/telefoneFuncionarios",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TelefoneFuncionario> getAllTelefoneFuncionarios() {
        log.debug("REST request to get all TelefoneFuncionarios");
        return telefoneFuncionarioRepository.findAll();
    }

    /**
     * GET  /telefoneFuncionarios/:id -> get the "id" telefoneFuncionario.
     */
    @RequestMapping(value = "/telefoneFuncionarios/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TelefoneFuncionario> getTelefoneFuncionario(@PathVariable Long id) {
        log.debug("REST request to get TelefoneFuncionario : {}", id);
        return Optional.ofNullable(telefoneFuncionarioRepository.findOne(id))
            .map(telefoneFuncionario -> new ResponseEntity<>(
                telefoneFuncionario,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /telefoneFuncionarios/:id -> delete the "id" telefoneFuncionario.
     */
    @RequestMapping(value = "/telefoneFuncionarios/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTelefoneFuncionario(@PathVariable Long id) {
        log.debug("REST request to delete TelefoneFuncionario : {}", id);
        telefoneFuncionarioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("telefoneFuncionario", id.toString())).build();
    }
}
