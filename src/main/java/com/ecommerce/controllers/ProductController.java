package com.ecommerce.controllers;

import com.ecommerce.dtos.ProductDTO;
import com.ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    // Controller tem lógica ou precisa ser "enxuto"?

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable
//            @RequestParam(value = "page", defaultValue = "0") Integer page, ###REFACTORY### PARAMETROS: page, size, sort
//            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
//            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
//            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy
    ) {
//        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

//        Page<ProductDTO> list = productService.findAllPaged(pageRequest);

        return ResponseEntity.ok().body(productService.findAllPaged(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO productDTO) {
        productDTO = productService.insert(productDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(productDTO.getName()).toUri();
        return ResponseEntity.created(uri).body(productDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        productDTO = productService.update(id, productDTO);
        return ResponseEntity.ok().body(productDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
