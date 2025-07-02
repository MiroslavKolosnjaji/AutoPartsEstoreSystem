package com.myproject.autopartsestoresystem.models.repository;

import com.myproject.autopartsestoresystem.models.entity.Model;
import com.myproject.autopartsestoresystem.models.entity.ModelId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface ModelRepository extends JpaRepository<Model, ModelId> {

//    void deleteById(ModelId id);

}
