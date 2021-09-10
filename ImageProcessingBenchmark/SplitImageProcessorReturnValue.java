package ImageProcessingBenchmark;

import java.awt.image.BufferedImage;

import terra.shell.utils.JProcess;
import terra.shell.utils.ReturnValue;

public class SplitImageProcessorReturnValue extends ReturnValue<BufferedImage> {
	private BufferedImage resizedImage;
	private final int x, y;

	public SplitImageProcessorReturnValue(JProcess p, int x, int y) {
		super(p);
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean processReturn(Object... values) {
		return false;
	}

	@Override
	public boolean setValues(BufferedImage values) {
		this.resizedImage = values;
		return true;
	}

	@Override
	public BufferedImage getReturnValue() {
		return resizedImage;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
