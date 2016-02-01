package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.commons.Funcoes;
import br.com.unifieo.tmc.domain.*;
import br.com.unifieo.tmc.repository.AvaliaCompetenciaRepository;
import br.com.unifieo.tmc.repository.CompetenciaPrestadorRepository;
import br.com.unifieo.tmc.repository.MoradorRepository;
import br.com.unifieo.tmc.repository.PrestadorServicoRepository;
import br.com.unifieo.tmc.service.CondominioService;
import br.com.unifieo.tmc.service.MailService;
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
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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
    private MoradorRepository moradorRepository;

    @Inject
    private MoradorService moradorService;

    @Inject
    private MailService mailService;

    @Inject
    private CondominioService condominioService;

    /**
     * POST  /avaliacoes -> Create a new avaliaCompetencia.
     */
    @RequestMapping(value = "/avaliaCompetencias",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AvaliaCompetencia> createAvaliaCompetencia(@RequestBody AvaliaCompetencia avaliaCompetencia) throws URISyntaxException {

        log.debug("REST request to save AvaliaCompetencia : {}", avaliaCompetencia);

        if (avaliaCompetencia.getId() != null)
            return ResponseEntity.badRequest().header("Failure", "A new avaliaCompetencia cannot already have an ID").body(null);

        Morador morador = moradorService.getCurrentMorador();

        avaliaCompetencia.setMorador(morador);
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
        final Condominio condominio = condominioService.getCurrentCondominio();
        return avaliaCompetenciaRepository.findAll()
            .stream()
            .filter(a -> a.getMorador().getCondominio().equals(condominio))
            .collect(Collectors.toList());
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

    @RequestMapping(value = "/avaliaCompetencias/new/{idPrestador}/{idCompetencia}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AvaliaCompetencia> newAvaliacao(@PathVariable Long idPrestador, @PathVariable Long idCompetencia) {
        PrestadorServico prestadorServico = prestadorServicoRepository.findOne(idPrestador);
        CompetenciaPrestador competenciaPrestador = competenciaPrestadorRepository.findOne(idCompetencia);
        AvaliaCompetencia avaliaCompetencia = new AvaliaCompetencia(prestadorServico, competenciaPrestador);
        return new ResponseEntity<>(avaliaCompetencia, HttpStatus.OK);
    }

    @RequestMapping(value = "/avaliaCompetencias/get/{idPrestador}/{idCompetencia}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AvaliaCompetencia> getAvaliacao(@PathVariable Long idPrestador, @PathVariable Long idCompetencia) {
        PrestadorServico prestadorServico = prestadorServicoRepository.findOne(idPrestador);
        CompetenciaPrestador competenciaPrestador = competenciaPrestadorRepository.findOne(idCompetencia);

        List<AvaliaCompetencia> avaliacoes = avaliaCompetenciaRepository.findAllByPrestadorServicoAndCompetenciaPrestador(prestadorServico, competenciaPrestador);
        Double notaMedia = avaliacoes.stream().filter(AvaliaCompetencia::isAtivo).map(a -> a.getNota()).mapToDouble(Integer::doubleValue).average().orElse(0.0D);

        AvaliaCompetencia avaliaCompetencia = new AvaliaCompetencia(prestadorServico, competenciaPrestador, notaMedia.intValue());
        return new ResponseEntity<>(avaliaCompetencia, HttpStatus.OK);
    }

    @RequestMapping(value = "/avaliaCompetencias/get/{idPrestador}/{idCompetencia}/{idMorador}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AvaliaCompetencia> getAvaliacaoByMorador(@PathVariable Long idPrestador, @PathVariable Long idCompetencia, @PathVariable Long idMorador) {
        PrestadorServico prestadorServico = prestadorServicoRepository.findOne(idPrestador);
        CompetenciaPrestador competenciaPrestador = competenciaPrestadorRepository.findOne(idCompetencia);
        Morador morador = moradorRepository.findOne(idMorador);

        List<AvaliaCompetencia> avaliacoes = avaliaCompetenciaRepository.findAllByPrestadorServicoAndCompetenciaPrestadorAndMorador(prestadorServico, competenciaPrestador, morador);

        final int today = Funcoes.getIntDate();
        AvaliaCompetencia avaliaCompetencia = new AvaliaCompetencia();
        Optional<AvaliaCompetencia> competencia = avaliacoes.stream().filter(AvaliaCompetencia::isAtivo).filter(a -> a.getData() == today).findAny();
        if (competencia.isPresent())
            avaliaCompetencia = competencia.get();

        return new ResponseEntity<>(avaliaCompetencia, HttpStatus.OK);
    }

    @RequestMapping(value = "/avaliaCompetencias/query/{idPrestador}/{idCompetencia}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<AvaliaCompetencia> getAvaliacoes(@PathVariable Long idPrestador, @PathVariable Long idCompetencia) {
        log.debug("REST request to get all AvaliaCompetencias");
        PrestadorServico prestadorServico = prestadorServicoRepository.findOne(idPrestador);
        CompetenciaPrestador competenciaPrestador = competenciaPrestadorRepository.findOne(idCompetencia);
        return avaliaCompetenciaRepository
            .findAllByPrestadorServicoAndCompetenciaPrestador(prestadorServico, competenciaPrestador)
            .stream()
            .filter(AvaliaCompetencia::isAtivo)
            .sorted((a1, a2) -> a2.getId().compareTo(a1.getId()))
            .sorted((a1, a2) -> Integer.compare(a2.getNota(), a1.getNota()))
            .collect(toList());
    }

    @RequestMapping(value = "/avaliaCompetencias/aprovacao/{idAvaliacao}/{aprovado}/{observacao}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<AvaliaCompetencia> getAprovacaoAvaliacao(@PathVariable Long idAvaliacao,
                                                                   @PathVariable boolean aprovado,
                                                                   @PathVariable byte[] observacao,
                                                                   HttpServletRequest request) {

        AvaliaCompetencia avaliaCompetencia = avaliaCompetenciaRepository.findOne(idAvaliacao);
        avaliaCompetencia.setAtivo(aprovado);
        AvaliaCompetencia result = avaliaCompetenciaRepository.save(avaliaCompetencia);

        String baseUrl = request.getScheme() +
            "://" +
            request.getServerName() +
            ":" +
            request.getServerPort();

        mailService.sendAvaliacaoPrestadorCompetencia(result, baseUrl, new String(observacao, StandardCharsets.UTF_8));

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("avaliaCompetencia", avaliaCompetencia.getId().toString()))
            .body(result);
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
