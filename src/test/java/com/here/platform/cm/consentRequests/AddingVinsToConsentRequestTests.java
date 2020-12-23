package com.here.platform.cm.consentRequests;

import static com.here.platform.cm.enums.CMErrorResponse.CONSENT_REQUEST_UPDATE;

import com.here.platform.cm.BaseCMTest;
import com.here.platform.cm.enums.ConsentRequestContainer;
import com.here.platform.cm.enums.ConsentRequestContainers;
import com.here.platform.cm.enums.Consents;
import com.here.platform.cm.enums.MPProviders;
import com.here.platform.cm.rest.model.ConsentInfo;
import com.here.platform.cm.rest.model.ConsentRequest;
import com.here.platform.cm.rest.model.ConsentRequestData;
import com.here.platform.cm.rest.model.ConsentRequestIdResponse;
import com.here.platform.cm.rest.model.ConsentRequestStatus;
import com.here.platform.cm.steps.api.ConsentRequestSteps2;
import com.here.platform.cm.steps.api.OnboardingSteps;
import com.here.platform.cm.steps.api.RemoveEntitiesSteps;
import com.here.platform.common.ResponseAssertion;
import com.here.platform.common.ResponseExpectMessages.StatusCode;
import com.here.platform.common.VinsToFile;
import com.here.platform.common.VinsToFile.FILE_TYPE;
import com.here.platform.common.annotations.CMFeatures.UpdateConsentRequest;
import com.here.platform.common.config.Conf;
import com.here.platform.common.strings.VIN;
import com.here.platform.ns.dto.Container;
import com.here.platform.ns.dto.User;
import com.here.platform.ns.dto.Users;
import com.here.platform.ns.helpers.Steps;
import io.qameta.allure.Issue;
import io.restassured.response.Response;
import java.io.File;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Add vin's to consent request")
@UpdateConsentRequest
public class AddingVinsToConsentRequestTests extends BaseCMTest {

    @Test
    @DisplayName("Verify Adding Invalid VINs Error")
    void addInvalidVINsTest() {
        var vinInLowerCase = VIN.generate(17).toLowerCase();
        var vinInUpperCaseButLastSymbol = VIN.generate(16) + "a";
        var validVIN = VIN.generate(17);
        User mpConsumer = Users.MP_CONSUMER.getUser();
        ConsentRequestContainer targetContainer = ConsentRequestContainers.generateNew(MPProviders.DAIMLER_REFERENCE);

        ConsentInfo consentInfo = Consents.generateNewConsentInfo(mpConsumer, targetContainer);
        var crid = new ConsentRequestSteps2(targetContainer.getProvider().getName(), consentInfo)
                .onboardAllForConsentRequest()
                .createConsentRequest()
                .getId();

        var addVinsToConsentRequestResponse = consentRequestController
                .withConsumerToken()
                .addVinsToConsentRequest(crid, FILE_TYPE.JSON, vinInLowerCase, vinInUpperCaseButLastSymbol);

        String cause = new ResponseAssertion(addVinsToConsentRequestResponse)
                .statusCodeIsEqualTo(StatusCode.BAD_REQUEST)
                .expectedErrorResponse(CONSENT_REQUEST_UPDATE).getCause();

        Assertions.assertThat(cause)
                .contains(List.of(vinInLowerCase, vinInUpperCaseButLastSymbol))
                .doesNotContain(validVIN);
    }

    @Test
    @DisplayName("Verify adding twice the same VINs are ignored")
    void addTheSameVINsIgnoredTest() {
        var validVINs = new String[]{VIN.generate(17), VIN.generate(17)};
        User mpConsumer = Users.MP_CONSUMER.getUser();
        ConsentRequestContainer targetContainer = ConsentRequestContainers.generateNew(MPProviders.DAIMLER_REFERENCE);

        ConsentInfo consentInfo = Consents.generateNewConsentInfo(mpConsumer, targetContainer);
        var expectedConsentRequestStatuses = new ConsentRequestStatus()
                .pending(2).approved(0).revoked(0).expired(0).rejected(0);

        new ConsentRequestSteps2(targetContainer.getProvider().getName(), consentInfo)
                .onboardAllForConsentRequest()
                .createConsentRequest()
                .addVINsToConsentRequest(validVINs)
                .verifyConsentStatus(expectedConsentRequestStatuses)
                .addVINsToConsentRequest(validVINs)
                .verifyConsentStatus(expectedConsentRequestStatuses);
    }

    @Test
    @Issue("NS-2802")
    @DisplayName("Verify adding duplicated VINs in a single file will be ignored")
    void duplicatedVINsTest() {
        var duplicatedVin = VIN.generate(17);
        var targetVINsWithDuplication = new String[]{VIN.generate(17), duplicatedVin, duplicatedVin};

        User mpConsumer = Users.MP_CONSUMER.getUser();
        ConsentRequestContainer targetContainer = ConsentRequestContainers.generateNew(MPProviders.DAIMLER_REFERENCE);
        ConsentInfo consentInfo = Consents.generateNewConsentInfo(mpConsumer, targetContainer);
        var expectedConsentRequestStatuses = new ConsentRequestStatus()
                .pending(targetVINsWithDuplication.length - 1)
                .approved(0).revoked(0).expired(0).rejected(0);

        new ConsentRequestSteps2(targetContainer.getProvider().getName(), consentInfo)
                .onboardAllForConsentRequest()
                .createConsentRequest()
                .addVINsToConsentRequest(targetVINsWithDuplication)
                .verifyConsentStatus(expectedConsentRequestStatuses);
    }

}