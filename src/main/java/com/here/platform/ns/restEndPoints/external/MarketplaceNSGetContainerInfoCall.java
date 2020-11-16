package com.here.platform.ns.restEndPoints.external;

import static com.here.platform.ns.dto.Users.MP_PROVIDER;

import com.here.platform.common.config.Conf;
import com.here.platform.ns.helpers.resthelper.RestHelper;
import com.here.platform.ns.restEndPoints.BaseRestControllerNS;
import io.restassured.response.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;


public class MarketplaceNSGetContainerInfoCall extends BaseRestControllerNS<MarketplaceNSGetContainerInfoCall> {

    public MarketplaceNSGetContainerInfoCall(String containerHrn) {
        callMessage = "Perform MP call to gather NS Containers info";
        setDefaultUser(MP_PROVIDER);
        String urlEncoded;
        try {
            urlEncoded = URLEncoder.encode(containerHrn, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("Error encoding hrn for api request", ex.getCause());
        }
        endpointUrl = String.format("%s/neutralServer/containers/%s", Conf.mp().getMarketplaceUrl(), urlEncoded);
    }

    @Override
    public Supplier<Response> defineCall() {
        return () -> RestHelper.get(this);
    }

}

