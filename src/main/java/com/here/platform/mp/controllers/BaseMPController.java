package com.here.platform.mp.controllers;

import static io.restassured.RestAssured.given;
import static io.restassured.config.HeaderConfig.headerConfig;

import com.here.platform.ns.utils.NS_Config;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


public abstract class BaseMPController<T> {

    private String authorizationToken = "";

    protected RequestSpecification mpClient(final String targetPath) {
        var baseService = given()
                .config(RestAssuredConfig.config()
                        .headerConfig(headerConfig().overwriteHeadersWithName("Authorization", "Content-Type")))
                .baseUri(NS_Config.URL_EXTERNAL_MARKETPLACE.toString())
                .basePath(targetPath)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .filters(new AllureRestAssured());

        if (!authorizationToken.isEmpty()) {
            baseService.header("Authorization", authorizationToken);
        }

        return baseService;
    }

    public T withToken(final String token) {
        authorizationToken = String.format("Bearer %s", token);
        return (T) this;
    }

}