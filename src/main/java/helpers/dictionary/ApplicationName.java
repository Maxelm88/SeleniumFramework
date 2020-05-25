package helpers.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ApplicationName {
    STORE(Names.STORE);
    private String description;

    public static ApplicationName getApplicationName(String applicationName) {
        return Arrays.stream(values())
                .filter(element -> element.description.equalsIgnoreCase(applicationName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("getApplicationName - not  find enum"));
    }

    public static class Names {
        public static final String STORE = "Store";
    }
}
