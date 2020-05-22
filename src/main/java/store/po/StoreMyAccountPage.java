package store.po;

import helpers.DescribeBy;
import org.openqa.selenium.By;

public class StoreMyAccountPage {
    public static final DescribeBy titleMyAccount = DescribeBy.builder()
            .by(By.xpath("//h1[text() =  'My account']"))
            .desc("My Account").build();
    public static final DescribeBy orderHistoryAndDetailsButton = DescribeBy.builder()
            .by(By.xpath("//span[text() =  'Order history and details']"))
            .desc("Order history and details")
            .build();
    public static final DescribeBy myCreditSlipsButton = DescribeBy.builder()
            .by(By.xpath("//span[text() =  'My credit slips']"))
            .desc("My credit slips")
            .build();
    public static final DescribeBy myAccountButton = DescribeBy.builder()
            .by(By.xpath("//span[text() =  'My addresses']"))
            .desc("My addresses")
            .build();
    public static final DescribeBy myPersonalInformationButton = DescribeBy.builder()
            .by(By.xpath("//span[text() =  'My personal information']"))
            .desc("My personal information")
            .build();
    public static final DescribeBy myWishlistsButton = DescribeBy.builder()
            .by(By.xpath("//span[text() =  'My wishlists']"))
            .desc("My wishlists")
            .build();
}
