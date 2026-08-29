package io.github.amichailides.merimna.employee.systemaccess;

import io.github.amichailides.merimna.employee.systemaccess.dto.EmployeeAccessDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/employees/{employeePublicId}/access")
@RequiredArgsConstructor
public class EmployeeAccessController {

    private final EmployeeAccessService employeeAccessService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<EmployeeAccessDTO> getAccessStatus(
            @PathVariable UUID employeePublicId
    ) {
        EmployeeAccessDTO result =
                employeeAccessService.getAccessStatus(employeePublicId);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/invitation/resend")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<Void> resendInvitation(
            @PathVariable UUID employeePublicId
    ) {
        employeeAccessService.resendInvitation(employeePublicId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/invitation")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<Void> cancelInvitation(
            @PathVariable UUID employeePublicId
    ) {
        employeeAccessService.cancelInvitation(employeePublicId);

        return ResponseEntity.noContent().build();
    }
}