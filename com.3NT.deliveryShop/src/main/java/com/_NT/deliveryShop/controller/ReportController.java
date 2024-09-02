package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.domain.dto.ReportDto.Create;
import static com._NT.deliveryShop.domain.dto.ReportDto.Put;
import static com._NT.deliveryShop.domain.dto.ReportDto.Result;

import com._NT.deliveryShop.domain.entity.UserRoleEnum;
import com._NT.deliveryShop.security.UserDetailsImpl;
import com._NT.deliveryShop.service.ReportService;
import com._NT.deliveryShop.service.authorizer.ReportAuthorizer;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService service;
    private final ReportAuthorizer reportAuthorizer;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Result postReport(@RequestBody Create dto, Authentication authentication) {

        reportAuthorizer.requireByOneself(authentication, dto.getOwnerId());
        return service.createReport(dto);
    }

    @GetMapping("/{id}")
    public Result getReport(@PathVariable UUID id, Authentication authentication) {

        reportAuthorizer.requireReportOwner(authentication, id);
        return service.readReport(id);
    }

    @GetMapping
    public List<Result> getAllReport(Pageable pageable,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UserRoleEnum role = userDetails.getUser().getRole();
        if (role.equals(UserRoleEnum.ADMIN)) {
            return service.readAllReport(pageable);
        } else {
            return service.readAllReportByUserId(userDetails.getUser().getUserId(), pageable);
        }
    }

    @PutMapping("/{id}")
    public Result putReport(@PathVariable UUID id,
        @RequestBody Put dto, Authentication authentication) {

        reportAuthorizer.requireReportOwner(authentication, id);
        return service.putReport(id, dto);
    }

    @DeleteMapping("/{id}")
    public Result.Deleted deleteReport(@PathVariable UUID id, Authentication authentication) {

        reportAuthorizer.requireReportOwner(authentication, id);
        return service.deleteReport(id, authentication);
    }
}