package helpers;

import static helpers.Common.reporter;

public class CommonAsserts {
    public static void assertTrue(String msgPass, String msgFail, boolean conditions)
    {
        if(conditions)
        {
            reporter().logPass(msgPass);
        } else {
            reporter().logFail(msgFail);
        }
    }
}
