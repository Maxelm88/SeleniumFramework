package helpers.reporter;

import helpers.throwables.GeneralStepException;
import helpers.throwables.NoTestDataException;
import helpers.throwables.TestPendingException;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StatusDetails;
import io.qameta.allure.model.StepResult;
import lombok.extern.log4j.Log4j;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static helpers.Common.driver;
import static helpers.Common.getAlertText;

@Log4j
public class AllureReportManager  extends ReportManager {

    private List<String> warnStack = new ArrayList<>();

    private String text;
    private String details;
    private boolean screen;

    public AllureReportManager(WebDriver driver) {
        super(driver);
    }

    @Override
    public void logPass(String text, boolean takeScreenshot) {
        this.text = text;
        this.screen = takeScreenshot;
        addStepToReport("pass", null);
    }

    @Override
    public void logFail(String text, Throwable t) {
        this.text = text;
        addStepToReport("fail", null);
    }

    public void logFail(String text, Throwable t, boolean setAsSkipped) {
        if(setAsSkipped) skipTest(text);
        else logFail(text, t);
    }

    @Override
    public void logWarn(String text, boolean takeScreenshot) {
        this.text = text;
        this.screen = takeScreenshot;
        warnStack.add(text + getExecStack(Thread.currentThread().getStackTrace()));
        addStepToReport("warn", null);
        setReportStatus(Status.BROKEN);
    }

    @Override
    public void logError(String text, Throwable t) {
        if(t instanceof TestPendingException) return;
        this.text = text;
        addStepToReport("error", t);
    }

    public void logError(String text, Throwable t, boolean setAsSkipped){
        if(setAsSkipped) skipTest(text);
        else logError(text, t);
    }

    @Override
    public void logSkip(String text, String details){
        this.text = text;
        this.details = details;
        addStepToReport("skip", null);
    }

    @Override
    public void skipTest(String text){
        logSkip(text);
        try{
            closeReportWithStatus(Status.SKIPPED, text, null);
        } catch (NullPointerException e) {
            log.warn("Can't manually set description and status: null uuid");
        }
    }

    @Override
    public byte[] saveScreenshot(){
        if(manager == null) {
            log.error("Failed to take screenshot: driver is null");
            return null;
        }
        if(driver instanceof RemoteWebDriver && ((RemoteWebDriver)driver).getSessionId() == null){
            log.error("Failed to take screenshot: driver has no active session");
            return null;
        }
        else try {
            return manager.takeScreenshot();
        } catch (IOException | WebDriverException e) {
            log.error("Failed to take screenshot: ", e);
            return null;
        }
    }

    private void addStepToReport(String type, Throwable t) {
        try {
            switch (type) {
                case "pass":
                    runStepWithDescription(AllureReportManager.class.getDeclaredMethod("reportPassStep")); break;
                case "warn":
                    runStepWithDescription(AllureReportManager.class.getDeclaredMethod("reportWarnStep")); break;
                case "fail":
                    runStepWithDescription(AllureReportManager.class.getDeclaredMethod("reportFailStep", Throwable.class), t); break;
                case "error":
                    runStepWithDescription(AllureReportManager.class.getDeclaredMethod("reportErrorStep", Throwable.class), t); break;
                case "skipp":
                    runStepWithDescription(AllureReportManager.class.getDeclaredMethod("reportSkippedStep")); break;
                default:
                    throw new IllegalStateException("Illegal reporting method type: " + type);
            }
        } catch (NoSuchMethodException e) {
            log.error("Failed to add step to report", e);
        }
    }

    private void runStepWithDescription(@Nonnull Method m, Object... args) {
        try {
            m.invoke(this, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Throwable c = e.getCause();
            if(c == e || !(c instanceof AssertionError || c instanceof GeneralStepException)) {
                throw new AssertionError("Nie udało się zrobić zrzutu ekranu.", e);
            }
            else if (c instanceof AssertionError) throw (AssertionError) c;
            else {
                if(c.getCause() == c || c.getCause() == null) throw (GeneralStepException) c;
                else if (c.getCause() instanceof AssertionError) throw (AssertionError) c.getCause();
                else if (RuntimeException.class.isAssignableFrom(c.getCause().getClass())) {
                    throw (RuntimeException) c.getCause();
                } else throw new RuntimeException(c.getCause());
            }
        }
    }

    public void closeReportWithStatus(Status s, String text, String trace) {
        if(Allure.getLifecycle().getCurrentTestCase().isPresent()) Allure.getLifecycle().updateTestCase(exec -> {
            exec.setStatus(s);
            exec.setStatusDetails(new StatusDetails().setMessage((text != null && !"".equals(text)) ? text : getDefaultMessage(s))
            .setTrace((trace == null || "".equals(trace)) ? text : trace));
        });
    }

    public void setReportStatus(Status s){
        try{
            if(Allure.getLifecycle().getCurrentTestCase().isPresent()) Allure.getLifecycle().updateTestCase(exec -> {
                exec.setStatus(s);
                if(s == Status.BROKEN) exec.setStatusDetails(new StatusDetails()
                .setMessage(text.contains("Bbrak danych testowych") || text.matches("^w trakcie budowania : Status \\w+$") ? text :
                        "Test przeszedł pomyślnie, ale wystąpiły ostrzeżenia. Sprawdź log testu.")
                .setTrace(text.contains("Brak danych testowych") ? text :
                        printWarnStack()));
            });
        } catch (NullPointerException e) {
            log.warn("Can't manually set status: null uuid");
        }
    }

    private String getDefaultMessage(Status s) {
        switch(s) {
            case PASSED: return "Test passed";
            case FAILED: return "Test failed";
            case BROKEN: return "Test broken";
            case SKIPPED: return "Test skipped";
            default: return "Unable to determine test outcome";
        }
    }

    private String printWarnStack(){
        StringBuilder warns = new StringBuilder();

        for(String warnEntry: warnStack) {
            warns.append(warnEntry).append("\n");
        }
        return warns.toString();
    }

    private String getExecStack(StackTraceElement[] stackTrace) {
        StringBuilder stackString = new StringBuilder("\n");

        for(StackTraceElement elem : stackTrace) {
            if (elem.getClassName().contains("ReportManager") || elem.getClassName().contains("Thread")) continue;

            stackString.append("\t");
            stackString.append("at ").append(elem.getClassName()).append(".").append(elem.getMethodName()).append("(");
            if(elem.isNativeMethod()) stackString.append("Native Method");
            else if(elem.getFileName() == null) stackString.append("Unknown Source");
            else if(elem.getLineNumber() < 0) stackString.append(elem.getFileName()).append(")");
            else stackString.append(elem.getFileName()).append(":").append(elem.getLineNumber()).append(")");
            stackString.append("\n");

            if(elem.getMethodName().contains("testMethod")) break;
        }
        return stackString.append("\n\n").toString();
    }

    private void reportPassStep(){
        String uuid = UUID.randomUUID().toString();
        StepResult sr = new StepResult();
        sr.setStatus(Status.PASSED);

        log.info(text);
        byte[] scr = null;
        if(screen) scr = saveScreenshot();

        if(Allure.getLifecycle().getCurrentTestCase().isPresent()) {
            sr.setName(text);
            Allure.getLifecycle().startStep(uuid, sr);
            if(scr != null && manager != null) {
                Allure.getLifecycle().addAttachment("screenshot", "image/png", null, scr);
            }
            Allure.getLifecycle().stopStep(uuid);
        }
    }

    private void reportFailStep(Throwable t) {
        String uuid = UUID.randomUUID().toString();
        StepResult sr = new StepResult();
        sr.setStatus(Status.FAILED);

        if(text == null || text.equals("")) {
            log.error(getStackTrace(t));
            sr.setName(t.getMessage());
        } else {
            log.error(text);
            sr.setName(text);
        }

        String alertText = (t == null || t instanceof TimeoutException) ? getAlertText(false) : null;

        if(t != null || (alertText != null && !alertText.equals(text))) {
            Parameter p;
            List<Parameter> params = new ArrayList<>();
            if(t != null) {
                p = new Parameter();
                p.setName("Thrown");
                p.setValue(t.toString());
                params.add(p);
            }

            sr.setParameters(params);
        }

        byte[] scr = saveScreenshot();

        if(Allure.getLifecycle().getCurrentTestCase().isPresent()) {
            Allure.getLifecycle().startStep(uuid, sr);
            if(manager != null && !(t instanceof NoTestDataException) && scr != null)
                Allure.getLifecycle().addAttachment("Screenshot", "image/png", null, scr);
            Allure.getLifecycle().stopStep(uuid);

            try{
                closeReportWithStatus(Status.FAILED, text, t!= null ? getStackTrace(t) + "\n\nOstrzeżenie:\n\n" + printWarnStack() : text + "\n\nOstrzeżenia:\n\n" + printWarnStack());
            } catch (NullPointerException e) {
                log.warn("Can't manually set description and status: null uuid");
            }

            getBrowserLogs(driver);
            //getNetworkLogs(driver);
            throw new AssertionError(text, t);
        }
    }

    private String getStackTrace(Throwable t) {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PrintWriter printWriter = new PrintWriter(baos);
            t.printStackTrace(printWriter);
            printWriter.flush();
            return baos.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to get stack trace", e);
        }
    }
}
