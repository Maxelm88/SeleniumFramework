package helpers.login;

import helpers.Common;
import helpers.CommonAsserts;
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
import java.util.Map;

import static helpers.Common.isElementVisible;
import static helpers.Common.reporter;
import static helpers.CommonActions.clickElement;
import static helpers.CommonActions.enterIntoTextField;
import static helpers.CommonWaits.waitUntilElementPresent;

public class Login {
   private static String URL = "";


    @Step("Logowanie do systemu {app} na środowisku {env}")
    public static void loginWeb(ApplicationName app, Profile env, String login, WebDriver driver) {
        reporter().logPass(String.format("# Logowanie do systemu: %s na środowisko: %s #", app.getDescription(), env.name()));

        Map<ApplicationName, Runnable> loginMap = new HashMap<>();
        loginMap.put(ApplicationName.STORE, () -> loginStore(app.getDescription(), env.name(), driver, login));

        loginMap.get(ApplicationName.getApplicationName(app.getDescription())).run();
    }

    private static void loginStore(String aplikacja, String srodowisko, WebDriver driver, String loginSave){
        PageFactory.initElements(driver, StoreSignInPage.class);
        PageFactory.initElements(driver, StoreHomepage.class);
        try {
            URL = "http://automationpractice.com/";
            driver.navigate().to(URL);
        } catch (TimeoutException e) {
            reporter().logFail(String.format("Problem z wyświetleniem strony logowania: %s - przekroczono limit czasu oczekiwania. Środowisko: %s  URL: %s", aplikacja, srodowisko, URL));
        }
        if(waitUntilElementPresent(StoreHomepage.buttonSignIn.by, 30) != null) {
            clickElement(StoreHomepage.buttonSignIn);
            enterIntoTextField(StoreSignInPage.emailAddressField, loginSave);
            enterIntoTextField(StoreSignInPage.passwordField, "user123");
            clickElement(StoreSignInPage.submitButton);
            CommonAsserts.assertTrue("Widoczny przycisk " + StoreMyAccountPage.orderHistoryAndDetailsButton.desc,
                    "Niewidoczny przycisk " + StoreMyAccountPage.orderHistoryAndDetailsButton.desc,
                    isElementVisible(StoreMyAccountPage.orderHistoryAndDetailsButton, 1));
        } else {
            Common.reporter().logFail("Problem z wyświetleniem strony logowania");
        }
    }
}
