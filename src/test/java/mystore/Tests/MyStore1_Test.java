package mystore.Tests;

import helpers.Common;
import helpers.data.provider.AbstractTestCaseData;
import helpers.data.provider.MyStoreTestCaseData;
import helpers.dictionary.ApplicationName;
import helpers.dictionary.DataRowStatus;
import helpers.login.Login;
import io.qameta.allure.Issue;
import io.qameta.allure.junit4.DisplayName;
import mystore.AbstractMyStoreTest;
import org.junit.Before;
import org.junit.Test;
import store.steps.StoreSteps;

@DisplayName(ApplicationName.Names.STORE)
public class MyStore1_Test extends AbstractMyStoreTest {

    private static final String JIRA_TICKET = "RST-1";
    private static final String TEST_NAME = JIRA_TICKET + " - Logowanie i wyszukanie słowa Lala";
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
        inputData = new MyStoreTestCaseData(manager, JIRA_TICKET, APP_NAME, APP_ENV);
    }

    @DisplayName(TEST_NAME)
    @Issue(JIRA_TICKET)
    @Test
    public void testMethod() {
        Common.reporter().logPass(APP_NAME.getDescription());
        Login.loginWeb(APP_NAME, APP_ENV, USER, driver);
        StoreSteps.wyszukanie("Lala");
        outputData.setSendToDb(true);
    }

}
