package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Cep;
import br.com.unifieo.tmc.repository.CepRepository;
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
 * REST controller for managing Cep.
 */
@RestController
@RequestMapping("/api")
public class CepResource {

    private final Logger log = LoggerFactory.getLogger(CepResource.class);

    @Inject
    private CepRepository cepRepository;

    /**
     * POST  /ceps -> Create a new cep.
     */
    @RequestMapping(value = "/ceps",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cep> createCep(@Valid @RequestBody Cep cep) throws URISyntaxException {
        log.debug("REST request to save Cep : {}", cep);
        if (cep.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new cep cannot already have an ID").body(null);
        }
        Cep result = cepRepository.save(cep);
        return ResponseEntity.created(new URI("/api/ceps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cep", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ceps -> Updates an existing cep.
     */
    @RequestMapping(value = "/ceps",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cep> updateCep(@Valid @RequestBody Cep cep) throws URISyntaxException {
        log.debug("REST request to update Cep : {}", cep);
        if (cep.getId() == null) {
            return createCep(cep);
        }
        Cep result = cepRepository.save(cep);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cep", cep.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ceps -> get all the ceps.
     */
    @RequestMapping(value = "/ceps",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Cep> getAllCeps() {
        log.debug("REST request to get all Ceps");
        return cepRepository.findAll();
    }

    /**
     * GET  /ceps/:id -> get the "id" cep.
     */
    @RequestMapping(value = "/ceps/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Cep> getCep(@PathVariable Long id) {
        log.debug("REST request to get Cep : {}", id);
        return Optional.ofNullable(cepRepository.findOne(id))
            .map(cep -> new ResponseEntity<>(
                cep,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ceps/:id -> delete the "id" cep.
     */
    @RequestMapping(value = "/ceps/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCep(@PathVariable Long id) {
        log.debug("REST request to delete Cep : {}", id);
        cepRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cep", id.toString())).build();
    }
}
