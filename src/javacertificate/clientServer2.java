package javacertificate;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import algoritmarsa.RSA;

public class clientServer2 {
    
    // Deklarasi socket
    private static Socket socket;
    
    public static BigInteger getEa() {
        return ea;
    }
    
    public static void setEa(BigInteger aEa) {
        ea = aEa;
    }
    
    public static BigInteger getNa() {
        return Na;
    }

    public static void setNa(BigInteger aNa) {
        Na = aNa;
    }

    public BigInteger getSertifikat() {
        return sertifikat;
    }

    public void setSertifikat(BigInteger sertifikat) {
        this.sertifikat = sertifikat;
    }

    public BigInteger getsUserLain() {
        return sUserLain;
    }

    public void setsUserLain(BigInteger sUserLain) {
        this.sUserLain = sUserLain;
    }
    
    // Sertifikat milik user 
    private BigInteger sertifikat;  
    
    // Sertifikat user lain 
    private BigInteger sUserLain;    
    private static String pesan;
    
    // Deklarasi I/O stream
    private static BufferedReader in;
    private static PrintWriter out;
    private static clientServer2 cl= new clientServer2();
    private  static  BigInteger ea;
    private static BigInteger Na;
    
    // Deklarasi boolean untuk status, diletakkan dalam scope private static agar dapat diakses keseluruhan class
    private static boolean active;
    
    public static void main(String[] args) throws IOException {
        // Inisialisasi socket dan I/O
        socket = null;
        out = null;
        in = null;
        BigInteger p = RSA.getRandomPrime(64);
        BigInteger q = RSA.getRandomPrime(64);
        BigInteger N = RSA.getModulus(p, q);
        BigInteger phi = RSA.getPhi(p, q);
        BigInteger e = RSA.getPublicKey(phi);
        BigInteger d = RSA.getPrivateKey(e, phi);

        try{
            // Mengkoneksikan client dengan socket yang sudah ada di port 4444 untuk host: localhost
            socket = new Socket("localhost",4441);
            // Menghubungkan I/O stream dengan socket
            out = new PrintWriter(socket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException er) {
            System.err.println("Don't know about host: localhost.");
            System.exit(1);
        } catch (IOException er) {
            System.err.println("Couldn't get I/O for " + "the connection to: localhost.");
            System.exit(1);
        }
        
        // Deklarasi input stream untuk membaca input dari console
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromUser;
        
         // Inisialisasi awal adalah true dimana setiap client terhubung berarti client aktif
        active = true;
        
        // Class threads diinstansiasi kemudian dijalankan agar setiap thread yg terhubung dpt merima pesan dari server
        new clientServer2.threads().start();
        
        // Generate key untuk user
        // Fungsi generate
        // Menyimpan public key+ private key si user
       
        // Selama thread masih aktif akan meminta input dari console
        while(active){
            fromUser = stdIn.readLine();
            if (fromUser != null) {
                // Untuk meminta sertifikat ke authority
                if(fromUser.startsWith("certificate")) {
                    fromUser= fromUser+" "+e+" "+N;
                }
                // Untuk decript sertifikat user lain
                else  if(fromUser.startsWith("decrypt")) {
                    BigInteger decripted;
                    decripted = RSA.decrypt(getEa(), getNa(), cl.getsUserLain().toByteArray());
                    System.out.println("Decrypted ciphertext in bytes: " + RSA.convert(decripted.toByteArray()));
                    System.out.println("Decrypted ciphertext: " + new String(decripted.toByteArray()));
                }
                // Untuk share ke user lain
                else if(fromUser.startsWith("@")) { 
                    fromUser= fromUser+" "+cl.getSertifikat();
                }
                // Inputan dari console nantinya akan ditampilkan dari PrintWriter stream pada thread
                out.println(fromUser);
            }
        }
 
        // Socket dan I/O stream ditutup
        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }
    
    private static class threads extends Thread {    
        // Tidak membutuhkan constructor, hanya berjalan menjalan fungsi run setiap diinstansiasi di fungsi utama
        public void run(){
            try {
                setEa(new BigInteger("54447217166781385718247467566894095932837289308213424624718261949989598047549876961018771538154391733650094798060665994609531231316803289934209593756273781327670418497355458010526000682649498198462157628479756341648048203701867022130558473160490618357327513323159062519998359675790884233498116384351900927691"));
                setNa(new BigInteger("71386351396446705719480013032150036889720001537435378952408387889986361884565394237780167127802424717452346513012873192932496503282030980135963689591558974980607441276176867294175126719259657521909854254658617869706076412873596982114825748401018960929238041787923302735140851969855208590511588134030915497081"));
               
                String fromServer;
                // Membaca input dari server
                while ((fromServer = in.readLine()) != null) {
                    // Cek apabila input dari server adalah "<Server> Exit." maka berhenti membaca dan keluar
                    if (fromServer.equals("Server : You quit.")){
                        break;
                    }
                    // Pesan dari server akan ditampilkan pada console
                    else if(fromServer.startsWith("certificate")) {
                        String[] words = fromServer.split("\\s", 2);
                        if (words.length > 1 && words[1] != null) {
                            words[1] = words[1].trim();
                            if (!words[1].isEmpty()) {
                                cl.setSertifikat(new BigInteger(words[1]));
                                System.out.println(words[1]);
                                System.out.println(new BigInteger(words[1]));
                            }
                        }
                    }
                    else if(fromServer.startsWith("Share")) {
                        String[] words = fromServer.split("\\s", 2);
                        if (words.length > 1 && words[1] != null) {
                            words[1] = words[1].trim();
                            if (!words[1].isEmpty()) {
                                cl.setsUserLain(new BigInteger(words[1]));
                                //System.out.println(new BigInteger(words[1]));
                                System.out.println("\nServer : Someone sent you a certificate.");
                            }
                        }
                    }
                    else {
                        System.out.println(fromServer);
                    }
                }
                // Ketika sudah berhenti membaca maka status thread diubah jadi false sehingga tidak dapat lagi membaca input dari user pada console
                active = false;
            } catch(IOException e){
            }
        }
    }
}
