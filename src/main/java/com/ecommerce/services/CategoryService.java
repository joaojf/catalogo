package com.ecommerce.services;

import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.models.Category;
import com.ecommerce.repositories.CategoryRepository;
import com.ecommerce.services.exceptions.DataBaseException;
import com.ecommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

//    @Transactional(readOnly = true) // Verificar com Rodrigo
//    public List<CategoryDTO> findAll() {
//        List<Category> categories = categoryRepository.findAll(Sort.by("name"));
//        return categories.stream().map(category -> new CategoryDTO(category)).collect(Collectors.toList()); // Verifcar com Rodrigo
//    }

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        return categories.map(x -> new CategoryDTO(x));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id); // Verificar com Rodrigo Sobre Optional Devolver um Null
        Category entity = category.orElseThrow(() -> new ResourceNotFoundException("Entity Not Found " + id));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category entity = new Category();
        entity.setName(categoryDTO.getName());
        entity = categoryRepository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        try {
            Category entity = categoryRepository.getReferenceById(id);
            entity.setName(categoryDTO.getName());
            entity = categoryRepository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id Not Found " + id);
        }
    }

    public void delete(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id Not Found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity Violation");
        }
    }
}
