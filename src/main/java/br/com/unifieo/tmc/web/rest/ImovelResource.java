package br.com.unifieo.tmc.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.Imovel;
import br.com.unifieo.tmc.repository.ImovelRepository;
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
 * REST controller for managing Imovel.
 */
@RestController
@RequestMapping("/api")
public class ImovelResource {

    private final Logger log = LoggerFactory.getLogger(ImovelResource.class);

    @Inject
    private ImovelRepository imovelRepository;

    /**
     * POST  /imovels -> Create a new imovel.
     */
    @RequestMapping(value = "/imovels",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Imovel> createImovel(@Valid @RequestBody Imovel imovel) throws URISyntaxException {
        log.debug("REST request to save Imovel : {}", imovel);
        if (imovel.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new imovel cannot already have an ID").body(null);
        }
        Imovel result = imovelRepository.save(imovel);
        return ResponseEntity.created(new URI("/api/imovels/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("imovel", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /imovels -> Updates an existing imovel.
     */
    @RequestMapping(value = "/imovels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Imovel> updateImovel(@Valid @RequestBody Imovel imovel) throws URISyntaxException {
        log.debug("REST request to update Imovel : {}", imovel);
        if (imovel.getId() == null) {
            return createImovel(imovel);
        }
        Imovel result = imovelRepository.save(imovel);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("imovel", imovel.getId().toString()))
                .body(result);
    }

    /**
     * GET  /imovels -> get all the imovels.
     */
    @RequestMapping(value = "/imovels",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Imovel> getAllImovels() {
        log.debug("REST request to get all Imovels");
        return imovelRepository.findAll();
    }

    /**
     * GET  /imovels/:id -> get the "id" imovel.
     */
    @RequestMapping(value = "/imovels/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Imovel> getImovel(@PathVariable Long id) {
        log.debug("REST request to get Imovel : {}", id);
        return Optional.ofNullable(imovelRepository.findOne(id))
            .map(imovel -> new ResponseEntity<>(
                imovel,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /imovels/:id -> delete the "id" imovel.
     */
    @RequestMapping(value = "/imovels/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteImovel(@PathVariable Long id) {
        log.debug("REST request to delete Imovel : {}", id);
        imovelRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("imovel", id.toString())).build();
    }
}
