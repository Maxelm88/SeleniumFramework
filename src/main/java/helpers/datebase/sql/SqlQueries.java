package helpers.datebase.sql;

import helpers.datebase.dto.CustomTestDTO;
import helpers.datebase.request.CustomDataRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlQueries {

    public static final String SELECT_POSTGRESQL = "SELECT * FROM public.\"daneLogowania\"";

    public static final String SELECT_MYSQL = "SELECT * FROM `daneLogowania`";

    public static String selectLoginMysql(String appName, String login, String env) {
        return String.format("SELECT * FROM `daneLogowania`"
                + "WHERE app = '%s' \n"
                + "AND login = '%s' \n"
                + "AND env = '%s'", appName, login, env);
    }

    public static final String insertDaneWynikowe(CustomTestDTO data) {

        return String.format("INSERT INTO `daneWynikowe` \n"
                        + "(`nazwa_testu`, `nazwa_aplikacji`, `status`, `etap`,"
                        + "`param1`, `param2`, `param3`, `param4`, `param5`, `param6`, `param7`, `param8`, `env`, "
                        + "`param9`, `param10`, `param11`, `param12`) "
                        + "VALUES ('%s', '%s', '%d', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                data.getNazwaTestu(),
                data.getNazwaAplikacji(),
                data.getStatus().getStatusId(),
                data.getEtap(),
                data.getParam1(),
                data.getParam2(),
                data.getParam3(),
                data.getParam4(),
                data.getParam5(),
                data.getParam6(),
                data.getParam7(),
                data.getParam8(),
                data.getEnv(),
                data.getParam9(),
                data.getParam10(),
                data.getParam11(),
                data.getParam12());
    }

    public static final String selectDaneWynikowe(CustomDataRequest req) {
        return String.format("SELECT * FROM `daneWynikowe` \n"
                        + "WHERE nazwa_testu = '%s' \n"
                        + "AND nazwa_aplikacji = '%s' \n"
                        + "AND env = '%s' \n"
                        + "AND status = '%d' \n",
                req.getTestName(),
                req.getAppName(),
                req.getEnv(),
                req.getStatus().getStatusId());
    }

    public static final String updateDaneWynikoweStatus(CustomDataRequest req) {
        return String.format("UPDATE `daneWynikowe` SET status = 0 \n"
                        + "WHERE nazwa_testu = '%s' \n"
                        + "AND nazwa_aplikacji = '%s' \n"
                        + "AND env = '%s' \n"
                        + "AND status = '1' \n"
                        + "LIMIT 1",
                req.getTestName(),
                req.getAppName(),
                req.getEnv());
    }
}
