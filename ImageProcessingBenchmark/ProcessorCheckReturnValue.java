package ImageProcessingBenchmark;

import java.net.Inet4Address;

import terra.shell.utils.JProcess;
import terra.shell.utils.ReturnValue;

public class ProcessorCheckReturnValue extends ReturnValue<Integer> {
	private final Integer numCpu;
	private Inet4Address localAddress;

	public ProcessorCheckReturnValue(JProcess p) {
		super(p);
		numCpu = Integer.valueOf(Runtime.getRuntime().availableProcessors());
		try {
			localAddress = (Inet4Address) Inet4Address.getLocalHost();
		} catch (Exception e) {
			localAddress = (Inet4Address) Inet4Address.getLoopbackAddress();
		}
	}

	@Override
	public boolean processReturn(Object... values) {
		return true;
	}

	@Override
	public boolean setValues(Integer values) {
		return true;
	}

	@Override
	public Integer getReturnValue() {
		return numCpu;
	}

	public Inet4Address getAddress() {
		return localAddress;
	}

}
