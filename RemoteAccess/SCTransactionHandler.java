package RemoteAccess;

import java.net.Socket;

import terra.shell.command.Terminal;
import terra.shell.utils.JProcess;
import terra.shell.utils.streams.UnclosableInStream;

public class SCTransactionHandler extends JProcess {
	private Socket s;

	public SCTransactionHandler(final Socket s) {
		this.s = s;
	}

	@Override
	public String getName() {
		return "RA:" + this.getUUID();
	}

	@Override
	public boolean start() {
		try {
			Terminal t = new Terminal(s.getOutputStream());
			t.setGInputStream(new UnclosableInStream(s.getInputStream()));
			t.setGOutputStream(s.getOutputStream());
			t.run();
		} catch (Exception e) {
			e.printStackTrace();
			log.log("Connection closed!");
			return true;
		}
		return false;
	}

}
