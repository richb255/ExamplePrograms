import java.net.*;


public class ClientConnection {
    private ClientThread clthread = null;

	
	public ClientConnection(Socket sock, Server serv)
	{
		System.out.println("A new client has connected.");
		clthread = new ClientThread(sock, serv, this);
		
		clthread.start();
	}
	
	public boolean isRunning()
	{

		return clthread.isRunning();
	}
	
	public ClientThread GetThread()
	{
		return clthread;		
	}
}
