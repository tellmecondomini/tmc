package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.service.PrestadorServicoService;
import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.PrestadorServico;
import br.com.unifieo.tmc.repository.PrestadorServicoRepository;
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
 * REST controller for managing PrestadorServico.
 */
@RestController
@RequestMapping("/api")
public class PrestadorServicoResource {

    private final Logger log = LoggerFactory.getLogger(PrestadorServicoResource.class);

    @Inject
    private PrestadorServicoRepository prestadorServicoRepository;

    @Inject
    private PrestadorServicoService prestadorServicoService;

    /**
     * POST  /prestadorServicos -> Create a new prestadorServico.
     */
    @RequestMapping(value = "/prestadorServicos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrestadorServico> createPrestadorServico(@Valid @RequestBody PrestadorServico prestadorServico) throws URISyntaxException {
        log.debug("REST request to save PrestadorServico : {}", prestadorServico);
        if (prestadorServico.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new prestadorServico cannot already have an ID").body(null);
        }
        PrestadorServico result = prestadorServicoService.save(prestadorServico);
        return ResponseEntity.created(new URI("/api/prestadorServicos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("prestadorServico", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /prestadorServicos -> Updates an existing prestadorServico.
     */
    @RequestMapping(value = "/prestadorServicos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrestadorServico> updatePrestadorServico(@Valid @RequestBody PrestadorServico prestadorServico) throws URISyntaxException {
        log.debug("REST request to update PrestadorServico : {}", prestadorServico);
        if (prestadorServico.getId() == null) {
            return createPrestadorServico(prestadorServico);
        }
        PrestadorServico result = prestadorServicoRepository.save(prestadorServico);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("prestadorServico", prestadorServico.getId().toString()))
                .body(result);
    }

    /**
     * GET  /prestadorServicos -> get all the prestadorServicos.
     */
    @RequestMapping(value = "/prestadorServicos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PrestadorServico> getAllPrestadorServicos() {
        log.debug("REST request to get all PrestadorServicos");
        return prestadorServicoRepository.findAll();
    }

    /**
     * GET  /prestadorServicos/:id -> get the "id" prestadorServico.
     */
    @RequestMapping(value = "/prestadorServicos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrestadorServico> getPrestadorServico(@PathVariable Long id) {
        log.debug("REST request to get PrestadorServico : {}", id);
        return Optional.ofNullable(prestadorServicoRepository.findOne(id))
            .map(prestadorServico -> new ResponseEntity<>(
                prestadorServico,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prestadorServicos/:id -> delete the "id" prestadorServico.
     */
    @RequestMapping(value = "/prestadorServicos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePrestadorServico(@PathVariable Long id) {
        log.debug("REST request to delete PrestadorServico : {}", id);
        prestadorServicoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("prestadorServico", id.toString())).build();
    }
}
