package UNFS.events;

import UNFS.UNFSFileDescriptor;

public class CreateFileEvent extends MajorFileUpdateEvent {
	// TODO Reconcile local file creation with RemoteFiles by adding in
	// reconciliation bit to the "extra" section of ZipEntry info. This bit will
	// help determine what to do in the event of duplicate files on remote machines
	public CreateFileEvent(UNFSFileDescriptor file) {
		super(null, file);
	}

}
