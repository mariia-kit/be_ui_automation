package com.here.platform.common;

import static com.here.platform.common.strings.SBB.sbb;
import static io.restassured.RestAssured.given;

import com.here.platform.cm.enums.ConsentRequestContainers;
import com.here.platform.cm.steps.api.OnboardingSteps;
import com.here.platform.common.config.Conf;
import com.here.platform.dataProviders.reference.controllers.ReferenceProviderController;
import com.here.platform.ns.dto.Containers;
import com.here.platform.ns.dto.ProviderResource;
import com.here.platform.ns.dto.Providers;
import com.here.platform.ns.dto.Users;
import com.here.platform.ns.dto.Vehicle;
import com.here.platform.ns.helpers.Steps;
import com.here.platform.ns.restEndPoints.external.AaaCall;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TestDataGeneration {

    public static void main(String[] args) {
        if (!"prod".equalsIgnoreCase(System.getProperty("env"))) {
            createPoliciesForProviderGroup();
//            createBaseProvidersIfNecessary();
//            createBaseContainersIfNecessary();
//            createBaseCMProvidersIfNecessary();
//            createBaseCMApplicationIfNecessary();
        } else {
            //TODO: enable provider onboard after new provider logic deploy to prod.
            //createBaseProvidersIfNecessaryProd();
            //createBaseContainersIfNecessaryProd();
            //createBaseCMApplicationIfNecessaryProd();
        }
    }

    private static void createBaseProvidersIfNecessary() {
        Stream.of(Providers.values())
                .filter(providers -> !providers.equals(Providers.NOT_EXIST))
                .forEach(providers -> Steps.createRegularProvider(providers.getProvider()));
    }

    public static void createBaseProvidersIfNecessaryProd() {
        Stream.of(Providers.DAIMLER_REAL, Providers.DAIMLER_EXPERIMENTAL, Providers.BMW, Providers.BMW_TEST,
                Providers.REFERENCE_PROVIDER_PROD)
                .forEach(providers -> Steps.createRegularProvider(providers.getProvider()));
    }

    public static void createBaseContainersIfNecessary() {
        Arrays.stream(Containers.values()).forEach(containers ->
                Steps.createRegularContainer(containers.getContainer())
        );
    }

    public static void createBaseContainersIfNecessaryProd() {
        Arrays.stream(Containers.values())
                .filter(container ->
                        container.getContainer().getDataProviderName().equals(Providers.DAIMLER_EXPERIMENTAL.getName())
                                ||
                                container.getContainer().getDataProviderName().equals(Providers.DAIMLER_REAL.getName()))
                .forEach(containers ->
                        Steps.createRegularContainer(containers.getContainer())
                );
    }

    public static void createBaseProvidersResourcesIfNecessary() {
        Arrays.stream(Containers.values()).forEach(containers ->
                Stream.of(containers.getContainer().getResourceNames().split(",")).parallel()
                        .forEach(res -> Steps.addResourceToProvider(
                                containers.getContainer().getDataProviderByName(),
                                new ProviderResource(res)
                                )
                        )
        );
    }

    private static void createBaseCMProvidersIfNecessary() {
        String consumerId = Conf.mpUsers().getMpConsumer().getRealm();
        Stream.of(ConsentRequestContainers.values())
                .map(ConsentRequestContainers::getProvider)
                .collect(Collectors.toSet())
                .forEach(provider ->
                        new OnboardingSteps(provider, consumerId).onboardTestProvider()
                );
    }

    private static void createBaseCMApplicationIfNecessary() {
        String consumerId = Conf.mpUsers().getMpConsumer().getRealm();
        Stream.of(ConsentRequestContainers.values()).forEach(containers ->
                new OnboardingSteps(containers.provider.getName(), consumerId)
                        .onboardTestProviderApplication(containers.getConsentContainer()));
    }

    public static void createBaseCMApplicationIfNecessaryProd() {
        String consumerId = Conf.mpUsers().getMpConsumer().getRealm();
        Stream.of(ConsentRequestContainers.values())
                .filter(container -> container.getProvider().getName().equals(Providers.DAIMLER_EXPERIMENTAL.getName())
                        ||
                        container.getProvider().getName().equals(Providers.DAIMLER_REAL.getName()))
                .forEach(containers ->
                        new OnboardingSteps(containers.provider.getName(), consumerId)
                                .onboardTestProviderApplication(containers.getConsentContainer()));
    }

    private static void createPoliciesForProviderGroup() {
        new AaaCall().addGroupToPolicy(Conf.nsUsers().getProviderGroupId(),
                Conf.nsUsers().getProviderPolicyId());
    }

    public static void setVehicleTokenForDaimler() {
        String token = Users.DAIMLER.getToken().split(":")[0];
        String refresh = Users.DAIMLER.getToken().split(":")[1];
        for (String vin : Arrays.asList(Vehicle.validVehicleIdLong, Vehicle.validVehicleId)) {
            String url = sbb(Conf.ns().getNsUrlBaseAccess()).append(Conf.ns().getNsUrlAccess()).append("token?")
                    .append("access_token=").append(token)
                    .append("&refresh_token=").append(refresh)
                    .append("&client_id=88440bf1-2fff-42b6-8f99-0510b6b5e6f8")
                    .append("&client_secret=2d839912-c5e6-4cfb-8543-9a1bed38efe6")
                    .append("&vehicle_id=").append(vin)
                    .append("&scope=mb:user:pool:reader mb:vehicle:status:general")
                    .bld();
            given()
                    .headers("Content-Type", "application/json",
                            "Authorization", "Bearer " + Users.PROVIDER.getToken())
                    .when()
                    .post(url)
                    .then()
                    .extract().response();
        }

        new ReferenceProviderController().addToken(Vehicle.validVehicleId, token, UUID.randomUUID().toString(), "general");
        new ReferenceProviderController().addToken(Vehicle.validRefVehicleId, token, UUID.randomUUID().toString(), "general");
        new ReferenceProviderController().addToken(Vehicle.validVehicleIdLong, token, UUID.randomUUID().toString(), "general");
    }

}
