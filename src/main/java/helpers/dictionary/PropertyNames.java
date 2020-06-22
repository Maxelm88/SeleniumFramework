package helpers.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropertyNames {
    APP_ENV("appEnv"),
    ENV("env"),
    SKIP("skip");

    private String description;

    public static String getSkip() {
        return System.getProperty(PropertyNames.SKIP.getDescription());
    }

    public static Profile getProfile() {
        return Profile.getEnum(System.getProperty(PropertyNames.APP_ENV.getDescription()));
    }

    public static PropertyEnv getEnv() {
        return PropertyEnv.getEnum(System.getProperty(PropertyNames.ENV.getDescription()));
    }
}
