package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Comentario;
import br.com.unifieo.tmc.domain.SolicitaRemocaoComentario;
import br.com.unifieo.tmc.repository.ComentarioRepository;
import br.com.unifieo.tmc.repository.SolicitaRemocaoComentarioRepository;
import br.com.unifieo.tmc.service.ComentarioService;
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
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Comentario.
 */
@RestController
@RequestMapping("/api")
public class ComentarioResource {

    private final Logger log = LoggerFactory.getLogger(ComentarioResource.class);

    @Inject
    private ComentarioRepository comentarioRepository;

    @Inject
    private ComentarioService comentarioService;

    @Inject
    private SolicitaRemocaoComentarioRepository solicitaRemocaoComentarioRepository;

    /**
     * POST  /comentarios -> Create a new comentario.
     */
    @RequestMapping(value = "/comentarios",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Comentario> createComentario(@RequestBody Comentario comentario) throws URISyntaxException {
        log.debug("REST request to save Comentario : {}", comentario);
        if (comentario.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new comentario cannot already have an ID").body(null);
        }
        Comentario result = comentarioService.save(comentario);
        return ResponseEntity.ok().body(result);
    }

    /**
     * PUT  /comentarios -> Updates an existing comentario.
     */
    @RequestMapping(value = "/comentarios",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Comentario> updateComentario(@Valid @RequestBody Comentario comentario) throws URISyntaxException {
        log.debug("REST request to update Comentario : {}", comentario);
        if (comentario.getId() == null) {
            return createComentario(comentario);
        }
        Comentario result = comentarioRepository.save(comentario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("comentario", comentario.getId().toString()))
            .body(result);
    }

    /**
     * GET  /comentarios -> get all the comentarios.
     */
    @RequestMapping(value = "/comentarios",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Comentario> getAllComentarios() {
        log.debug("REST request to get all Comentarios");
        return comentarioRepository.findAll();
    }

    /**
     * GET  /comentarios/:id -> get the "id" comentario.
     */
    @RequestMapping(value = "/comentarios/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Comentario> getComentario(@PathVariable Long id) {
        log.debug("REST request to get Comentario : {}", id);
        return Optional.ofNullable(comentarioRepository.findOne(id))
            .map(comentario -> new ResponseEntity<>(
                comentario,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /comentarios/:id -> delete the "id" comentario.
     */
    @RequestMapping(value = "/comentarios/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteComentario(@PathVariable Long id) {
        log.debug("REST request to delete Comentario : {}", id);
        Comentario comentario = comentarioRepository.findOne(id);
        comentario.setAtivo(false);
        comentarioRepository.save(comentario);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("comentario", id.toString())).build();
    }

    @RequestMapping(value = "/comentarios/remove/{id}/{moradorId}/{motivo}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteComentarioByMorador(@PathVariable Long id, @PathVariable Long moradorId, @PathVariable String motivo) {
        log.debug("REST request to delete Comentario By Morador : {}", id);
        Comentario comentario = comentarioRepository.findOne(id);
        return Optional.ofNullable(solicitaRemocaoComentarioRepository.findOneByComentarioAndAprovado(comentario, null))
            .map(s -> ResponseEntity.badRequest().headers(HeaderUtil.createSolicitacaoJaExisteAlert()).build())
            .orElseGet(() -> {
                comentarioService.getSolicitacaoRemocaoByMorador(comentario, moradorId, motivo);
                return ResponseEntity.ok().build();
            });
    }

    @RequestMapping(value = "/comentarios/remove/{idComentario}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SolicitaRemocaoComentario> deleteComentarioByMorador(@PathVariable Long idComentario) {
        Comentario comentario = comentarioRepository.findOne(idComentario);
        return Optional.ofNullable(solicitaRemocaoComentarioRepository.findOneByComentario(comentario))
            .map(solicitacao -> new ResponseEntity<>(solicitacao, HttpStatus.OK)).get();
    }

}
