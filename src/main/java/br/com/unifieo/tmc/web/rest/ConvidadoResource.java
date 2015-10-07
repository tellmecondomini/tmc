package br.com.unifieo.tmc.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.Convidado;
import br.com.unifieo.tmc.repository.ConvidadoRepository;
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
 * REST controller for managing Convidado.
 */
@RestController
@RequestMapping("/api")
public class ConvidadoResource {

    private final Logger log = LoggerFactory.getLogger(ConvidadoResource.class);

    @Inject
    private ConvidadoRepository convidadoRepository;

    /**
     * POST  /convidados -> Create a new convidado.
     */
    @RequestMapping(value = "/convidados",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Convidado> createConvidado(@Valid @RequestBody Convidado convidado) throws URISyntaxException {
        log.debug("REST request to save Convidado : {}", convidado);
        if (convidado.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new convidado cannot already have an ID").body(null);
        }
        Convidado result = convidadoRepository.save(convidado);
        return ResponseEntity.created(new URI("/api/convidados/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("convidado", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /convidados -> Updates an existing convidado.
     */
    @RequestMapping(value = "/convidados",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Convidado> updateConvidado(@Valid @RequestBody Convidado convidado) throws URISyntaxException {
        log.debug("REST request to update Convidado : {}", convidado);
        if (convidado.getId() == null) {
            return createConvidado(convidado);
        }
        Convidado result = convidadoRepository.save(convidado);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("convidado", convidado.getId().toString()))
                .body(result);
    }

    /**
     * GET  /convidados -> get all the convidados.
     */
    @RequestMapping(value = "/convidados",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Convidado> getAllConvidados() {
        log.debug("REST request to get all Convidados");
        return convidadoRepository.findAll();
    }

    /**
     * GET  /convidados/:id -> get the "id" convidado.
     */
    @RequestMapping(value = "/convidados/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Convidado> getConvidado(@PathVariable Long id) {
        log.debug("REST request to get Convidado : {}", id);
        return Optional.ofNullable(convidadoRepository.findOne(id))
            .map(convidado -> new ResponseEntity<>(
                convidado,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /convidados/:id -> delete the "id" convidado.
     */
    @RequestMapping(value = "/convidados/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteConvidado(@PathVariable Long id) {
        log.debug("REST request to delete Convidado : {}", id);
        convidadoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("convidado", id.toString())).build();
    }
}
