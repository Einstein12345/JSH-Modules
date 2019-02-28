package TFB.client.handler;

import TFB.client.Client;
import TFB.client.handler.parser.CmdParser;

public class CommandClientHandler extends ClientHandler {
	@Override
	public void handleClient(Client c) {
		log.log("Handling client: " + c.getClientAddress());
		while (c.hasMoreLines()) {
			String cmd = c.readLine();
			String[] cmdfull = cmd.split(":");
			if(cmdfull[0].equals("c")){
				log.log("C");
				if(cmdfull.length > 0){
					CmdParser.parse(cmdfull);
				}
			}
			if (cmdfull[0].equals("e")) {
				if (cmdfull.length > 0) {
					log.log("Client Error: " + cmdfull[1]);
					c.eDown(cmdfull[1]);
				} else {
					log.log("Client Error: No client log information");
					c.eDown("Unknown");
				}
			}
		}
		log.log("Client Connection Completed!");
	}

	@Override
	public String handleType() {
		return "cmd";
	}

}
