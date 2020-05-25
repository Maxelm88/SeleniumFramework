package store.steps;

import io.qameta.allure.Step;
import store.po.StoreHomepage;

import static helpers.CommonActions.clickElement;
import static helpers.CommonActions.enterIntoElement;

public class StoreSteps {

    @Step("Wyszukiwanie: {nameSearch}")
    public static void wyszukanie(String nameSearch) {
        enterIntoElement(StoreHomepage.fieldSearch, nameSearch, false);
        clickElement(StoreHomepage.buttonSearch);
    }

}
