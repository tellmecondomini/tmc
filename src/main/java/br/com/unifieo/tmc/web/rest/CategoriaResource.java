package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.domain.Categoria;
import br.com.unifieo.tmc.repository.CategoriaRepository;
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

/**
 * REST controller for managing Categoria.
 */
@RestController
@RequestMapping("/api")
public class CategoriaResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaResource.class);

    @Inject
    private CategoriaRepository categoriaRepository;

    /**
     * POST  /categorias -> Create a new categoria.
     */
    @RequestMapping(value = "/categorias",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categoria> createCategoria(@Valid @RequestBody Categoria categoria) throws URISyntaxException {
        log.debug("REST request to save Categoria : {}", categoria);
        if (categoria.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new categoria cannot already have an ID").body(null);
        }
        Categoria result = categoriaRepository.save(categoria);
        return ResponseEntity.created(new URI("/api/categorias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("categoria", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categorias -> Updates an existing categoria.
     */
    @RequestMapping(value = "/categorias",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categoria> updateCategoria(@Valid @RequestBody Categoria categoria) throws URISyntaxException {
        log.debug("REST request to update Categoria : {}", categoria);
        if (categoria.getId() == null) {
            return createCategoria(categoria);
        }
        Categoria result = categoriaRepository.save(categoria);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("categoria", categoria.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categorias -> get all the categorias.
     */
    @RequestMapping(value = "/categorias",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Categoria> getAllCategorias() {
        log.debug("REST request to get all Categorias");
        return categoriaRepository.findAll();
    }

    /**
     * GET  /categorias/:id -> get the "id" categoria.
     */
    @RequestMapping(value = "/categorias/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Categoria> getCategoria(@PathVariable Long id) {
        log.debug("REST request to get Categoria : {}", id);
        return Optional.ofNullable(categoriaRepository.findOne(id))
            .map(categoria -> new ResponseEntity<>(
                categoria,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /categorias/:id -> delete the "id" categoria.
     */
    @RequestMapping(value = "/categorias/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        log.debug("REST request to delete Categoria : {}", id);
        categoriaRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("categoria", id.toString())).build();
    }
}
