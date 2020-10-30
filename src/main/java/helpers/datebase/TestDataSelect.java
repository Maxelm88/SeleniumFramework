package helpers.datebase;

import helpers.datebase.request.CustomDataRequest;
import helpers.dictionary.DataRowStatus;
import helpers.dictionary.Profile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestDataSelect {

    public static final String TODAY = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    public static CustomDataRequest getSelectTestData(String applikacja, Profile appEnv, String rst, String stage){
        return CustomDataRequest.builder()
                .appName(applikacja)
                .testName(rst)
                .env(appEnv)
                .stage(stage)
                .status(DataRowStatus.AKTYWNY)
                .build();
    }

    public static CustomDataRequest getSelectTestData(String applikacja, Profile appEnv, String rst){
        return getSelectTestData(applikacja, appEnv, rst, null);
    }

}
