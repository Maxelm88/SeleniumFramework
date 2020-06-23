package helpers.dictionary;

import java.util.Arrays;

public enum  Profile {
    LOCAL,
    CP,
    UT,
    DRT,
    DEV;

    public static Profile getProfileFromProperty(){
        return Profile.getEnum(System.getProperty(PropertyNames.APP_ENV.getDescription()));
    }

    public static Profile getEnum(String profile){
        return Arrays.stream(values())
                .filter(element -> element.name().equals(profile))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not find profile"));
    }
}
