package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.AvaliaCompetencia;
import br.com.unifieo.tmc.domain.CompetenciaPrestador;
import br.com.unifieo.tmc.domain.Morador;
import br.com.unifieo.tmc.domain.PrestadorServico;
import br.com.unifieo.tmc.repository.AvaliaCompetenciaRepository;
import br.com.unifieo.tmc.repository.CompetenciaPrestadorRepository;
import br.com.unifieo.tmc.repository.PrestadorServicoRepository;
import br.com.unifieo.tmc.service.MoradorService;
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

    @Inject
    private PrestadorServicoRepository prestadorServicoRepository;

    @Inject
    private CompetenciaPrestadorRepository competenciaPrestadorRepository;

    @Inject
    private MoradorService moradorService;

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

    @RequestMapping(value = "/avaliaCompetencias/nota/{idPrestador}/{idCompetencia}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AvaliaCompetencia> getAvaliacao(@PathVariable Long idPrestador, @PathVariable Long idCompetencia) {

        AvaliaCompetencia avaliaCompetencia;

        PrestadorServico prestadorServico = prestadorServicoRepository.findOne(idPrestador);
        CompetenciaPrestador competenciaPrestador = competenciaPrestadorRepository.findOne(idCompetencia);

        avaliaCompetencia = avaliaCompetenciaRepository.findOneByPrestadorServicoAndCompetenciaPrestador(prestadorServico, competenciaPrestador);

        Morador morador = moradorService.getCurrentMorador();
        if (morador == null)
            new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        if (avaliaCompetencia == null)
            avaliaCompetencia = new AvaliaCompetencia(prestadorServico, competenciaPrestador, morador);

        return new ResponseEntity<>(avaliaCompetencia, HttpStatus.OK);
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
