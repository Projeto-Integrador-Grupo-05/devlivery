package com.generation.devlivery.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.generation.devlivery.model.Produto;
import com.generation.devlivery.repository.CategoriaRepository;
import com.generation.devlivery.repository.ProdutoRepository;
import com.generation.devlivery.security.JwtService;

@Service
public class ProdutoService {

	public List<Produto> recomendarProdutosSaudaveis(List<Produto> produtos) {
        return produtos.stream()
                .filter(produto -> isProdutoSaudavel(produto))
                .collect(Collectors.toList());
    }
	
	private boolean isProdutoSaudavel(Produto produto) {
        return Boolean.TRUE.equals(produto.getSaudavel()) || 
               (produto.getCalorias() != null && produto.getCalorias() < 200) ||
               (produto.getPreco() != null && produto.getPreco().compareTo(new java.math.BigDecimal("10.00")) < 0);
    }
}