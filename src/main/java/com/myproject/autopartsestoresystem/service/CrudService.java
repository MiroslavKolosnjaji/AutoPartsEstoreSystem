package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.customer.CustomerDTO;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
public interface CrudService<T, ID> {

    T save(T entity);
    T update(ID id, T entity);
    List<T> getAll();
    T getById(ID id);
    void delete(ID id);
}
