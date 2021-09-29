package ImageProcessingBenchmark;

import terra.shell.logging.LogManager;
import terra.shell.utils.JProcess;
import terra.shell.utils.JProcess.Depends;
import terra.shell.utils.JProcess.ReturnType;
import terra.shell.utils.ReturnValue;

@Depends(dependencies = { ProcessorCheckReturnValue.class, module.class, ImageProcessor.class,
		ImageProcessorReturnValue.class, SplitImageProcessor.class, SplitImageProcessorReturnValue.class })
@ReturnType(getReturnType = terra.shell.utils.system.ReturnType.ASYNCHRONOUS)
public class ProcessorCheckProcess extends JProcess {

	private ProcessorCheckReturnValue rv;
	private module m;

	/**
	 * 
	 */
	private static final long serialVersionUID = 3950880510819802732L;

	public ProcessorCheckProcess() {
		super();
	}

	public ProcessorCheckProcess(module m) {
		super();
		this.m = m;
	}

	@Override
	public String getName() {
		return "ProcessorCheck";
	}

	@Override
	public boolean start() {
		if (rv != null)
			rv.setValues(Runtime.getRuntime().availableProcessors());
		else {
			m.addNumCores(Runtime.getRuntime().availableProcessors());
		}
		return true;
	}

	@Override
	public void processReturn(ReturnValue rv) {
		getLogger().log("Got ReturnValue");
		m.addNumCores((int) rv.getReturnValue());
	}

	@Override
	public void createReturn() {
		if (rv == null)
			rv = new ProcessorCheckReturnValue(this);
	}

	@Override
	public ReturnValue<Integer> getReturn() {
		return rv;
	}

}
