package mystore;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import store.steps.StoreSteps;

@DisplayName("Testowanie My Store")
public class MyStore1_Test extends AbstractMyStoreTest {

    @DisplayName("Wyszukiwanie")
    @Test
    public void testMethod() {
        StoreSteps.openPage();
        StoreSteps.wyszukanie("Lala");
    }


}
