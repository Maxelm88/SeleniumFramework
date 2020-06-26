package mystore;

import helpers.Common;
import helpers.dictionary.DataRowStatus;
import helpers.login.Login;
import helpers.throwables.ReportSummaryParams;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Test;
import store.steps.StoreSteps;

@DisplayName("Testowanie My Store")
public class MyStore1_Test extends AbstractMyStoreTest {

    private static final String TEST_NAME = "MyStore1";
    private static final String USER = "user@user.pl";

    @After
    public void sendResults(){
        Common.reportSummaryAndSendResults(ReportSummaryParams.builder()
                .testName(TEST_NAME)
                .appName(APP_NAME)
                .env(APP_ENV)
                .status(DataRowStatus.AKTYWNY)
                .passed(status)
                .finalReport(true)
                .param(USER)
                .build());
    }

    @DisplayName("Wyszukiwanie")
    @Test
    public void testMethod() {
        Login.loginWeb(APP_NAME, APP_ENV, USER, driver);
        StoreSteps.wyszukanie("Lala");
        status = true;
    }

}
