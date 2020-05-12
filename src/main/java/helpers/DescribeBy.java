package helpers;

import org.openqa.selenium.By;

public class DescribeBy {

    public final By by;
    public final String desc;

    public DescribeBy(By by, String desc) {
        this.by = by;
        this.desc = desc;
    }

}
