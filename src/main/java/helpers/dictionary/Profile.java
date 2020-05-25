package helpers.dictionary;

import java.util.Arrays;

public enum  Profile {
    LCOAL,
    CP,
    UT,
    DRT,
    DEV;

    public static Profile getEnum(String profile){
        return Arrays.stream(values())
                .filter(element -> element.name().equals(profile))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not find profile"));
    }
}
