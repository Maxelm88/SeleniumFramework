package helpers.datebase;

import helpers.Common;
import helpers.datebase.sql.SqlQueries;
import helpers.dictionary.Profile;
import lombok.extern.log4j.Log4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class TestDataManager {

    private DataSelector ds;

    public TestDataManager() {
        ds = new DataSelector();
    }

    public DataSelector getDataSelector() {
        return ds;
    }

    public class DataSelector {
        DataSelector() {
        }

        public List<String> getLoginCredentials(String appName, String login, Profile env) {

            try {
                ResultSet result = Connections.executeSelect(Connections.getLoginWeb(env), SqlQueries.selectLogin(appName, login, env.name()));
                result.next();
                List<String> out = new ArrayList<>();
                out.add(result.getString("url"));
                out.add(result.getString("login"));
                out.add(result.getString("pass"));
                Common.reporter().logPass("Pobrano dane z bazy");
                return out;
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

}
