package com._NT.deliveryShop.domain.dto;

import com._NT.deliveryShop.domain.entity.Report;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.springframework.data.util.Streamable;

public interface ReportDto {

    @With
    @Data
    @Builder
    class Create {

        private Long ownerId;
        private String title;
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

        private Long updater;
        private String title;
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
