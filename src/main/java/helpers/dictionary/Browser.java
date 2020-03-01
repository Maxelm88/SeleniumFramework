package helpers.dictionary;

public enum Browser {

    CHROME ("chrome"),
    FIREFOX ("firefox");

    private String name;

    Browser(String name) {this.name = name;}

    @Override
    public String toString() {return name;}
}
