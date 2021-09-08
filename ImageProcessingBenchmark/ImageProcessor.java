package ImageProcessingBenchmark;

import java.awt.Image;
import java.awt.image.BufferedImage;

import terra.shell.utils.JProcess;
import terra.shell.utils.JProcess.Depends;
import terra.shell.utils.JProcess.ReturnType;
import terra.shell.utils.ReturnValue;

@Depends(dependencies = ImageProcessorReturnValue.class)
@ReturnType(getReturnType = terra.shell.utils.system.ReturnType.ASYNCHRONOUS)
public class ImageProcessor extends JProcess {
	public static int procsDone = 0;
	private ImageProcessorReturnValue rv;
	private final BufferedImage image;
	private final module m;

	public ImageProcessor(BufferedImage image, module m) {
		this.image = image;
		this.m = m;
	}

	public void createReturn() {
		rv = new ImageProcessorReturnValue(this);
	}

	public ReturnValue getReturn() {
		return rv;
	}

	@Override
	public String getName() {
		return "ImageProcessor";
	}

	@Override
	public boolean start() {
		// TODO Complete image resize operations for each core available
		int numCores = Runtime.getRuntime().availableProcessors();
		for (int i = 0; i < numCores; i++) {
			JProcess p = new JProcess() {

				@Override
				public String getName() {
					return ("Image Proc");
				}

				@Override
				public boolean start() {
					image.getScaledInstance(image.getWidth() / 4, image.getHeight() / 4, Image.SCALE_SMOOTH);
					procsDone++;
					return true;
				}

			};
			p.run();
		}
		while (procsDone != numCores) {
			;
		}
		return true;
	}

	@Override
	public void processReturn(ReturnValue e) {
		if (e instanceof ImageProcessorReturnValue) {
			m.imgProcComplete();
		}
		if (e instanceof ProcessorCheckReturnValue) {
			ProcessorCheckReturnValue pcrk = (ProcessorCheckReturnValue) e;
			m.addNumCores(pcrk.getAddress(), pcrk.getReturnValue());
		}
	}

}
