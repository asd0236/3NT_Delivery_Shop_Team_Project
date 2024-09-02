package com._NT.deliveryShop.controller;

import static com._NT.deliveryShop.domain.dto.AnswerDto.Create;
import static com._NT.deliveryShop.domain.dto.AnswerDto.Put;
import static com._NT.deliveryShop.domain.dto.AnswerDto.Result;
import static com._NT.deliveryShop.domain.entity.UserRoleEnum.PreAuthorizeRole.ADMIN;

import com._NT.deliveryShop.service.AnswerService;
import com._NT.deliveryShop.service.authorizer.AnswerAuthorizer;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/api/v1")
public class AnswerController {

    private final AnswerService service;
    private final AnswerAuthorizer answerAuthorizer;

    @PreAuthorize("hasRole(" + ADMIN + ")")
    @PostMapping("/answers")
    @ResponseStatus(HttpStatus.CREATED)
    public Result postAnswer(@RequestBody Create dto, Authentication authentication) {

        answerAuthorizer.requireByOneself(authentication, dto.getOwnerId());
        return service.createAnswer(dto);
    }

    @GetMapping("/reports/{reportId}/answers")
    public Result getAnswer(@PathVariable UUID reportId, Authentication authentication) {

        answerAuthorizer.requireReportOwner(authentication, reportId);
        return service.readAnswerByReportId(reportId);
    }

    @PreAuthorize("hasRole(" + ADMIN + ")")
    @GetMapping("/answers")
    public List<Result> getAllAnswer(Pageable pageable) {

        return service.readAllAnswer(pageable);
    }

    @PreAuthorize("hasRole(" + ADMIN + ")")
    @PutMapping("/answers/{id}")
    public Result putAnswer(@PathVariable UUID id,
        @RequestBody Put dto, Authentication authentication) {

        answerAuthorizer.requireByOneself(authentication, dto.getUpdater());
        return service.putAnswer(id, dto);
    }

    @PreAuthorize("hasRole(" + ADMIN + ")")
    @DeleteMapping("/answers/{id}")
    public Result.Deleted deleteAnswer(@PathVariable UUID id, Authentication authentication) {

        return service.deleteAnswer(id, authentication);
    }
}