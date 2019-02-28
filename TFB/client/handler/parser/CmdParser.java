package TFB.client.handler.parser;

import terra.shell.logging.LogManager;
import terra.shell.logging.Logger;
import TFB.TFB_Serv;

public class CmdParser {
	static Logger log = LogManager.getLogger("TFB:CMDPARSE");
	public static void parse(String... cmd){
		if(cmd.length >= 0){
		if(cmd[1].equals("stop")){
			TFB_Serv.isGoing = false;
			log.log("Stopping server");
		}
		}
	}
	public static String[] getLog(){
		return null;
	}

}
