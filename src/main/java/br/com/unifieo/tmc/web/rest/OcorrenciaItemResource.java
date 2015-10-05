package br.com.unifieo.tmc.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.OcorrenciaItem;
import br.com.unifieo.tmc.repository.OcorrenciaItemRepository;
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
 * REST controller for managing OcorrenciaItem.
 */
@RestController
@RequestMapping("/api")
public class OcorrenciaItemResource {

    private final Logger log = LoggerFactory.getLogger(OcorrenciaItemResource.class);

    @Inject
    private OcorrenciaItemRepository ocorrenciaItemRepository;

    /**
     * POST  /ocorrenciaItems -> Create a new ocorrenciaItem.
     */
    @RequestMapping(value = "/ocorrenciaItems",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OcorrenciaItem> createOcorrenciaItem(@Valid @RequestBody OcorrenciaItem ocorrenciaItem) throws URISyntaxException {
        log.debug("REST request to save OcorrenciaItem : {}", ocorrenciaItem);
        if (ocorrenciaItem.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new ocorrenciaItem cannot already have an ID").body(null);
        }
        OcorrenciaItem result = ocorrenciaItemRepository.save(ocorrenciaItem);
        return ResponseEntity.created(new URI("/api/ocorrenciaItems/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("ocorrenciaItem", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /ocorrenciaItems -> Updates an existing ocorrenciaItem.
     */
    @RequestMapping(value = "/ocorrenciaItems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OcorrenciaItem> updateOcorrenciaItem(@Valid @RequestBody OcorrenciaItem ocorrenciaItem) throws URISyntaxException {
        log.debug("REST request to update OcorrenciaItem : {}", ocorrenciaItem);
        if (ocorrenciaItem.getId() == null) {
            return createOcorrenciaItem(ocorrenciaItem);
        }
        OcorrenciaItem result = ocorrenciaItemRepository.save(ocorrenciaItem);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("ocorrenciaItem", ocorrenciaItem.getId().toString()))
                .body(result);
    }

    /**
     * GET  /ocorrenciaItems -> get all the ocorrenciaItems.
     */
    @RequestMapping(value = "/ocorrenciaItems",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OcorrenciaItem> getAllOcorrenciaItems() {
        log.debug("REST request to get all OcorrenciaItems");
        return ocorrenciaItemRepository.findAll();
    }

    /**
     * GET  /ocorrenciaItems/:id -> get the "id" ocorrenciaItem.
     */
    @RequestMapping(value = "/ocorrenciaItems/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OcorrenciaItem> getOcorrenciaItem(@PathVariable Long id) {
        log.debug("REST request to get OcorrenciaItem : {}", id);
        return Optional.ofNullable(ocorrenciaItemRepository.findOne(id))
            .map(ocorrenciaItem -> new ResponseEntity<>(
                ocorrenciaItem,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ocorrenciaItems/:id -> delete the "id" ocorrenciaItem.
     */
    @RequestMapping(value = "/ocorrenciaItems/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOcorrenciaItem(@PathVariable Long id) {
        log.debug("REST request to delete OcorrenciaItem : {}", id);
        ocorrenciaItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ocorrenciaItem", id.toString())).build();
    }
}
