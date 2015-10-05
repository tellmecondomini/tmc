package br.com.unifieo.tmc.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.OcorrenciaTipo;
import br.com.unifieo.tmc.repository.OcorrenciaTipoRepository;
import br.com.unifieo.tmc.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
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
 * REST controller for managing OcorrenciaTipo.
 */
@RestController
@RequestMapping("/api")
public class OcorrenciaTipoResource {

    private final Logger log = LoggerFactory.getLogger(OcorrenciaTipoResource.class);

    @Inject
    private OcorrenciaTipoRepository ocorrenciaTipoRepository;

    /**
     * POST  /ocorrenciaTipos -> Create a new ocorrenciaTipo.
     */
    @RequestMapping(value = "/ocorrenciaTipos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OcorrenciaTipo> createOcorrenciaTipo(@RequestBody OcorrenciaTipo ocorrenciaTipo) throws URISyntaxException {
        log.debug("REST request to save OcorrenciaTipo : {}", ocorrenciaTipo);
        if (ocorrenciaTipo.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new ocorrenciaTipo cannot already have an ID").body(null);
        }
        OcorrenciaTipo result = ocorrenciaTipoRepository.save(ocorrenciaTipo);
        return ResponseEntity.created(new URI("/api/ocorrenciaTipos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("ocorrenciaTipo", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /ocorrenciaTipos -> Updates an existing ocorrenciaTipo.
     */
    @RequestMapping(value = "/ocorrenciaTipos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OcorrenciaTipo> updateOcorrenciaTipo(@RequestBody OcorrenciaTipo ocorrenciaTipo) throws URISyntaxException {
        log.debug("REST request to update OcorrenciaTipo : {}", ocorrenciaTipo);
        if (ocorrenciaTipo.getId() == null) {
            return createOcorrenciaTipo(ocorrenciaTipo);
        }
        OcorrenciaTipo result = ocorrenciaTipoRepository.save(ocorrenciaTipo);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("ocorrenciaTipo", ocorrenciaTipo.getId().toString()))
                .body(result);
    }

    /**
     * GET  /ocorrenciaTipos -> get all the ocorrenciaTipos.
     */
    @RequestMapping(value = "/ocorrenciaTipos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OcorrenciaTipo> getAllOcorrenciaTipos() {
        log.debug("REST request to get all OcorrenciaTipos");
        return ocorrenciaTipoRepository.findAll();
    }

    /**
     * GET  /ocorrenciaTipos/:id -> get the "id" ocorrenciaTipo.
     */
    @RequestMapping(value = "/ocorrenciaTipos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OcorrenciaTipo> getOcorrenciaTipo(@PathVariable Long id) {
        log.debug("REST request to get OcorrenciaTipo : {}", id);
        return Optional.ofNullable(ocorrenciaTipoRepository.findOne(id))
            .map(ocorrenciaTipo -> new ResponseEntity<>(
                ocorrenciaTipo,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ocorrenciaTipos/:id -> delete the "id" ocorrenciaTipo.
     */
    @RequestMapping(value = "/ocorrenciaTipos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOcorrenciaTipo(@PathVariable Long id) {
        log.debug("REST request to delete OcorrenciaTipo : {}", id);
        ocorrenciaTipoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ocorrenciaTipo", id.toString())).build();
    }
}
