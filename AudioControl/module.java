package AudioControl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Scanner;

import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;
import terra.shell.utils.keys.Event;
import terra.shell.utils.system.EventListener;
import terra.shell.utils.system.EventManager;
import terra.shell.utils.system.GeneralEvent;

public class module extends Module {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6118339495649128502L;
	private Socket s;
	private Socket sa;

	private ServerSocket ss;
	private ServerSocket audio;

	private boolean ok;

	private InetAddress curMain;

	private InputStream main;

	private Thread buffer;

	private Hashtable<String, Socket> audioCon = new Hashtable<String, Socket>();
	private Hashtable<String, InputStream> audioIn = new Hashtable<String, InputStream>();

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "AudioController"; 
	}

	@Override
	public void run() {
		EventManager.registerListener(new AudioListener(), "AUDIO");
		startBuffer();
		if (ok) {
			try {
				audio = new ServerSocket();
			} catch (Exception e) {
				e.printStackTrace();
				ok = false;
			}
			Thread t = new Thread(new Runnable() {
				public void run() {
					getLogger().log("Starting AudioIn Server...");
					while (ok) {
						try {
							Socket sa = audio.accept();
							audioCon.put(sa.getInetAddress().getHostAddress(),
									sa);
							audioIn.put(sa.getInetAddress().getHostAddress(),
									sa.getInputStream());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			t.setName("[AudioController] AudioIn");
			t.start();
			getLogger().log("Starting Client Connection Server...");
			while (true) {
				try {
					s = ss.accept();
				} catch (Exception e) {
					e.printStackTrace();
					getLogger().log("Failed to accept a connection! ");
					return;
				}

			}
		}
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

	@Override
	public String getOrg() {
		// TODO Auto-generated method stub
		return "T3RRA";
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		EventManager.registerEvType("audio");
		try {
			ss = new ServerSocket(2014);
			audio = new ServerSocket(2015);
			ok = true;
		} catch (Exception e) {
			e.printStackTrace();
			ok = false;
		}
	}

	@Override
	public void trigger(DummyEvent event) {
		// TODO Auto-generated method stub

	}

	private class Handler {
		public void handle(final Socket s) {
			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						Scanner sc = new Scanner(s.getInputStream());
						PrintWriter out = new PrintWriter(s.getOutputStream());

						String tmp;
						while (!(tmp = sc.nextLine()).equals("EXIT")) {
							String[] tp = tmp.split(":");
							if (tp[0].equals("change")) {
								if (audioCon.containsKey(s.getInetAddress()
										.getHostAddress())) {
									if (audioIn.containsKey(s.getInetAddress()
											.getHostAddress())) {
										main = audioIn.get(s.getInetAddress()
												.getHostAddress());
										curMain = s.getInetAddress();
										AudioEvent ae = new AudioEvent(
												AudioEvent.TYPE_CHANGE);
										EventManager.invokeEvent(ae);
									}
								}
							}
							if (tp[0].equals("pause")) {
								AudioEvent ae = new AudioEvent(
										AudioEvent.TYPE_PAUSE);
								EventManager.invokeEvent(ae);
							}
							if (tp[0].equals("start")) {
								AudioEvent ae = new AudioEvent(
										AudioEvent.TYPE_START);
								EventManager.invokeEvent(ae);
							}
							if (tp[0].equals("skip")) {
								AudioEvent ae = new AudioEvent(
										AudioEvent.TYPE_SKIP);
								EventManager.invokeEvent(ae);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (curMain == s.getInetAddress()) {
						curMain = null;
						main = null;
					}
					audioCon.remove(s.getInetAddress().getHostAddress());
					audioIn.remove(s.getInetAddress().getHostAddress());
					return;
				}
			});
			t.setName("[AudioController] HANDLE:"
					+ s.getInetAddress().getHostAddress());
			t.start();
		}
	}

	@SuppressWarnings("unused")
	public final class AudioEvent extends GeneralEvent {
		public static final int TYPE_CHANGE = 0;
		public static final int TYPE_PAUSE = 1;
		public static final int TYPE_START = 2;
		public static final int TYPE_STOP = 3;
		public static final int TYPE_SKIP = 4;
		public static final int TYPE_PREV = 5;
		public static final int TYPE_VOL_UP = 6;
		public static final int TYPE_VOL_DOWN = 7;

		// private final int TYPE
		public AudioEvent(int type) {
			super("AUDIO", type);
		}

	}

	private void startBuffer() {
		if (buffer != null && buffer.isAlive()) {
			buffer.interrupt();
			buffer = null;
		}
		buffer = new Thread(new Runnable() {
			public void run() {
				try {
					final File aO = new File("/tmp/AUDIO_OUT");
					if (!aO.exists()) {
						File.createTempFile("AUDIO_OUT", "", new File("/tmp"));
					}
					BufferedInputStream in = new BufferedInputStream(main);
					BufferedOutputStream out = new BufferedOutputStream(
							new FileOutputStream(aO));
					int b;
					while (!Thread.interrupted()) {
						try {
							b = in.read();
							out.write(b);
							out.flush();
							out = null;
							out = new BufferedOutputStream(
									new FileOutputStream(aO));
						} catch (Exception e) {
							break;
						}
					}
					out.close();
					out = null;
					in.close();
					in = null;
					return;
				} catch (NullPointerException e) {
					try {
						Thread.sleep(10);
					} catch (Exception e1) {
					}
					run();
				} catch (Exception e) {
					e.printStackTrace();
					// run();
					return;
				}
				return;
			}
		});
	}

	private class AudioListener extends EventListener {

		@Override
		public void trigger(Event e) {
			try {
				if (e != null) {
					GeneralEvent ge = (GeneralEvent) e;
					Object[] args = ge.getArgs();
					if (args[0].equals("change"))
						startBuffer();
				}
			} catch (Exception e1) {

			}
		}

	}
}
