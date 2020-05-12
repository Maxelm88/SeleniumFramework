package store.po;

import helpers.DescribeBy;
import org.openqa.selenium.By;

public class StoreHomepage {
    public static final DescribeBy buttonSignIn = new DescribeBy(By.xpath("//a[@class = 'login']"), "Sign in");
    public static final DescribeBy fieldSearch = new DescribeBy(By.xpath("//input[@name = 'search_query']"), "Search field");
    public static final DescribeBy buttonSearch = new DescribeBy(By.xpath("//button[@name = 'submit_search']"), "Search");
    public static final DescribeBy imageMyStore = new DescribeBy(By.xpath("//img[@alt = 'My Store']"), "My Store image");
}
