package helpers.datebase;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DatabaseName {

    LOGIN_WEB_MYSQL("loginWeb", "loginWebMysql"),
    STORE_POSTGRESQL("store", "storePostgresql"),
    STORE_MYSQL("store", "storeMysql");

    private String description;
    private String nameInProperty;
}
