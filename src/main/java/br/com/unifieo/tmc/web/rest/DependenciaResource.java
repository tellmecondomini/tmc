package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Dependencia;
import br.com.unifieo.tmc.repository.DependenciaRepository;
import br.com.unifieo.tmc.service.DependenciaService;
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
 * REST controller for managing Dependencia.
 */
@RestController
@RequestMapping("/api")
public class DependenciaResource {

    private final Logger log = LoggerFactory.getLogger(DependenciaResource.class);

    @Inject
    private DependenciaRepository dependenciaRepository;

    @Inject
    private DependenciaService dependenciaService;

    /**
     * POST  /dependencias -> Create a new dependencia.
     */
    @RequestMapping(value = "/dependencias",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dependencia> createDependencia(@Valid @RequestBody Dependencia dependencia) throws URISyntaxException {
        log.debug("REST request to save Dependencia : {}", dependencia);
        if (dependencia.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new dependencia cannot already have an ID").body(null);
        }
        Dependencia result = dependenciaService.save(dependencia);
        return ResponseEntity.created(new URI("/api/dependencias/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("dependencia", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /dependencias -> Updates an existing dependencia.
     */
    @RequestMapping(value = "/dependencias",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dependencia> updateDependencia(@Valid @RequestBody Dependencia dependencia) throws URISyntaxException {
        log.debug("REST request to update Dependencia : {}", dependencia);
        if (dependencia.getId() == null) {
            return createDependencia(dependencia);
        }
        Dependencia result = dependenciaRepository.save(dependencia);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("dependencia", dependencia.getId().toString()))
                .body(result);
    }

    /**
     * GET  /dependencias -> get all the dependencias.
     */
    @RequestMapping(value = "/dependencias",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Dependencia> getAllDependencias() {
        log.debug("REST request to get all Dependencias");
        return dependenciaRepository.findAll();
    }

    /**
     * GET  /dependencias/:id -> get the "id" dependencia.
     */
    @RequestMapping(value = "/dependencias/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Dependencia> getDependencia(@PathVariable Long id) {
        log.debug("REST request to get Dependencia : {}", id);
        return Optional.ofNullable(dependenciaRepository.findOne(id))
            .map(dependencia -> new ResponseEntity<>(
                dependencia,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dependencias/:id -> delete the "id" dependencia.
     */
    @RequestMapping(value = "/dependencias/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDependencia(@PathVariable Long id) {
        log.debug("REST request to delete Dependencia : {}", id);
        dependenciaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dependencia", id.toString())).build();
    }
}
