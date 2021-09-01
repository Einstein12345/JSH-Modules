package UNFS.events;

import UNFS.UNFSFileDescriptor;

public class ListDirectoryEvent extends FileEvent {
	UNFSFileDescriptor dir[];
	boolean complete;

	public ListDirectoryEvent(Object[] args) {
		super(args);
	}

	public final boolean completed() {
		return complete;
	}

	public final UNFSFileDescriptor[] getDiresctoryListing() {
		return dir;
	}

	public final void setDirectoryListing(UNFSFileDescriptor[] dir) {
		this.dir = dir;
		complete = true;
	}

}
