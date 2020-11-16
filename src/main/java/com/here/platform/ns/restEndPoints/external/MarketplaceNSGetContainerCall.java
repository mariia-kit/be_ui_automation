package com.here.platform.ns.restEndPoints.external;

import static com.here.platform.ns.dto.Users.MP_PROVIDER;

import com.here.platform.common.config.Conf;
import com.here.platform.ns.helpers.resthelper.RestHelper;
import com.here.platform.ns.restEndPoints.BaseRestControllerNS;
import io.restassured.response.Response;
import java.util.function.Supplier;


public class MarketplaceNSGetContainerCall extends BaseRestControllerNS<MarketplaceNSGetContainerCall> {

    public MarketplaceNSGetContainerCall(String providerName) {
        callMessage = "Perform MP call to gather NS Containers list info";
        setDefaultUser(MP_PROVIDER);

        endpointUrl = String.format("%s/neutralServer/providers/%s/containers", Conf.mp().getMarketplaceUrl(), providerName);
    }

    @Override
    public Supplier<Response> defineCall() {
        return () -> RestHelper.get(this);
    }

}
