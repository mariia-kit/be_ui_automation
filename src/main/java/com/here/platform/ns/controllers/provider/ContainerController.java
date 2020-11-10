package com.here.platform.ns.controllers.provider;

import com.here.platform.common.config.Conf;
import com.here.platform.ns.controllers.BaseNeutralService;
import com.here.platform.ns.dto.Container;
import io.qameta.allure.Step;
import io.restassured.response.Response;


public class ContainerController extends BaseNeutralService<ContainerController> {

    private final String containersBasePath = Conf.ns().getNsUrlProvider() + "providers";

    @Step
    public Response getContainer(Container container) {
        return neutralServerClient(containersBasePath)
                .get("/{providerId}/containers_info/{containerId}", container.getDataProviderName(), container.getId());

    }

    @Step
    public Response addContainer(Container container) {
        return neutralServerClient(containersBasePath)
                .body(container.generateContainerBody())
                .put("/{providerId}/containers_info/{containerId}", container.getDataProviderName(), container.getId());
    }

    @Step
    public Response deleteContainer(Container container) {
        return neutralServerClient(containersBasePath)
                .delete(
                        "/{providerId}/containers_info/{containerId}",
                        container.getDataProviderName(),
                        container.getId()
                );
    }

    @Step
    public Response getContainersList(String providerId) {
        return neutralServerClient(containersBasePath)
                .get("/{providerId}/containers_info", providerId);
    }

}
