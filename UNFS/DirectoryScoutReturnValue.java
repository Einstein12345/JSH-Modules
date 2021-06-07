package UNFS;

import terra.shell.utils.JProcess;
import terra.shell.utils.ReturnValue;
import terra.shell.utils.system.ByteClassLoader.Replaceable;

@Replaceable(replaceable = false)
public class DirectoryScoutReturnValue extends ReturnValue {

	public DirectoryScoutReturnValue(JProcess p) {
		super(p);
	}

	@Override
	public boolean setValues(Object... values) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object[] getReturnValue() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @deprecated Use {@link #setValues(Object...)} instead
	 */
	@Override
	public boolean processReturn(Object... values) {
		return setValues(values);
	}

}
