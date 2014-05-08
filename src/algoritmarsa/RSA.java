package algoritmarsa;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA {
    /**
    * Menghitung sebuah nDigit BigInteger prima 
    *
    * @param nDigit banyaknya digit dari bilangan yang akan dibuat
    * @return            nDigit BigInteger Prima
    */
    public static BigInteger getRandomPrime(int nDigit) {
        return BigInteger.probablePrime(nDigit, new SecureRandom());
    }
    /**
    * Menghitung nilai phi yaitu Euler's Totient Function dari p dan q
    *
    * @param p BigInteger prima p
    * @param q BigInteger prima q
    * @return            Hasil perkalian dari (p-1) dan (q-1)
    */
    public static BigInteger getPhi(BigInteger p, BigInteger q) {
        return p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    }
    /**
    * Menghitung nilai modulus yang akan digunakan untuk enkripsi/dekripsi
    *
    * @param p BigInteger prima p
    * @param q BigInteger prima q
    * @return            Hasil perkalian dari p dan q
    */
    public static BigInteger getModulus(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }
    /**
    * Menghitung nilai dari eksponen e yang disertakan dalam public key untuk enkripsi
    *
    * @param phi BigInteger phi
    * @return            Sebuah BigInteger prima yang &lt phi dan gcd(e,phi) adalah 1
    */
    public static BigInteger getPublicKey(BigInteger phi) {
        BigInteger e = phi.divide(BigInteger.valueOf((int)(Math.random()*100)));
        while(e.gcd(phi).compareTo(BigInteger.ONE) == 1) {
            e = e.nextProbablePrime();
        }
        return e;
    }
    /**
    * Menghitung nilai dari eksponen d yang disertakan dalam private key untuk dekripsi
    *
    * @param publicKey BigInteger e yang menyatakan eksponen public key
    * @param phi BigInteger phi
    * @return            Sebuah BigInteger hasil dari modulus inverse e dengan phi
    */
    public static BigInteger getPrivateKey(BigInteger publicKey, BigInteger phi) {
        return publicKey.modInverse(phi);
    }
    /**
    * Mengonversikan nilai dari setiap karakter menjadi nilai desimal ascii untuk setiap bytenya
    *
    * @param ar array byte yang merupakan sekumpulan karakter
    * @return            Sebuah String hasil penggabungan semua nilai desimal dari setiap bytenya
    */
    public static String convert(byte[] ar) {
        String hasil = "";
        for(byte b : ar) 
            hasil += Byte.toString(b);
        return hasil;
    }
    /**
    * Melakukan enkripsi pesan
    *
    * @param msg array byte dari String plaintext
    * @param e eksponen public key
    * @param n modulus
    * @return            BigInteger hasil dari operasi enkripsi
    */
    public static BigInteger encrypt(BigInteger e, BigInteger n, byte[] msg) {        
        return (new BigInteger(msg).modPow(e, n));
    }
    /**
    * Melakukan dekripsi pesan
    *
    * @param cipher array byte dari String ciphertext
    * @param d eksponen private key
    * @param n modulus
    * @return            BigInteger hasil dari operasi dekripsi
    */
    public static BigInteger decrypt(BigInteger d, BigInteger n, byte[] cipher) {
        return(new BigInteger(cipher).modPow(d, n));
    }
}
