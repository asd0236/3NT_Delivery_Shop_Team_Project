package com._NT.deliveryShop.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;


@Entity
@Table(name = "p_answer")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends Post {

    @With
    @OneToOne
    @JoinColumn(name = "id")
    private Report report;

    @Builder
    public Answer(String title, String content, Report report) {
        super(title, content);
        this.report = report;
    }

    protected Answer(UUID id, User owner, String title, String content, Report report) {
        super(id, owner, title, content);
        this.report = report;
    }

    @Override
    public Answer withOwner(User owner) {
        if (owner == null)
            return new Answer(this.id, null, this.title, this.content, this.report);
        else
            return this.owner == owner ? this
                : new Answer(this.id, owner, this.title, this.content, this.report);
    }

    @Override
    public Answer withId(UUID id) {
        if (id == null)
            return new Answer(null, this.owner, this.title, this.content, this.report);
        else
            return id.equals(this.id) ? this
                : new Answer(id, this.owner, this.title, this.content, this.report);
    }

    @Override
    public Answer withTitle(String title) {
        if (title == null)
            return new Answer(this.id, this.owner, null, this.content, this.report);
        else
            return title.equals(this.title) ? this
                : new Answer(this.id, this.owner, title, this.content, this.report);
    }
}
