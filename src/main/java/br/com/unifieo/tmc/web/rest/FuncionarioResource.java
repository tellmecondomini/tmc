package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Funcionario;
import br.com.unifieo.tmc.domain.User;
import br.com.unifieo.tmc.repository.FuncionarioRepository;
import br.com.unifieo.tmc.repository.UserRepository;
import br.com.unifieo.tmc.service.FuncionarioService;
import br.com.unifieo.tmc.service.UserService;
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
import javax.servlet.http.HttpServletRequest;
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

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /funcionarios -> Create a new funcionarioDTO.
     */
    @RequestMapping(value = "/funcionarios",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Funcionario> createFuncionario(@RequestBody FuncionarioDTO funcionarioDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Funcionario : {}", funcionarioDTO);

        if (funcionarioDTO.getId() != null)
            return ResponseEntity.badRequest().header("Failure", "A new funcionario cannot already have an ID").body(null);

        Optional<User> userWithAuthoritiesByLogin = userService.getUserWithAuthoritiesByLogin(funcionarioDTO.getEmail());
        if (userWithAuthoritiesByLogin.isPresent()) {
            User usuario = userWithAuthoritiesByLogin.get();
            if (usuario != null)
                return ResponseEntity.badRequest().header("Failure", "e-mail address already in use").body(null);
        }

        String baseUrl = request.getScheme() +
            "://" +
            request.getServerName() +
            ":" +
            request.getServerPort();

        Funcionario result = funcionarioService.save(funcionarioDTO, baseUrl);

        return ResponseEntity.created(new URI("/api/funcionarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("funcionario", result.getNome()))
            .body(result);
    }

    /**
     * PUT  /funcionarios -> Updates an existing funcionarioDTO.
     */
    @RequestMapping(value = "/funcionarios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Funcionario> updateFuncionario(@RequestBody FuncionarioDTO funcionarioDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update Funcionario : {}", funcionarioDTO);

        if (funcionarioDTO.getId() == null)
            return createFuncionario(funcionarioDTO, request);

        String baseUrl = request.getScheme() +
            "://" +
            request.getServerName() +
            ":" +
            request.getServerPort();

        Funcionario result = funcionarioService.save(funcionarioDTO, baseUrl);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("funcionario", funcionarioDTO.getNome()))
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
        return funcionarioService.findAllByCondominioAtual();
    }

    /**
     * GET  /funcionarios/:id -> get the "id" funcionario.
     */
    @RequestMapping(value = "/funcionarios/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FuncionarioDTO> getFuncionario(@PathVariable Long id) {
        log.debug("REST request to get Funcionario : {}", id);
        return Optional.ofNullable(funcionarioRepository.findOne(id))
            .map(funcionario -> new ResponseEntity<>(new FuncionarioDTO(funcionario), HttpStatus.OK))
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
        Funcionario funcionario = funcionarioRepository.findOne(id);
        funcionario.setAtivo(false);
        funcionarioRepository.save(funcionario);
        User user = userService.getUserWithAuthoritiesByLogin(funcionario.getEmail()).get();
        user.setActivated(false);
        userRepository.save(user);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("funcionario", id.toString())).build();
    }
}
