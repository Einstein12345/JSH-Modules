package ClusterTest;

import java.io.IOException;
import java.io.InputStream;

/**
 * Unified Networked File System File Control Block
 * <p>
 * Standard:
 * <p>
 * B. File Name Size
 * <p>
 * C. File Name, up to 30 characters
 * <p>
 * D. File Ownership - TODO ADD USER IDS TO SYSTEM
 * <p>
 * E. File Permissions
 * <p>
 * 
 */
public class UNFSFCB {
	private final InputStream in;
	private String fileName;
	// exec - Execute
	// uW - user Write
	// uR - user Read
	// eW - everyone Write
	// eR - everyone Read
	private boolean exec, uW, uR, eW, eR;
	private int datOffset;

	public UNFSFCB(InputStream in) throws IOException {
		this.in = in;
		in.mark(0);
		int fNameSize = in.read();
		if (fNameSize == 0 || fNameSize > 30) {
			throw new IOException("FileNameSize in UNFSFCB is of invalid size");
		}
		char[] fNameChar = new char[fNameSize];
		for (int i = 0; i < fNameSize; i++) {
			fNameChar[i] = (char) in.read();
		}
		fileName = new String(fNameChar);
		// TODO Read user ownership information
		byte perms = (byte) in.read();
		byte mask = 0b00000001;
		if ((perms & mask) != 0) {
			exec = true;
		}
		mask = 0b00000010;
		if ((perms & mask) != 0) {
			uW = true;
		}
		mask = 0b00000100;
		if ((perms & mask) != 0) {
			uR = true;
		}
		mask = 0b00001000;
		if ((perms & mask) != 0) {
			eW = true;
		}
		mask = 0b00010000;
		if ((perms & mask) != 0) {
			eR = true;
		}
		// Byte 1 is fNameSize, byte 2 - [1 + fNameSize] is fName, bytes [1 + fNameSize]
		// - [1 + fNameSize + 1], is perms
		datOffset = 2 + fNameSize;
	}

	public boolean canUserWrite() {
		return uW;
	}

	public boolean canUserRead() {
		return uR;
	}

	public boolean canEveryoneWrite() {
		return eW;
	}

	public boolean canEveryoneRead() {
		return eR;
	}

	public boolean canBeExecuted() {
		return exec;
	}

	public byte[] readData(int off, int len) throws IOException {
		byte[] dat = new byte[len];
		in.reset();
		in.read(dat, datOffset + off, len);
		return dat;
	}

}
