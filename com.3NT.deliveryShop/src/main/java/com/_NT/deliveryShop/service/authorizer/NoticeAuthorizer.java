package com._NT.deliveryShop.service.authorizer;

import com._NT.deliveryShop.domain.entity.Notice;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import com._NT.deliveryShop.repository.helper.ServiceErrorHelper;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class NoticeAuthorizer extends AbstractAuthorizer {

    public NoticeAuthorizer(
        AuthenticationInspector authInspector,
        ServiceErrorHelper errorHelper,
        RepositoryHelper repositoryHelper) {
        super(authInspector, errorHelper, repositoryHelper);
    }

    public void requireNoticeOwner(Authentication authentication, Notice requestedNotice) {
        if (authInspector.isAdmin(authentication))
            return;
        User user = authInspector.getUserOrThrow(authentication);

        if (user != requestedNotice.getOwner()) {
            throw errorHelper.forbidden("Not the owner of the notice");
        }
    }

    public void requireNoticeOwner(Authentication authentication, UUID noticeId) {
        Notice notice = repositoryHelper.findNoticeOrThrow404(noticeId);
        requireNoticeOwner(authentication, notice);
    }

}
