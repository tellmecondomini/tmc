package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Funcionario;
import br.com.unifieo.tmc.repository.FuncionarioRepository;
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
 * REST controller for managing Funcionario.
 */
@RestController
@RequestMapping("/api")
public class FuncionarioResource {

    private final Logger log = LoggerFactory.getLogger(FuncionarioResource.class);

    @Inject
    private FuncionarioRepository funcionarioRepository;

    /**
     * POST  /funcionarios -> Create a new funcionario.
     */
    @RequestMapping(value = "/funcionarios",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Funcionario> createFuncionario(@Valid @RequestBody Funcionario funcionario) throws URISyntaxException {
        log.debug("REST request to save Funcionario : {}", funcionario);
        if (funcionario.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new funcionario cannot already have an ID").body(null);
        }
        Funcionario result = funcionarioRepository.save(funcionario);
        return ResponseEntity.created(new URI("/api/funcionarios/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("funcionario", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /funcionarios -> Updates an existing funcionario.
     */
    @RequestMapping(value = "/funcionarios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Funcionario> updateFuncionario(@Valid @RequestBody Funcionario funcionario) throws URISyntaxException {
        log.debug("REST request to update Funcionario : {}", funcionario);
        if (funcionario.getId() == null) {
            return createFuncionario(funcionario);
        }
        Funcionario result = funcionarioRepository.save(funcionario);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("funcionario", funcionario.getId().toString()))
                .body(result);
    }

    /**
     * GET  /funcionarios -> get all the funcionarios.
     */
    @RequestMapping(value = "/funcionarios",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Funcionario> getAllFuncionarios(@RequestParam(required = false) String filter) {
        if ("endereco-is-null".equals(filter)) {
            log.debug("REST request to get all Funcionarios where endereco is null");
            return StreamSupport
                .stream(funcionarioRepository.findAll().spliterator(), false)
                .filter(funcionario -> funcionario.getEndereco() == null)
                .collect(Collectors.toList());
        }

        log.debug("REST request to get all Funcionarios");
        return funcionarioRepository.findAll();
    }

    /**
     * GET  /funcionarios/:id -> get the "id" funcionario.
     */
    @RequestMapping(value = "/funcionarios/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Funcionario> getFuncionario(@PathVariable Long id) {
        log.debug("REST request to get Funcionario : {}", id);
        return Optional.ofNullable(funcionarioRepository.findOne(id))
            .map(funcionario -> new ResponseEntity<>(
                funcionario,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /funcionarios/:id -> delete the "id" funcionario.
     */
    @RequestMapping(value = "/funcionarios/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFuncionario(@PathVariable Long id) {
        log.debug("REST request to delete Funcionario : {}", id);
        funcionarioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("funcionario", id.toString())).build();
    }
}
