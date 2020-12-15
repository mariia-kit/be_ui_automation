package com.here.platform.cm.consentStatus.approve;


import com.here.platform.cm.consentStatus.BaseConsentStatusTests;
import com.here.platform.cm.controllers.ConsentStatusController.NewConsent;
import com.here.platform.cm.dataAdapters.ConsentContainerToNsContainer;
import com.here.platform.cm.enums.CMErrorResponse;
import com.here.platform.cm.rest.model.ConsentInfo;
import com.here.platform.cm.rest.model.ConsentInfo.StateEnum;
import com.here.platform.cm.rest.model.ConsentRequestStatus;
import com.here.platform.cm.rest.model.SuccessApproveData;
import com.here.platform.common.ResponseAssertion;
import com.here.platform.common.ResponseExpectMessages.StatusCode;
import com.here.platform.common.annotations.CMFeatures.ApproveConsent;
import com.here.platform.common.annotations.ErrorHandler;
import com.here.platform.common.annotations.Sentry;
import com.here.platform.common.extensions.ConsentRequestRemoveExtension;
import com.here.platform.common.strings.VIN;
import com.here.platform.dataProviders.reference.controllers.ReferenceTokenController;
import com.here.platform.ns.helpers.Steps;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;


@DisplayName("Approve consent")
@ApproveConsent
class ApproveConsentTests extends BaseConsentStatusTests {

    private String crid;

    @RegisterExtension
    ConsentRequestRemoveExtension consentRequestRemoveExtension = new ConsentRequestRemoveExtension();

    @AfterEach
    void cleanUp() {
        Steps.removeRegularContainer(new ConsentContainerToNsContainer(testContainer).nsContainer());
    }

    @Test
    @DisplayName("Verify Approve Consent GetStatus")
    @Tag("cm_prod")
    @Tag("fabric_test")
    void createApproveGetConsentStatusTest() {
        crid = createValidConsentRequest();
        consentRequestRemoveExtension.cridToRemove(crid).vinToRemove(testVin);

        var validCode = ReferenceTokenController
                .produceConsentAuthCode(testVin, testContainer.getId() + ":general");

        final var consentToApprove = NewConsent.builder()
                .vinHash(new VIN(testVin).hashed())
                .consentRequestId(crid)
                .authorizationCode(validCode)
                .build();

        var approveConsentResponse = consentStatusController
                .approveConsent(consentToApprove, dataSubject.getBearerToken());

        var successApproveData = new ResponseAssertion(approveConsentResponse)
                .statusCodeIsEqualTo(StatusCode.OK)
                .bindAs(SuccessApproveData.class);

        var expectedApprovedConsentInfo = new ConsentInfo()
                .consentRequestId(crid)
                .purpose(testConsentRequestData.getPurpose())
                .title(testConsentRequestData.getTitle())
                .consumerName(mpConsumer.getName())
                .consumerId(mpConsumer.getRealm())
                .state(StateEnum.APPROVED)
                .revokeTime(null)
                .containerName(testContainer.getName())
                .containerId(testContainer.getId())
                .containerDescription(testContainer.getContainerDescription())
                .resources(testContainer.getResources())
                .additionalLinks(testConsentRequestData.getAdditionalLinks())
                .privacyPolicy(testConsentRequestData.getPrivacyPolicy())
                .vinLabel(new VIN(testVin).label());

        Assertions.assertThat(successApproveData.getApprovedConsentInfo())
                .isEqualToIgnoringGivenFields(expectedApprovedConsentInfo, ResponseAssertion.timeFieldsToIgnore);
        Assertions.assertThat(successApproveData.getApprovedConsentInfo().getApproveTime())
                .isAfter(successApproveData.getApprovedConsentInfo().getCreateTime());

        final var expectedStatusesForConsent = new ConsentRequestStatus()
                .approved(1)
                .pending(0)
                .revoked(0)
                .expired(0)
                .rejected(0);
        final var actualStatusesForConsent = consentRequestController
                .withConsumerToken()
                .getStatusForConsentRequestById(consentToApprove.getConsentRequestId());

        new ResponseAssertion(actualStatusesForConsent).responseIsEqualToObject(expectedStatusesForConsent);
    }

    @Test
    @DisplayName("Verify it is not possible to approve absent consent")
    void isNotPossibleToApproveConsentThatDoesNotExistTest() {
        var randomConsentRequestId = crypto.sha256();
        final var consentToApprove = NewConsent.builder()
                .vinHash(new VIN(testVin).hashed())
                .consentRequestId(randomConsentRequestId)
                .authorizationCode(crypto.sha1())
                .build();

        var approveResponse = consentStatusController
                .withConsumerToken()
                .approveConsent(consentToApprove, dataSubject.getBearerToken());
        new ResponseAssertion(approveResponse)
                .statusCodeIsEqualTo(StatusCode.NOT_FOUND)
                .expectedErrorResponse(CMErrorResponse.CONSENT_REQUEST_NOT_FOUND);

        consentRequestController.withConsumerToken();
        final var actualStatusesForConsent = consentRequestController
                .getStatusForConsentRequestById(randomConsentRequestId);

        final var expectedStatuses = new ConsentRequestStatus()
                .approved(0)
                .pending(0)
                .revoked(0)
                .expired(0)
                .rejected(0);

        new ResponseAssertion(actualStatusesForConsent)
                .statusCodeIsEqualTo(StatusCode.OK)
                .responseIsEqualToObject(expectedStatuses);
    }

    @Test
    @ErrorHandler
    @DisplayName("Verify approve consent with empty Consent body")
    void approveConsentErrorHandlerTest() {
        var approveResponse = consentStatusController
                .withConsumerToken()
                .approveConsent(NewConsent.builder().build(), dataSubject.getBearerToken());

        new ResponseAssertion(approveResponse)
                .statusCodeIsEqualTo(StatusCode.BAD_REQUEST)
                .expectedErrorResponse(CMErrorResponse.CONSENT_VALIDATION);
    }

    @Test
    @Sentry
    @DisplayName("Verify sentry is blocking consent approval with empty CM application token")
    void sentryBlockApproveConsentRequestTest() {
        crid = createValidConsentRequest();
        consentRequestRemoveExtension.cridToRemove(crid).vinToRemove(testVin);

        final var consentToApprove = NewConsent.builder()
                .vinHash(new VIN(testVin).hashed())
                .consentRequestId(crid)
                .authorizationCode(crypto.sha1())
                .build();

        var approveResponse = consentStatusController
                .withConsumerToken()
                .approveConsent(consentToApprove, "");
        new ResponseAssertion(approveResponse).statusCodeIsEqualTo(StatusCode.UNAUTHORIZED);
    }

}
