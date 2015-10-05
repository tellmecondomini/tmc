package br.com.unifieo.tmc.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.OcorrenciaPrioridade;
import br.com.unifieo.tmc.repository.OcorrenciaPrioridadeRepository;
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
 * REST controller for managing OcorrenciaPrioridade.
 */
@RestController
@RequestMapping("/api")
public class OcorrenciaPrioridadeResource {

    private final Logger log = LoggerFactory.getLogger(OcorrenciaPrioridadeResource.class);

    @Inject
    private OcorrenciaPrioridadeRepository ocorrenciaPrioridadeRepository;

    /**
     * POST  /ocorrenciaPrioridades -> Create a new ocorrenciaPrioridade.
     */
    @RequestMapping(value = "/ocorrenciaPrioridades",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OcorrenciaPrioridade> createOcorrenciaPrioridade(@Valid @RequestBody OcorrenciaPrioridade ocorrenciaPrioridade) throws URISyntaxException {
        log.debug("REST request to save OcorrenciaPrioridade : {}", ocorrenciaPrioridade);
        if (ocorrenciaPrioridade.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new ocorrenciaPrioridade cannot already have an ID").body(null);
        }
        OcorrenciaPrioridade result = ocorrenciaPrioridadeRepository.save(ocorrenciaPrioridade);
        return ResponseEntity.created(new URI("/api/ocorrenciaPrioridades/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("ocorrenciaPrioridade", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /ocorrenciaPrioridades -> Updates an existing ocorrenciaPrioridade.
     */
    @RequestMapping(value = "/ocorrenciaPrioridades",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OcorrenciaPrioridade> updateOcorrenciaPrioridade(@Valid @RequestBody OcorrenciaPrioridade ocorrenciaPrioridade) throws URISyntaxException {
        log.debug("REST request to update OcorrenciaPrioridade : {}", ocorrenciaPrioridade);
        if (ocorrenciaPrioridade.getId() == null) {
            return createOcorrenciaPrioridade(ocorrenciaPrioridade);
        }
        OcorrenciaPrioridade result = ocorrenciaPrioridadeRepository.save(ocorrenciaPrioridade);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("ocorrenciaPrioridade", ocorrenciaPrioridade.getId().toString()))
                .body(result);
    }

    /**
     * GET  /ocorrenciaPrioridades -> get all the ocorrenciaPrioridades.
     */
    @RequestMapping(value = "/ocorrenciaPrioridades",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OcorrenciaPrioridade> getAllOcorrenciaPrioridades() {
        log.debug("REST request to get all OcorrenciaPrioridades");
        return ocorrenciaPrioridadeRepository.findAll();
    }

    /**
     * GET  /ocorrenciaPrioridades/:id -> get the "id" ocorrenciaPrioridade.
     */
    @RequestMapping(value = "/ocorrenciaPrioridades/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OcorrenciaPrioridade> getOcorrenciaPrioridade(@PathVariable Long id) {
        log.debug("REST request to get OcorrenciaPrioridade : {}", id);
        return Optional.ofNullable(ocorrenciaPrioridadeRepository.findOne(id))
            .map(ocorrenciaPrioridade -> new ResponseEntity<>(
                ocorrenciaPrioridade,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ocorrenciaPrioridades/:id -> delete the "id" ocorrenciaPrioridade.
     */
    @RequestMapping(value = "/ocorrenciaPrioridades/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOcorrenciaPrioridade(@PathVariable Long id) {
        log.debug("REST request to delete OcorrenciaPrioridade : {}", id);
        ocorrenciaPrioridadeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ocorrenciaPrioridade", id.toString())).build();
    }
}
