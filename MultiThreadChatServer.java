import java.io.*;
import java.net.*;

public class MultiThreadChatServer{

    
    static  Socket clientSocket = null;
    static  ServerSocket serverSocket = null;

    static  clientThread t[] = new clientThread[10];           
    
    public static void main(String args[]) {
	
	int port_number=2222;
	
	if (args.length < 1)
	    {
		System.out.println("Usage: java MultiThreadChatServer \n"+
				   "Now using port number="+port_number);
	    } else {
		port_number=Integer.valueOf(args[0]).intValue();
	    }
	
	try {
	    serverSocket = new ServerSocket(port_number);
        }
        catch (IOException e)
	    {System.out.println(e);}
	
		
	while(true){
	    try {
		clientSocket = serverSocket.accept();
		for(int i=0; i<=9; i++){
		    if(t[i]==null)
			{
			    (t[i] = new clientThread(clientSocket,t)).start();
			    break;
			}
		}
	    }
	    catch (IOException e) {
		System.out.println(e);}
	}
    }
} 


class clientThread extends Thread{
    
    DataInputStream is = null;
    PrintStream os = null;
    Socket clientSocket = null;       
    clientThread t[]; 
    
    public clientThread(Socket clientSocket, clientThread[] t){
	this.clientSocket=clientSocket;
        this.t=t;
    }
    
    public void run() 
    {
	String line;
        String name;
	try{
	    is = new DataInputStream(clientSocket.getInputStream());
	    os = new PrintStream(clientSocket.getOutputStream());
	    os.println("Enter your name.");
	    name = is.readLine();
	    os.println("Hello "+name+" to our chat room.\nTo leave enter /quit in a new line"); 
	    for(int i=0; i<=9; i++)
		if (t[i]!=null && t[i]!=this)  
		    t[i].os.println("*** A new user "+name+" entered the chat room !!! ***" );
	    while (true) {
		line = is.readLine();
                if(line.startsWith("/quit")) break; 
		for(int i=0; i<=9; i++)
		    if (t[i]!=null)  t[i].os.println("<"+name+"> "+line); 
	    }
	    for(int i=0; i<=9; i++)
		if (t[i]!=null && t[i]!=this)  
		    t[i].os.println("*** The user "+name+" is leaving the chat room !!! ***" );
	    
	    os.println("*** Bye "+name+" ***"); 


	    for(int i=0; i<=9; i++)
		if (t[i]==this) t[i]=null;  
		
	    
	    is.close();
	    os.close();
	    clientSocket.close();
	}
	catch(IOException e){};
    }
}
