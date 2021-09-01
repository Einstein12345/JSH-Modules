package UNFS.events;

import UNFS.UNFSFileDescriptor;
import terra.shell.modules.ModuleEvent;

public class ListDirectoryEvent extends ModuleEvent {
	UNFSFileDescriptor dir[];
	boolean complete;

	public ListDirectoryEvent(Object[] args) {
		super("UNFS", args);
	}

	public boolean completed() {
		return complete;
	}

	public UNFSFileDescriptor[] getDirectoryListing() {
		return dir;
	}

	public void setDirectoryListing(UNFSFileDescriptor[] dir) {
		this.dir = dir;
		complete = true;
	}

}
