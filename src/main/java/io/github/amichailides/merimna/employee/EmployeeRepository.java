package io.github.amichailides.merimna.employee;

import io.github.amichailides.merimna.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
