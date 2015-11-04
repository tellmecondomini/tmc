package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Funcionario;
import br.com.unifieo.tmc.repository.FuncionarioRepository;
import br.com.unifieo.tmc.service.FuncionarioService;
import br.com.unifieo.tmc.web.rest.dto.FuncionarioDTO;
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
 * REST controller for managing Funcionario.
 */
@RestController
@RequestMapping("/api")
public class FuncionarioResource {

    private final Logger log = LoggerFactory.getLogger(FuncionarioResource.class);

    @Inject
    private FuncionarioRepository funcionarioRepository;

    @Inject
    private FuncionarioService funcionarioService;

    /**
     * POST  /funcionarios -> Create a new funcionarioDTO.
     */
    @RequestMapping(value = "/funcionarios",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Funcionario> createFuncionario(@RequestBody FuncionarioDTO funcionarioDTO) throws URISyntaxException {
        log.debug("REST request to save Funcionario : {}", funcionarioDTO);
        if (funcionarioDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new funcionario cannot already have an ID").body(null);
        }
        Funcionario result = funcionarioService.save(funcionarioDTO);
        return ResponseEntity.created(new URI("/api/funcionarios/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("funcionario", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /funcionarios -> Updates an existing funcionarioDTO.
     */
    @RequestMapping(value = "/funcionarios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Funcionario> updateFuncionario(@RequestBody FuncionarioDTO funcionarioDTO) throws URISyntaxException {
        log.debug("REST request to update Funcionario : {}", funcionarioDTO);
        if (funcionarioDTO.getId() == null) {
            return createFuncionario(funcionarioDTO);
        }
        Funcionario result = funcionarioService.save(funcionarioDTO);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("funcionario", funcionarioDTO.getId().toString()))
                .body(result);
    }

    /**
     * GET  /funcionarios -> get all the funcionarios.
     */
    @RequestMapping(value = "/funcionarios",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Funcionario> getAllFuncionarios() {
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
