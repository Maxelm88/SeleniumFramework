package helpers.reporter;

import lombok.extern.log4j.Log4j;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;

@Log4j
public class ReportManagerFactory {

    public static ReportManager buildReporter(ReporterType type, Object driver) {
        Class<? extends ReportManager> clazz  = type.getReporterClass();
        try{
            if (driver instanceof WebDriver)
                return clazz.getConstructor(WebDriver.class).newInstance((WebDriver)driver);
//            else if (driver instanceof TestObject)
//                return clazz.getConstructor(TestObject.class).newInstance((TestObject)driver);
            else throw new IllegalStateException();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("Failed to instantiate ReportManager");
            throw new RuntimeException(e);
        }
    }

    public static ReportManager buildSkippingReporter(ReporterType type) {
        try {
            return type.reporterClass.getConstructor(WebDriver.class).newInstance((WebDriver) null);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            log.error("Failed to instatiate ReportManager");
            throw new RuntimeException(e);
        }
    }

    public enum ReporterType {
        ALLURE(AllureReportManager.class);

        private Class<? extends ReportManager> reporterClass;

        ReporterType(Class<? extends ReportManager> clazz) {
            this.reporterClass = clazz;
        }
        public Class<? extends ReportManager> getReporterClass() {
            return reporterClass;
        }
    }
}
