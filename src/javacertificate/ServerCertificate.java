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
                setDa(new BigInteger("81546851167105528930875222780166718864283181164045479163889748660013506929580917248668199340574500395541257019601630194472543901157823474288972704743291565552489883343627009444784764291857140153629565407026170208970563494263776950247417554811726528756047082694402719053879248347959017287891209918521746051773"));
                setNa(new BigInteger("108667903679158164216493740155324338798711714626005576578493145358363936346064224723004026860601450084613465910915639786101626482604842158336487842427021862512050676569321518486648699340277808085396611744132143247652466513478030710379003125768282448801398325245264016737884182642093410098509541570191720006119"));
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
                                         if (!words[1].isEmpty()) {
                                           
                                                 //cari public key yang diminta
                                                    //enkrip pesan menggunakan private key si authority
                                                     Date date= new Date();
                                                     String msg=""+name+"|"+date.toString()+"|"+words[1]+" "+words[2];
                                                     BigInteger cipher = RSA.encrypt(getDa(), getNa(), msg.getBytes());
                                                    this.out.println("certificate "+cipher);
                                                    
                                                
                                       
                                     }
                                   }
                                }
                                   else  if(line.startsWith("@")){
                                        String[] words = line.split("\\s", 2);
                                        if (words.length > 1 && words[1] != null) {
                                        words[1] = words[1].trim();
                                        this.out.println(words[0]);
                                         if (!words[1].isEmpty()) {
                                 
                                        for (ServerCertificate.ServerCertificateThread t : threadClient) {
                                        if(t.alias.equals(words[0])){
                                         String msg = " Share " + words[1];
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
