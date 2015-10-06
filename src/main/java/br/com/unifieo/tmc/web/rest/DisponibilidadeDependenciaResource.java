package br.com.unifieo.tmc.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.DisponibilidadeDependencia;
import br.com.unifieo.tmc.repository.DisponibilidadeDependenciaRepository;
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
 * REST controller for managing DisponibilidadeDependencia.
 */
@RestController
@RequestMapping("/api")
public class DisponibilidadeDependenciaResource {

    private final Logger log = LoggerFactory.getLogger(DisponibilidadeDependenciaResource.class);

    @Inject
    private DisponibilidadeDependenciaRepository disponibilidadeDependenciaRepository;

    /**
     * POST  /disponibilidadeDependencias -> Create a new disponibilidadeDependencia.
     */
    @RequestMapping(value = "/disponibilidadeDependencias",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DisponibilidadeDependencia> createDisponibilidadeDependencia(@Valid @RequestBody DisponibilidadeDependencia disponibilidadeDependencia) throws URISyntaxException {
        log.debug("REST request to save DisponibilidadeDependencia : {}", disponibilidadeDependencia);
        if (disponibilidadeDependencia.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new disponibilidadeDependencia cannot already have an ID").body(null);
        }
        DisponibilidadeDependencia result = disponibilidadeDependenciaRepository.save(disponibilidadeDependencia);
        return ResponseEntity.created(new URI("/api/disponibilidadeDependencias/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("disponibilidadeDependencia", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /disponibilidadeDependencias -> Updates an existing disponibilidadeDependencia.
     */
    @RequestMapping(value = "/disponibilidadeDependencias",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DisponibilidadeDependencia> updateDisponibilidadeDependencia(@Valid @RequestBody DisponibilidadeDependencia disponibilidadeDependencia) throws URISyntaxException {
        log.debug("REST request to update DisponibilidadeDependencia : {}", disponibilidadeDependencia);
        if (disponibilidadeDependencia.getId() == null) {
            return createDisponibilidadeDependencia(disponibilidadeDependencia);
        }
        DisponibilidadeDependencia result = disponibilidadeDependenciaRepository.save(disponibilidadeDependencia);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("disponibilidadeDependencia", disponibilidadeDependencia.getId().toString()))
                .body(result);
    }

    /**
     * GET  /disponibilidadeDependencias -> get all the disponibilidadeDependencias.
     */
    @RequestMapping(value = "/disponibilidadeDependencias",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<DisponibilidadeDependencia> getAllDisponibilidadeDependencias() {
        log.debug("REST request to get all DisponibilidadeDependencias");
        return disponibilidadeDependenciaRepository.findAll();
    }

    /**
     * GET  /disponibilidadeDependencias/:id -> get the "id" disponibilidadeDependencia.
     */
    @RequestMapping(value = "/disponibilidadeDependencias/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DisponibilidadeDependencia> getDisponibilidadeDependencia(@PathVariable Long id) {
        log.debug("REST request to get DisponibilidadeDependencia : {}", id);
        return Optional.ofNullable(disponibilidadeDependenciaRepository.findOne(id))
            .map(disponibilidadeDependencia -> new ResponseEntity<>(
                disponibilidadeDependencia,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /disponibilidadeDependencias/:id -> delete the "id" disponibilidadeDependencia.
     */
    @RequestMapping(value = "/disponibilidadeDependencias/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDisponibilidadeDependencia(@PathVariable Long id) {
        log.debug("REST request to delete DisponibilidadeDependencia : {}", id);
        disponibilidadeDependenciaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("disponibilidadeDependencia", id.toString())).build();
    }
}
