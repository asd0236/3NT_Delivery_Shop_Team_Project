package com._NT.deliveryShop.service.authorizer;

import com._NT.deliveryShop.domain.entity.Answer;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import com._NT.deliveryShop.repository.helper.ServiceErrorHelper;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AnswerAuthorizer extends ReportAuthorizer {

    public AnswerAuthorizer(
        AuthenticationInspector authInspector,
        ServiceErrorHelper errorHelper,
        RepositoryHelper repositoryHelper) {
        super(authInspector, errorHelper, repositoryHelper);
    }

    public void requireAnswerOwner(Authentication authentication, Answer requestedAnswer) {
        if (authInspector.isAdmin(authentication))
            return;
        User user = authInspector.getUserOrThrow(authentication);

        if (user != requestedAnswer.getOwner()) {
            throw errorHelper.forbidden("Not the owner of the answer");
        }
    }

    public void requireAnswerOwner(Authentication authentication, UUID answerId) {
        Answer answer = repositoryHelper.findAnswerOrThrow404(answerId);
        requireAnswerOwner(authentication, answer);
    }

}
