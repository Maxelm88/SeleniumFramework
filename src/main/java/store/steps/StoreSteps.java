package store.steps;

import io.qameta.allure.Step;
import store.po.StoreHomepage;

import static helpers.Common.openUrl;
import static helpers.CommonActions.clickElement;
import static helpers.CommonActions.enterIntoElement;

public class StoreSteps {

    @Step("Otworzenie strony")
    public static void openPage(){
        openUrl(true, "http://automationpractice.com/");
    }

    @Step("Wyszukiwanie: {nameSearch}")
    public static void wyszukanie(String nameSearch) {
        enterIntoElement(StoreHomepage.fieldSearch, nameSearch, false);
        clickElement(StoreHomepage.buttonSearch);
    }
}
