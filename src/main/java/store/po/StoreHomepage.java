package store.po;

import helpers.DescribeBy;
import org.openqa.selenium.By;

public class StoreHomepage {
    public static final DescribeBy buttonSignIn = DescribeBy.builder()
            .by(By.xpath("//a[@class = 'login']"))
            .desc("Sign in")
            .build();
    public static final DescribeBy fieldSearch = DescribeBy.builder()
            .by(By.xpath("//input[@name = 'search_query']"))
            .desc("Search field")
            .build();
    public static final DescribeBy buttonSearch = DescribeBy.builder()
            .by(By.xpath("//button[@name = 'submit_search']"))
            .desc("Search")
            .build();
    public static final DescribeBy imageMyStore = DescribeBy.builder()
            .by(By.xpath("//img[@alt = 'My Store']"))
            .desc("My Store image")
            .build();
}
