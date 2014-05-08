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

public class ServerCertificate {
    private ArrayList<ServerCertificate.ServerCertificateThread> threadClient;//inisialisasi arraylist
   
    public ServerCertificate() {
		
		threadClient = new ArrayList<ServerCertificate.ServerCertificateThread>();//membuat objek threadClient
	}

    public void StartServer()
    {
        ServerSocket serverSocket = null;
        

        try {
            serverSocket = new ServerSocket(4444);//menginsi port socket
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(-1);
        }

        while (true)
        {
            try {
                ServerCertificate.ServerCertificateThread client;//membuat object thread client
                client = new ServerCertificate.ServerCertificateThread(serverSocket.accept(), this);
                client.setDaemon(true);
             
             
              threadClient.add(client);
                
                threadClient.get(threadClient.size()-1).start();//menjalankan thread
            
            }
            catch (Exception e){
                   
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
                                        String[] words = line.split("\\s", 2);
                                        if (words.length > 1 && words[1] != null) {
                                        words[1] = words[1].trim();
                                         if (!words[1].isEmpty()) {
                                           
                                                 //cari public key yang diminta
                                                    //enkrip pesan menggunakan private key si authority
                                                     Date date= new Date();
                                                    this.out.println("Certificate anggap ini balasan dari server yang sudah di enkripsi: "+""+name+"|"+date.toString()+"|"+words[1]);
                                                    
                                                
                                       
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
                                         String msg = name + " mengirim certifikat: " + words[1];
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
