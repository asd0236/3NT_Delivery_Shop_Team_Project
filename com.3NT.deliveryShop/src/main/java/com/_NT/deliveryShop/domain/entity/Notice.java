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
@Table(name = "p_notice")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends Post {

    @Builder
    public Notice(String title, String content) {
        super(title, content);
    }

    protected Notice(UUID id, User owner, String title, String content) {
        super(id, owner, title, content);
    }

    @Override
    public Notice withOwner(User owner) {
        if (owner == null)
            return new Notice(this.id, null, this.title, this.content);
        else
            return this.owner == owner ? this
                : new Notice(this.id, owner, this.title, this.content);
    }

    @Override
    public Notice withId(UUID id) {
        if (id == null)
            return new Notice(null, this.owner, this.title, this.content);
        else
            return id.equals(this.id) ? this : new Notice(id, this.owner, this.title, this.content);
    }

    @Override
    public Notice withTitle(String title) {
        if (title == null)
            return new Notice(this.id, this.owner, null, this.content);
        else
            return title.equals(this.title) ? this
                : new Notice(this.id, this.owner, title, this.content);
    }

}
