package com.myproject.autopartsestoresystem.common.service;

import com.myproject.autopartsestoresystem.common.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityNotFoundException;

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
