package helpers;

import org.junit.Test;

public class Aes {


    @Test
    public void encryptAny() {
        System.out.println(Common.encryptAES("user123"));
    }

    @Test
    public void decryptAny() {
        System.out.println(Common.decryptAES("811B7057098AADC30298A5D52FE3E406\n" +
                "70559A073D99CC7F75ACDA0E6305BA4F"));
    }
}
