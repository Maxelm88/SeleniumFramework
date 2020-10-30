package helpers.skip;

import lombok.AllArgsConstructor;
import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


@AllArgsConstructor
public class RunTest implements TestRule {

    private SkipChecker checker;

    @Override
    public Statement apply(Statement base, Description description){
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                if(!checker.isSkip()){
                    throw new AssumptionViolatedException("Skipping text!");
                } else {
                    base.evaluate();
                }
            }
        };
    }
}
