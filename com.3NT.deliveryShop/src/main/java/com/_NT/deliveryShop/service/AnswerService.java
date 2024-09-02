package com._NT.deliveryShop.service;

import static com._NT.deliveryShop.domain.dto.AnswerDto.Create;
import static com._NT.deliveryShop.domain.dto.AnswerDto.Put;
import static com._NT.deliveryShop.domain.dto.AnswerDto.Result;

import com._NT.deliveryShop.domain.entity.Answer;
import com._NT.deliveryShop.domain.entity.Report;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.AnswerRepository;
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
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final RepositoryHelper repoHelper;
    private final AuthenticationInspector inspector;

    @Transactional
    public Result createAnswer(Create dto) {
        User owner = repoHelper.findUserOrThrow404(dto.getOwnerId());
        Report report = repoHelper.findReportOrThrow404(dto.getReportId());
        Answer answer = dto.asEntity(it -> it
            .withOwner(owner)
            .withReport(report));

        return Result.of(answerRepository.save(answer));
    }

    public Result readAnswer(UUID id) {

        return Result.of(repoHelper.findAnswerOrThrow404(id));
    }

    public Result readAnswerByReportId(UUID reportId) {

        return Result.of(answerRepository.findByReportIdAndIsDeletedFalse(reportId));
    }

    public List<Result> readAllAnswer(Pageable pageable) {
        return Result.of(answerRepository.findAllByIsDeletedFalse(pageable));
    }

    public List<Result> readAllAnswerByUserId(Long userId, Pageable pageable) {
        return Result.of(answerRepository.findAllByOwnerUserIdAndIsDeletedFalse(userId, pageable));
    }

    @Transactional
    public Result putAnswer(UUID id, Put dto) {
        Answer answer = repoHelper.findAnswerOrThrow404(id);

        answer.setTitle(dto.getTitle());
        answer.setContent(dto.getContent());
        answer.setUpdateBy(dto.getUpdater());

        return Result.of(answerRepository.save(answer));
    }

    @Transactional
    public Result.Deleted deleteAnswer(UUID id, Authentication authentication) {
        Answer answer = repoHelper.findAnswerOrThrow404(id);
        Long deleter = inspector.getUserOrThrow(authentication).getUserId();

        answerRepository.softDeleteAnswer(id, LocalDateTime.now(), deleter);
        return Result.Deleted.of(answer);
    }
}
