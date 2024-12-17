package com.generation.devlivery.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.generation.devlivery.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    
    List<Produto> findAllByNomeProdutoContainingIgnoreCase(String nomeProduto);
}
