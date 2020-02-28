package helpers.reporter.screenshot;


import lombok.extern.log4j.Log4j;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.SimpleShootingStrategy;
import ru.yandex.qatools.ashot.shooting.ViewportPastingDecorator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Log4j
public class SeleniumScreenshotManager implements ScreenshotManager {
    private WebDriver driver;

    public SeleniumScreenshotManager(WebDriver driver) {
        this.driver = driver;
    }

    public byte[] takeScreenshot() throws IOException {
        if(driver.toString().contains("null")) {
            log.warn("Failed to take screenshot: driver already quited");
        return null;
        }

        try {
           Alert a = driver.switchTo().alert();
           log.info("Zamknięto alert: " + a.getText());
           a.dismiss();
        } catch (NoAlertPresentException ignored){}

        try {
            String przywroconyURL = driver.getCurrentUrl();
            log.info("Aktualny adres URL: " + przywroconyURL);
            Screenshot screenshot = new AShot().shootingStrategy(new ViewportPastingDecorator(new SimpleShootingStrategy())).takeScreenshot(driver);
            final BufferedImage image = screenshot.getImage();
            saveImage(image);

            byte[] out;
            try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                ImageIO.write(image, "png", baos);
                baos.flush();
                out = baos.toByteArray();
            }
            return out;
        } catch (WebDriverException e) {
            log.error("Failed to take screenshot", e);
            return null;
        }
    }

    @Override
    public String takeScreenshotWithSource() throws IOException {
        String przywroconyURL = driver.getCurrentUrl();
        log.info("Aktualny adres URL: " + przywroconyURL);
        Screenshot screenshot = new AShot().shootingStrategy(new ViewportPastingDecorator(new SimpleShootingStrategy())).takeScreenshot(driver);
        final BufferedImage image = screenshot.getImage();
        return saveImage(image);
    }
}
