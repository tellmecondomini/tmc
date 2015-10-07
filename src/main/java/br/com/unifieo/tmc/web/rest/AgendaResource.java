package br.com.unifieo.tmc.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.unifieo.tmc.domain.Agenda;
import br.com.unifieo.tmc.repository.AgendaRepository;
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
 * REST controller for managing Agenda.
 */
@RestController
@RequestMapping("/api")
public class AgendaResource {

    private final Logger log = LoggerFactory.getLogger(AgendaResource.class);

    @Inject
    private AgendaRepository agendaRepository;

    /**
     * POST  /agendas -> Create a new agenda.
     */
    @RequestMapping(value = "/agendas",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Agenda> createAgenda(@Valid @RequestBody Agenda agenda) throws URISyntaxException {
        log.debug("REST request to save Agenda : {}", agenda);
        if (agenda.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new agenda cannot already have an ID").body(null);
        }
        Agenda result = agendaRepository.save(agenda);
        return ResponseEntity.created(new URI("/api/agendas/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("agenda", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /agendas -> Updates an existing agenda.
     */
    @RequestMapping(value = "/agendas",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Agenda> updateAgenda(@Valid @RequestBody Agenda agenda) throws URISyntaxException {
        log.debug("REST request to update Agenda : {}", agenda);
        if (agenda.getId() == null) {
            return createAgenda(agenda);
        }
        Agenda result = agendaRepository.save(agenda);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("agenda", agenda.getId().toString()))
                .body(result);
    }

    /**
     * GET  /agendas -> get all the agendas.
     */
    @RequestMapping(value = "/agendas",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Agenda> getAllAgendas() {
        log.debug("REST request to get all Agendas");
        return agendaRepository.findAll();
    }

    /**
     * GET  /agendas/:id -> get the "id" agenda.
     */
    @RequestMapping(value = "/agendas/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Agenda> getAgenda(@PathVariable Long id) {
        log.debug("REST request to get Agenda : {}", id);
        return Optional.ofNullable(agendaRepository.findOne(id))
            .map(agenda -> new ResponseEntity<>(
                agenda,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /agendas/:id -> delete the "id" agenda.
     */
    @RequestMapping(value = "/agendas/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAgenda(@PathVariable Long id) {
        log.debug("REST request to delete Agenda : {}", id);
        agendaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("agenda", id.toString())).build();
    }
}
