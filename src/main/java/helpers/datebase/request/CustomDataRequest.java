package helpers.datebase.request;

import helpers.dictionary.DataRowStatus;
import helpers.dictionary.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomDataRequest implements Cloneable {

    private final String testName;
    private final String appName;
    private final Profile env;
    private final DataRowStatus status;
    private final String stage;
    private String creationDate;

    @Singular("withParam")
    private Map<String, String> requiredParams;

}
