package br.com.unifieo.tmc.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.Dependente;
import br.com.unifieo.tmc.repository.DependenteRepository;
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
 * REST controller for managing Dependente.
 */
@RestController
@RequestMapping("/api")
public class DependenteResource {

    private final Logger log = LoggerFactory.getLogger(DependenteResource.class);

    @Inject
    private DependenteRepository dependenteRepository;

    /**
     * POST  /dependentes -> Create a new dependente.
     */
    @RequestMapping(value = "/dependentes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dependente> createDependente(@Valid @RequestBody Dependente dependente) throws URISyntaxException {
        log.debug("REST request to save Dependente : {}", dependente);
        if (dependente.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new dependente cannot already have an ID").body(null);
        }
        Dependente result = dependenteRepository.save(dependente);
        return ResponseEntity.created(new URI("/api/dependentes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("dependente", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /dependentes -> Updates an existing dependente.
     */
    @RequestMapping(value = "/dependentes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dependente> updateDependente(@Valid @RequestBody Dependente dependente) throws URISyntaxException {
        log.debug("REST request to update Dependente : {}", dependente);
        if (dependente.getId() == null) {
            return createDependente(dependente);
        }
        Dependente result = dependenteRepository.save(dependente);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("dependente", dependente.getId().toString()))
                .body(result);
    }

    /**
     * GET  /dependentes -> get all the dependentes.
     */
    @RequestMapping(value = "/dependentes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Dependente> getAllDependentes() {
        log.debug("REST request to get all Dependentes");
        return dependenteRepository.findAll();
    }

    /**
     * GET  /dependentes/:id -> get the "id" dependente.
     */
    @RequestMapping(value = "/dependentes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dependente> getDependente(@PathVariable Long id) {
        log.debug("REST request to get Dependente : {}", id);
        return Optional.ofNullable(dependenteRepository.findOne(id))
            .map(dependente -> new ResponseEntity<>(
                dependente,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dependentes/:id -> delete the "id" dependente.
     */
    @RequestMapping(value = "/dependentes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDependente(@PathVariable Long id) {
        log.debug("REST request to delete Dependente : {}", id);
        dependenteRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dependente", id.toString())).build();
    }
}
