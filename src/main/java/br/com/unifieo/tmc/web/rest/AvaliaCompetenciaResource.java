package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.AvaliaCompetencia;
import br.com.unifieo.tmc.domain.CompetenciaPrestador;
import br.com.unifieo.tmc.domain.PrestadorServico;
import br.com.unifieo.tmc.repository.AvaliaCompetenciaRepository;
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
 * REST controller for managing AvaliaCompetencia.
 */
@RestController
@RequestMapping("/api")
public class AvaliaCompetenciaResource {

    private final Logger log = LoggerFactory.getLogger(AvaliaCompetenciaResource.class);

    @Inject
    private AvaliaCompetenciaRepository avaliaCompetenciaRepository;

    /**
     * POST  /avaliacoes -> Create a new avaliaCompetencia.
     */
    @RequestMapping(value = "/avaliaCompetencias",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AvaliaCompetencia> createAvaliaCompetencia(@RequestBody AvaliaCompetencia avaliaCompetencia) throws URISyntaxException {
        log.debug("REST request to save AvaliaCompetencia : {}", avaliaCompetencia);
        if (avaliaCompetencia.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new avaliaCompetencia cannot already have an ID").body(null);
        }
        AvaliaCompetencia result = avaliaCompetenciaRepository.save(avaliaCompetencia);
        return ResponseEntity.created(new URI("/api/avaliacoes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("avaliaCompetencia", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /avaliacoes -> Updates an existing avaliaCompetencia.
     */
    @RequestMapping(value = "/avaliaCompetencias",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AvaliaCompetencia> updateAvaliaCompetencia(@RequestBody AvaliaCompetencia avaliaCompetencia) throws URISyntaxException {
        log.debug("REST request to update AvaliaCompetencia : {}", avaliaCompetencia);
        if (avaliaCompetencia.getId() == null) {
            return createAvaliaCompetencia(avaliaCompetencia);
        }
        AvaliaCompetencia result = avaliaCompetenciaRepository.save(avaliaCompetencia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("avaliaCompetencia", avaliaCompetencia.getId().toString()))
            .body(result);
    }

    /**
     * GET  /avaliacoes -> get all the avaliacoes.
     */
    @RequestMapping(value = "/avaliaCompetencias",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AvaliaCompetencia> getAllAvaliaCompetencias() {
        log.debug("REST request to get all AvaliaCompetencias");
        return avaliaCompetenciaRepository.findAll();
    }

    /**
     * GET  /avaliacoes/:id -> get the "id" avaliaCompetencia.
     */
    @RequestMapping(value = "/avaliaCompetencias/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AvaliaCompetencia> getAvaliaCompetencia(@PathVariable Long id) {
        log.debug("REST request to get AvaliaCompetencia : {}", id);
        return Optional.ofNullable(avaliaCompetenciaRepository.findOne(id))
            .map(avaliaCompetencia -> new ResponseEntity<>(
                avaliaCompetencia,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/avaliaCompetencias/{prestadorServico}/{competenciaPrestador}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Integer getNota(@PathVariable PrestadorServico prestadorServico,
                           @PathVariable CompetenciaPrestador competenciaPrestador) {

        return 15;
    }

    /**
     * DELETE  /avaliacoes/:id -> delete the "id" avaliaCompetencia.
     */
    @RequestMapping(value = "/avaliaCompetencias/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAvaliaCompetencia(@PathVariable Long id) {
        log.debug("REST request to delete AvaliaCompetencia : {}", id);
        avaliaCompetenciaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("avaliaCompetencia", id.toString())).build();
    }
}
