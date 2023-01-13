package com.ecommerce.services;

import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.dtos.ProductDTO;
import com.ecommerce.models.Product;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.repositories.ProductRepository;
import com.ecommerce.services.exceptions.DataBaseException;
import com.ecommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

//    @Transactional(readOnly = true) // Verificar com Rodrigo
//    public List<ProductDTO> findAll() {
//        List<Product> products = productRepository.findAll(Sort.by("name"));
//        return products.stream().map(Product -> new ProductDTO(Product)).collect(Collectors.toList()); // Verifcar com Rodrigo
//    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(product -> new ProductDTO(product));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> product = productRepository.findById(id); // Verificar com Rodrigo Sobre Optional Devolver um Null
        Product entity = product.orElseThrow(() -> new ResourceNotFoundException("Entity Not Found " + id));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product entity = new Product();
        copyDtoToEntity(productDTO, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        try {
            Product entity = productRepository.getReferenceById(id);
            copyDtoToEntity(productDTO, entity);
            entity = productRepository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id Not Found " + id);
        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id Not Found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity Violation");
        }
    }
    private void copyDtoToEntity(ProductDTO productDTO, Product entity) {
        entity.setName(productDTO.getName());
        entity.setDescription(productDTO.getDescription());
        entity.setDate(productDTO.getDate());
        entity.setImgUrl(productDTO.getImgUrl());
        entity.setPrice(productDTO.getPrice());

        entity.getCategories().clear();
        for (CategoryDTO categoryDTO : productDTO.getCategories()) {
            categoryRepository.getReferenceById(categoryDTO.getId());
        }
    }
}
