package ImageProcessingBenchmark;

import terra.shell.utils.JProcess;
import terra.shell.utils.JProcess.Depends;
import terra.shell.utils.JProcess.ReturnType;
import terra.shell.utils.ReturnValue;

@Depends(dependencies = ProcessorCheckReturnValue.class)
@ReturnType(getReturnType = terra.shell.utils.system.ReturnType.ASYNCHRONOUS)
public class ProcessorCheckProcess extends JProcess {

	ProcessorCheckReturnValue rv;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3950880510819802732L;

	public ProcessorCheckProcess(module m) {

	}

	@Override
	public String getName() {
		return "ProcessorCheck";
	}

	@Override
	public boolean start() {
		return true;
	}

	@Override
	public void processReturn(ReturnValue rv) {

	}

	@Override
	public void createReturn() {
		rv = new ProcessorCheckReturnValue(this);

	}

	@Override
	public ReturnValue<Integer> getReturn() {
		if (rv == null) {
			createReturn();
		}
		return rv;
	}

}
