package helpers.login;

import helpers.Common;
import helpers.CommonActions;
import helpers.CommonAsserts;
import helpers.CommonWaits;
import helpers.datebase.TestDataManager;
import helpers.dictionary.ApplicationName;
import helpers.dictionary.Profile;
import io.qameta.allure.Step;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import store.po.StoreHomepage;
import store.po.StoreMyAccountPage;
import store.po.StoreSignInPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login {
    public static String url;
    private static String pass;

    @Step("Logowanie do systemu {app} na środowisku {env}")
    public static void loginWeb(ApplicationName app, Profile env, String login, WebDriver driver) {
        Common.reporter().logPass(String.format("# Logowanie do systemu: %s na środowisko: %s #", app.getDescription(), env.name()));

        try {
            driver.manage().window().maximize();
        } catch (NullPointerException e) {
            Common.reporter().logWarn("Problem z maksymalizacja okna przeglądarki");
        }

        List<String> daneLogowania = new TestDataManager().getDataSelector().getLoginCredentials(app.getDescription(), login, env);
        url = daneLogowania.get(0);
        pass = Common.decryptAES(daneLogowania.get(2));
        Common.reporter().logPass("Uruchomienie przeglądarki z URL: " + url);

        Map<ApplicationName, Runnable> loginMap = new HashMap<>();
        loginMap.put(ApplicationName.STORE, () -> loginStore(app.getDescription(), env.name(), driver, login, pass));

        loginMap.get(ApplicationName.getEnum(app.getDescription())).run();
    }

    private static void loginStore(String aplikacja, String srodowisko, WebDriver driver, String loginSave, String pass) {
        PageFactory.initElements(driver, StoreSignInPage.class);
        PageFactory.initElements(driver, StoreHomepage.class);
        try {
            driver.navigate().to(url);
        } catch (TimeoutException e) {
            Common.reporter().logFail(String.format("Problem z wyświetleniem strony logowania: %s - przekroczono limit czasu oczekiwania. Środowisko: %s  URL: %s", aplikacja, srodowisko, url));
        }
        if (CommonWaits.waitUntilElementPresent(StoreHomepage.buttonSignIn.by, 30) != null) {
            CommonActions.clickElement(StoreHomepage.buttonSignIn);
            CommonActions.enterIntoTextField(StoreSignInPage.emailAddressField, loginSave);
            CommonActions.enterIntoTextField(StoreSignInPage.passwordField, pass);
            CommonActions.clickElement(StoreSignInPage.submitButton);
            CommonAsserts.assertTrue("Widoczny przycisk " + StoreMyAccountPage.orderHistoryAndDetailsButton.desc,
                    "Niewidoczny przycisk " + StoreMyAccountPage.orderHistoryAndDetailsButton.desc,
                    Common.isElementVisible(StoreMyAccountPage.orderHistoryAndDetailsButton, 1));
        } else {
            Common.reporter().logFail("Problem z wyświetleniem strony logowania");
        }
    }
}
