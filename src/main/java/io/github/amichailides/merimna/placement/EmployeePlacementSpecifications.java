package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.domain.EmployeePlacement;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class EmployeePlacementSpecifications {

    public static Specification<EmployeePlacement> hasEmployeePublicId(UUID employeePublicId) {
        return (root, query, cb) ->
                employeePublicId == null
                        ? null
                        : cb.equal(root.join("employee").get("publicId"), employeePublicId);
    }

    public static Specification<EmployeePlacement> hasHouseUnitPublicId(UUID houseUnitPublicId) {
        return (root, query, cb) ->
                houseUnitPublicId == null
                ? null
                : cb.equal(root.join("houseUnit").get("publidId"), houseUnitPublicId);
    }

    public static Specification<EmployeePlacement> isActive(LocalDate now) {
        return (root, query, cb) ->
                cb.and(
                        cb.lessThanOrEqualTo(root.get("startDate"), now),
                        cb.or(
                                cb.isNull(root.get("endDate")),
                                cb.greaterThanOrEqualTo(root.get("endDate"), now)
                        )
                );
    }

    public static Specification<EmployeePlacement> startDateFrom(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null :
                        cb.greaterThanOrEqualTo(root.get("startDate"), date);
    }

    public static Specification<EmployeePlacement> startDateTo(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null :
                        cb.lessThanOrEqualTo(root.get("startDate"), date);
    }

    public static Specification<EmployeePlacement> endDateFrom(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null :
                        cb.greaterThanOrEqualTo(root.get("endDate"), date);
    }

    public static Specification<EmployeePlacement> endDateTo(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null :
                        cb.lessThanOrEqualTo(root.get("endDate"), date);
    }
}
