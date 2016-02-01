package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Cep;
import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.domain.PrestadorServico;
import br.com.unifieo.tmc.repository.CepRepository;
import br.com.unifieo.tmc.repository.PrestadorServicoRepository;
import br.com.unifieo.tmc.repository.TelefonePrestadorServicoRepository;
import br.com.unifieo.tmc.service.CondominioService;
import br.com.unifieo.tmc.service.UserService;
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
 * REST controller for managing PrestadorServico.
 */
@RestController
@RequestMapping("/api")
public class PrestadorServicoResource {

    private final Logger log = LoggerFactory.getLogger(PrestadorServicoResource.class);

    @Inject
    private PrestadorServicoRepository prestadorServicoRepository;

    @Inject
    private CepRepository cepRepository;

    @Inject
    private CondominioService condominioService;

    @Inject
    private UserService userService;

    @Inject
    private TelefonePrestadorServicoRepository telefonePrestadorServicoRepository;

    /**
     * POST  /prestadorServicos -> Create a new prestadorServico.
     */
    @RequestMapping(value = "/prestadorServicos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrestadorServico> createPrestadorServico(@RequestBody PrestadorServico prestadorServico) throws URISyntaxException {
        log.debug("REST request to save PrestadorServico : {}", prestadorServico);

        if (prestadorServico.getId() != null)
            return ResponseEntity.badRequest().header("Failure", "A new prestadorServico cannot already have an ID").body(null);

        Cep cep = Optional
            .ofNullable(cepRepository.findOneByCep(prestadorServico.getCep().getCep()))
            .orElseGet(() -> cepRepository.save(prestadorServico.getCep()));

        cep.setLogradouro(prestadorServico.getCep().getLogradouro());
        cep.setBairro(prestadorServico.getCep().getBairro());
        cep.setCidade(prestadorServico.getCep().getCidade());
        cep.setCep(prestadorServico.getCep().getCep());
        cep.setUf(prestadorServico.getCep().getUf());

        cep = cepRepository.save(cep);
        prestadorServico.setCep(cep);

        prestadorServico.setMorador(userService.getMoradorAtual());
        prestadorServico.setCondominio(condominioService.getCurrentCondominio());

        PrestadorServico prestadorServicoSaved = prestadorServicoRepository.save(prestadorServico);

        prestadorServico.getTelefonePrestadorServicos().stream().forEach(telefone -> telefone.setPrestadorServico(prestadorServicoSaved));
        telefonePrestadorServicoRepository.save(prestadorServico.getTelefonePrestadorServicos());

        return ResponseEntity.created(new URI("/api/prestadorServicos/" + prestadorServicoSaved.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("prestadorServico", prestadorServicoSaved.getId().toString()))
            .body(prestadorServicoSaved);
    }

    /**
     * PUT  /prestadorServicos -> Updates an existing prestadorServico.
     */
    @RequestMapping(value = "/prestadorServicos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrestadorServico> updatePrestadorServico(@RequestBody PrestadorServico prestadorServico) throws URISyntaxException {
        log.debug("REST request to update PrestadorServico : {}", prestadorServico);
        if (prestadorServico.getId() == null)
            return createPrestadorServico(prestadorServico);
        Cep cep = Optional
            .ofNullable(cepRepository.findOneByCep(prestadorServico.getCep().getCep()))
            .orElseGet(() -> cepRepository.save(prestadorServico.getCep()));
        prestadorServico.setCep(cep);
        PrestadorServico prestadorServicoSaved = prestadorServicoRepository.save(prestadorServico);
        prestadorServico.getTelefonePrestadorServicos().stream().forEach(telefone -> telefone.setPrestadorServico(prestadorServicoSaved));
        telefonePrestadorServicoRepository.save(prestadorServico.getTelefonePrestadorServicos());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("prestadorServico", prestadorServicoSaved.getId().toString()))
            .body(prestadorServicoSaved);
    }

    /**
     * GET  /prestadorServicos -> get all the prestadorServicos.
     */
    @RequestMapping(value = "/prestadorServicos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PrestadorServico> getAllPrestadorServicos() {
        log.debug("REST request to get all PrestadorServicos");
        Condominio condominio = condominioService.getCurrentCondominio();
        return prestadorServicoRepository.findAllByCondominio(condominio);
    }

    /**
     * GET  /prestadorServicos/:id -> get the "id" prestadorServico.
     */
    @RequestMapping(value = "/prestadorServicos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrestadorServico> getPrestadorServico(@PathVariable Long id) {
        log.debug("REST request to get PrestadorServico : {}", id);
        return Optional.ofNullable(prestadorServicoRepository.findOne(id))
            .map(prestadorServico -> new ResponseEntity<>(
                prestadorServico,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /prestadorServicos/:id -> delete the "id" prestadorServico.
     */
    @RequestMapping(value = "/prestadorServicos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePrestadorServico(@PathVariable Long id) {
        log.debug("REST request to delete PrestadorServico : {}", id);
        prestadorServicoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("prestadorServico", id.toString())).build();
    }
}
