package com.macias34.codemastery.user.model;

import com.macias34.codemastery.user.entity.UserEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<UserEntity> withFilters(UserFilter userFilter) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (userFilter.getRole() != null ) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get("role"), userFilter.getRole()));
            }

            if (userFilter.getUsername() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("username")),
                                "%" + userFilter.getUsername().toLowerCase() + "%"));
            }

            if (userFilter.getEmail() != null) {
                predicate = criteriaBuilder.and(predicate,
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
                                "%" + userFilter.getEmail().toLowerCase() + "%"));
            }

            return predicate;
        };
    }
}
