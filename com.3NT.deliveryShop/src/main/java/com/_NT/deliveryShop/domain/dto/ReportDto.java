package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Report;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.data.util.Streamable;

public interface ReportDto {

    @With
    @Data
    @Builder
    class Create {

        @NotBlank(message = "유저 아이디를 입력해주세요.")
        private Long ownerId;

        @NotBlank(message = "제목을 입력해주세요.")
        @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하로 입력해주세요.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        private String content;

        public Report asEntity(
            Function<? super Report, ? extends Report> initialize) {
            return initialize.apply(Report.builder()
                .title(title)
                .content(content)
                .build());
        }
    }

    @With
    @Data
    @Builder
    class Put {

        @NotBlank(message = "유저 아이디를 입력해주세요.")
        private Long updater;

        @NotBlank(message = "제목을 입력해주세요.")
        @Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하로 입력해주세요.")
        private String title;

        @NotBlank(message = "내용을 입력해주세요.")
        private String content;
    }

    @With
    @Data
    @Builder
    class Result {

        private UUID id;
        private Long ownerId;
        private String title;
        private String content;

        public static Result of(Report report) {
            return Result.builder()
                .id(report.getId())
                .ownerId(report.getOwner().getUserId())
                .title(report.getTitle())
                .content(report.getContent())
                .build();
        }

        public static List<Result> of(Streamable<Report> reports) {
            return reports.stream()
                .map(Result::of)
                .collect(Collectors.toList());
        }

        @Data
        @Builder
        public static class Deleted {

            private UUID id;

            public static Deleted of(Report report) {
                return Deleted.builder()
                    .id(report.getId())
                    .build();
            }
        }
    }
}
