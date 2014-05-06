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
    private ArrayList<ServerCertificateThread> threadClient;//inisialisasi arraylist
   
    public ServerCertificate() {
		
		threadClient = new ArrayList<ServerCertificateThread>();//membuat objek threadClient
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
                ServerCertificateThread client;//membuat object thread client
                client = new ServerCertificateThread(serverSocket.accept(), this);
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
        
  
  
  

class ServerCertificateThread extends Thread {
    private Socket socket = null;
    private ServerCertificate server;
    private String name;					// untuk membedakan klien satu dan yang lain
    private String alias;
    private PrintWriter out;
  
    
   
    public ServerCertificateThread(Socket socket, ServerCertificate server) {
	super("KKMultiServerThread");//membuat objek thread baru
	this.socket = socket;//inisialisasi socket
        this.server = server;//inisialisasi socket
    }
        
    public void run() {

	try {
				//input output data
				out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// identifikasi thread memasukkan nama client
				name = in.readLine();
                                alias = "@" + name;
                                Date date= new Date();
                                      
                                //generate certificate
                                  //fungsi RSA
                              	out.println("anggap ini certifikat: "+""+name+"|"+date.toString()+"|"+"public key");
    
			
                               
                               
                             
		
	    

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