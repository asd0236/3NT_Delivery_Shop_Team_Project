package com._NT.deliveryShop.service;

import static com._NT.deliveryShop.domain.dto.ReportDto.Create;
import static com._NT.deliveryShop.domain.dto.ReportDto.Put;
import static com._NT.deliveryShop.domain.dto.ReportDto.Result;

import com._NT.deliveryShop.domain.entity.Report;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.ReportRepository;
import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import com._NT.deliveryShop.service.authorizer.AuthenticationInspector;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final RepositoryHelper repoHelper;
    private final AuthenticationInspector inspector;

    @Transactional
    public Result createReport(Create dto) {
        User owner = repoHelper.findUserOrThrow404(dto.getOwnerId());
        Report report = dto.asEntity(it -> it
            .withOwner(owner));

        return Result.of(reportRepository.save(report));
    }

    public Result readReport(UUID id) {
        return Result.of(repoHelper.findReportOrThrow404(id));
    }

    public List<Result> readAllReport(Pageable pageable) {
        return Result.of(reportRepository.findAllByIsDeletedFalse(pageable));
    }

    public List<Result> readAllReportByUserId(Long userId, Pageable pageable) {
        return Result.of(reportRepository.findAllByOwnerUserIdAndIsDeletedFalse(userId, pageable));
    }

    @Transactional
    public Result putReport(UUID id, Put dto) {
        Report report = repoHelper.findReportOrThrow404(id);

        report.setTitle(dto.getTitle());
        report.setContent(dto.getContent());
        report.setUpdateBy(dto.getUpdater());

        return Result.of(reportRepository.save(report));
    }

    @Transactional
    public Result.Deleted deleteReport(UUID id, Authentication authentication) {
        Report report = repoHelper.findReportOrThrow404(id);
        Long deleter = inspector.getUserOrThrow(authentication).getUserId();

        reportRepository.softDeleteReport(id, LocalDateTime.now(), deleter);
        return Result.Deleted.of(report);
    }
}
