package TFB;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

import terra.shell.logging.Logger;
import terra.shell.utils.JProcess;
import TFB.client.Client;
import TFB.client.handler.ClientHandler;
import TFB.client.handler.CommandClientHandler;
import TFB.client.handler.GUIClientHandler;

public class TFB_Serv extends JProcess {
	public static boolean isGoing = true;
	private static Hashtable<String, ClientHandler> handlers = new Hashtable<String, ClientHandler>();
	private static Logger sLog;

	public TFB_Serv() {
		sLog = log;
	}

	public void startServ() {
		CommandClientHandler cch = new CommandClientHandler();
		GUIClientHandler gch = new GUIClientHandler();
		registerHandler(gch, "gui");
		registerHandler(cch, "cmd");
		isGoing = true;
		try {
			ServerSocket ss = new ServerSocket(35555);
			Socket s = null;
			getLogger().log("Server started on 35555");
			while (isGoing) {
				s = ss.accept();
				final Socket tmp = s;
				getLogger().log("Client Connected: "+s.getInetAddress());
				Thread t = new Thread(new Runnable(){
					public void run(){
						Client c = new Client(tmp, log);
					}
				});
				t.setName(s.getInetAddress()+"");
				t.start();
			}
			getLogger().log("Server stopped!");
			return;
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().log("Unable to start TFBSERVER");
			return;
		}

	}

	public void stopServ() {
		isGoing = false;
	}

	public static void handleClient(Client c) {
		try {
			handlers.get(c.getType()).handleClient(c);
		} catch (Exception e) {
			sgetLogger().log("Unable to  handle client type: " + c.getType());
			c.eDown("Can't Handle Client Type");
			c.eDown("Unknown Type");
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "TFBS";
	}

	@Override
	public boolean start() {
		startServ();
		return true;
	}

	public static void registerHandler(ClientHandler h, String htype) {
		if (handlers.containsKey(htype)) {
			handlers.put(htype, h);
			sgetLogger().log("Handler For Type " + htype + " Replaced");

		} else
			handlers.put(htype, h);
		sgetLogger().log("Registered Handler For Type: " + htype);
	}

}
