package br.com.unifieo.tmc.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.OcorrenciaSubItem;
import br.com.unifieo.tmc.repository.OcorrenciaSubItemRepository;
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
 * REST controller for managing OcorrenciaSubItem.
 */
@RestController
@RequestMapping("/api")
public class OcorrenciaSubItemResource {

    private final Logger log = LoggerFactory.getLogger(OcorrenciaSubItemResource.class);

    @Inject
    private OcorrenciaSubItemRepository ocorrenciaSubItemRepository;

    /**
     * POST  /ocorrenciaSubItems -> Create a new ocorrenciaSubItem.
     */
    @RequestMapping(value = "/ocorrenciaSubItems",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OcorrenciaSubItem> createOcorrenciaSubItem(@Valid @RequestBody OcorrenciaSubItem ocorrenciaSubItem) throws URISyntaxException {
        log.debug("REST request to save OcorrenciaSubItem : {}", ocorrenciaSubItem);
        if (ocorrenciaSubItem.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new ocorrenciaSubItem cannot already have an ID").body(null);
        }
        OcorrenciaSubItem result = ocorrenciaSubItemRepository.save(ocorrenciaSubItem);
        return ResponseEntity.created(new URI("/api/ocorrenciaSubItems/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("ocorrenciaSubItem", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /ocorrenciaSubItems -> Updates an existing ocorrenciaSubItem.
     */
    @RequestMapping(value = "/ocorrenciaSubItems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OcorrenciaSubItem> updateOcorrenciaSubItem(@Valid @RequestBody OcorrenciaSubItem ocorrenciaSubItem) throws URISyntaxException {
        log.debug("REST request to update OcorrenciaSubItem : {}", ocorrenciaSubItem);
        if (ocorrenciaSubItem.getId() == null) {
            return createOcorrenciaSubItem(ocorrenciaSubItem);
        }
        OcorrenciaSubItem result = ocorrenciaSubItemRepository.save(ocorrenciaSubItem);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("ocorrenciaSubItem", ocorrenciaSubItem.getId().toString()))
                .body(result);
    }

    /**
     * GET  /ocorrenciaSubItems -> get all the ocorrenciaSubItems.
     */
    @RequestMapping(value = "/ocorrenciaSubItems",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<OcorrenciaSubItem> getAllOcorrenciaSubItems() {
        log.debug("REST request to get all OcorrenciaSubItems");
        return ocorrenciaSubItemRepository.findAll();
    }

    /**
     * GET  /ocorrenciaSubItems/:id -> get the "id" ocorrenciaSubItem.
     */
    @RequestMapping(value = "/ocorrenciaSubItems/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<OcorrenciaSubItem> getOcorrenciaSubItem(@PathVariable Long id) {
        log.debug("REST request to get OcorrenciaSubItem : {}", id);
        return Optional.ofNullable(ocorrenciaSubItemRepository.findOne(id))
            .map(ocorrenciaSubItem -> new ResponseEntity<>(
                ocorrenciaSubItem,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /ocorrenciaSubItems/:id -> delete the "id" ocorrenciaSubItem.
     */
    @RequestMapping(value = "/ocorrenciaSubItems/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOcorrenciaSubItem(@PathVariable Long id) {
        log.debug("REST request to delete OcorrenciaSubItem : {}", id);
        ocorrenciaSubItemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ocorrenciaSubItem", id.toString())).build();
    }
}
