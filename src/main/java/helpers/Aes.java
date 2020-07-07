package helpers;

import org.junit.Test;

public class Aes {


    @Test
    public void encryptAny() {
        System.out.println(Common.encryptAES("test"));
    }

    @Test
    public void decryptAny() {
        System.out.println(Common.decryptAES("test"));
    }
}
