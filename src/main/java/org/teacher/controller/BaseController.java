package org.teacher.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teacher.utils.PaginationUtils;

import java.util.List;

public abstract class BaseController<T, ID> {

    protected final JpaRepository<T, ID> repository;
    protected final String resourceName;

    protected BaseController(JpaRepository<T, ID> repository, String resourceName) {
        this.repository = repository;
        this.resourceName = resourceName;
    }

    @GetMapping
    public ResponseEntity<List<T>> getAll(
            @RequestParam(value = "_page", defaultValue = "1") int page,
            @RequestParam(value = "_perPage", defaultValue = "10") int perPage
    ) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        Page<T> resultPage = repository.findAll(pageable);

        HttpHeaders headers = PaginationUtils.createContentRangeHeader(resultPage, resourceName, page, perPage);
        return ResponseEntity.ok().headers(headers).body(resultPage.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> getOne(@PathVariable ID id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public T create(@RequestBody T entity) {
        return repository.save(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody T entity) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(repository.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
