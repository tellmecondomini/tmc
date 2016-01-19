package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.CompetenciaPrestador;
import br.com.unifieo.tmc.repository.CompetenciaPrestadorRepository;
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
 * REST controller for managing CompetenciaPrestador.
 */
@RestController
@RequestMapping("/api")
public class CompetenciaPrestadorResource {

    private final Logger log = LoggerFactory.getLogger(CompetenciaPrestadorResource.class);

    @Inject
    private CompetenciaPrestadorRepository competenciaPrestadorRepository;

    /**
     * POST  /competenciaPrestadors -> Create a new competenciaPrestador.
     */
    @RequestMapping(value = "/competenciaPrestadors",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompetenciaPrestador> createCompetenciaPrestador(@Valid @RequestBody CompetenciaPrestador competenciaPrestador) throws URISyntaxException {
        log.debug("REST request to save CompetenciaPrestador : {}", competenciaPrestador);
        if (competenciaPrestador.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new competenciaPrestador cannot already have an ID").body(null);
        }
        CompetenciaPrestador result = competenciaPrestadorRepository.save(competenciaPrestador);
        return ResponseEntity.created(new URI("/api/competenciaPrestadors/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("competenciaPrestador", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /competenciaPrestadors -> Updates an existing competenciaPrestador.
     */
    @RequestMapping(value = "/competenciaPrestadors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompetenciaPrestador> updateCompetenciaPrestador(@Valid @RequestBody CompetenciaPrestador competenciaPrestador) throws URISyntaxException {
        log.debug("REST request to update CompetenciaPrestador : {}", competenciaPrestador);
        if (competenciaPrestador.getId() == null) {
            return createCompetenciaPrestador(competenciaPrestador);
        }
        CompetenciaPrestador result = competenciaPrestadorRepository.save(competenciaPrestador);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("competenciaPrestador", competenciaPrestador.getId().toString()))
                .body(result);
    }

    /**
     * GET  /competenciaPrestadors -> get all the competenciaPrestadors.
     */
    @RequestMapping(value = "/competenciaPrestadors",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CompetenciaPrestador> getAllCompetenciaPrestadors() {
        log.debug("REST request to get all CompetenciaPrestadors");
        return competenciaPrestadorRepository.findAll();
    }

    /**
     * GET  /competenciaPrestadors/:id -> get the "id" competenciaPrestador.
     */
    @RequestMapping(value = "/competenciaPrestadors/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CompetenciaPrestador> getCompetenciaPrestador(@PathVariable Long id) {
        log.debug("REST request to get CompetenciaPrestador : {}", id);
        return Optional.ofNullable(competenciaPrestadorRepository.findOne(id))
            .map(competenciaPrestador -> new ResponseEntity<>(
                competenciaPrestador,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /competenciaPrestadors/:id -> delete the "id" competenciaPrestador.
     */
    @RequestMapping(value = "/competenciaPrestadors/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCompetenciaPrestador(@PathVariable Long id) {
        log.debug("REST request to delete CompetenciaPrestador : {}", id);
        competenciaPrestadorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("competenciaPrestador", id.toString())).build();
    }
}
