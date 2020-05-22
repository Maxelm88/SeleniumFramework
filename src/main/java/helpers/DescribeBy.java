package helpers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.openqa.selenium.By;

@Builder
@AllArgsConstructor
public class DescribeBy {

    public final By by;
    public final String desc;

}
