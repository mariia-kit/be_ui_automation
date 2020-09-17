package com.here.platform.e2e;

import static com.here.platform.ns.dto.Users.MP_CONSUMER;

import com.here.platform.ns.controllers.access.ContainerDataController;
import com.here.platform.ns.dto.Container;
import com.here.platform.ns.dto.Containers;
import com.here.platform.ns.dto.DataProvider;
import com.here.platform.ns.dto.Providers;
import com.here.platform.ns.dto.Vehicle;
import com.here.platform.ns.helpers.Steps;
import com.here.platform.ns.instruments.ProdAfterCleanUp;
import com.here.platform.ns.restEndPoints.NeutralServerResponseAssertion;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@DisplayName("Prod Integration Tests")
@ExtendWith(ProdAfterCleanUp.class)
class ProdIntegrationTest extends BaseE2ETest {

    @Test
    @DisplayName("Verify access service")
    @Tag("ignored-dev")
    @Tag("ignored-sit")
    @Tag("prod")
    void accessTest() {
        DataProvider provider = Providers.REFERENCE_PROVIDER_PROD.getProvider();
        Container container = Containers.generateNew(provider)
                .withId("payasyoudrive")
                .withName("payasyoudrive")
                .withResourceNames("payasyoudrive")
                .withConsentRequired(false);

        var response = new ContainerDataController()
                .withToken(MP_CONSUMER)
                .getContainerForVehicle(provider, Vehicle.validVehicleId, container);
        new NeutralServerResponseAssertion(response)
                .expectedCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Verify provider service")
    @Tag("ignored-dev")
    @Tag("ignored-sit")
    @Tag("prod")
    void providerTest() {
        DataProvider provider = Providers.REFERENCE_PROVIDER.getProvider();
        Container container = Containers.generateNew(provider);

        Steps.createRegularContainer(container);
        Steps.removeRegularContainer(container);
    }

    @Test
    @DisplayName("Verify create")
    @Tag("ignored")
    void createProdData() {
        DataProvider provider = Providers.REFERENCE_PROVIDER.getProvider();
        Container container = Containers.generateNew(provider)
                .withName("payasyoudrive")
                .withResourceNames("payasyoudrive")
                .withConsentRequired(false);

        Steps.createRegularProvider(provider);
        Steps.createRegularContainer(container);
    }

}
