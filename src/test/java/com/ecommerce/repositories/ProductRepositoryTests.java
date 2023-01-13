package com.ecommerce.repositories;

import com.ecommerce.models.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {
    @Autowired
    private ProductRepository productRepository;
    @Test // Posso considerar os dados que estão no seed, para teste? Sim pq estou carrengandotodo o contexto do Jpa
    public void deleteShouldDeleteObjectWhenIdExists() {
        long exintingId = 1L;

        productRepository.deleteById(exintingId);

        Optional<Product> result = productRepository.findById(exintingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
        long nonExistingId = 1000L;

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            productRepository.deleteById(nonExistingId);
        });

    }
}
