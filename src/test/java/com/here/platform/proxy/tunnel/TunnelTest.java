package com.here.platform.proxy.tunnel;

import com.here.platform.ns.dto.SentryErrorsList;
import com.here.platform.ns.dto.Users;
import com.here.platform.proxy.BaseProxyTests;
import com.here.platform.proxy.conrollers.ServiceProvidersController;
import com.here.platform.proxy.conrollers.TunnelController;
import com.here.platform.proxy.dto.ProxyErrorList;
import com.here.platform.proxy.dto.ProxyProvider;
import com.here.platform.proxy.dto.ProxyProvider.CredentialsAuthMethod;
import com.here.platform.proxy.dto.ProxyProviderResource;
import com.here.platform.proxy.dto.ProxyProviderResources;
import com.here.platform.proxy.dto.ProxyProviders;
import com.here.platform.proxy.helper.ProxyProviderAssertion;
import com.here.platform.proxy.helper.RemoveObjCollector;
import com.here.platform.proxy.steps.ProxySteps;
import io.qameta.allure.Issue;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Proxy Tunnel")
@DisplayName("[External Proxy] Verify Proxy Service Tunnel")
public class TunnelTest extends BaseProxyTests {

    @Test
    @DisplayName("[External Proxy] Verify retrieve proxy data Successful")
    void verifyProxyCanBeRetrieved() {
        ProxyProvider proxyProvider = ProxyProviders.REFERENCE_PROXY.getProxyProvider();
        ProxyProviderResource resource = ProxyProviderResources.generate();
        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.createProxyResource(proxyProvider, resource);

        ProxySteps.createListingAndSubscription(resource);

        var tunnel = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider, resource);
        new ProxyProviderAssertion(tunnel)
                .expectedCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("[External Proxy] Verify retrieve proxy data No Subscription")
    void verifyProxyCanBeRetrievedNoSubs() {
        ProxyProvider proxyProvider = ProxyProviders.REFERENCE_PROXY.getProxyProvider();
        ProxyProviderResource resource = ProxyProviderResources.generate();

        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.createProxyResource(proxyProvider, resource);

        var tunnel = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider, resource);
        new ProxyProviderAssertion(tunnel)
                .expectedError(ProxyErrorList.getNoAccessError(
                        Users.MP_CONSUMER.getUser().getUserId(),
                        resource.getHrn()));
    }


    @Test
    @DisplayName("[External Proxy] Verify retrieve proxy data No Resource No Policy")
    void verifyProxyCanBeRetrievedNoResourceNoPolicy() {
        ProxyProvider proxyProvider = ProxyProviders.REFERENCE_PROXY.getProxyProvider();
        ProxyProviderResource resource = new ProxyProviderResource(
                "Auto-testing-reference-no-policy",
                "proxy/data/nopolicy");

        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.createProxyResource(proxyProvider, resource);
        ProxySteps.deleteProxyResource(resource.getId());

        var tunnel = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider, resource);
        new ProxyProviderAssertion(tunnel)
                .expectedError(ProxyErrorList.getNoAccessError( Users.MP_CONSUMER.getUser().getUserId(),
                        resource.getHrn()));
    }

    @Test
    @Disabled
    @DisplayName("[External Proxy] Verify retrieve proxy data Res names intersects with deleted one")
    void verifyProxyCanBeRetrievedResNamesIntersectsDeleted() {
        ProxyProvider proxyProvider = ProxyProviders.REFERENCE_PROXY.getProxyProvider();
        ProxyProviderResource resource = new ProxyProviderResource(
                "Auto-testing-reference-no-res",
                "/proxy/data/123456nores");
        ProxyProviderResource resource2 = new ProxyProviderResource(
                "Auto-testing-reference-no-res",
                "/proxy/data/123456nores");

        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.createProxyResource(proxyProvider, resource);
        ProxySteps.createProxyResource(proxyProvider, resource2);
        ProxySteps.deleteProxyResource(resource2.getId());

        var tunnel = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider, resource);
        new ProxyProviderAssertion(tunnel)
                .expectedError(ProxyErrorList.getNoAccessError(
                        Users.MP_CONSUMER.getUser().getUserId(),
                        resource2.getHrn()));
    }

    @Test
    @Disabled
    @DisplayName("[External Proxy] Verify retrieve proxy data No Resource on Data Provider")
    void verifyProxyCanBeRetrievedNoResourceOnDP() {
        ProxyProvider proxyProvider = ProxyProviders.REFERENCE_PROXY.getProxyProvider();
        ProxyProviderResource resource = ProxyProviderResources.generate();
        resource.setPath("nosuch/url");

        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.createProxyResource(proxyProvider, resource);

        ProxySteps.createListingAndSubscription(resource);

        var tunnel = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider, resource);
        new ProxyProviderAssertion(tunnel)
                .expectedTunnelError(ProxyErrorList.getResourceNotAccessibleError(resource.getPath()));
    }

    @Test
    @DisplayName("[External Proxy] Verify retrieve proxy data No Token")
    void verifyProxyCanBeRetrievedNoToken() {
        ProxyProvider proxyProvider = ProxyProviders.REFERENCE_PROXY.getProxyProvider();
        ProxyProviderResource resource = new ProxyProviderResource(
                "Auto-testing-reference-no-token",
                "proxy/data/123456notoken");

        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.createProxyResource(proxyProvider, resource);

        var tunnel = new TunnelController()
                .getData(proxyProvider, resource);
        new ProxyProviderAssertion(tunnel)
                .expectedSentryError(SentryErrorsList.TOKEN_NOT_FOUND.getError());
    }

    @Test
    @DisplayName("[External Proxy] Verify retrieve new proxy data Successful")
    void verifyNewProxyCanBeRetrieved() {
        ProxyProvider proxyProvider = ProxyProviders.REFERENCE_PROXY.getProxyProvider();
        ProxyProviderResource resource = ProxyProviderResources.generate();

        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.createProxyResource(proxyProvider, resource);
        RemoveObjCollector.addProxyResHrn(resource.getHrn());

        ProxySteps.createListingAndSubscription(resource);

        var tunnel = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider, resource);
        new ProxyProviderAssertion(tunnel)
                .expectedCode(HttpStatus.SC_OK)
                .verifyResponseTime(10000L);
        new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider.getIdentifier(), resource.getPath() + "?query=none");
        var tunnel2 = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider, resource);
        new ProxyProviderAssertion(tunnel2)
                .expectedCode(HttpStatus.SC_OK)
                .verifyResponseTime(500L);
    }

    @Test
    @Disabled("Currently one method implemented")
    @DisplayName("[External Proxy] Verify retrieve new proxy data Auth not implemented")
    void verifyNewProxyCanBeRetrievedNoAuth() {
        ProxyProvider proxyProvider = ProxyProviders.generate()
                .withAuthMethod(CredentialsAuthMethod.BASIC_AUTH, "root", "qwerty");
        ProxyProviderResource resource = ProxyProviderResources.generate();

        ProxySteps.createProxyResource(proxyProvider, resource);
        RemoveObjCollector.addProxyResHrn(resource.getHrn());

        ProxySteps.createListingAndSubscription(resource);

        var tunnel = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider, resource);
        new ProxyProviderAssertion(tunnel)
                .expectedCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("[External Proxy] Verify retrieve new proxy data with query")
    void verifyNewProxyCanBeRetrievedWithQuery() {
        ProxyProvider proxyProvider = ProxyProviders.REFERENCE_PROXY.getProxyProvider();
        ProxyProviderResource resource = ProxyProviderResources.generate();

        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.createProxyResource(proxyProvider, resource);

        ProxySteps.createListingAndSubscription(resource);

        var tunnel = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider.getIdentifier(), resource.getPath() + "?language=en-us");
        new ProxyProviderAssertion(tunnel)
                .expectedCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("[External Proxy] Verify retrieve proxy data with Generic resource path")
    void verifyProxyCanBeRetrievedWithGenericPath() {
        ProxyProvider proxyProvider = ProxyProviders.REFERENCE_PROXY.getProxyProvider();
        ProxyProviderResource genericResource = ProxyProviderResources.generateGenericPath();

        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.createProxyResource(proxyProvider, genericResource);
        ProxyProviderResource resource = ProxyProviderResources.generate();
        ProxySteps.createProxyResource(proxyProvider, resource);

        ProxySteps.createListingAndSubscription(resource);

        var tunnel = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider, resource);
        new ProxyProviderAssertion(tunnel)
                .expectedCode(HttpStatus.SC_OK);
        new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider.getIdentifier(), resource.getPath());
        new ProxyProviderAssertion(tunnel)
                .expectedCode(HttpStatus.SC_OK);

    }

    @Test
    @Disabled("Reproduced on prod with Alerts where apiKey is returned")
    @DisplayName("[External Proxy] Verify retrieve proxy data with invalid path")
    void verifyProxyCanBeRetrievedWithInvalidPath() {
        ProxyProvider proxyProvider = ProxyProviders.REFERENCE_PROXY.getProxyProvider();
        ProxyProviderResource resource = ProxyProviderResources.REFERENCE_RESOURCE.getResource();

        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.readProxyProviderResource(resource);

        ProxySteps.createListingAndSubscription(resource);

        var tunnel = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider, resource);
        new ProxyProviderAssertion(tunnel)
                .expectedCode(HttpStatus.SC_OK);
        new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider.getIdentifier(), resource.getPath() + "/4534%gtg66676");
        new ProxyProviderAssertion(tunnel)
                .expectedCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("[External Proxy] Verify retrieve new proxy data Error from Provider")
    void verifyNewProxyCanBeRetrievedErrorExpected() {
        ProxyProvider proxyProvider = ProxyProviders.REFERENCE_PROXY.getProxyProvider();
        ProxyProviderResource resource = ProxyProviderResources.generate();

        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.createProxyResource(proxyProvider, resource);

        ProxySteps.createListingAndSubscription(resource);

        var tunnel = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider.getIdentifier(), resource.getPath() + "?error=error");
        new ProxyProviderAssertion(tunnel)
                .expectedCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("[External Proxy] Verify retrieve new proxy data Empty response from Provider")
    void verifyNewProxyCanBeRetrievedEmptyResponse() {
        ProxyProvider proxyProvider = ProxyProviders.REFERENCE_PROXY.getProxyProvider();
        ProxyProviderResource resource = ProxyProviderResources.generate();

        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.createProxyResource(proxyProvider, resource);

        ProxySteps.createListingAndSubscription(resource);

        var tunnel = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider.getIdentifier(), resource.getPath() + "?empty=true");
        new ProxyProviderAssertion(tunnel)
                .expectedCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @Disabled
    @DisplayName("[External Proxy] Verify retrieve proxy data Successful when resource with generic path exists")
    void verifyProxyCanBeRetrievedWithGenericResourcePath() {
        ProxyProvider proxyProvider = ProxyProviders.REFERENCE_PROXY.getProxyProvider();

        ProxyProviderResource resource = ProxyProviderResources.generate();
        ProxyProviderResource secondResource = ProxyProviderResources.generate();
        ProxyProviderResource genericPathResource = new ProxyProviderResource(
                "Auto-testing-reference-Generic-Path",
                "/proxy/data/*");

        ProxySteps.createProxyProvider(proxyProvider);
        ProxySteps.readProxyProvider(proxyProvider);
        ProxySteps.createProxyResource(proxyProvider, resource);
        ProxySteps.createProxyResource(proxyProvider, secondResource);
        ProxySteps.readProxyProviderResource(genericPathResource);

        ProxySteps.createListingAndSubscription(genericPathResource);

        var tunnel = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider, resource);
        new ProxyProviderAssertion(tunnel)
                .expectedCode(HttpStatus.SC_OK);
        var tunnel2 = new TunnelController()
                .withConsumerToken()
                .getData(proxyProvider, secondResource);
        new ProxyProviderAssertion(tunnel2)
                .expectedCode(HttpStatus.SC_OK);
    }
}
