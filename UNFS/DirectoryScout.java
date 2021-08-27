package UNFS;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import terra.shell.config.Configuration;
import terra.shell.launch.Launch;
import terra.shell.utils.JProcess;
import terra.shell.utils.JProcess.Depends;
import terra.shell.utils.JProcess.ReturnType;
import terra.shell.utils.ReturnValue;
import terra.shell.utils.system.ByteClassLoader.Replaceable;

@Depends(dependencies = DirectoryScoutReturnValue.class)
@ReturnType(getReturnType = terra.shell.utils.system.ReturnType.ASYNCHRONOUS)
@Replaceable(replaceable = false)
public class DirectoryScout extends JProcess {
	protected ReturnValue rv;
	private static final long serialVersionUID = 6788713984586951441L;
	private String[] zipFsPaths;

	@Override
	public String getName() {
		return "UNFS-DirectoryScout";
	}

	@Override
	public boolean start() {
		// TODO Identify where to place Compressed file with all accessible file
		// information
		// TODO Read compressed file name contents to ascertain directory structuring

		// Open top level directory file.
		Configuration config = Launch.getConfig("UNFS");
		if (config == null) {
			config = new Configuration(new File(Launch.getConfD() + "/UNFS"));
			File f = new File(System.getProperty("user.home") + "/.unfs");
			if (!f.exists()) {
				try {
					getLogger().log("Creating UNFS FileTable at " + f.getAbsolutePath());
					ZipOutputStream zOut = new ZipOutputStream(new FileOutputStream(f));
					zOut.putNextEntry(new ZipEntry("/"));
					zOut.closeEntry();
					zOut.flush();
					zOut.close();
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			config.setValue("UNFS-FileTable", System.getProperty("user.home") + "/.unfs");
			config.setValue("UNFS-Allow", "true");
		}
		boolean unfsAllow = Boolean.parseBoolean((String) config.getValue("UNFS-Allow"));
		if (!unfsAllow)
			return true;
		String ftPath = (String) config.getValue("UNFS-FileTable");
		File ft = new File(ftPath);
		if (!ft.exists()) {
			return false;
		}
		try {
			ZipFile zipFs = new ZipFile(ft);
			Enumeration<? extends ZipEntry> entries = zipFs.entries();
			zipFsPaths = new String[zipFs.size()];
			int i = 0;
			while (entries.hasMoreElements()) {
				zipFsPaths[i] = entries.nextElement().getName();
			}
			if(rv == null) {
				createReturn();
			}
			rv.setValues(zipFsPaths);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void createReturn() {
		this.rv = new DirectoryScoutReturnValue(this, zipFsPaths);
	}

	@Override
	public void processReturn(ReturnValue rv) {
		if (rv instanceof DirectoryScoutReturnValue) {
			DirectoryScoutReturnValue drv = (DirectoryScoutReturnValue) rv;
			
		}
	}

}
