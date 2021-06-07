package ClusterTest;

import terra.shell.utils.JProcess;
import terra.shell.utils.ReturnValue;

public final class ClusterTestProcReturnValue extends ReturnValue {
	protected Object[] values;
	public ClusterTestProcReturnValue(JProcess p) {
		super(p);
	}

	/**
	 * @deprecated Use {@link #setValues(Object...)} instead
	 */
	@Override
	public boolean processReturn(Object... values) {
		return setValues(values);
	}

	@Override
	public boolean setValues(Object... values) {
		this.values = values;
		return true;
	}

	@Override
	public Object[] getReturnValue() {
		return values;
	}
	
}
