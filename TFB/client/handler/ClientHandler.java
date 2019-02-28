package TFB.client.handler;

import terra.shell.logging.LogManager;
import terra.shell.logging.Logger;
import TFB.client.Client;

public abstract class ClientHandler {
	protected Logger log = LogManager.getLogger("TFB:"+handleType());
	public abstract void handleClient(Client c);
	public abstract String handleType();
}
