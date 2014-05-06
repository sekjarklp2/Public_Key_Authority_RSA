package rsaexample;

import java.math.BigInteger;

public class RSAExample {
    public static void main(String[] args) {
        BigInteger p = RSA.getRandomPrime(512);
        BigInteger q = RSA.getRandomPrime(512);
        BigInteger N = RSA.getModulus(p, q);
        BigInteger phi = RSA.getPhi(p, q);
        BigInteger e = RSA.getPublicKey(phi);
        BigInteger d = RSA.getPrivateKey(e, phi);
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
