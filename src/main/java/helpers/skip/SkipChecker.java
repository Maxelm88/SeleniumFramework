package helpers.skip;

import helpers.Common;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SkipChecker {

    private String value;

    public boolean isSkip() {
        return !Common.checkSkip(value);
    }
}
