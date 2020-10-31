package helpers.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ApplicationName {
    STORE(Names.STORE),
    GOOGLE(Names.GOOGLE);

    private String description;

    public static ApplicationName getEnum(String applicationName) {
        return Arrays.stream(values())
                .filter(element -> element.description.equalsIgnoreCase(applicationName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("getEnum - not  find enum"));
    }

    @UtilityClass
    public static class Names {
        public static final String STORE = "Store";
        public static final String GOOGLE = "Google";

    }
}
