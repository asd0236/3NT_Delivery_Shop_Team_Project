package com._NT.deliveryShop.service.authorizer;

import com._NT.deliveryShop.domain.entity.Report;
import com._NT.deliveryShop.domain.entity.User;
import com._NT.deliveryShop.repository.helper.RepositoryHelper;
import com._NT.deliveryShop.repository.helper.ServiceErrorHelper;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class ReportAuthorizer extends AbstractAuthorizer {

    public ReportAuthorizer(
        AuthenticationInspector authInspector,
        ServiceErrorHelper errorHelper,
        RepositoryHelper repositoryHelper) {
        super(authInspector, errorHelper, repositoryHelper);
    }

    public void requireReportOwner(Authentication authentication, Report requestedReport) {
        if (authInspector.isAdmin(authentication))
            return;
        User user = authInspector.getUserOrThrow(authentication);

        if (user != requestedReport.getOwner()) {
            throw errorHelper.forbidden("Not the owner of the report");
        }
    }

    public void requireReportOwner(Authentication authentication, UUID reportId) {
        Report report = repositoryHelper.findReportOrThrow404(reportId);
        requireReportOwner(authentication, report);
    }

}
