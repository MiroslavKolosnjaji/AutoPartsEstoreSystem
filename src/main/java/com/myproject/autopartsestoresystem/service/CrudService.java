package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
public interface CrudService<T, ID> {

    T save(T entity) throws EntityAlreadyExistsException;
    T update(ID id, T entity) throws EntityNotFoundException;
    List<T> getAll();
    T getById(ID id) throws EntityNotFoundException;
    void delete(ID id) throws EntityNotFoundException;
}
