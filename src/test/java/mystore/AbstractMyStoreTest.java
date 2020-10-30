package mystore;

import abstractBase.AbstractRstWebTest;
import helpers.datebase.dto.CustomTestDTO;
import helpers.dictionary.ApplicationName;
import helpers.dictionary.PropertyNames;
import org.junit.Before;
import store.StoreCommon;

public abstract class AbstractMyStoreTest extends AbstractRstWebTest {
        protected static final String SKIP = PropertyNames.getSkip();
    protected static final ApplicationName APP_NAME = ApplicationName.STORE;
    protected static boolean status = false;
    protected CustomTestDTO daneTestowe;

    @Before
    public void initApp() {
        StoreCommon.initElement(driver);

    }

}
