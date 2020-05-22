package store.po;

import helpers.DescribeBy;
import org.openqa.selenium.By;

public class StoreSignInPage {
    public static final DescribeBy emailAddressField = DescribeBy.builder()
            .by(By.id("email"))
            .desc("Email address")
            .build();
    public static final DescribeBy  passwordField  = DescribeBy.builder()
            .by(By.id("passwd"))
            .desc("Password").build();
    public static final DescribeBy submitButton = DescribeBy.builder()
            .by(By.id("SubmitLogin"))
            .desc("Sign in")
            .build();
}
