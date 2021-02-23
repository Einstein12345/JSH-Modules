package TFB.client.handler;

import java.net.MalformedURLException;
import java.net.URL;

import TFB.Window;
import TFB.module;
import TFB.client.Client;

public class GUIClientHandler extends ClientHandler {

	@Override
	public void handleClient(Client c) {
		while (c.hasMoreLines()) {
			String in = c.readLine();
			String[] tot = in.split(":");
			if (tot.length > 0) {
				if (tot.length > 1)
					if (tot[0].equals("gimage")) {
						String imgurl = tot[1];
						try {
							URL url = new URL(imgurl);
							Window w = new Window(url);
							module.addWindow(w);
						} catch (MalformedURLException me) {
							c.eDown(me.getLocalizedMessage());
							getLogger().log("Failed to find image specified by client "
									+ me.getLocalizedMessage());
						}
					}
			}
		}
	}

	@Override
	public String handleType() {
		// TODO Auto-generated method stub
		return "gui";
	}

}