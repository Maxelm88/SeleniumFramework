package helpers.dictionary;

public enum BrowserArg {
    HEADLESS ("headless"),
    INCOGNITO ("incognito");

    private String name;

    BrowserArg(String name) {this.name = name;}

    public String getName() {return name;}
}
