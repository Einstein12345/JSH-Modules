package UNFS;

public class UNFSFileDescriptor {
	private final boolean isDir;
	private final String filePath;
	private final long size, crc;

	public UNFSFileDescriptor(String filePath, long size, long crc) {
		if (filePath.endsWith("/"))
			isDir = true;
		else
			isDir = false;
		this.filePath = filePath;
		this.size = size;
		this.crc = crc;
	}

	public boolean isDir() {
		return isDir;
	}

	public long getCrc() {
		return crc;
	}

	public long getSize() {
		return size;
	}

	public String getFilePath() {
		return filePath;
	}

}
