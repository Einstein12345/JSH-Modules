package RemoteAccess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import terra.shell.config.Configuration;
import terra.shell.launch.Launch;
import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;

public class module extends Module {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7810286745284331061L;
	private boolean ok = true;
	private Configuration conf;
	private File conff = new File("/config/ra");

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "RA";
	}

	@Override
	public void run() {
		if (!ok) {
			return;
		}
		conf = Launch.getConfig("ra/configuration");
		if (conf == null) {
			conf = new Configuration(conff);
		}
		log.log("Setting up server socket on " + conf.getValue("port"));
		try {
			ServerSocket ss = new ServerSocket(Integer.parseInt((String) conf
					.getValue("port")));
			Socket s;
			log.log("Server setup complete!");
			while ((s = ss.accept()) != null) {
				s.setKeepAlive(true);
				SCTransactionHandler sc = new SCTransactionHandler(s);
				sc.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.log("Failed to start server!");
		}
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrg() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEnable() {
		if (!conff.exists()) {
			// if (conff.mkdir()) {
			try {
				conff.createNewFile();
				PrintStream out = new PrintStream(new FileOutputStream(conff),
						true);
				out.println("port:1701");
				out.println("lock:false");
				out.println("allowtransfer:true");
				out.println("priority:low");
				out.println("varaccess:true");
				out.close();
				out = null;
			} catch (Exception e) {
				e.printStackTrace();
				log.log("Failed to load Configuration for Remote Access!");
				ok = false;
			}
			// }
		}
	}

	@Override
	public void trigger(DummyEvent event) {
		// TODO Auto-generated method stub

	}

}
