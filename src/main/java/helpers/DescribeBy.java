package helpers;

import io.qameta.allure.Step;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

@Builder
@AllArgsConstructor
@Data
public class DescribeBy {

    public final By by;
    public final String desc;
    private static final int MAX_WAITING_TIME = 5;

    public void clickElement() {
        clickElement(true);
    }

    @Step("Kliknięcie w element {desc}")
    public void clickElement(boolean scroll) {
        try {
            WebElement element = Common.getElement(by);
            Common.pauseFor(1);
            if (!element.isEnabled()) {
                Common.reporter().logFail("Element [" + desc + "] jest wyłączony");
            }
            if (scroll) {
                CommonActions.scrollToElement(element);
            }
            if (element.isDisplayed()) {
                element.click();
                Common.reporter().logPass("Kliknięto w element [" + desc + "]");
            }
        } catch (WebDriverException e) {
            Common.handleWebDriverExceptions(e, desc);
        }
    }

    public void enterIntoTextField(String value) {
        CommonActions.enterIntoTextField(this, value);
    }

    public void shouldBeVisible() {
        CommonAsserts.assertTrue(getMsgVisible(desc), getMsgNotVisible(desc), CommonWaits.waitUntilElementVisible(this, MAX_WAITING_TIME) != null);
    }

    private static String getMsgVisible(String desc) {
        return String.format("Element [%s] pojawił się oczekiwanym czasie.", desc);
    }

    private static String getMsgNotVisible(String desc) {
        return String.format("Element [%s] nie pojawił się oczekiwanym czasie.", desc);
    }
}
