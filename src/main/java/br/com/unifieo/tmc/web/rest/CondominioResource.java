package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.repository.CondominioRepository;
import br.com.unifieo.tmc.service.CondominioService;
import br.com.unifieo.tmc.web.rest.dto.CondominioDTO;
import br.com.unifieo.tmc.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Condominio.
 */
@RestController
@RequestMapping("/api")
public class CondominioResource {

    private final Logger log = LoggerFactory.getLogger(CondominioResource.class);

    @Inject
    private CondominioRepository condominioRepository;

    @Inject
    private CondominioService condominioService;

    /**
     * POST  /condominios -> Create a new condominioDTO.
     */
    @RequestMapping(value = "/condominios",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Condominio> createCondominio(@RequestBody CondominioDTO condominioDTO) throws URISyntaxException {

        log.debug("REST request to save Condominio : {}", condominioDTO);
        if (condominioDTO.getId() != null)
            return ResponseEntity.badRequest().header("Failure", "A new condominioDTO cannot already have an ID").body(null);

        Condominio result = condominioService.save(condominioDTO);

        return ResponseEntity.created(new URI("/api/condominios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("condominioDTO", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /condominios -> Updates an existing condominio.
     */
    @RequestMapping(value = "/condominios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Condominio> updateCondominio(@RequestBody Condominio condominio) throws URISyntaxException {

        log.debug("REST request to update Condominio : {}", condominio);
        if (condominio.getId() == null)
            return null; // createCondominio(condominio);

        Condominio result = condominioRepository.save(condominio);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("condominio", condominio.getId().toString()))
            .body(result);
    }

    /**
     * GET  /condominios -> get all the condominios.
     */
    @RequestMapping(value = "/condominios",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Condominio> getAllCondominios() {
        log.debug("REST request to get all Condominios");
        return condominioRepository.findAll();
    }

    /**
     * GET  /condominios/:id -> get the "id" condominio.
     */
    @RequestMapping(value = "/condominios/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Condominio> getCondominio(@PathVariable Long id) {
        log.debug("REST request to get Condominio : {}", id);
        return Optional
            .ofNullable(condominioRepository.findOne(id))
            .map(condominio -> new ResponseEntity<>(condominio, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /condominios/:id -> delete the "id" condominio.
     */
    @RequestMapping(value = "/condominios/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCondominio(@PathVariable Long id) {
        log.debug("REST request to delete Condominio : {}", id);
        condominioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("condominio", id.toString())).build();
    }
}
