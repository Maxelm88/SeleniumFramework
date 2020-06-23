package helpers.datebase.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlQueries {

    public static final String SELECT_POSTGRESQL = "SELECT * FROM public.\"daneLogowania\"";

    public static final String SELECT_MYSQL = "SELECT * FROM \"daneLogowania\" ";

    public static String selectLogin(String appName, String login, String env) {
        return String.format("SELECT * FROM public.\"daneLogowania\" \n"
                + "WHERE app = '%s' \n"
                + "AND login = '%s' \n"
                + "AND env = '%s'", appName, login, env);
    }
}
