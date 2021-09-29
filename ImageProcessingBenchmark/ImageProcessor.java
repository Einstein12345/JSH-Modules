package ImageProcessingBenchmark;

import java.awt.Image;

import terra.shell.emulation.concurrency.helper.SerializableImage;
import terra.shell.utils.JProcess;
import terra.shell.utils.JProcess.Depends;
import terra.shell.utils.JProcess.ReturnType;
import terra.shell.utils.ReturnValue;

@Depends(dependencies = { ImageProcessorReturnValue.class, module.class, LocalImageResizer.class,
		SplitImageProcessor.class, SplitImageProcessorReturnValue.class, ProcessorCheckProcess.class,
		ProcessorCheckReturnValue.class })
@ReturnType(getReturnType = terra.shell.utils.system.ReturnType.ASYNCHRONOUS)
public class ImageProcessor extends JProcess {
	public static int procsDone = 0;
	private ImageProcessorReturnValue rv;
	private SerializableImage image;
	private module m;

	public ImageProcessor() {
	}

	public ImageProcessor(SerializableImage image, module m) {
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
		LocalImageResizer[] limArray = new LocalImageResizer[numCores];
		for (int i = 0; i < numCores; i++) {
			LocalImageResizer lim = new LocalImageResizer(image);
			lim.run();
			limArray[i] = lim;
		}
		while (procsDone != numCores) {
			for (int i = 0; i < limArray.length; i++) {
				LocalImageResizer lim = limArray[i];
				if (lim != null && !lim.isRunning()) {
					limArray[i] = null;
					procsDone++;
				}
			}
		}
		if (rv == null)
			m.imgProcComplete();
		return true;
	}

	@Override
	public void processReturn(ReturnValue e) {
		m.imgProcComplete();
	}

}
