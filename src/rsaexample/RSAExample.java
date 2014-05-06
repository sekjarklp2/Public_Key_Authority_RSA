package rsaexample;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSAExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BigInteger p = BigInteger.probablePrime(512, new SecureRandom());
        BigInteger q = BigInteger.probablePrime(512, new SecureRandom());
        BigInteger N = p.multiply(q);
        BigInteger totient = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = totient.divide(BigInteger.valueOf((int)(Math.random()*100)));
        while(e.gcd(totient).compareTo(BigInteger.ONE) == 1) {
            e = e.nextProbablePrime();
        }
        BigInteger d = e.modInverse(totient);
        String msg = "test";
        System.out.println("Plaintext: " + msg);
        System.out.println("Plaintext in bytes: " + RSA.convert(msg.getBytes()));
        // encrypt
        BigInteger cipher = RSA.encrypt(e, N, msg.getBytes());
        System.out.println("Ciphertext in big Integer: " + cipher);
        // decrypt
        BigInteger decripted = RSA.decrypt(d, N, cipher.toByteArray());
        System.out.println("Decripted ciphertext in bytes: " + RSA.convert(decripted.toByteArray()));
        System.out.println("Decripted ciphertext: " + new String(decripted.toByteArray()));
    }    
}
