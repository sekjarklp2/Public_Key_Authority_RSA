/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javacertificate;


/**
 *
 * @author hades
 */
import java.net.*;
import java.io.*;
import java.util.*;
import algoritmarsa.RSA;
import java.math.BigInteger;

public class ServerCertificate {

    /**
     * @return the da
     */
    public static BigInteger getDa() {
        return da;
    }

    /**
     * @param aDa the da to set
     */
    public static void setDa(BigInteger aDa) {
        da = aDa;
    }

    /**
     * @return the Na
     */
    public static BigInteger getNa() {
        return Na;
    }

    /**
     * @param aNa the Na to set
     */
    public static void setNa(BigInteger aNa) {
        Na = aNa;
    }
    private ArrayList<ServerCertificate.ServerCertificateThread> threadClient;//inisialisasi arraylist
  private  static  BigInteger da;
    private static BigInteger Na;
    public ServerCertificate() {
		
		threadClient = new ArrayList<ServerCertificate.ServerCertificateThread>();//membuat objek threadClient
	}

    public void StartServer()
    {
        ServerSocket serverSocket = null;
         
           

        try {
            serverSocket = new ServerSocket(4441);//menginsi port socket
            
        } catch (IOException er) {
            System.err.println("Could not listen on port: 4441.");
            System.exit(-1);
        }

        while (true)
        {
            try {
                setDa(new BigInteger("1005441568964038108725070606086620237883380303344160266935329406901216364571343580813805170814118657992286570605815115393415443708197619438534699853402238841418639965991853528206896413232025162350318403781003325244830937094964054555775571178863520026003857053541904440901378159897233855955051288474729938883"));
                setNa(new BigInteger("71386351396446705719480013032150036889720001537435378952408387889986361884565394237780167127802424717452346513012873192932496503282030980135963689591558974980607441276176867294175126719259657521909854254658617869706076412873596982114825748401018960929238041787923302735140851969855208590511588134030915497081"));
                ServerCertificate.ServerCertificateThread client;//membuat object thread client
                client = new ServerCertificate.ServerCertificateThread(serverSocket.accept(), this);
                client.setDaemon(true);
             
             
              threadClient.add(client);
                
                threadClient.get(threadClient.size()-1).start();//menjalankan thread
            
            }
            catch (Exception er){
                   
        }
        }
    }
    
    public static void main(String[] args) throws Exception {
		System.out.println("waiting for connections");
                
		new ServerCertificate().StartServer();//membuat objek baru KKMMultiserver,menjalangkan fungsi StartServer
                 
           
    }
        
  
  
  
void broadcast( String msg) {//karena fungsi ini dipanggil berkali2 maka saya buat fungsi sendiri
		for (ServerCertificate.ServerCertificateThread t : threadClient) {//untuk semua thread pada threadClient
			
				t.out.println(msg);//pesan akan dikirim ke semua thread kecuali thread pengirim
			
                      
		}
	}
class ServerCertificateThread extends Thread {
    private Socket socket = null;
    private ServerCertificate server;
    private String name;					// untuk membedakan klien satu dan yang lain
    private String alias;
    private String nilaie;
    private String nilaiN;
    private String pesan;
    private PrintWriter out;
    
  
    
   
    public ServerCertificateThread(Socket socket, ServerCertificate server) {
	super("ServerCertificateThread");//membuat objek thread baru
	this.socket = socket;//inisialisasi socket
        this.server = server;//inisialisasi socket
    }
        
    public void run() {

	try {
                                
				//input output data
				out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                             
                                //BigInteger cipher = RSA.encrypt(e, N, pesan.getBytes());
                                
                                //out.println(new String (cipher.toByteArray()));
				// identifikasi thread memasukkan nama client
                                out.println("enter your ID.");
                                
				name = in.readLine();
                                alias = "@" + name;
                                System.out.println("it's "+name);
				out.println("Server: welcome "+name);
                                server.broadcast("Server : " + name + " entered the room");//pesan ini akan dibroadcast pada seluruh thread kecuali thread pengirim
                                
                                 this.out.println("Server Who's online?");
                                   if(threadClient.size()!=1){
                                   for (ServerCertificate.ServerCertificateThread t : threadClient) {//untuk semua thread pada threadClient
						if(t!=this)
                                                {this.out.println(t.name);//pesan akan dikirim ke semua thread kecuali thread pengirim    
                                    
                                                }
                                   }
                                   }
                                   else{this.out.println("Server: You are alone");}
                                   String line;
                                   while ((line = in.readLine()) != null && !line.equals("Bye")) {//selama input tidak Bye akan terus 
                                    if(line.startsWith("certificate")){ // minta public key seseorang
                                        String[] words = line.split("\\s", 3);
                                        if (words.length > 1 && words[1] != null) {
                                        words[1] = words[1].trim();
                                        words[2]= words[2].trim();
                                        BigInteger ku1 = new BigInteger(words[1]);
                                        BigInteger ku2 = new BigInteger(words[2]);

                                         if (!words[1].isEmpty()) {
                                           
                                                 //cari public key yang diminta
                                                    //enkrip pesan menggunakan private key si authority
                                                     Date date= new Date();
                                                     String msg=name+"|"+date.toString()+"|"+ku1+" "+ku2;
                                                     BigInteger cipher = RSA.encrypt(getDa(), getNa(), msg.getBytes());
                                                    this.out.println("certificate "+cipher);
                                                   //  this.out.println(ku1);
                                                   //  this.out.println("Plaintext in bytes: " + RSA.convert(msg.getBytes()));
                                                 //   BigInteger decripted = RSA.decrypt(new BigInteger("54447217166781385718247467566894095932837289308213424624718261949989598047549876961018771538154391733650094798060665994609531231316803289934209593756273781327670418497355458010526000682649498198462157628479756341648048203701867022130558473160490618357327513323159062519998359675790884233498116384351900927691"), new BigInteger("71386351396446705719480013032150036889720001537435378952408387889986361884565394237780167127802424717452346513012873192932496503282030980135963689591558974980607441276176867294175126719259657521909854254658617869706076412873596982114825748401018960929238041787923302735140851969855208590511588134030915497081"), cipher.toByteArray());
                                                  //  this.out.println("Decripted ciphertext in bytes: " + RSA.convert(decripted.toByteArray()));
                                                //    this.out.println("Decripted ciphertext: " + new String(decripted.toByteArray()));
                                                
                                       
                                     }
                                   }
                                }
                                   else  if(line.startsWith("@")){
                                        String[] words = line.split("\\s", 2);
                                        if (words.length > 1 && words[1] != null) {
                                        words[1] = words[1].trim();
                                        
                                         if (!words[1].isEmpty()) {
                                 
                                        for (ServerCertificate.ServerCertificateThread t : threadClient) {
                                        if(t.alias.equals(words[0])){
                                         String msg = "Share " + words[1];
					//System.out.println(msg);
                                        t.out.println(msg);
                                        this.out.println(words[0] + " received your message");
                                        }
                                        }
                                       
                                     }
                                   }
                                }
                                    else{
                                    String msg = name + ":" + line;
					System.out.println(msg);
					server.broadcast( msg);
                                    }
				}
                                System.out.println(name + " leave the room");
                               server.broadcast( name + " leave the room");
                               
                               threadClient.remove(this);
                               
                               out.println("Server :you quit");
                               
			
                               
                               
                             
		
	    

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
