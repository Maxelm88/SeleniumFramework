package helpers.datebase;

import helpers.dictionary.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Properties;

@Data
@Builder
@AllArgsConstructor
public class ConnectionParams {

    private static final String PROPERTY_URL = "url";
    private static final String PROPERTY_LOGIN = "login";
    private static final String PROPERTY_PASSWORD = "password";

    private String url;
    private String login;
    private String password;

    public static ConnectionParams getParams(Properties properties, DatabaseName databaseName, Profile profile) {
        return ConnectionParams.builder()
                .url(properties.getProperty(getPropertyString(databaseName, profile, PROPERTY_URL)))
                .login(properties.getProperty(getPropertyString(databaseName, profile, PROPERTY_LOGIN)))
                .password(properties.getProperty(getPropertyString(databaseName, profile, PROPERTY_PASSWORD)))
                .build();
    }

    private static String getPropertyString(DatabaseName databaseName, Profile profile, String propertyName) {
        return String.format("%s.%s.%s", databaseName.getNameInProperty(), profile.name(), propertyName);
    }
}
