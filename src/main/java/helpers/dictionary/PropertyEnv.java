package helpers.dictionary;

import java.util.Arrays;

public enum PropertyEnv {
    LOCAL,
    REMOTE;

    public static PropertyEnv getEnum(String env) {
        return Arrays.stream(values())
                .filter(element -> element.name().equalsIgnoreCase(env))
                .findFirst()
                .orElse(PropertyEnv.LOCAL);
    }
}
