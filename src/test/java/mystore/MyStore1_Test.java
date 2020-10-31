package mystore;

import helpers.Common;
import helpers.data.provider.AbstractTestCaseData;
import helpers.data.provider.MyStoreTestCaseData;
import helpers.dictionary.DataRowStatus;
import helpers.login.Login;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import store.steps.StoreSteps;

@DisplayName("Testowanie My Store")
public class MyStore1_Test extends AbstractMyStoreTest {

    private static final String TEST_NAME = "MyStore1";
    private static final String JIRA_TICKET = "RST-1";
    private static final String USER = "user@user.pl";

    MyStoreTestCaseData inputData;
    MyStoreTestCaseData outputData;

    @Override
    public AbstractTestCaseData getInputData() {
        return inputData;
    }

    @Override
    public AbstractTestCaseData getOutputData() {
        return outputData;
    }

    @Override
    public void initOutputData() {

        outputData = MyStoreTestCaseData.builder()
                .applicationName(APP_NAME)
                .jiraTicket(JIRA_TICKET)
                .profile(APP_ENV)
                .dataRowStatus(DataRowStatus.AKTYWNY)
                .nazwaTestu(TEST_NAME)
                .finalReport(true)
                .user(USER)
                .build();
    }

    @Before
    public void getDataFromDb() {
//        select = CustomDataRequest.builder()
//                .appName(APP_NAME.getDescription())
//                .env(APP_ENV)
//                .testName(JIRA_TICKET)
//                .status(DataRowStatus.AKTYWNY)
//                .build();
//
//        daneTestowe = manager.getCustomDataManager().getCustomTestDataWithParams(select);

        //TODO ogarnąć błąd java.lang.RuntimeException: getEnum - not  find enum
        inputData = new MyStoreTestCaseData(manager, JIRA_TICKET, APP_NAME, APP_ENV);
    }

    @DisplayName("Wyszukiwanie")
    @Test
    public void testMethod() {
//        Common.reporter().logPass(daneTestowe.getParam1());
        Common.reporter().logPass(APP_NAME.getDescription());
        Login.loginWeb(APP_NAME, APP_ENV, USER, driver);
        StoreSteps.wyszukanie("Lala");
        outputData.setSendToDb(true);
        status = true;
    }

}
