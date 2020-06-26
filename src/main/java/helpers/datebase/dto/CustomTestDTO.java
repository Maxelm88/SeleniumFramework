package helpers.datebase.dto;

import helpers.dictionary.DataRowStatus;
import helpers.reporter.ReportManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomTestDTO {

    public static final int PARAM_AMOUNT = 12;

    private int lp;
    private String nazwaTestu;
    private String nazwaAplikacji;
    private String env;
    private Date creationDate;
    private DataRowStatus status;
    private String etap;
    private String param1;
    private String param2;
    private String param3;
    private String param4;
    private String param5;
    private String param6;
    private String param7;
    private String param8;
    private String param9;
    private String param10;
    private String param11;
    private String param12;

    public boolean hasParams() {
        return StringUtils.isNotEmpty(param1) || StringUtils.isNotEmpty(param2)
                || StringUtils.isNotEmpty(param3) || StringUtils.isNotEmpty(param4)
                || StringUtils.isNotEmpty(param5) || StringUtils.isNotEmpty(param6)
                || StringUtils.isNotEmpty(param7) || StringUtils.isNotEmpty(param8)
                || StringUtils.isNotEmpty(param9) || StringUtils.isNotEmpty(param10)
                || StringUtils.isNotEmpty(param11) || StringUtils.isNotEmpty(param12);
    }

    public void reportParams(ReportManager reporter) {
        reporter.logPass(param1);
        reporter.logPass(param2);
        reporter.logPass(param3);
        reporter.logPass(param4);
        reporter.logPass(param5);
        reporter.logPass(param6);
        reporter.logPass(param7);
        reporter.logPass(param8);
        reporter.logPass(param9);
        reporter.logPass(param10);
        reporter.logPass(param11);
        reporter.logPass(param12);
    }
}
