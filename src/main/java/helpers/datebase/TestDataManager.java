package helpers.datebase;

import helpers.Common;
import helpers.data.provider.AbstractTestCaseData;
import helpers.datebase.dto.CustomTestDTO;
import helpers.datebase.request.CustomDataRequest;
import helpers.datebase.sql.SqlQueries;
import helpers.dictionary.Profile;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j
public class TestDataManager {

    private DataSelector ds;
    private CustomDataManager cdm;

    public TestDataManager() {
        ds = new DataSelector();
        cdm = new CustomDataManager();
    }

    public DataSelector getDataSelector() {
        return ds;
    }

    public class DataSelector {
        DataSelector() {
        }

        public List<String> getLoginCredentials(String appName, String login, Profile env) {

            try {
                ResultSet result = Connections.executeSelect(Connections.getLoginWeb(env), SqlQueries.selectLoginMysql(appName, login, env.name()));
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

        public List<String> getDataFromDb(CustomDataRequest req) {
            try {
                ResultSet result = Connections.executeSelect(Connections.getDaneWynikoweMysql(req.getEnv()), SqlQueries.selectDaneWynikowe(req));
                result.next();
                List<String> out = new ArrayList<>();
                out.add(result.getString("nazwa_testu"));
                out.add(result.getString("param1"));
                out.add(result.getString("param2"));
                out.add(result.getString("param3"));
                out.add(result.getString("param4"));
                out.add(result.getString("param5"));
                out.add(result.getString("param6"));
                out.add(result.getString("param7"));
                out.add(result.getString("param8"));
                out.add(result.getString("param9"));
                out.add(result.getString("param10"));
                out.add(result.getString("param11"));
                out.add(result.getString("param12"));
                deactiveStatusDataTest(req);
                return out;
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        private void deactiveStatusDataTest(CustomDataRequest req) {
            Connections.executeQuery(Connections.getDaneWynikoweMysql(req.getEnv()), SqlQueries.updateDaneWynikoweStatus(req));
        }

    }

    public CustomDataManager getCustomDataManager() {
        return cdm;
    }

    public class CustomDataManager {
        CustomDataManager() {
        }

        public void sendCustomTestData(CustomTestDTO data, Profile env) {
            Connections.executeQuery(Connections.getDaneWynikoweMysql(env), SqlQueries.insertDaneWynikowe(data));
        }

        public CustomTestDTO getCustomTestDataWithParams(CustomDataRequest req) {
            Common.reporter().logPass("Pobieram dane wejściowe do uruchomienia testu");
            List<String> resp = new TestDataManager().getDataSelector().getDataFromDb(req);
            CustomTestDTO dane = new CustomTestDTO();
            dane.setParam1(resp.get(1));
            dane.setParam2(resp.get(2));
            dane.setParam3(resp.get(3));
            dane.setParam4(resp.get(4));
            dane.setParam5(resp.get(5));
            dane.setParam6(resp.get(6));
            dane.setParam7(resp.get(7));
            dane.setParam8(resp.get(8));
            dane.setParam9(resp.get(9));
            dane.setParam10(resp.get(10));
            dane.setParam11(resp.get(11));
            dane.setParam12(resp.get(12));
            return dane;
        }
    }

}
