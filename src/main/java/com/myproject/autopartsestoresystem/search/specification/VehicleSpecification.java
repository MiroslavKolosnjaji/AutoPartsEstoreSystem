package com.myproject.autopartsestoresystem.search.specification;

import com.myproject.autopartsestoresystem.models.entity.Model;
import com.myproject.autopartsestoresystem.model.Vehicle;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class VehicleSpecification implements Specification<Vehicle> {


    private String brand;
    private String model;
    private String series;


    @Override
    public Predicate toPredicate(Root<Vehicle> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Join<Vehicle, Model> join = root.join("model");
        List<Predicate> predicates = new ArrayList<>();

        if (brand != null) {
            predicates.add(criteriaBuilder.equal(join.get("brand").get("id"), brand));
        }

        if (model != null) {
            predicates.add(criteriaBuilder.equal(join.get("name"), model));
        }

        if (series != null) {
            predicates.add(criteriaBuilder.equal(join.get("series"), series));
        }

        if (predicates.isEmpty()) {
            return criteriaBuilder.conjunction();
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
