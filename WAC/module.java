package WAC;

import java.io.BufferedInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;

public class module extends Module {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5837254250265530304L;
	private ServerSocket clients;
	private boolean ok;
	private String dv = "0.1";

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "WAC";
	}

	@Override
	public void run() {
		if (ok) {
			getLogger().log("Running WAC...");
			getLogger().log("Starting Server on port 2014");
			startServer();
		}
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "0.1";
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "D.S.";
	}

	@Override
	public String getOrg() {
		// TODO Auto-generated method stub
		return "T3RRA";
	}

	@Override
	public void onEnable() {
		getLogger().log("Enabling WAC");
		try {
			getLogger().log("Binding ServerSocket to port 2014");
			clients = new ServerSocket(2014);
			ok = true;
			getLogger().log("Checking Dependencies...");
			try {
				if (JAS.module.getV().equals(dv)) {
					ok = true;
				} else {
					ok = false;
					getLogger().log("JAS Version " + dv
							+ " is not installed or could not be found!");
				}
			} catch (Exception e) {
				ok = false;
				getLogger().log("Could not detect dependencies! " + e.getMessage());
			}
		} catch (Exception e) {
			getLogger().log("Failed to bind ServerSocket to port 2014! "
					+ e.getMessage());
			ok = false;
		}
	}

	@Override
	public void trigger(DummyEvent event) {
		// TODO Auto-generated method stub

	}

	private void startServer() {
		Thread server = new Thread(new Runnable() {
			public void run() {
				Socket s;
				while (ok) {
					try {
						s = clients.accept();
						handleClient(s);
						s = null;
					} catch (Exception e) {
						getLogger().log("Failed to connect to client, "
								+ e.getMessage());
					}
				}
			}
		});
		server.setName("WAC_SERVER");
		server.start();
	}

	private void handleClient(final Socket s) {
		if (s == null) {
			getLogger().log("Received Null Socket connection in handler!");
			return;
		}
		getLogger().log("Received Client Connection from "
				+ s.getInetAddress().getHostAddress());
		Thread cl = new Thread(new Runnable() {
			public void run() {
				BufferedInputStream bin = null;
				PrintStream out = null;
				try {
					bin = new BufferedInputStream(s.getInputStream());
					out = new PrintStream(s.getOutputStream(), true);
				} catch (Exception e) {
					e.printStackTrace();
					getLogger().log("Failed to create necessary streams from Socket!");
					return;
				}
				JAS.module.registerSink(s.getInetAddress().getHostName());
				final JAS.Datasink sink = JAS.module.getSink(s.getInetAddress()
						.getHostName());
				// Init DataTransfer
				// DONE Init Datatransfer from Socket to Sink
				if (sink == null) {
					getLogger().log("Odd the DataSink needed cannot be found!");
					return;
				}
				out.println("GO");
				int errors = 0;
				while (ok) {
					try {
						// Read from socket into tmp
						final int tmp = bin.read();
						// Check for EOS
						if (tmp == -1) {
							break;
						}
						// Write tmp to sink and let JAS do the rest
						sink.write(tmp);
					} catch (Exception e) {
						if (errors < 10) {
							errors++;
						} else {
							e.printStackTrace();
							getLogger().log("Over 10 errors have occurred in this sink, closing for safety!");
							break;
						}
					}
				}
				try {
					bin.close();
				} catch (Exception e) {
					e.printStackTrace();
					getLogger().log("Odd I can't seem to close the Socket InputStream!");
				}
				// Cleanup, Cleanup everybody everywhere..
				out.println("CLOSE");
				out.flush();
				out.close();
				bin = null;
				out = null;
				// Yes, this is a gc call GET OVER IT
				System.gc();
				return;
			}
		});

	}
}
