package com.here.platform.cm.pages;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;


public class VINEnteringPage extends BaseCMPage {

    private final SelenideElement vinNumberInput = $("#vinNumber");

    public VINEnteringPage isLoaded() {
        this.vinNumberInput.waitUntil(Condition.visible, 10000);
        return this;
    }

    @Step
    public void fillVINAndContinue(String vin) {
        this.vinNumberInput.setValue(vin);
        $(byText("Continue")).click();
    }

}