package TFB.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import terra.shell.logging.Logger;
import TFB.TFB_Serv;

public class Client {
	protected static Socket s;
	protected static PrintWriter out;
	protected static Scanner in;
	protected static BufferedInputStream bin;
	protected static BufferedOutputStream bout;
	private static Logger log;
	private static String type;

	public Client(Socket s, Logger log) {
		this.s = s;
		if (log != null)
			this.log = log;
		else
			eDown("Failed to get Logger!");
		try {
			out = new PrintWriter(s.getOutputStream(), true);
			bin = new BufferedInputStream(s.getInputStream());
			bout = new BufferedOutputStream(s.getOutputStream());
			in = new Scanner(s.getInputStream());
			getLogger().log("Getting client type!");
			String type = in.nextLine();
			this.type = type;
			getLogger().log("Client is " + type);
			TFB_Serv.handleClient(this);
		} catch (Exception e) {
			getLogger().log("Failed to create connection to client!");
			eDown("Failed in creation of tunnel to client!");
		}
	}
	
	public static String getType(){
		return type;
	}

	public InetAddress getClientAddress() {
		if (s != null)
			return s.getInetAddress();
		else
			return null;
	}

	public String readLine() {
		String line;
		if (in != null) {
			return in.nextLine();
		} else {
			return "";
		}
	}

	public String read() {
		String line;
		if (in != null) {
			return in.next();
		} else {
			return "";
		}
	}

	public byte[] readBytes(int bytes) {
		byte[] byin = new byte[bytes];
		if (bin != null) {
			try {
				bin.read(byin);
			} catch (Exception e) {
				getLogger().log("Failed to read bytes from Client!");
			}
		}
		return byin;
	}

	public void println(String line) {
		if (out != null) {
			out.println("m:" + line);
		}
	}

	public void sendBytes(byte[] b) throws IOException {
		if (bin != null && out != null && in != null && bout != null) {
			out.println("b:" + b.length);
			in.nextLine();
			out.print("Sending");
			in.nextLine();
			bout.write(b, 0, b.length);
			in.nextLine();
		}
	}

	public boolean closeConnection() {
		try {
			if (!s.isClosed() && s != null)
				s.close();
			else
				return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void eDown(String reason) {
		try {
			if (out != null)
				out.println("e:");
			if (s != null)
				if (!s.isClosed())
					s.close();
			if (out != null)
				out.flush();
			if (out != null)
				out.close();
			getLogger().log("Connection closed to client! Emergency Close Called!");
			getLogger().log("Reason: " + reason);
			s = null;
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().log("Failed to call Emergency Shutdown, something is VERY wrong!");
		}
	}

	public boolean hasMoreLines() {
		return in.hasNextLine();
	}

	public boolean hasMore() {
		return in.hasNext();
	}

}
