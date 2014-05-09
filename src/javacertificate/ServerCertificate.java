package javacertificate;

import java.net.*;
import java.io.*;
import java.util.*;
import algoritmarsa.RSA;
import java.math.BigInteger;

public class ServerCertificate {

    public static BigInteger getDa() {
        return da;
    }

    public static void setDa(BigInteger aDa) {
        da = aDa;
    }

    public static BigInteger getNa() {
        return Na;
    }

    public static void setNa(BigInteger aNa) {
        Na = aNa;
    }
    
    // Inisialisasi arraylist
    private ArrayList<ServerCertificate.ServerCertificateThread> threadClient; 
    private  static  BigInteger da;
    private static BigInteger Na;
    
    public ServerCertificate() {
        // Membuat objek threadClient
        threadClient = new ArrayList<ServerCertificate.ServerCertificateThread>(); 
    }

    public void StartServer()
    {
        ServerSocket serverSocket = null;       
        try {
            // Menginisialisasi port socket
            serverSocket = new ServerSocket(4441); 
            
        } catch (IOException er) {
            System.err.println("Could not listen on port: 4441.");
            System.exit(-1);
        }

        while (true)
        {
            try {
                setDa(new BigInteger("1005441568964038108725070606086620237883380303344160266935329406901216364571343580813805170814118657992286570605815115393415443708197619438534699853402238841418639965991853528206896413232025162350318403781003325244830937094964054555775571178863520026003857053541904440901378159897233855955051288474729938883"));
                setNa(new BigInteger("71386351396446705719480013032150036889720001537435378952408387889986361884565394237780167127802424717452346513012873192932496503282030980135963689591558974980607441276176867294175126719259657521909854254658617869706076412873596982114825748401018960929238041787923302735140851969855208590511588134030915497081"));
                //membuat object thread client
                ServerCertificate.ServerCertificateThread client; 
                client = new ServerCertificate.ServerCertificateThread(serverSocket.accept(), this);
                client.setDaemon(true);
                threadClient.add(client);
                //menjalankan thread
                threadClient.get(threadClient.size()-1).start(); 
            }
            catch (Exception er){
            }
        }
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println("Waiting for connections.");
        // Membuat objek baru KKMMultiserver,menjalangkan fungsi StartServer
        new ServerCertificate().StartServer();
    }
    
    // Fungsi ini dipanggil berkali2 oleh karena itu dibuat fungsi sendiri
    void broadcast( String msg) { 
        // Untuk semua thread pada threadClient
        for (ServerCertificate.ServerCertificateThread t : threadClient) {  
            // Pesan akan dikirim ke semua thread kecuali thread pengirim
            t.out.println(msg);
        }
    }

    class ServerCertificateThread extends Thread {
        private Socket socket = null;
        private ServerCertificate server;
        // Untuk membedakan klien satu dan yang lain
        private String name;					
        private String alias;
        private String nilaie;
        private String nilaiN;
        private String pesan;
        private PrintWriter out;  
        int count;

        public ServerCertificateThread(Socket socket, ServerCertificate server) {
            // Membuat objek thread baru
            super("ServerCertificateThread");
            this.socket = socket;
            this.server = server;
        }

        public void run() {
            try {
                // Input output data
                out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //BigInteger cipher = RSA.encrypt(e, N, pesan.getBytes());
                //out.println(new String (cipher.toByteArray()));

                // Identifikasi thread memasukkan nama client
                out.println("Enter your ID : ");
                name = in.readLine();
                alias = "@" + name;
                System.out.println(name + " is connected.");
                out.println("\nServer : Welcome, " + name + ".");
                out.println("\nList of commands\n1. certificate : Generate certificate.\n2. @<user> : Send the certificate to specified user.\n3. decrypt : Decrypt the received certificate.\n4. <chat> : Type message to chat.\n");

                // Pesan ini akan dibroadcast pada seluruh thread kecuali thread pengirim
                server.broadcast("Server : " + name + " entered the room.");

                this.out.println("\nList of users");
                   if(threadClient.size()!=1){
                       // Untuk semua thread pada threadClient
                       for (ServerCertificate.ServerCertificateThread t : threadClient) {
                           if(t!=this) {
                               // Pesan akan dikirim ke semua thread kecuali thread pengirim  
                               count++;
                               this.out.println( count + ". " + t.name);
                           }
                       }
                   }
                   else {
                       this.out.println("Server : You are alone.");
                   }

                   String line;
                   // Selama input tidak Bye akan terus 
                   while ((line = in.readLine()) != null && !line.equals("Bye")) {
                       // Minta public key seseorang
                       if(line.startsWith("certificate")){
                           String[] words = line.split("\\s", 3);
                           if (words.length > 1 && words[1] != null) {
                               words[1] = words[1].trim();
                               words[2]= words[2].trim();
                               BigInteger ku1 = new BigInteger(words[1]);
                               BigInteger ku2 = new BigInteger(words[2]);

                               if (!words[1].isEmpty()) {
                                 // Cari public key yang diminta
                                 // Enkrip pesan menggunakan private key si authority
                                   Date date= new Date();
                                   String msg=name+"|"+date.toString()+"|"+ku1+" "+ku2;
                                   BigInteger cipher = RSA.encrypt(getDa(), getNa(), msg.getBytes());
                                   this.out.println("certificate "+cipher);
                                   //this.out.println(ku1);
                                   //this.out.println("Plaintext in bytes: " + RSA.convert(msg.getBytes()));
                                   //BigInteger decripted = RSA.decrypt(new BigInteger("54447217166781385718247467566894095932837289308213424624718261949989598047549876961018771538154391733650094798060665994609531231316803289934209593756273781327670418497355458010526000682649498198462157628479756341648048203701867022130558473160490618357327513323159062519998359675790884233498116384351900927691"), new BigInteger("71386351396446705719480013032150036889720001537435378952408387889986361884565394237780167127802424717452346513012873192932496503282030980135963689591558974980607441276176867294175126719259657521909854254658617869706076412873596982114825748401018960929238041787923302735140851969855208590511588134030915497081"), cipher.toByteArray());
                                   //this.out.println("Decripted ciphertext in bytes: " + RSA.convert(decripted.toByteArray()));
                                   //this.out.println("Decripted ciphertext: " + new String(decripted.toByteArray()))
                               }
                           }
                       }
                       else  if(line.startsWith("@")) {
                           String[] words = line.split("\\s", 2);
                           if (words.length > 1 && words[1] != null) {
                               words[1] = words[1].trim();
                               if (!words[1].isEmpty()) {
                                   for (ServerCertificate.ServerCertificateThread t : threadClient) {
                                       if(t.alias.equals(words[0])){
                                           String msg = "Share " + words[1];
                                           //System.out.println(msg);
                                           t.out.println(msg);
                                           this.out.println("\nServer : " + words[0] + " has received your message.");
                                       }
                                   }
                               }
                           }
                       }
                       else {
                           String msg = name + " : " + line;
                           System.out.println(msg);
                           server.broadcast( msg);
                       }
                   }
                   System.out.println("Server : " + name + " has left the room.");
                   server.broadcast( "\nServer : " + name + " has left the room.");
                   threadClient.remove(this);
                   out.println("\nServer : You quit.");
                   out.close();
                   in.close();
                   socket.close();
            } catch (IOException e) {
                //e.printStackTrace();
                threadClient.remove(this);
            }
        }
    }
}
