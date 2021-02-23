package JAS;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import terra.shell.utils.JProcess;

public class JASD extends JProcess {
	private module m;
	private File f = new File("/dev/dsp");
	private boolean ok = true;
	private boolean pause;

	public JASD(module m) {
		this.m = m;
		if (!f.exists()) {
			getLogger().log("Well looks like /dev/dsp isnt existent! FIX IT!");
			ok = false;
		}
		if (!f.canWrite()) {
			getLogger().log("Can't write to /dev/dsp!");
			ok = false;
		}
	}

	@Override
	public String getName() {
		return "JASD";
	}

	@Override
	public boolean start() {
		if (!ok) {
			getLogger().log("Guess What! I don't care if you");
			getLogger().log("want me to start or not! I dont");
			getLogger().log("think its safe to come out! Maybe");
			getLogger().log("if you reload me I might think differently");
			return false;
		}
		BufferedOutputStream bout = null;
		try {
			bout = new BufferedOutputStream(new FileOutputStream(f, true));
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().log("Well that sucks...");
			return false;
		}
		int errors = 0;
		boolean error = false;
		while (!m.stop()) {
			try {
				Thread.sleep(10);
				// getLogger().log("Sleep");
			} catch (Exception e) {
				e.printStackTrace();
				getLogger().log("FAILING");
				error = true;
				break;
			}
			int in = m.getCurrent().read();
			if (pause) {
				if (in == -1) {
					in = 0;
				}
				try {
					bout.write(in);
					if (error) {
						error = false;
						errors = 0;
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (error && errors == 4) {
						getLogger().log("Four errors detected! Halting JASD!");
						break;
					} else {
						error = true;
						errors++;
					}
				}
			}
		}
		try {
			bout.close();
		} catch (Exception e) {
			e.printStackTrace();
			getLogger().log("Can't close the output to /dev/dsp! Resource Leak detected!");
		}
		return true;
	}

	public void pause() {
		pause = !pause;
		getLogger().log("Pause: " + pause);
	}

}
