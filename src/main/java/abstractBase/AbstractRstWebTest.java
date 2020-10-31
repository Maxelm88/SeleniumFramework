package abstractBase;

import helpers.Common;
import helpers.data.provider.AbstractTestCaseData;
import helpers.datebase.TestDataManager;
import helpers.dictionary.Profile;
import helpers.dictionary.PropertyNames;
import helpers.skip.RunTest;
import helpers.skip.SkipChecker;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.openqa.selenium.WebDriver;

public abstract class AbstractRstWebTest {

    protected static final Profile APP_ENV = PropertyNames.getProfile();
    protected WebDriver driver;

    @ClassRule
    public static RunTest runTest = new RunTest(new SkipChecker(PropertyNames.getSkip()));
    protected TestDataManager manager;

    public abstract AbstractTestCaseData getInputData();

    public abstract AbstractTestCaseData getOutputData();

    protected abstract void initApp();

    protected abstract void initOutputData();

    public abstract void testMethod();

    @Before
    public void setUp() {
        initOutputData();
        driver = Common.setUpClass();
        initApp();
        initManager();
    }

    @After
    public void tearDown() {
        Common.quitDriver();
        Common.reportSummaryAndSendResults(getOutputData(), APP_ENV);
    }

    private void initManager() {
        try {
            manager = new TestDataManager();
        } catch (Exception e) {
            Common.reporter().logError("Problem z inicjacją managera.", e);
        }
    }
}
