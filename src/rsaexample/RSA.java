package rsaexample;

import java.math.BigInteger;

public class RSA {
    public static String convert(byte[] ar) {
        String hasil = "";
        for(byte b : ar) 
            hasil += Byte.toString(b);
        return hasil;
    }
    public static BigInteger encrypt(BigInteger e, BigInteger n, byte[] msg) {        
        return (new BigInteger(msg).modPow(e, n));
    }
    
    public static BigInteger decrypt(BigInteger d, BigInteger n, byte[] cipher) {
        return(new BigInteger(cipher).modPow(d, n));
    }
}
