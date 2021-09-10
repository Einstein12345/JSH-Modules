package ImageProcessingBenchmark;

import java.awt.image.BufferedImage;

import terra.shell.utils.JProcess;

public class SplitImageProcessor extends JProcess {

	public SplitImageProcessor(module m, BufferedImage img, int x, int y) {

	}

	@Override
	public String getName() {
		return "SplitImageProcessor";
	}

	@Override
	public boolean start() {
		return false;
	}

}
