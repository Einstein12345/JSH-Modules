package UNFS.events;

import java.time.LocalTime;

import UNFS.UNFSFileDescriptor;

public class MajorFileUpdateEvent extends FileEvent {
	private UNFSFileDescriptor file;
	private final LocalTime time;

	public MajorFileUpdateEvent(Object[] args, UNFSFileDescriptor file) {
		super(args);
		this.file = file;
		time = LocalTime.now();
	}

	public UNFSFileDescriptor updatedFileDescriptor() {
		return file;
	}

	public LocalTime editTime() {
		return time;
	}

}
