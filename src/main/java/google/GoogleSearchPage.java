package google;

import helpers.DescribeBy;
import org.openqa.selenium.By;

public class GoogleSearchPage {
    public static final DescribeBy poleWyszukania = DescribeBy.builder()
            .by(By.name("q"))
            .desc("Pole szukania")
            .build();
    public static final DescribeBy przyciskWyszukaj = DescribeBy.builder()
            .by(By.name("btnK"))
            .desc("Wyszukaj")
            .build();
    public static final DescribeBy next = DescribeBy.builder()
            .by(By.xpath("//span[contains(text(), 'Następna')]"))
            .desc("next")
            .build();

}
