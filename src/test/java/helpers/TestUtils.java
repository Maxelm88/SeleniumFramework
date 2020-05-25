package helpers;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {

    public static boolean isSkip(String skip){
        return skip != null && !skip.equals("false");
    }
}
