package ClusterTest;

import terra.shell.launch.Launch;
import terra.shell.logging.LogManager;
import terra.shell.modules.Module;
import terra.shell.utils.JProcess;
import terra.shell.utils.JProcess.Depends;
import terra.shell.utils.JProcess.ReturnType;
import terra.shell.utils.ReturnValue;

@Depends(dependencies = ClusterTestProcReturnValue.class)
@ReturnType(getReturnType = terra.shell.utils.system.ReturnType.ASYNCHRONOUS)
public class ClusterTestProc extends JProcess {

	private static final long serialVersionUID = -2296188400014993785L;

	protected ReturnValue rv;

	public ClusterTestProc() {
		super();
	}

	@Override
	public String getName() {
		return "TestClusterProcess";
	}

	@Override
	public boolean start() {
		rv.setValues(this.getUUID(), this.getSUID());
		getLogger().log("Process started");
		getLogger().log("My UUID is: " + rv.getReturnValue()[0]);
		getLogger().log("My SUID is: " + rv.getReturnValue()[1]);
		getLogger().log("Launch contains " + Launch.cmds.size() + " commands");
		getLogger().log("Process completed");
		return true;
	}

	@Override
	public void processReturn(ReturnValue rv) {
		Object[] ret = rv.getReturnValue();
		LogManager.out.println("Remote UUID: " + ret[0]);
		LogManager.out.println("Remote SUID: " + ret[1]);
		//TODO Need to figure out how to encapsulate information from Remote in ReturnValue only if on a remote system??
	}

	@Override
	public ReturnValue getReturn() {
		return rv;
	}
	
	@Override
	public void createReturn() {
		rv = new ClusterTestProcReturnValue(this);
	}

}