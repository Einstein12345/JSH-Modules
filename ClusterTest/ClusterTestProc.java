package ClusterTest;

import terra.shell.launch.Launch;
import terra.shell.logging.LogManager;
import terra.shell.utils.JProcess;
import terra.shell.utils.JProcess.Depends;
import terra.shell.utils.JProcess.ReturnType;
import terra.shell.utils.ReturnValue;

@Depends(dependencies = ClusterTestProcReturnValue.class)
@ReturnType(getReturnType = terra.shell.utils.system.ReturnType.ASYNCHRONOUS)
public class ClusterTestProc extends JProcess {

	private static final long serialVersionUID = -2296188400014993785L;

	private ReturnValue rv = new ClusterTestProcReturnValue(this);

	public ClusterTestProc() {
		super();
	}

	@Override
	public String getName() {
		return "TestClusterProcess";
	}

	@Override
	public boolean start() {
		getLogger().log("Process started");
		getLogger().log("My UUID is: " + this.getUUID());
		getLogger().log("Launch contains " + Launch.cmds.size() + " commands");
		getLogger().log("Process completed");
		rv.processReturn(new String[] {"RETURN", "testing", "123" });
		return true;
	}
	
	@Override
	public void processReturn(ReturnValue rv) {
		Object[] ret = rv.getReturnValue();
		LogManager.out.println(ret[0]);
		LogManager.out.println(ret[1]);
		LogManager.out.println(ret[2]);
	}

	@Override
	public ReturnValue getReturn() {
		return rv;
	}

}