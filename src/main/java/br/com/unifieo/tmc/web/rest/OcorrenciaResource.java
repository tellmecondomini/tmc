package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Ocorrencia;
import br.com.unifieo.tmc.repository.OcorrenciaRepository;
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
 * REST controller for managing Ocorrencia.
 */
@RestController
@RequestMapping("/api")
public class OcorrenciaResource {

    private final Logger log = LoggerFactory.getLogger(OcorrenciaResource.class);

    @Inject
    private OcorrenciaRepository ocorrenciaRepository;

    /**
     * POST  /ocorrencias -> Create a new ocorrencia.
     */
    @RequestMapping(value = "/ocorrencias",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ocorrencia> createOcorrencia(@Valid @RequestBody Ocorrencia ocorrencia) throws URISyntaxException {
        log.debug("REST request to save Ocorrencia : {}", ocorrencia);
        if (ocorrencia.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new ocorrencia cannot already have an ID").body(null);
        }
        Ocorrencia result = ocorrenciaRepository.save(ocorrencia);
        return ResponseEntity.created(new URI("/api/ocorrencias/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("ocorrencia", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /ocorrencias -> Updates an existing ocorrencia.
     */
    @RequestMapping(value = "/ocorrencias",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ocorrencia> updateOcorrencia(@Valid @RequestBody Ocorrencia ocorrencia) throws URISyntaxException {
        log.debug("REST request to update Ocorrencia : {}", ocorrencia);
        if (ocorrencia.getId() == null) {
            return createOcorrencia(ocorrencia);
        }
        Ocorrencia result = ocorrenciaRepository.save(ocorrencia);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("ocorrencia", ocorrencia.getId().toString()))
                .body(result);
    }

    /**
     * GET  /ocorrencias -> get all the ocorrencias.
     */
    @RequestMapping(value = "/ocorrencias",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Ocorrencia> getAllOcorrencias() {
        log.debug("REST request to get all Ocorrencias");
        return ocorrenciaRepository.findAll();
    }

    /**
     * GET  /ocorrencias/:id -> get the "id" ocorrencia.
     */
    @RequestMapping(value = "/ocorrencias/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ocorrencia> getOcorrencia(@PathVariable Long id) {
        log.debug("REST request to get Ocorrencia : {}", id);
        return Optional.ofNullable(ocorrenciaRepository.findOne(id))
            .map(ocorrencia -> new ResponseEntity<>(
                ocorrencia,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ocorrencias/:id -> delete the "id" ocorrencia.
     */
    @RequestMapping(value = "/ocorrencias/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOcorrencia(@PathVariable Long id) {
        log.debug("REST request to delete Ocorrencia : {}", id);
        ocorrenciaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ocorrencia", id.toString())).build();
    }
}
