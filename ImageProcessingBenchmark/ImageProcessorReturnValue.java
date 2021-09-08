package ImageProcessingBenchmark;

import terra.shell.utils.JProcess;
import terra.shell.utils.ReturnValue;

public class ImageProcessorReturnValue extends ReturnValue<String> {

	public ImageProcessorReturnValue(JProcess p) {
		super(p);
	}

	@Override
	public boolean processReturn(Object... values) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean setValues(String values) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getReturnValue() {
		// TODO Auto-generated method stub
		return "DONE";
	}

}
