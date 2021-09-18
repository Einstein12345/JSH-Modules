package BasicFX;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.effects.DelayEffect;
import be.tarsos.dsp.filters.BandPass;
import be.tarsos.dsp.io.jvm.AudioPlayer;
import be.tarsos.dsp.io.jvm.JVMAudioInputStream;
import terra.shell.modules.Module;
import terra.shell.utils.keys.Event;

public class module extends Module {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7118754946596258574L;
	// TODO Setup listener type timer thread to query rotary encoder pins to
	// determine buffer size, and volume for delay
	// TODO Setup listener to query switch pins to determine FX active
	// TODO Setup listener to query switch pin to determine which FX settings
	// are being changed
	boolean bypass = false, delay = true, allpass = true;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "AUDIOFX";
	}

	@Override
	public void run() {
		// TODO implement Java Audio API to create distortions effects, use
		// event listener to determine when knobs pushed or so on to determine
		// FX changes
		final AudioFormat af = new AudioFormat(44100, 16, 1, true, false);
		final DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
		try {
			TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
			AudioInputStream stream = new AudioInputStream(line);
			JVMAudioInputStream jstream = new JVMAudioInputStream(stream);
			final BandPass bp = new BandPass(1200, 100, 44100);
			final DelayEffect d = new DelayEffect(.5, 44100, .3);
			final AudioDispatcher ad = new AudioDispatcher(jstream, 1024, 512);
			ad.addAudioProcessor(bp);
			ad.addAudioProcessor(d);
			ad.addAudioProcessor(new AudioPlayer(af));
			new Thread(new DeviceListener(), "DevListener").start();
			new Thread(ad, "AudioDispatch").start();

			// Gets info, applies fx with default settings and outputs the audio
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * Thread t = new Thread(new Runnable() { public void run() { try {
		 * while (true) { final TargetDataLine line = (TargetDataLine)
		 * AudioSystem.getLine(info); byte buffer[] = new byte[441]; // 10 ms of
		 * audio line.open(af);
		 * 
		 * line.read(buffer, 0, buffer.length); b = buffer; if (!bypass)
		 * applyFX(); } } catch (Exception e) { e.printStackTrace(); return; } }
		 * });
		 */
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
		// TODO Auto-generated method stub

	}

	@Override
	public void trigger(Event event) {
		// TODO Auto-generated method stub

	}


}
