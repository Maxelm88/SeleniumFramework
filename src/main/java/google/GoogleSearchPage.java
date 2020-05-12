package google;

import helpers.DescribeBy;
import org.openqa.selenium.By;

public class GoogleSearchPage {
    public static final DescribeBy poleWyszukania = new DescribeBy(By.name("q"), "Pole szukania");
    public static final DescribeBy przyciskWyszukaj = new DescribeBy(By.name("btnK"), "Wyszukaj");
    public static final DescribeBy next = new DescribeBy(By.xpath("//span[contains(text(), 'Następna')]"), "next");
}


