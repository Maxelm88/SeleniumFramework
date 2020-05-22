package store.steps;

import helpers.CommonAsserts;
import io.qameta.allure.Step;
import store.po.StoreHomepage;
import store.po.StoreMyAccountPage;
import store.po.StoreSignInPage;

import static helpers.Common.isElementVisible;
import static helpers.Common.openUrl;
import static helpers.CommonActions.clickElement;
import static helpers.CommonActions.enterIntoElement;

public class StoreSteps {

    @Step("Otworzenie strony")
    public static void openPage() {
        openUrl(true, "http://automationpractice.com/");
    }

    @Step("Wyszukiwanie: {nameSearch}")
    public static void wyszukanie(String nameSearch) {
        enterIntoElement(StoreHomepage.fieldSearch, nameSearch, false);
        clickElement(StoreHomepage.buttonSearch);
    }

    @Step("Logowanie na user: {login}")
    public static void logowanie(String login, String pass) {
        clickElement(StoreHomepage.buttonSignIn);
        enterIntoElement(StoreSignInPage.emailAddressField, login, true);
        enterIntoElement(StoreSignInPage.passwordField, pass, true);
        clickElement(StoreSignInPage.submitButton);
        CommonAsserts.assertTrue("Widoczny przycisk " + StoreMyAccountPage.orderHistoryAndDetailsButton.desc,
                "Niewidoczny przycisk " + StoreMyAccountPage.orderHistoryAndDetailsButton.desc,
                isElementVisible(StoreMyAccountPage.orderHistoryAndDetailsButton, 1));
    }
}
