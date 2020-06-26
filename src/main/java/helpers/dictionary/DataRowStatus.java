package helpers.dictionary;

public enum DataRowStatus {
    AKTYWNY(1),
    NIEAKTYWNY(2);

    private int id;

    private DataRowStatus(int id) {
        this.id = id;
    }

    public int getStatusId() {
        return this.id;
    }

    public static DataRowStatus[] statuses = {AKTYWNY, NIEAKTYWNY};
}
