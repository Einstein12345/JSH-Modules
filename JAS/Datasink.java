package JAS;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public final class Datasink {
	private InputStream in;
	private BufferedOutputStream out;
	private File sink;

	public Datasink(File sink) {
		this.sink = sink;
		if (!sink.exists()) {
			return;
		}
		try {
			in = new FileInputStream(sink);
			out = new BufferedOutputStream(new FileOutputStream(sink));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int read() {
		try {
			return in.read();
		} catch (Exception e) {
			return 0;
		}
	}

	public void write(int i) {
		try {
			out.flush();
			out.close();
			out = null;
			// Recreate OutputStream
			out = new BufferedOutputStream(new FileOutputStream(sink));
			out.write(i);
		} catch (Exception e) {

		}
	}

}
