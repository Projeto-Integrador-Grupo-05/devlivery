package com.generation.devlivery.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.generation.devlivery.model.Produto;


@Service
public class ProdutoService {

	public List<Produto> recomendarProdutosSaudaveis(List<Produto> produtos) {
        return produtos.stream()
                .filter(produto -> isProdutoSaudavel(produto))
                .collect(Collectors.toList());
    }
	
	private boolean isProdutoSaudavel(Produto produto) {
        return Boolean.TRUE.equals(produto.getSaudavel()) || 
               (produto.getSaudavel() != null && produto.getSaudavel() == true);
    }
}