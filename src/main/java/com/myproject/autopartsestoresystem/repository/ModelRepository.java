package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Model;
import com.myproject.autopartsestoresystem.model.ModelId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface ModelRepository extends JpaRepository<Model, ModelId> {

//    void deleteById(ModelId id);

}
