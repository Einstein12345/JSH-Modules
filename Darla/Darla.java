package Darla;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.vosk.LibVosk;
import org.vosk.LogLevel;
import org.vosk.Model;
import org.vosk.Recognizer;

import terra.shell.config.Configuration;
import terra.shell.launch.Launch;
import terra.shell.utils.JProcess;

public class Darla extends JProcess {

	private TargetDataLine audioIn;
	private Configuration conf;

	public Darla(Configuration conf) throws LineUnavailableException {
		final File libLocNix = new File(Launch.getConfD() + "/DarlaLib", "libvosk.so");
		final File libLocMac = new File(Launch.getConfD() + "/DarlaLib", "libvosk.dylib");
		if (libLocNix.exists()) {
			getLogger().debug("Loading libvosk.so");
			System.load(libLocNix.getAbsolutePath());
		} else if (libLocMac.exists()) {
			getLogger().debug("Loading libvosk.dylib");
			System.load(libLocMac.getAbsolutePath());
		} else {
			getLogger().err("FAILED TO LOAD libvosk LIBRARY");
			return;
		}
		try {
			LibVosk.setLogLevel(LogLevel.DEBUG);
			AudioFormat f = new AudioFormat(48000.0f, 16, 1, true, true);
			audioIn = AudioSystem.getTargetDataLine(f);
			this.conf = conf;
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().debug("Failed to run constructor");
		}
	}

	@Override
	public String getName() {
		return "Darla";
	}

	@Override
	public boolean start() {
		// TODO Download and set model to english
		Model m = new Model((String) conf.getValue("vosk-model-loc"));
		Recognizer r = new Recognizer(m, 48000.0f);
		int nBytes;
		byte[] b = new byte[4096];
		String result;
		while ((nBytes = audioIn.read(b, 0, 4096)) >= 0) {
			if (r.acceptWaveForm(b, nBytes)) {
				result = r.getResult();
			} else {
				result = r.getPartialResult();
			}
			if (result.startsWith("darla")) {
				new ParseInput(result).start();
			}
		}
		return false;
	}

}
