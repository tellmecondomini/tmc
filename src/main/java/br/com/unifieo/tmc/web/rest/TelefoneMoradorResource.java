package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.TelefoneMorador;
import br.com.unifieo.tmc.repository.TelefoneMoradorRepository;
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
 * REST controller for managing TelefoneMorador.
 */
@RestController
@RequestMapping("/api")
public class TelefoneMoradorResource {

    private final Logger log = LoggerFactory.getLogger(TelefoneMoradorResource.class);

    @Inject
    private TelefoneMoradorRepository telefoneMoradorRepository;

    /**
     * POST  /telefoneMoradors -> Create a new telefoneMorador.
     */
    @RequestMapping(value = "/telefoneMoradors",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TelefoneMorador> createTelefoneMorador(@Valid @RequestBody TelefoneMorador telefoneMorador) throws URISyntaxException {
        log.debug("REST request to save TelefoneMorador : {}", telefoneMorador);
        if (telefoneMorador.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new telefoneMorador cannot already have an ID").body(null);
        }
        TelefoneMorador result = telefoneMoradorRepository.save(telefoneMorador);
        return ResponseEntity.created(new URI("/api/telefoneMoradors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("telefoneMorador", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /telefoneMoradors -> Updates an existing telefoneMorador.
     */
    @RequestMapping(value = "/telefoneMoradors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TelefoneMorador> updateTelefoneMorador(@Valid @RequestBody TelefoneMorador telefoneMorador) throws URISyntaxException {
        log.debug("REST request to update TelefoneMorador : {}", telefoneMorador);
        if (telefoneMorador.getId() == null) {
            return createTelefoneMorador(telefoneMorador);
        }
        TelefoneMorador result = telefoneMoradorRepository.save(telefoneMorador);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("telefoneMorador", telefoneMorador.getId().toString()))
            .body(result);
    }

    /**
     * GET  /telefoneMoradors -> get all the telefoneMoradors.
     */
    @RequestMapping(value = "/telefoneMoradors",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TelefoneMorador> getAllTelefoneMoradors() {
        log.debug("REST request to get all TelefoneMoradors");
        return telefoneMoradorRepository.findAll();
    }

    /**
     * GET  /telefoneMoradors/:id -> get the "id" telefoneMorador.
     */
    @RequestMapping(value = "/telefoneMoradors/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TelefoneMorador> getTelefoneMorador(@PathVariable Long id) {
        log.debug("REST request to get TelefoneMorador : {}", id);
        return Optional.ofNullable(telefoneMoradorRepository.findOne(id))
            .map(telefoneMorador -> new ResponseEntity<>(
                telefoneMorador,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /telefoneMoradors/:id -> delete the "id" telefoneMorador.
     */
    @RequestMapping(value = "/telefoneMoradors/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTelefoneMorador(@PathVariable Long id) {
        log.debug("REST request to delete TelefoneMorador : {}", id);
        telefoneMoradorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("telefoneMorador", id.toString())).build();
    }
}
