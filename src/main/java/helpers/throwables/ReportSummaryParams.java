package helpers.throwables;

import helpers.dictionary.ApplicationName;
import helpers.dictionary.DataRowStatus;
import helpers.dictionary.Profile;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Builder
@Data
public class ReportSummaryParams {

    private String testName;
    private ApplicationName appName;
    private Profile env;
    private DataRowStatus status;
    private String stage;
    private boolean passed;
    private boolean finalReport;
    @Singular
    private List<String> params;

    public String[] getVarArgs() {
        if (params != null) {
            return params.stream().toArray(String[]::new);
        }
        return null;
    }
}
