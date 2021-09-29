package ImageProcessingBenchmark;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import terra.shell.emulation.concurrency.helper.SerializableImage;
import terra.shell.utils.JProcess;

public class LocalImageResizer extends JProcess {
	private SerializableImage image;
	private boolean isDone;

	public LocalImageResizer(SerializableImage image) {
		this.image = image;
	}

	@Override
	public String getName() {
		return ("Image Proc");
	}

	@Override
	public boolean start() {
		Image scaled = image.getScaledInstance(image.getWidth() / 4, image.getHeight() / 4, Image.SCALE_SMOOTH);
		BufferedImage img = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null), image.getType());
		img.getGraphics().drawImage(scaled, 0, 0, scaled.getWidth(null), scaled.getHeight(null), null);
		try {
			ImageIO.write(img, getName(), OutputStream.nullOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		isDone = true;
		return true;
	}

}
