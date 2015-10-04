package br.com.unifieo.tmc.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.TelefonePrestadorServico;
import br.com.unifieo.tmc.repository.TelefonePrestadorServicoRepository;
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
 * REST controller for managing TelefonePrestadorServico.
 */
@RestController
@RequestMapping("/api")
public class TelefonePrestadorServicoResource {

    private final Logger log = LoggerFactory.getLogger(TelefonePrestadorServicoResource.class);

    @Inject
    private TelefonePrestadorServicoRepository telefonePrestadorServicoRepository;

    /**
     * POST  /telefonePrestadorServicos -> Create a new telefonePrestadorServico.
     */
    @RequestMapping(value = "/telefonePrestadorServicos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TelefonePrestadorServico> createTelefonePrestadorServico(@Valid @RequestBody TelefonePrestadorServico telefonePrestadorServico) throws URISyntaxException {
        log.debug("REST request to save TelefonePrestadorServico : {}", telefonePrestadorServico);
        if (telefonePrestadorServico.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new telefonePrestadorServico cannot already have an ID").body(null);
        }
        TelefonePrestadorServico result = telefonePrestadorServicoRepository.save(telefonePrestadorServico);
        return ResponseEntity.created(new URI("/api/telefonePrestadorServicos/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("telefonePrestadorServico", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /telefonePrestadorServicos -> Updates an existing telefonePrestadorServico.
     */
    @RequestMapping(value = "/telefonePrestadorServicos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TelefonePrestadorServico> updateTelefonePrestadorServico(@Valid @RequestBody TelefonePrestadorServico telefonePrestadorServico) throws URISyntaxException {
        log.debug("REST request to update TelefonePrestadorServico : {}", telefonePrestadorServico);
        if (telefonePrestadorServico.getId() == null) {
            return createTelefonePrestadorServico(telefonePrestadorServico);
        }
        TelefonePrestadorServico result = telefonePrestadorServicoRepository.save(telefonePrestadorServico);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("telefonePrestadorServico", telefonePrestadorServico.getId().toString()))
                .body(result);
    }

    /**
     * GET  /telefonePrestadorServicos -> get all the telefonePrestadorServicos.
     */
    @RequestMapping(value = "/telefonePrestadorServicos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TelefonePrestadorServico> getAllTelefonePrestadorServicos() {
        log.debug("REST request to get all TelefonePrestadorServicos");
        return telefonePrestadorServicoRepository.findAll();
    }

    /**
     * GET  /telefonePrestadorServicos/:id -> get the "id" telefonePrestadorServico.
     */
    @RequestMapping(value = "/telefonePrestadorServicos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TelefonePrestadorServico> getTelefonePrestadorServico(@PathVariable Long id) {
        log.debug("REST request to get TelefonePrestadorServico : {}", id);
        return Optional.ofNullable(telefonePrestadorServicoRepository.findOne(id))
            .map(telefonePrestadorServico -> new ResponseEntity<>(
                telefonePrestadorServico,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /telefonePrestadorServicos/:id -> delete the "id" telefonePrestadorServico.
     */
    @RequestMapping(value = "/telefonePrestadorServicos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTelefonePrestadorServico(@PathVariable Long id) {
        log.debug("REST request to delete TelefonePrestadorServico : {}", id);
        telefonePrestadorServicoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("telefonePrestadorServico", id.toString())).build();
    }
}
