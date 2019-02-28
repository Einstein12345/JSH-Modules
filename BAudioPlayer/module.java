package BAudioPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;
import terra.shell.utils.keys.Event;
import terra.shell.utils.system.EventListener;

public class module extends Module {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6586471690919754614L;
	private InputStream audio;
	private boolean ok;
	private boolean change;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		if (ok) {
			startPlayback();
		}
	}

	private void startPlayback() {
		try {
			Clip c = AudioSystem.getClip();
			AudioInputStream ain = AudioSystem.getAudioInputStream(audio);
			c.open(ain);
			c.start();
			while (true) {

			}
		} catch (Exception e) {
			e.printStackTrace();
			log.log("Houston, we have a problem!");
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
		log.log("Starting AudioPlayer (Basic)");
		File audio = new File("/tmp/AUDIO_OUT");
		if (audio.exists()) {
			try {
				this.audio = new FileInputStream(audio);
				ok = true;
			} catch (Exception e) {
				e.printStackTrace();
				ok = false;
			}
		} else {
			log.log("/tmp/AUDIO_OUT not found? Is AudioController installed?");
			ok = false;
		}
	}

	@Override
	public void trigger(DummyEvent event) {
		// TODO Auto-generated method stub

	}

	private class Listen extends EventListener {

		@Override
		public void trigger(Event e) {			
		}

	}

}
