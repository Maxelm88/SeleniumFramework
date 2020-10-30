package mystore;

import helpers.Common;
import helpers.data.provider.AbstractTestCaseData;
import helpers.data.provider.MyStoreTestCaseData;
import helpers.datebase.request.CustomDataRequest;
import helpers.dictionary.DataRowStatus;
import helpers.login.Login;
import helpers.throwables.ReportSummaryParams;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
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
    }

    private CustomDataRequest select;
    private static final String SELECT_TEST = TEST_NAME;

    @Before
    public void getDataFromDb() {
        select = CustomDataRequest.builder()
                .appName(APP_NAME.getDescription())
                .env(APP_ENV)
                .testName(JIRA_TICKET)
                .status(DataRowStatus.AKTYWNY)
                .build();

        daneTestowe = manager.getCustomDataManager().getCustomTestDataWithParams(select);

//        inputData = new MyStoreTestCaseData(manager, JIRA_TICKET, APP_NAME, APP_ENV);
    }

    @After
    public void sendResults() {
        Common.reportSummaryAndSendResults(ReportSummaryParams.builder()
                .testName(JIRA_TICKET)
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
//        Common.reporter().logPass(daneTestowe.getParam1());
//        Common.reporter().logPass(inputData.getUser());
        Login.loginWeb(APP_NAME, APP_ENV, USER, driver);
        StoreSteps.wyszukanie("Lala");
        status = true;
    }

}
