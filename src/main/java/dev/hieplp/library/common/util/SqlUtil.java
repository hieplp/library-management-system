package dev.hieplp.library.common.util;

import dev.hieplp.library.common.payload.request.GetListRequest;
import dev.hieplp.library.common.payload.response.GetListResponse;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.util.ArrayList;

@Slf4j
@Component
public class SqlUtil {
    public <T> Specification<T> buildCondition(GetListRequest request) {
        return new Specification<>() {
            @Serial
            private static final long serialVersionUID = -20322017466946410L;

            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                var predicates = new ArrayList<Predicate>();

                if (request.getSearchBy() != null && !request.getSearchBy().isEmpty()) {
                    var condition = "%" + request.getSearchValue() + "%";
                    var predicate = criteriaBuilder.like(root.get(request.getSearchBy()), condition);
                    predicates.add(predicate);
                }

                if (request.getFilterBy() != null && !request.getFilterBy().isEmpty()) {
                    var predicate = criteriaBuilder.equal(root.get(request.getFilterBy()), request.getFilterValue());
                    predicates.add(predicate);
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }

    public Pageable buildPageable(GetListRequest request) {
        if (request.getPage() < 1) {
            request.setPage(1);
        }

        if (request.getSize() < 1) {
            request.setSize(10);
        }

        return PageRequest.of(request.getPage() - 1, request.getSize());
    }

    public Pageable buildOrder(GetListRequest request, Pageable pageable) {
        if (request.getOrder() == null || request.getOrder().isEmpty()) {
            return pageable;
        }

        var sort = Sort.by(request.getOrder()).ascending();
        if ("desc".equalsIgnoreCase(request.getOrderBy())) {
            sort = sort.descending();
        }

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    public Pageable buildPaginationAndOrder(GetListRequest request) {
        var pageable = buildPageable(request);
        return buildOrder(request, pageable);
    }

    public <T, Y> Page<Y> getPages(GetListRequest request, JpaSpecificationExecutor<T> repo, Class<Y> clazz) {
        return repo.findAll(buildCondition(request), buildPaginationAndOrder(request)).map(object -> {
            try {
                log.warn("Object: {}", object);
                var response = clazz.getConstructor().newInstance();
                BeanUtils.copyProperties(object, response);
                return response;
            } catch (Exception e) {
                log.error("Error when copy properties: {}", e.getMessage());
                return null;
            }
        });
    }

    public <T, Y> GetListResponse<Y> getList(GetListRequest request, JpaSpecificationExecutor<T> repo, Class<Y> clazz) {
        var pages = getPages(request, repo, clazz);
        return GetListResponse.<Y>builder()
                .list(pages.getContent())
                .total(pages.getTotalElements())
                .build();
    }
}
