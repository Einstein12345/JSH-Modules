package Darla;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import terra.shell.config.Configuration;
import terra.shell.launch.Launch;
import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;

public class module extends Module {

	private Configuration darlaConf;
	private URLClassLoader voskLoader;

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Darla";
	}

	@Override
	public void run() {
		try {
			new Darla(darlaConf);
		} catch (Exception e) {
			e.printStackTrace();
			log.err("Darla failed to launch");
		}
	}

	@Override
	public String getVersion() {
		// TODO Auto-generated method stub
		return "0.1.1";
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return "D.S.";
	}

	@Override
	public String getOrg() {
		// TODO Auto-generated method stub
		return "T3RRA";
	}

	@Override
	public void onEnable() {
		// Import vosk library
		log.log("Enabling Darla");
		darlaConf = Launch.getConfig("Darla");
		if (darlaConf == null) {
			log.log("Configuration not found, creating...");
			darlaConf = new Configuration(new File(Launch.getConfD() + "/Darla"));
			darlaConf.setValue("vosk-loc", Launch.getConfD() + "/DarlaLib/vosk.jar");
			darlaConf.setValue("vosk-model-loc", Launch.getConfD() + "/DarlaLib/en_model.zip");
			darlaConf.setValue("jna-loc", "/DarlaLib/jna.jar");
			log.log("Created configuration with defaults");
			File darlaLib = new File(Launch.getConfD() + "/DarlaLib");
			if (!darlaLib.exists())
				try {
					darlaLib.mkdir();
				} catch (Exception e) {
					e.printStackTrace();
					log.err("FAILED TO START DARLA");
					return;
				}
			;
			File voskJar = new File(darlaLib, "vosk.jar");
			if (!voskJar.exists()) {
				log.log("Vosk library not found, attempting download");
				try {
					URL voskJarUrl = new URL(
							"https://alphacephei.com/maven/com/alphacephei/vosk/0.3.27/vosk-0.3.27.jar");
					FileOutputStream fOut = new FileOutputStream(voskJar);
					voskJarUrl.openConnection();
					ReadableByteChannel jarChannel = Channels.newChannel(voskJarUrl.openStream());
					FileChannel jarOut = fOut.getChannel();
					jarOut.transferFrom(jarChannel, 0, Long.MAX_VALUE);
					fOut.flush();
					fOut.close();
				} catch (IOException e) {
					e.printStackTrace();
					log.err("FAILED TO DOWNLOAD Vosk LIB");
					return;
				}
				log.log("Downloaded Vosk library");
			}
			File voskModel = new File(darlaLib, "en_model.zip");
			if (!voskModel.exists()) {
				log.log("Vosk language model not found, attempting download");
				try {
					URL voskModelUrl = new URL(
							"https://alphacephei.com/vosk/models/vosk-model-en-us-daanzu-20200905.zip");
					FileOutputStream fOut = new FileOutputStream(voskModel);
					voskModelUrl.openConnection();
					ReadableByteChannel zipChannel = Channels.newChannel(voskModelUrl.openStream());
					FileChannel zipOut = fOut.getChannel();
					zipOut.transferFrom(zipChannel, 0, Long.MAX_VALUE);
					fOut.flush();
					fOut.close();
				} catch (Exception e) {
					e.printStackTrace();
					log.err("FAILED TO DOWNLOAD VOSK LANGUAGE MODEL");
					return;
				}
				log.log("Downloaded Vosk language model");
			}

		}
		// TODO Check if JNA Exists in classpath, if not download, and load

		File voskJar = new File((String) darlaConf.getValue("vosk-loc"));
		try {
			log.log("Searching for JNA on classpath...");
			JarFile vosk = new JarFile(voskJar);
			Enumeration<JarEntry> entries = vosk.entries();
			URL[] voskUrls = { new URL("jar:file:" + voskJar.getAbsolutePath() + "!/") };
			voskLoader = this.getClass().getClassLoader();
			voskLoader = URLClassLoader.newInstance(voskUrls);

			try {
				this.getClass().getClassLoader().loadClass("com/sun/jna/PointerType");
			} catch (Exception e) {
				log.log("JNA not found on classpath, downloading");
				File jnaJar = new File(Launch.getConfD(), (String) darlaConf.getValue("jna-loc"));
				if (!jnaJar.exists()) {
					try {
						URL jnaUrl = new URL("https://repo1.maven.org/maven2/net/java/dev/jna/jna/5.8.0/jna-5.8.0.jar");
						jnaUrl.openConnection();
						ReadableByteChannel jnaChannel = Channels.newChannel(jnaUrl.openStream());
						FileOutputStream fOut = new FileOutputStream(jnaJar);
						FileChannel jnaOut = fOut.getChannel();
						jnaOut.transferFrom(jnaChannel, 0, Long.MAX_VALUE);
						fOut.flush();
						fOut.close();
						log.log("JNA library downloaded");
					} catch (Exception e1) {
						e1.printStackTrace();
						log.err("Failed to download JNA library");
						return;
					}
				}
				log.log("Loading JNA Library...");
				try {
					JarFile jnaJarFile = new JarFile(jnaJar);
					Enumeration<JarEntry> jnaEntries = jnaJarFile.entries();
					URL[] jnaUrls = { new URL("jar:file:" + jnaJar.getAbsolutePath() + "!/"),
							new URL("jar:file:" + voskJar.getAbsolutePath() + "!/") };
					voskLoader = URLClassLoader.newInstance(jnaUrls);
					while (jnaEntries.hasMoreElements()) {
						JarEntry el = jnaEntries.nextElement();
						if (el.isDirectory() || !el.getName().endsWith(".class")) {
							continue;
						}
						String className = el.getName().replace('/', '.').substring(0, el.getName().length() - 6);
						Class<?> c = voskLoader.loadClass(className);
					}
					jnaJarFile.close();
				} catch (Exception e1) {
					e1.printStackTrace();
					log.err("Failed to load JNA library");
					vosk.close();
					return;
				}
			}

			while (entries.hasMoreElements()) {
				JarEntry e = entries.nextElement();
				if (e.isDirectory() || !e.getName().endsWith(".class"))
					continue;
				String className = e.getName().replace('/', '.').substring(0, e.getName().length() - 6);
				Class<?> c = voskLoader.loadClass(className);
			}
			vosk.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.err("FAILED TO LOAD Vosk LIB");
			return;
		}

	}

	@Override
	public void trigger(DummyEvent event) {
		// TODO Auto-generated method stub

	}

}
