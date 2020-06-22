package store.steps;

import helpers.CommonActions;
import io.qameta.allure.Step;
import store.po.StoreHomepage;

public class StoreSteps {

    @Step("Wyszukiwanie: {nameSearch}")
    public static void wyszukanie(String nameSearch) {
        CommonActions.enterIntoElement(StoreHomepage.fieldSearch, nameSearch, false);
        CommonActions.clickElement(StoreHomepage.buttonSearch);
    }

}
