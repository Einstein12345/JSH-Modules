package ImageProcessingBenchmark;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.Inet4Address;
import java.net.URL;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import terra.shell.launch.Launch;
import terra.shell.logging.LogManager;
import terra.shell.modules.ModuleEvent.DummyEvent;
import terra.shell.utils.streams.NullInputStream;

public class module extends terra.shell.modules.Module {

	private Hashtable<Inet4Address, Integer> availableCores;
	private boolean started = false;
	private int numNodes = -1;
	private int imgCheckComplete = 0;

	@Override
	public String getName() {
		return "ImageProcessing Benchmark Module";
	}

	@Override
	public void run() {
		while (!started) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		log.log("Starting Image Processing Benchmark...");
		log.log("Gathering network node resource info");
		final long startTimeProcessorCheck = System.currentTimeMillis();
		while (availableCores.size() != numNodes) {
			try {
				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		final long endTimeProcessorCheck = System.currentTimeMillis();
		final long diffTimeProcessorCheck = endTimeProcessorCheck - startTimeProcessorCheck;
		log.log("ProcessorCheck took " + diffTimeProcessorCheck + " milliseconds");
		log.log("Found " + availableCores + " total cores on network");
		log.log("Attempting to load reference image");
		BufferedImage img;
		try {
			img = ImageIO.read(new URL("https://i.imgur.com/BHPUd0d.jpg"));
			if (img == null) {
				throw new NullPointerException("Image is null");
			}
			log.log("Got reference image");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		final ImageProcessor imgProc = new ImageProcessor(img, this);
		log.log("Starting ImageProcessor resize testing over " + numNodes + " nodes, utilizing " + availableCores
				+ " cores");
		final long startTimeImageProcessor = System.currentTimeMillis();
		imgProc.run();
		Launch.getConnectionMan().sendToAll(imgProc, LogManager.out, new NullInputStream());
		while (imgCheckComplete != numNodes) {
			try {
				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		final long endTimeImageProcessor = System.currentTimeMillis();
		log.log("Completed node based ImageProcessing resize test");
		log.log("Finished in " + (endTimeImageProcessor - startTimeImageProcessor) + " milliseconds");
		log.log("Beginning local only resize test...");
		// TODO Complete image resize operation for each
		final long startTimeLocalResize = System.currentTimeMillis();
		for (int i = 0; i < availableCores.size(); i++) {
			img.getScaledInstance(img.getHeight() / 4, img.getWidth() / 4, Image.SCALE_SMOOTH);
		}
		final long endTimeLocalResize = System.currentTimeMillis();
		final long diffTimeLocalResize = endTimeLocalResize - startTimeLocalResize;
		log.log("Local resize test done in " + diffTimeLocalResize + " milliseconds");

		log.log("Setting up singular image resize operation test...");
		img = null;
		System.gc();

		try {
			img = ImageIO.read(new URL("https://wallpaperaccess.com/full/82311.jpg"));
			if (img == null) {
				throw new NullPointerException("Image is null");
			}
			log.log("Got reference image");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		BufferedImage[] imgSplit = new BufferedImage[availableCores.size()];
		int splitWidth = img.getWidth() / imgSplit.length;
		int splitHeight = img.getHeight() / imgSplit.length;
		int x = 0, y = 0;
		for (int i = 0; i < imgSplit.length; i++) {
			imgSplit[i] = img.getSubimage(x, y, splitWidth, splitHeight);
			x += splitWidth;
			if (splitWidth > img.getWidth()) {
				y += splitHeight;
				if (splitHeight > img.getHeight()) {
					log.log("Ran out of room, looping image around...");
					x = 0;
					y = 0;
				}
			}
		}
		// TODO write SplitImageProcessor class to take split image and resize it
		// correctly, then return resized image
	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public String getAuthor() {
		return null;
	}

	@Override
	public String getOrg() {
		return null;
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void trigger(DummyEvent event) {
		ProcessorCheckProcess p = new ProcessorCheckProcess(this);
		p.run();
		Launch.getConnectionMan().sendToAll(p, LogManager.out, new NullInputStream());
		started = true;
		numNodes = Launch.getConnectionMan().numberOfNodes();
	}

	public void addNumCores(Inet4Address ip, Integer num) {
		availableCores.put(ip, num);
	}

	public void imgProcComplete() {
		imgCheckComplete++;
	}

}
