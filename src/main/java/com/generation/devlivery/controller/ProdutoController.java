package com.generation.devlivery.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.generation.devlivery.model.Produto;
import com.generation.devlivery.repository.ProdutoRepository;
import com.generation.devlivery.service.ProdutoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produto")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<Produto>> getAll() {
        return ResponseEntity.ok(produtoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getById(@PathVariable Long id) { 
        return produtoRepository.findById(id)
                .map(resp -> ResponseEntity.ok(resp))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/nome/{nomeProduto}")
    public ResponseEntity<List<Produto>> getByNomeProduto(@PathVariable String nomeProduto) {
        return ResponseEntity.ok(produtoRepository.findAllByNomeProdutoContainingIgnoreCase(nomeProduto));
    }

    @PostMapping
    public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(produtoRepository.save(produto));
    }

    @PutMapping
    public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
        if (produtoRepository.existsById(produto.getIdProduto())) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(produtoRepository.save(produto));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { 
        Optional<Produto> produto = produtoRepository.findById(id); 
        
        if (produto.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
        
        produtoRepository.deleteById(id);
    }
    
    @GetMapping("/produtos-saudaveis")
	public ResponseEntity<List<Produto>> getProdutosSaudaveis() {
	    List<Produto> todosProdutos = produtoRepository.findAll(); 
	    List<Produto> produtosSaudaveis = produtoService.recomendarProdutosSaudaveis(todosProdutos);

	    return ResponseEntity.ok(produtosSaudaveis);
	}
}