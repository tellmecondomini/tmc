package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.TelefoneCondominio;
import br.com.unifieo.tmc.repository.TelefoneCondominioRepository;
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
 * REST controller for managing TelefoneCondominio.
 */
@RestController
@RequestMapping("/api")
public class TelefoneCondominioResource {

    private final Logger log = LoggerFactory.getLogger(TelefoneCondominioResource.class);

    @Inject
    private TelefoneCondominioRepository telefoneCondominioRepository;

    /**
     * POST  /telefoneCondominios -> Create a new telefoneCondominio.
     */
    @RequestMapping(value = "/telefoneCondominios",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TelefoneCondominio> createTelefoneCondominio(@Valid @RequestBody TelefoneCondominio telefoneCondominio) throws URISyntaxException {
        log.debug("REST request to save TelefoneCondominio : {}", telefoneCondominio);
        if (telefoneCondominio.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new telefoneCondominio cannot already have an ID").body(null);
        }
        TelefoneCondominio result = telefoneCondominioRepository.save(telefoneCondominio);
        return ResponseEntity.created(new URI("/api/telefoneCondominios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("telefoneCondominio", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /telefoneCondominios -> Updates an existing telefoneCondominio.
     */
    @RequestMapping(value = "/telefoneCondominios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TelefoneCondominio> updateTelefoneCondominio(@Valid @RequestBody TelefoneCondominio telefoneCondominio) throws URISyntaxException {
        log.debug("REST request to update TelefoneCondominio : {}", telefoneCondominio);
        if (telefoneCondominio.getId() == null) {
            return createTelefoneCondominio(telefoneCondominio);
        }
        TelefoneCondominio result = telefoneCondominioRepository.save(telefoneCondominio);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("telefoneCondominio", telefoneCondominio.getId().toString()))
            .body(result);
    }

    /**
     * GET  /telefoneCondominios -> get all the telefoneCondominios.
     */
    @RequestMapping(value = "/telefoneCondominios",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TelefoneCondominio> getAllTelefoneCondominios() {
        log.debug("REST request to get all TelefoneCondominios");
        return telefoneCondominioRepository.findAll();
    }

    /**
     * GET  /telefoneCondominios/:id -> get the "id" telefoneCondominio.
     */
    @RequestMapping(value = "/telefoneCondominios/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TelefoneCondominio> getTelefoneCondominio(@PathVariable Long id) {
        log.debug("REST request to get TelefoneCondominio : {}", id);
        return Optional.ofNullable(telefoneCondominioRepository.findOne(id))
            .map(telefoneCondominio -> new ResponseEntity<>(
                telefoneCondominio,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /telefoneCondominios/:id -> delete the "id" telefoneCondominio.
     */
    @RequestMapping(value = "/telefoneCondominios/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTelefoneCondominio(@PathVariable Long id) {
        log.debug("REST request to delete TelefoneCondominio : {}", id);
        telefoneCondominioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("telefoneCondominio", id.toString())).build();
    }
}
