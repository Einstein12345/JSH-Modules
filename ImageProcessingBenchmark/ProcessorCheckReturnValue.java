package ImageProcessingBenchmark;

import java.net.Inet4Address;

import terra.shell.logging.LogManager;
import terra.shell.utils.JProcess;
import terra.shell.utils.ReturnValue;

public class ProcessorCheckReturnValue extends ReturnValue<Integer> {
	private Integer numCpu;

	public ProcessorCheckReturnValue(JProcess p) {
		super(p);
		numCpu = Integer.valueOf(Runtime.getRuntime().availableProcessors());
	}

	@Override
	public boolean processReturn(Object... values) {
		return true;
	}

	@Override
	public boolean setValues(Integer values) {
		numCpu = values;
		LogManager.write("PCRV: " + values + "\n");
		return true;
	}

	@Override
	public Integer getReturnValue() {
		return numCpu;
	}

}
