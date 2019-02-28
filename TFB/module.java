package TFB;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent;

public class module extends Module {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8404813979600711288L;
	private static Hashtable<Integer, Window> windows = new Hashtable<Integer, Window>();
	Hashtable<String, String> defs = new Hashtable<String, String>();
	Hashtable<String, String> vars = new Hashtable<String, String>();
	public static Hashtable<String, Image> theme = new Hashtable<String, Image>();
	public static TFB_Serv server;

	public module() {
		defs.put("/config/TFB/main.config", "tfb:true\n" + "xvfb:false\n"
				+ "touch:false\n" + "use:/dev/fb0\n" + "res:320.240");
		defs.put("/config/TFB/locations.config", "icons:/res/TFB/icons\n"
				+ "themes:/res/TFB/themes");
	}

	@Override
	public String getName() {
		return "TFB";
	}

	@Override
	public void run() {

	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "0.1";
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "DS";
	}

	private native void updateScreen(int[] pix);

	@Override
	public String getOrg() {
		// TODO Auto-generated method stub
		return "T3RRA";
	}

	@Override
	public void onEnable() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				log.log("TFB Enabled!");
				log.log("Searching for config files...");
				File[] con = new File[] { new File("/config/TFB/main.config"),
						new File("/config/TFB/locations.config") };
				for (File f : con) {
					if (!f.exists()) {
						log.log("File: " + f.getAbsolutePath()
								+ " is not found, creating");
						try {
							// f.mkdirs();
							f.createNewFile();
							PrintStream out = new PrintStream(
									new FileOutputStream(f));
							out.println(defs.get(f.getAbsolutePath()));
							out.close();
						} catch (Exception e) {
							e.printStackTrace();
							log.log("Unable to create file: "
									+ f.getAbsolutePath());
						}
					}
				}
				log.log("Populating Variables");
				vars.put("tfb", "true");
				vars.put("xvfb", "false");
				vars.put("touch", "false");
				vars.put("use", "/dev/fb0");
				vars.put("icons", "/res/TFB/icons");
				vars.put("themes", "/res/TFB/themes");
				vars.put("res", "320.240");
				for (File f : con) {
					if (f.exists()) {
						try {
							Scanner sc = new Scanner(new FileInputStream(f));
							while (sc.hasNext()) {
								String s = sc.nextLine();
								String[] v = s.split(":");
								try {
									vars.put(v[0], v[1]);
									log.log("Setting Variable " + v[0] + " as "
											+ v[1]);
								} catch (Exception e) {
									if (e instanceof ArrayIndexOutOfBoundsException) {
										log.log("Incomplete Variable declaration in file "
												+ f + ": " + s);
									} else {
										e.printStackTrace();
										log.log("Unknown error while reading variables in "
												+ f);
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							log.log("Cannot read file: " + f);
						}
					}
				}
				log.log("Finished Variable Population");
				log.log("Finding default launcher theme");
				if (vars.get("themes") != null) {
					File f = new File(vars.get("themes"));
					if (f.exists() && f.isDirectory()) {
						File[] to = f.listFiles();
						for (File i : to) {
							try {
								theme.put(i.getName(), ImageIO.read(i));
								log.log("Theme File Loaded: " + i);
							} catch (Exception e) {
								log.log("Unable to read Theme File: " + i);
							}
						}
					} else {
						log.log("Themes folder " + f.getAbsolutePath()
								+ " not found!");
					}
				}

				Thread draw = new Thread(new Runnable() {
					public void run() {
						Timer t = new Timer(0, new Runner());
						t.setDelay(10);
						t.start();
					}
				});
				draw.setName("TFBD");
				draw.start();

				log.log("Starting TFB Server...");
				server = new TFB_Serv();
				server.run();
			}
		});
		t.setName("TFBModule");
		t.start();
	}

	public static void addWindow(Window w) {
		for (int a = 0; a < windows.size(); a++) {
			final Hashtable<Integer, Window> temp = windows;
			windows.put(a + 1, temp.get(a));
		}
	}

	public static TFB_Serv getServer() {
		return server;
	}

	class Runner implements ActionListener {
		private BufferedImage fb;
		private boolean allgood = true;
		private File f = new File(vars.get("use"));
		// JFrame test = new JFrame("Test");
		boolean launch;

		public Runner() {
			try {
				log.log(vars.get("use"));
				if (f.exists()) {
					fb = ImageIO.read(f);
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.log("Failed to create virtual FB!");
				allgood = false;
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String[] res = vars.get("res").split("\\.");
			int width = Integer.parseInt(res[0]);
			int height = Integer.parseInt(res[1]);
			final BufferedImage buffer = new BufferedImage(width, height,
					BufferedImage.TYPE_BYTE_BINARY);
			if (allgood) {
				Graphics2D g = (Graphics2D) buffer.getGraphics();
				for (int i = 0; i < windows.size(); i++) {
					BufferedImage im = windows.get(i).update();
					g.drawImage(im, 0, 0, im.getWidth(), im.getHeight(), null);
				}
				if (launch) {
					// test.repaint();
				}
				try {
					byte[] b = ((DataBufferByte) buffer.getData()
							.getDataBuffer()).getData();
					FileOutputStream fout = new FileOutputStream(f);
					fout.write(b, 0, b.length);
					fout.flush();
					fout.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			if (!launch) {
				try {
					// if (!GraphicsEnvironment.isHeadless()) {
					// test = new JFrame("Test") {
					// public void paint(Graphics g) {
					// g.drawImage(buffer, 0, 0, buffer.getWidth(),
					// buffer.getHeight(), this);
					// }
					// };
					// test.setSize(700, 1000);
					// test.setVisible(true);
					launch = true;
					// }
				} catch (Exception e1) {

				}
			}

		}

	}

	@Override
	public void trigger(ModuleEvent.DummyEvent me) {
		// TODO Auto-generated method stub

	}

}
