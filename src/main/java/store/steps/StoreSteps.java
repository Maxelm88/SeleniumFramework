package store.steps;

import io.qameta.allure.Step;
import store.po.StoreHomepage;

public class StoreSteps {

    @Step("Wyszukiwanie: {nameSearch}")
    public static void wyszukanie(String nameSearch) {
        StoreHomepage.fieldSearch.enterIntoTextField(nameSearch);
        StoreHomepage.buttonSearch.clickElement();
    }

}
