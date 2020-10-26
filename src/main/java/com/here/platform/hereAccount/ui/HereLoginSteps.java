package com.here.platform.hereAccount.ui;

import static com.codeborne.selenide.Selenide.open;

import com.codeborne.selenide.Selenide;
import com.here.platform.common.DataSubject;
import com.here.platform.dataProviders.daimler.DataSubjects;
import com.here.platform.ns.dto.User;
import io.qameta.allure.Step;
import lombok.experimental.UtilityClass;


@UtilityClass
public class HereLoginSteps {

    private final HereLoginPage loginPage = new HereLoginPage();

    @Step("Login data subject")
    public void loginDataSubject(DataSubjects dataSubjects) {
        loginDataSubject(dataSubjects.dataSubject);
    }

    @Step("Login data subject")
    public void loginDataSubject(DataSubject dataSubject) {
        loginPage
                .isLoaded()
                .fillUserEmail(dataSubject.getEmail())
                .fillUserPassword(dataSubject.getPass())
                .clickSignIn()
                .aproveConsentIfPresent();
    }

    @Step("Login marketplace user")
    public void loginMPUser(User mpUser) {
        loginPage
                .isLoaded()
                .fillUserEmail(mpUser.getEmail())
                .clickNextEmail()
                .fillRealm(mpUser.getRealm())
                .clickNextRealm()
                .fillUserPassword(mpUser.getPass())
                .clickSignIn();
    }

    @Step
    public void logout() {
        open("logout");
        loginPage.isLoaded();
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
    }

}
