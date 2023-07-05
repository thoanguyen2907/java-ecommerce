package com.example.javaecommerce.utils;

import com.example.javaecommerce.model.request.RequestDTO;
import com.example.javaecommerce.model.request.SearchRequestDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;

@Service
public class FilterSpecification<T> {
    public Specification<T> getSearchSpecification(final List<SearchRequestDTO> searchRequestDTOList,
                                                   final RequestDTO.GlobalOperator globalOperator) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (SearchRequestDTO requestDTO : searchRequestDTOList) {
                switch (requestDTO.getOperation()) {
                    case EQUAL:
                        Predicate equal = criteriaBuilder.equal(root.get(requestDTO.getColumn()), requestDTO.getValue());
                        predicates.add(equal);
                        break;
                    case LIKE:
                        Predicate like = criteriaBuilder.like(root.get(requestDTO.getColumn()), "%" + requestDTO.getValue() + "%");
                        predicates.add(like);
                        break;

                    case LESS_THAN:
                        Predicate lessThan = criteriaBuilder.lessThan(root.get(requestDTO.getColumn()), requestDTO.getValue());
                        predicates.add(lessThan);
                        break;

                    case GREATER_THAN:
                        Predicate greaterThan = criteriaBuilder.greaterThan(root.get(requestDTO.getColumn()), requestDTO.getValue());
                        predicates.add(greaterThan);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + "");
                }
            }
            if (globalOperator.equals(RequestDTO.GlobalOperator.AND)) {
                // Converting predicates list to an array
                return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
            } else {
                return criteriaBuilder.or(predicates.toArray(Predicate[]::new));
            }
        };
    }
}

