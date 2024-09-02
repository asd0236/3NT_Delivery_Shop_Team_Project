package com._NT.deliveryShop.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "p_report")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends Post {

    @Builder
    public Report(String title, String content) {
        super(title, content);
    }

    protected Report(UUID id, User owner, String title, String content) {
        super(id, owner, title, content);
    }

    @Override
    public Report withOwner(User owner) {
        if (owner == null)
            return new Report(this.id, null, this.title, this.content);
        else
            return this.owner == owner ? this
                : new Report(this.id, owner, this.title, this.content);
    }

    @Override
    public Report withId(UUID id) {
        if (id == null)
            return new Report(null, this.owner, this.title, this.content);
        else
            return id.equals(this.id) ? this : new Report(id, this.owner, this.title, this.content);
    }

    @Override
    public Report withTitle(String title) {
        if (title == null)
            return new Report(this.id, this.owner, null, this.content);
        else
            return title.equals(this.title) ? this
                : new Report(this.id, this.owner, title, this.content);
    }
}
