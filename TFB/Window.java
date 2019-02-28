package TFB;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

import terra.shell.logging.LogManager;
import terra.shell.logging.Logger;

public class Window {
	private Logger log = LogManager.getLogger("WM");
	private URL u;
	private File f;
	public Window(URL u){
		this.u = u;
	}
	public Window(File f){
		this.f = f;
	}
	
	public BufferedImage update(){
		BufferedImage i = null;
		if(u != null){
			try{
			i = ImageIO.read(u);
			}catch(Exception e){
				e.printStackTrace();
				log.log("Failed to update Window!");
			}
		} else if(f != null && f.exists()){
			try{
				i = ImageIO.read(f);
			}catch (Exception e){
				e.printStackTrace();
				log.log("Failed to update Window!");
			}
		}
		return i;
	}
}
