package helpers.reporter.screenshot;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public interface ScreenshotManager {
    byte[] takeScreenshot() throws IOException;

    String takeScreenshotWithSource() throws IOException;

    default String saveImage(BufferedImage buffImg) throws IOException {
        Logger log = Logger.getLogger(this.getClass());

        String filename = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date()) + UUID.randomUUID() + ".png";
        String dest = ".\\target\\screenshots\\" + filename;

        File screen = new File(dest);
        if(!screen.exists()) {
            File par = new File(screen.getParent());
            if(!par.exists() && !par.mkdirs()) {
                log.warn("Failed to create screenshot directory");
            }
            else if (!screen.createNewFile()) {
                log.warn("Failed to save screenshots file");
            }
            ImageIO.write(buffImg, "PNG", screen);
            log.info("Zrzut ekranu umieszczono w pliku " + dest);
        }

        return dest;
    }

}
