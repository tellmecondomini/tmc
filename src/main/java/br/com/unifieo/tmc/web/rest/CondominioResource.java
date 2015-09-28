package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.repository.CondominioRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Condominio.
 */
@RestController
@RequestMapping("/api")
public class CondominioResource {

    private final Logger log = LoggerFactory.getLogger(CondominioResource.class);

    @Inject
    private CondominioRepository condominioRepository;

    /**
     * POST  /condominios -> Create a new condominio.
     */
    @RequestMapping(value = "/condominios",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Condominio> createCondominio(@Valid @RequestBody Condominio condominio) throws URISyntaxException {
        log.debug("REST request to save Condominio : {}", condominio);
        if (condominio.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new condominio cannot already have an ID").body(null);
        }
        Condominio result = condominioRepository.save(condominio);
        return ResponseEntity.created(new URI("/api/condominios/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("condominio", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /condominios -> Updates an existing condominio.
     */
    @RequestMapping(value = "/condominios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Condominio> updateCondominio(@Valid @RequestBody Condominio condominio) throws URISyntaxException {
        log.debug("REST request to update Condominio : {}", condominio);
        if (condominio.getId() == null) {
            return createCondominio(condominio);
        }
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
    public List<Condominio> getAllCondominios(@RequestParam(required = false) String filter) {
        if ("endereco-is-null".equals(filter)) {
            log.debug("REST request to get all Condominios where endereco is null");
            return StreamSupport
                .stream(condominioRepository.findAll().spliterator(), false)
                .filter(condominio -> condominio.getEndereco() == null)
                .collect(Collectors.toList());
        }

        if ("responsavel-is-null".equals(filter)) {
            log.debug("REST request to get all Condominios where responsavel is null");
            return StreamSupport
                .stream(condominioRepository.findAll().spliterator(), false)
                .filter(condominio -> condominio.getResponsavel() == null)
                .collect(Collectors.toList());
        }

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
        return Optional.ofNullable(condominioRepository.findOne(id))
            .map(condominio -> new ResponseEntity<>(
                condominio,
                HttpStatus.OK))
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
