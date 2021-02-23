package ClusterTest;

import java.util.UUID;

import terra.shell.utils.JProcess;
import terra.shell.utils.ReturnValue;

public class ClusterTestProcReturnValue extends ReturnValue {
	private Object[] values;
	private UUID pid;
	
	public ClusterTestProcReturnValue(JProcess p) {
		super(p);
	}

	@Override
	public boolean processReturn(Object... values) {
		this.values = values;
		return true;
	}

	@Override
	public Object[] getReturnValue() {
		return values;
	}
}
