package Darla;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

import terra.shell.config.Configuration;
import terra.shell.launch.Launch;
import terra.shell.modules.Module;
import terra.shell.modules.ModuleEvent.DummyEvent;
import terra.shell.utils.JProcess;
import terra.shell.utils.system.ByteClassLoader;

public class module extends Module {

	private Configuration darlaConf;
	private File darlaLib = new File(Launch.getConfD() + "/DarlaLib");
	private ByteClassLoader voskLoader;
	private final int[] darlaBytesAsInt = { 0xca, 0xfe, 0xba, 0xbe, 0x00, 0x00, 0x00, 0x33, 0x00, 0xbe, 0x07, 0x00,
			0x02, 0x01, 0x00, 0x0b, 0x44, 0x61, 0x72, 0x6c, 0x61, 0x2f, 0x44, 0x61, 0x72, 0x6c, 0x61, 0x07, 0x00, 0x04,
			0x01, 0x00, 0x1a, 0x74, 0x65, 0x72, 0x72, 0x61, 0x2f, 0x73, 0x68, 0x65, 0x6c, 0x6c, 0x2f, 0x75, 0x74, 0x69,
			0x6c, 0x73, 0x2f, 0x4a, 0x50, 0x72, 0x6f, 0x63, 0x65, 0x73, 0x73, 0x01, 0x00, 0x07, 0x61, 0x75, 0x64, 0x69,
			0x6f, 0x49, 0x6e, 0x01, 0x00, 0x24, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x78, 0x2f, 0x73, 0x6f, 0x75, 0x6e, 0x64,
			0x2f, 0x73, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x64, 0x2f, 0x54, 0x61, 0x72, 0x67, 0x65, 0x74, 0x44, 0x61, 0x74,
			0x61, 0x4c, 0x69, 0x6e, 0x65, 0x3b, 0x01, 0x00, 0x04, 0x63, 0x6f, 0x6e, 0x66, 0x01, 0x00, 0x22, 0x4c, 0x74,
			0x65, 0x72, 0x72, 0x61, 0x2f, 0x73, 0x68, 0x65, 0x6c, 0x6c, 0x2f, 0x63, 0x6f, 0x6e, 0x66, 0x69, 0x67, 0x2f,
			0x43, 0x6f, 0x6e, 0x66, 0x69, 0x67, 0x75, 0x72, 0x61, 0x74, 0x69, 0x6f, 0x6e, 0x3b, 0x01, 0x00, 0x06, 0x3c,
			0x69, 0x6e, 0x69, 0x74, 0x3e, 0x01, 0x00, 0x25, 0x28, 0x4c, 0x74, 0x65, 0x72, 0x72, 0x61, 0x2f, 0x73, 0x68,
			0x65, 0x6c, 0x6c, 0x2f, 0x63, 0x6f, 0x6e, 0x66, 0x69, 0x67, 0x2f, 0x43, 0x6f, 0x6e, 0x66, 0x69, 0x67, 0x75,
			0x72, 0x61, 0x74, 0x69, 0x6f, 0x6e, 0x3b, 0x29, 0x56, 0x01, 0x00, 0x0a, 0x45, 0x78, 0x63, 0x65, 0x70, 0x74,
			0x69, 0x6f, 0x6e, 0x73, 0x07, 0x00, 0x0d, 0x01, 0x00, 0x2c, 0x6a, 0x61, 0x76, 0x61, 0x78, 0x2f, 0x73, 0x6f,
			0x75, 0x6e, 0x64, 0x2f, 0x73, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x64, 0x2f, 0x4c, 0x69, 0x6e, 0x65, 0x55, 0x6e,
			0x61, 0x76, 0x61, 0x69, 0x6c, 0x61, 0x62, 0x6c, 0x65, 0x45, 0x78, 0x63, 0x65, 0x70, 0x74, 0x69, 0x6f, 0x6e,
			0x01, 0x00, 0x04, 0x43, 0x6f, 0x64, 0x65, 0x0a, 0x00, 0x03, 0x00, 0x10, 0x0c, 0x00, 0x09, 0x00, 0x11, 0x01,
			0x00, 0x03, 0x28, 0x29, 0x56, 0x07, 0x00, 0x13, 0x01, 0x00, 0x0c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x69, 0x6f,
			0x2f, 0x46, 0x69, 0x6c, 0x65, 0x07, 0x00, 0x15, 0x01, 0x00, 0x17, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61,
			0x6e, 0x67, 0x2f, 0x53, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x42, 0x75, 0x69, 0x6c, 0x64, 0x65, 0x72, 0x0a, 0x00,
			0x14, 0x00, 0x10, 0x0a, 0x00, 0x18, 0x00, 0x1a, 0x07, 0x00, 0x19, 0x01, 0x00, 0x19, 0x74, 0x65, 0x72, 0x72,
			0x61, 0x2f, 0x73, 0x68, 0x65, 0x6c, 0x6c, 0x2f, 0x6c, 0x61, 0x75, 0x6e, 0x63, 0x68, 0x2f, 0x4c, 0x61, 0x75,
			0x6e, 0x63, 0x68, 0x0c, 0x00, 0x1b, 0x00, 0x1c, 0x01, 0x00, 0x08, 0x67, 0x65, 0x74, 0x43, 0x6f, 0x6e, 0x66,
			0x44, 0x01, 0x00, 0x10, 0x28, 0x29, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x69, 0x6f, 0x2f, 0x46, 0x69, 0x6c,
			0x65, 0x3b, 0x0a, 0x00, 0x14, 0x00, 0x1e, 0x0c, 0x00, 0x1f, 0x00, 0x20, 0x01, 0x00, 0x06, 0x61, 0x70, 0x70,
			0x65, 0x6e, 0x64, 0x01, 0x00, 0x2d, 0x28, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67, 0x2f,
			0x4f, 0x62, 0x6a, 0x65, 0x63, 0x74, 0x3b, 0x29, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67,
			0x2f, 0x53, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x42, 0x75, 0x69, 0x6c, 0x64, 0x65, 0x72, 0x3b, 0x08, 0x00, 0x22,
			0x01, 0x00, 0x09, 0x2f, 0x44, 0x61, 0x72, 0x6c, 0x61, 0x4c, 0x69, 0x62, 0x0a, 0x00, 0x14, 0x00, 0x24, 0x0c,
			0x00, 0x1f, 0x00, 0x25, 0x01, 0x00, 0x2d, 0x28, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67,
			0x2f, 0x53, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x3b, 0x29, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e,
			0x67, 0x2f, 0x53, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x42, 0x75, 0x69, 0x6c, 0x64, 0x65, 0x72, 0x3b, 0x0a, 0x00,
			0x14, 0x00, 0x27, 0x0c, 0x00, 0x28, 0x00, 0x29, 0x01, 0x00, 0x08, 0x74, 0x6f, 0x53, 0x74, 0x72, 0x69, 0x6e,
			0x67, 0x01, 0x00, 0x14, 0x28, 0x29, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67, 0x2f, 0x53,
			0x74, 0x72, 0x69, 0x6e, 0x67, 0x3b, 0x08, 0x00, 0x2b, 0x01, 0x00, 0x0a, 0x6c, 0x69, 0x62, 0x76, 0x6f, 0x73,
			0x6b, 0x2e, 0x73, 0x6f, 0x0a, 0x00, 0x12, 0x00, 0x2d, 0x0c, 0x00, 0x09, 0x00, 0x2e, 0x01, 0x00, 0x27, 0x28,
			0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67, 0x2f, 0x53, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x3b,
			0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67, 0x2f, 0x53, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x3b,
			0x29, 0x56, 0x08, 0x00, 0x30, 0x01, 0x00, 0x0d, 0x6c, 0x69, 0x62, 0x76, 0x6f, 0x73, 0x6b, 0x2e, 0x64, 0x79,
			0x6c, 0x69, 0x62, 0x0a, 0x00, 0x12, 0x00, 0x32, 0x0c, 0x00, 0x33, 0x00, 0x34, 0x01, 0x00, 0x06, 0x65, 0x78,
			0x69, 0x73, 0x74, 0x73, 0x01, 0x00, 0x03, 0x28, 0x29, 0x5a, 0x0a, 0x00, 0x01, 0x00, 0x36, 0x0c, 0x00, 0x37,
			0x00, 0x38, 0x01, 0x00, 0x09, 0x67, 0x65, 0x74, 0x4c, 0x6f, 0x67, 0x67, 0x65, 0x72, 0x01, 0x00, 0x1e, 0x28,
			0x29, 0x4c, 0x74, 0x65, 0x72, 0x72, 0x61, 0x2f, 0x73, 0x68, 0x65, 0x6c, 0x6c, 0x2f, 0x6c, 0x6f, 0x67, 0x67,
			0x69, 0x6e, 0x67, 0x2f, 0x4c, 0x6f, 0x67, 0x67, 0x65, 0x72, 0x3b, 0x08, 0x00, 0x3a, 0x01, 0x00, 0x12, 0x4c,
			0x6f, 0x61, 0x64, 0x69, 0x6e, 0x67, 0x20, 0x6c, 0x69, 0x62, 0x76, 0x6f, 0x73, 0x6b, 0x2e, 0x73, 0x6f, 0x0a,
			0x00, 0x3c, 0x00, 0x3e, 0x07, 0x00, 0x3d, 0x01, 0x00, 0x1a, 0x74, 0x65, 0x72, 0x72, 0x61, 0x2f, 0x73, 0x68,
			0x65, 0x6c, 0x6c, 0x2f, 0x6c, 0x6f, 0x67, 0x67, 0x69, 0x6e, 0x67, 0x2f, 0x4c, 0x6f, 0x67, 0x67, 0x65, 0x72,
			0x0c, 0x00, 0x3f, 0x00, 0x40, 0x01, 0x00, 0x05, 0x64, 0x65, 0x62, 0x75, 0x67, 0x01, 0x00, 0x15, 0x28, 0x4c,
			0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67, 0x2f, 0x53, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x3b, 0x29,
			0x56, 0x0a, 0x00, 0x12, 0x00, 0x42, 0x0c, 0x00, 0x43, 0x00, 0x29, 0x01, 0x00, 0x0f, 0x67, 0x65, 0x74, 0x41,
			0x62, 0x73, 0x6f, 0x6c, 0x75, 0x74, 0x65, 0x50, 0x61, 0x74, 0x68, 0x0a, 0x00, 0x45, 0x00, 0x47, 0x07, 0x00,
			0x46, 0x01, 0x00, 0x10, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67, 0x2f, 0x53, 0x79, 0x73, 0x74,
			0x65, 0x6d, 0x0c, 0x00, 0x48, 0x00, 0x40, 0x01, 0x00, 0x04, 0x6c, 0x6f, 0x61, 0x64, 0x08, 0x00, 0x4a, 0x01,
			0x00, 0x15, 0x4c, 0x6f, 0x61, 0x64, 0x69, 0x6e, 0x67, 0x20, 0x6c, 0x69, 0x62, 0x76, 0x6f, 0x73, 0x6b, 0x2e,
			0x64, 0x79, 0x6c, 0x69, 0x62, 0x08, 0x00, 0x4c, 0x01, 0x00, 0x1e, 0x46, 0x41, 0x49, 0x4c, 0x45, 0x44, 0x20,
			0x54, 0x4f, 0x20, 0x4c, 0x4f, 0x41, 0x44, 0x20, 0x6c, 0x69, 0x62, 0x76, 0x6f, 0x73, 0x6b, 0x20, 0x4c, 0x49,
			0x42, 0x52, 0x41, 0x52, 0x59, 0x0a, 0x00, 0x3c, 0x00, 0x4e, 0x0c, 0x00, 0x4f, 0x00, 0x40, 0x01, 0x00, 0x03,
			0x65, 0x72, 0x72, 0x09, 0x00, 0x51, 0x00, 0x53, 0x07, 0x00, 0x52, 0x01, 0x00, 0x11, 0x6f, 0x72, 0x67, 0x2f,
			0x76, 0x6f, 0x73, 0x6b, 0x2f, 0x4c, 0x6f, 0x67, 0x4c, 0x65, 0x76, 0x65, 0x6c, 0x0c, 0x00, 0x54, 0x00, 0x55,
			0x01, 0x00, 0x05, 0x44, 0x45, 0x42, 0x55, 0x47, 0x01, 0x00, 0x13, 0x4c, 0x6f, 0x72, 0x67, 0x2f, 0x76, 0x6f,
			0x73, 0x6b, 0x2f, 0x4c, 0x6f, 0x67, 0x4c, 0x65, 0x76, 0x65, 0x6c, 0x3b, 0x0a, 0x00, 0x57, 0x00, 0x59, 0x07,
			0x00, 0x58, 0x01, 0x00, 0x10, 0x6f, 0x72, 0x67, 0x2f, 0x76, 0x6f, 0x73, 0x6b, 0x2f, 0x4c, 0x69, 0x62, 0x56,
			0x6f, 0x73, 0x6b, 0x0c, 0x00, 0x5a, 0x00, 0x5b, 0x01, 0x00, 0x0b, 0x73, 0x65, 0x74, 0x4c, 0x6f, 0x67, 0x4c,
			0x65, 0x76, 0x65, 0x6c, 0x01, 0x00, 0x16, 0x28, 0x4c, 0x6f, 0x72, 0x67, 0x2f, 0x76, 0x6f, 0x73, 0x6b, 0x2f,
			0x4c, 0x6f, 0x67, 0x4c, 0x65, 0x76, 0x65, 0x6c, 0x3b, 0x29, 0x56, 0x07, 0x00, 0x5d, 0x01, 0x00, 0x1f, 0x6a,
			0x61, 0x76, 0x61, 0x78, 0x2f, 0x73, 0x6f, 0x75, 0x6e, 0x64, 0x2f, 0x73, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x64,
			0x2f, 0x41, 0x75, 0x64, 0x69, 0x6f, 0x46, 0x6f, 0x72, 0x6d, 0x61, 0x74, 0x04, 0x47, 0x3b, 0x80, 0x00, 0x0a,
			0x00, 0x5c, 0x00, 0x60, 0x0c, 0x00, 0x09, 0x00, 0x61, 0x01, 0x00, 0x08, 0x28, 0x46, 0x49, 0x49, 0x5a, 0x5a,
			0x29, 0x56, 0x0a, 0x00, 0x63, 0x00, 0x65, 0x07, 0x00, 0x64, 0x01, 0x00, 0x1f, 0x6a, 0x61, 0x76, 0x61, 0x78,
			0x2f, 0x73, 0x6f, 0x75, 0x6e, 0x64, 0x2f, 0x73, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x64, 0x2f, 0x41, 0x75, 0x64,
			0x69, 0x6f, 0x53, 0x79, 0x73, 0x74, 0x65, 0x6d, 0x0c, 0x00, 0x66, 0x00, 0x67, 0x01, 0x00, 0x11, 0x67, 0x65,
			0x74, 0x54, 0x61, 0x72, 0x67, 0x65, 0x74, 0x44, 0x61, 0x74, 0x61, 0x4c, 0x69, 0x6e, 0x65, 0x01, 0x00, 0x47,
			0x28, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x78, 0x2f, 0x73, 0x6f, 0x75, 0x6e, 0x64, 0x2f, 0x73, 0x61, 0x6d, 0x70,
			0x6c, 0x65, 0x64, 0x2f, 0x41, 0x75, 0x64, 0x69, 0x6f, 0x46, 0x6f, 0x72, 0x6d, 0x61, 0x74, 0x3b, 0x29, 0x4c,
			0x6a, 0x61, 0x76, 0x61, 0x78, 0x2f, 0x73, 0x6f, 0x75, 0x6e, 0x64, 0x2f, 0x73, 0x61, 0x6d, 0x70, 0x6c, 0x65,
			0x64, 0x2f, 0x54, 0x61, 0x72, 0x67, 0x65, 0x74, 0x44, 0x61, 0x74, 0x61, 0x4c, 0x69, 0x6e, 0x65, 0x3b, 0x09,
			0x00, 0x01, 0x00, 0x69, 0x0c, 0x00, 0x05, 0x00, 0x06, 0x09, 0x00, 0x01, 0x00, 0x6b, 0x0c, 0x00, 0x07, 0x00,
			0x08, 0x0a, 0x00, 0x6d, 0x00, 0x6f, 0x07, 0x00, 0x6e, 0x01, 0x00, 0x13, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c,
			0x61, 0x6e, 0x67, 0x2f, 0x45, 0x78, 0x63, 0x65, 0x70, 0x74, 0x69, 0x6f, 0x6e, 0x0c, 0x00, 0x70, 0x00, 0x11,
			0x01, 0x00, 0x0f, 0x70, 0x72, 0x69, 0x6e, 0x74, 0x53, 0x74, 0x61, 0x63, 0x6b, 0x54, 0x72, 0x61, 0x63, 0x65,
			0x08, 0x00, 0x72, 0x01, 0x00, 0x19, 0x46, 0x61, 0x69, 0x6c, 0x65, 0x64, 0x20, 0x74, 0x6f, 0x20, 0x72, 0x75,
			0x6e, 0x20, 0x63, 0x6f, 0x6e, 0x73, 0x74, 0x72, 0x75, 0x63, 0x74, 0x6f, 0x72, 0x01, 0x00, 0x0f, 0x4c, 0x69,
			0x6e, 0x65, 0x4e, 0x75, 0x6d, 0x62, 0x65, 0x72, 0x54, 0x61, 0x62, 0x6c, 0x65, 0x01, 0x00, 0x12, 0x4c, 0x6f,
			0x63, 0x61, 0x6c, 0x56, 0x61, 0x72, 0x69, 0x61, 0x62, 0x6c, 0x65, 0x54, 0x61, 0x62, 0x6c, 0x65, 0x01, 0x00,
			0x04, 0x74, 0x68, 0x69, 0x73, 0x01, 0x00, 0x0d, 0x4c, 0x44, 0x61, 0x72, 0x6c, 0x61, 0x2f, 0x44, 0x61, 0x72,
			0x6c, 0x61, 0x3b, 0x01, 0x00, 0x09, 0x6c, 0x69, 0x62, 0x4c, 0x6f, 0x63, 0x4e, 0x69, 0x78, 0x01, 0x00, 0x0e,
			0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x69, 0x6f, 0x2f, 0x46, 0x69, 0x6c, 0x65, 0x3b, 0x01, 0x00, 0x09, 0x6c,
			0x69, 0x62, 0x4c, 0x6f, 0x63, 0x4d, 0x61, 0x63, 0x01, 0x00, 0x01, 0x66, 0x01, 0x00, 0x21, 0x4c, 0x6a, 0x61,
			0x76, 0x61, 0x78, 0x2f, 0x73, 0x6f, 0x75, 0x6e, 0x64, 0x2f, 0x73, 0x61, 0x6d, 0x70, 0x6c, 0x65, 0x64, 0x2f,
			0x41, 0x75, 0x64, 0x69, 0x6f, 0x46, 0x6f, 0x72, 0x6d, 0x61, 0x74, 0x3b, 0x01, 0x00, 0x01, 0x65, 0x01, 0x00,
			0x15, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67, 0x2f, 0x45, 0x78, 0x63, 0x65, 0x70, 0x74,
			0x69, 0x6f, 0x6e, 0x3b, 0x01, 0x00, 0x0d, 0x53, 0x74, 0x61, 0x63, 0x6b, 0x4d, 0x61, 0x70, 0x54, 0x61, 0x62,
			0x6c, 0x65, 0x07, 0x00, 0x80, 0x01, 0x00, 0x20, 0x74, 0x65, 0x72, 0x72, 0x61, 0x2f, 0x73, 0x68, 0x65, 0x6c,
			0x6c, 0x2f, 0x63, 0x6f, 0x6e, 0x66, 0x69, 0x67, 0x2f, 0x43, 0x6f, 0x6e, 0x66, 0x69, 0x67, 0x75, 0x72, 0x61,
			0x74, 0x69, 0x6f, 0x6e, 0x01, 0x00, 0x07, 0x67, 0x65, 0x74, 0x4e, 0x61, 0x6d, 0x65, 0x08, 0x00, 0x83, 0x01,
			0x00, 0x05, 0x44, 0x61, 0x72, 0x6c, 0x61, 0x01, 0x00, 0x05, 0x73, 0x74, 0x61, 0x72, 0x74, 0x07, 0x00, 0x86,
			0x01, 0x00, 0x0e, 0x6f, 0x72, 0x67, 0x2f, 0x76, 0x6f, 0x73, 0x6b, 0x2f, 0x4d, 0x6f, 0x64, 0x65, 0x6c, 0x08,
			0x00, 0x88, 0x01, 0x00, 0x0e, 0x76, 0x6f, 0x73, 0x6b, 0x2d, 0x6d, 0x6f, 0x64, 0x65, 0x6c, 0x2d, 0x6c, 0x6f,
			0x63, 0x0a, 0x00, 0x7f, 0x00, 0x8a, 0x0c, 0x00, 0x8b, 0x00, 0x8c, 0x01, 0x00, 0x08, 0x67, 0x65, 0x74, 0x56,
			0x61, 0x6c, 0x75, 0x65, 0x01, 0x00, 0x26, 0x28, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67,
			0x2f, 0x53, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x3b, 0x29, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e,
			0x67, 0x2f, 0x4f, 0x62, 0x6a, 0x65, 0x63, 0x74, 0x3b, 0x07, 0x00, 0x8e, 0x01, 0x00, 0x10, 0x6a, 0x61, 0x76,
			0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67, 0x2f, 0x53, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x0a, 0x00, 0x85, 0x00, 0x90,
			0x0c, 0x00, 0x09, 0x00, 0x40, 0x07, 0x00, 0x92, 0x01, 0x00, 0x13, 0x6f, 0x72, 0x67, 0x2f, 0x76, 0x6f, 0x73,
			0x6b, 0x2f, 0x52, 0x65, 0x63, 0x6f, 0x67, 0x6e, 0x69, 0x7a, 0x65, 0x72, 0x0a, 0x00, 0x91, 0x00, 0x94, 0x0c,
			0x00, 0x09, 0x00, 0x95, 0x01, 0x00, 0x14, 0x28, 0x4c, 0x6f, 0x72, 0x67, 0x2f, 0x76, 0x6f, 0x73, 0x6b, 0x2f,
			0x4d, 0x6f, 0x64, 0x65, 0x6c, 0x3b, 0x46, 0x29, 0x56, 0x0a, 0x00, 0x91, 0x00, 0x97, 0x0c, 0x00, 0x98, 0x00,
			0x99, 0x01, 0x00, 0x0e, 0x61, 0x63, 0x63, 0x65, 0x70, 0x74, 0x57, 0x61, 0x76, 0x65, 0x46, 0x6f, 0x72, 0x6d,
			0x01, 0x00, 0x06, 0x28, 0x5b, 0x42, 0x49, 0x29, 0x5a, 0x0a, 0x00, 0x91, 0x00, 0x9b, 0x0c, 0x00, 0x9c, 0x00,
			0x29, 0x01, 0x00, 0x09, 0x67, 0x65, 0x74, 0x52, 0x65, 0x73, 0x75, 0x6c, 0x74, 0x0a, 0x00, 0x91, 0x00, 0x9e,
			0x0c, 0x00, 0x9f, 0x00, 0x29, 0x01, 0x00, 0x10, 0x67, 0x65, 0x74, 0x50, 0x61, 0x72, 0x74, 0x69, 0x61, 0x6c,
			0x52, 0x65, 0x73, 0x75, 0x6c, 0x74, 0x08, 0x00, 0xa1, 0x01, 0x00, 0x05, 0x64, 0x61, 0x72, 0x6c, 0x61, 0x0a,
			0x00, 0x8d, 0x00, 0xa3, 0x0c, 0x00, 0xa4, 0x00, 0xa5, 0x01, 0x00, 0x0a, 0x73, 0x74, 0x61, 0x72, 0x74, 0x73,
			0x57, 0x69, 0x74, 0x68, 0x01, 0x00, 0x15, 0x28, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67,
			0x2f, 0x53, 0x74, 0x72, 0x69, 0x6e, 0x67, 0x3b, 0x29, 0x5a, 0x07, 0x00, 0xa7, 0x01, 0x00, 0x10, 0x44, 0x61,
			0x72, 0x6c, 0x61, 0x2f, 0x50, 0x61, 0x72, 0x73, 0x65, 0x49, 0x6e, 0x70, 0x75, 0x74, 0x0a, 0x00, 0xa6, 0x00,
			0x90, 0x0a, 0x00, 0xa6, 0x00, 0xaa, 0x0c, 0x00, 0x84, 0x00, 0x34, 0x0b, 0x00, 0xac, 0x00, 0xae, 0x07, 0x00,
			0xad, 0x01, 0x00, 0x22, 0x6a, 0x61, 0x76, 0x61, 0x78, 0x2f, 0x73, 0x6f, 0x75, 0x6e, 0x64, 0x2f, 0x73, 0x61,
			0x6d, 0x70, 0x6c, 0x65, 0x64, 0x2f, 0x54, 0x61, 0x72, 0x67, 0x65, 0x74, 0x44, 0x61, 0x74, 0x61, 0x4c, 0x69,
			0x6e, 0x65, 0x0c, 0x00, 0xaf, 0x00, 0xb0, 0x01, 0x00, 0x04, 0x72, 0x65, 0x61, 0x64, 0x01, 0x00, 0x07, 0x28,
			0x5b, 0x42, 0x49, 0x49, 0x29, 0x49, 0x01, 0x00, 0x01, 0x6d, 0x01, 0x00, 0x10, 0x4c, 0x6f, 0x72, 0x67, 0x2f,
			0x76, 0x6f, 0x73, 0x6b, 0x2f, 0x4d, 0x6f, 0x64, 0x65, 0x6c, 0x3b, 0x01, 0x00, 0x01, 0x72, 0x01, 0x00, 0x15,
			0x4c, 0x6f, 0x72, 0x67, 0x2f, 0x76, 0x6f, 0x73, 0x6b, 0x2f, 0x52, 0x65, 0x63, 0x6f, 0x67, 0x6e, 0x69, 0x7a,
			0x65, 0x72, 0x3b, 0x01, 0x00, 0x06, 0x6e, 0x42, 0x79, 0x74, 0x65, 0x73, 0x01, 0x00, 0x01, 0x49, 0x01, 0x00,
			0x01, 0x62, 0x01, 0x00, 0x02, 0x5b, 0x42, 0x01, 0x00, 0x06, 0x72, 0x65, 0x73, 0x75, 0x6c, 0x74, 0x01, 0x00,
			0x12, 0x4c, 0x6a, 0x61, 0x76, 0x61, 0x2f, 0x6c, 0x61, 0x6e, 0x67, 0x2f, 0x53, 0x74, 0x72, 0x69, 0x6e, 0x67,
			0x3b, 0x07, 0x00, 0xb8, 0x01, 0x00, 0x0a, 0x53, 0x6f, 0x75, 0x72, 0x63, 0x65, 0x46, 0x69, 0x6c, 0x65, 0x01,
			0x00, 0x0a, 0x44, 0x61, 0x72, 0x6c, 0x61, 0x2e, 0x6a, 0x61, 0x76, 0x61, 0x00, 0x21, 0x00, 0x01, 0x00, 0x03,
			0x00, 0x00, 0x00, 0x02, 0x00, 0x02, 0x00, 0x05, 0x00, 0x06, 0x00, 0x00, 0x00, 0x02, 0x00, 0x07, 0x00, 0x08,
			0x00, 0x00, 0x00, 0x03, 0x00, 0x01, 0x00, 0x09, 0x00, 0x0a, 0x00, 0x02, 0x00, 0x0b, 0x00, 0x00, 0x00, 0x04,
			0x00, 0x01, 0x00, 0x0c, 0x00, 0x0e, 0x00, 0x00, 0x01, 0x8a, 0x00, 0x07, 0x00, 0x05, 0x00, 0x00, 0x00, 0xb8,
			0x2a, 0xb7, 0x00, 0x0f, 0xbb, 0x00, 0x12, 0x59, 0xbb, 0x00, 0x14, 0x59, 0xb7, 0x00, 0x16, 0xb8, 0x00, 0x17,
			0xb6, 0x00, 0x1d, 0x12, 0x21, 0xb6, 0x00, 0x23, 0xb6, 0x00, 0x26, 0x12, 0x2a, 0xb7, 0x00, 0x2c, 0x4d, 0xbb,
			0x00, 0x12, 0x59, 0xbb, 0x00, 0x14, 0x59, 0xb7, 0x00, 0x16, 0xb8, 0x00, 0x17, 0xb6, 0x00, 0x1d, 0x12, 0x21,
			0xb6, 0x00, 0x23, 0xb6, 0x00, 0x26, 0x12, 0x2f, 0xb7, 0x00, 0x2c, 0x4e, 0x2c, 0xb6, 0x00, 0x31, 0x99, 0x00,
			0x16, 0x2a, 0xb6, 0x00, 0x35, 0x12, 0x39, 0xb6, 0x00, 0x3b, 0x2c, 0xb6, 0x00, 0x41, 0xb8, 0x00, 0x44, 0xa7,
			0x00, 0x27, 0x2d, 0xb6, 0x00, 0x31, 0x99, 0x00, 0x16, 0x2a, 0xb6, 0x00, 0x35, 0x12, 0x49, 0xb6, 0x00, 0x3b,
			0x2d, 0xb6, 0x00, 0x41, 0xb8, 0x00, 0x44, 0xa7, 0x00, 0x0d, 0x2a, 0xb6, 0x00, 0x35, 0x12, 0x4b, 0xb6, 0x00,
			0x4d, 0xb1, 0xb2, 0x00, 0x50, 0xb8, 0x00, 0x56, 0xbb, 0x00, 0x5c, 0x59, 0x12, 0x5e, 0x10, 0x10, 0x04, 0x04,
			0x04, 0xb7, 0x00, 0x5f, 0x3a, 0x04, 0x2a, 0x19, 0x04, 0xb8, 0x00, 0x62, 0xb5, 0x00, 0x68, 0x2a, 0x2b, 0xb5,
			0x00, 0x6a, 0xa7, 0x00, 0x13, 0x3a, 0x04, 0x19, 0x04, 0xb6, 0x00, 0x6c, 0x2a, 0xb6, 0x00, 0x35, 0x12, 0x71,
			0xb6, 0x00, 0x3b, 0xb1, 0x00, 0x01, 0x00, 0x80, 0x00, 0xa4, 0x00, 0xa7, 0x00, 0x6d, 0x00, 0x03, 0x00, 0x73,
			0x00, 0x00, 0x00, 0x52, 0x00, 0x14, 0x00, 0x00, 0x00, 0x18, 0x00, 0x04, 0x00, 0x19, 0x00, 0x23, 0x00, 0x1a,
			0x00, 0x42, 0x00, 0x1b, 0x00, 0x49, 0x00, 0x1c, 0x00, 0x52, 0x00, 0x1d, 0x00, 0x59, 0x00, 0x1e, 0x00, 0x63,
			0x00, 0x1f, 0x00, 0x6c, 0x00, 0x20, 0x00, 0x73, 0x00, 0x21, 0x00, 0x76, 0x00, 0x22, 0x00, 0x7f, 0x00, 0x23,
			0x00, 0x80, 0x00, 0x26, 0x00, 0x86, 0x00, 0x27, 0x00, 0x96, 0x00, 0x28, 0x00, 0x9f, 0x00, 0x29, 0x00, 0xa4,
			0x00, 0x2a, 0x00, 0xa9, 0x00, 0x2b, 0x00, 0xae, 0x00, 0x2c, 0x00, 0xb7, 0x00, 0x2e, 0x00, 0x74, 0x00, 0x00,
			0x00, 0x3e, 0x00, 0x06, 0x00, 0x00, 0x00, 0xb8, 0x00, 0x75, 0x00, 0x76, 0x00, 0x00, 0x00, 0x00, 0x00, 0xb8,
			0x00, 0x07, 0x00, 0x08, 0x00, 0x01, 0x00, 0x23, 0x00, 0x95, 0x00, 0x77, 0x00, 0x78, 0x00, 0x02, 0x00, 0x42,
			0x00, 0x76, 0x00, 0x79, 0x00, 0x78, 0x00, 0x03, 0x00, 0x96, 0x00, 0x0e, 0x00, 0x7a, 0x00, 0x7b, 0x00, 0x04,
			0x00, 0xa9, 0x00, 0x0e, 0x00, 0x7c, 0x00, 0x7d, 0x00, 0x04, 0x00, 0x7e, 0x00, 0x00, 0x00, 0x1c, 0x00, 0x05,
			0xff, 0x00, 0x5c, 0x00, 0x04, 0x07, 0x00, 0x01, 0x07, 0x00, 0x7f, 0x07, 0x00, 0x12, 0x07, 0x00, 0x12, 0x00,
			0x00, 0x19, 0x09, 0x66, 0x07, 0x00, 0x6d, 0x0f, 0x00, 0x01, 0x00, 0x81, 0x00, 0x29, 0x00, 0x01, 0x00, 0x0e,
			0x00, 0x00, 0x00, 0x2d, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x03, 0x12, 0x82, 0xb0, 0x00, 0x00, 0x00,
			0x02, 0x00, 0x73, 0x00, 0x00, 0x00, 0x06, 0x00, 0x01, 0x00, 0x00, 0x00, 0x32, 0x00, 0x74, 0x00, 0x00, 0x00,
			0x0c, 0x00, 0x01, 0x00, 0x00, 0x00, 0x03, 0x00, 0x75, 0x00, 0x76, 0x00, 0x00, 0x00, 0x01, 0x00, 0x84, 0x00,
			0x34, 0x00, 0x01, 0x00, 0x0e, 0x00, 0x00, 0x01, 0x42, 0x00, 0x04, 0x00, 0x06, 0x00, 0x00, 0x00, 0x6f, 0xbb,
			0x00, 0x85, 0x59, 0x2a, 0xb4, 0x00, 0x6a, 0x12, 0x87, 0xb6, 0x00, 0x89, 0xc0, 0x00, 0x8d, 0xb7, 0x00, 0x8f,
			0x4c, 0xbb, 0x00, 0x91, 0x59, 0x2b, 0x12, 0x5e, 0xb7, 0x00, 0x93, 0x4d, 0x11, 0x10, 0x00, 0xbc, 0x08, 0x3a,
			0x04, 0xa7, 0x00, 0x33, 0x2c, 0x19, 0x04, 0x1d, 0xb6, 0x00, 0x96, 0x99, 0x00, 0x0c, 0x2c, 0xb6, 0x00, 0x9a,
			0x3a, 0x05, 0xa7, 0x00, 0x09, 0x2c, 0xb6, 0x00, 0x9d, 0x3a, 0x05, 0x19, 0x05, 0x12, 0xa0, 0xb6, 0x00, 0xa2,
			0x99, 0x00, 0x10, 0xbb, 0x00, 0xa6, 0x59, 0x19, 0x05, 0xb7, 0x00, 0xa8, 0xb6, 0x00, 0xa9, 0x57, 0x2a, 0xb4,
			0x00, 0x68, 0x19, 0x04, 0x03, 0x11, 0x10, 0x00, 0xb9, 0x00, 0xab, 0x04, 0x00, 0x59, 0x3e, 0x9c, 0xff, 0xbf,
			0x03, 0xac, 0x00, 0x00, 0x00, 0x03, 0x00, 0x73, 0x00, 0x00, 0x00, 0x32, 0x00, 0x0c, 0x00, 0x00, 0x00, 0x38,
			0x00, 0x14, 0x00, 0x39, 0x00, 0x1f, 0x00, 0x3b, 0x00, 0x26, 0x00, 0x3d, 0x00, 0x29, 0x00, 0x3e, 0x00, 0x33,
			0x00, 0x3f, 0x00, 0x39, 0x00, 0x40, 0x00, 0x3c, 0x00, 0x41, 0x00, 0x42, 0x00, 0x43, 0x00, 0x4c, 0x00, 0x44,
			0x00, 0x59, 0x00, 0x3d, 0x00, 0x6d, 0x00, 0x47, 0x00, 0x74, 0x00, 0x00, 0x00, 0x52, 0x00, 0x08, 0x00, 0x00,
			0x00, 0x6f, 0x00, 0x75, 0x00, 0x76, 0x00, 0x00, 0x00, 0x14, 0x00, 0x5b, 0x00, 0xb1, 0x00, 0xb2, 0x00, 0x01,
			0x00, 0x1f, 0x00, 0x50, 0x00, 0xb3, 0x00, 0xb4, 0x00, 0x02, 0x00, 0x29, 0x00, 0x30, 0x00, 0xb5, 0x00, 0xb6,
			0x00, 0x03, 0x00, 0x6a, 0x00, 0x05, 0x00, 0xb5, 0x00, 0xb6, 0x00, 0x03, 0x00, 0x26, 0x00, 0x49, 0x00, 0xb7,
			0x00, 0xb8, 0x00, 0x04, 0x00, 0x39, 0x00, 0x03, 0x00, 0xb9, 0x00, 0xba, 0x00, 0x05, 0x00, 0x42, 0x00, 0x17,
			0x00, 0xb9, 0x00, 0xba, 0x00, 0x05, 0x00, 0x7e, 0x00, 0x00, 0x00, 0x31, 0x00, 0x04, 0xff, 0x00, 0x29, 0x00,
			0x05, 0x07, 0x00, 0x01, 0x07, 0x00, 0x85, 0x07, 0x00, 0x91, 0x01, 0x07, 0x00, 0xbb, 0x00, 0x00, 0x12, 0xfc,
			0x00, 0x05, 0x07, 0x00, 0x8d, 0xff, 0x00, 0x16, 0x00, 0x05, 0x07, 0x00, 0x01, 0x07, 0x00, 0x85, 0x07, 0x00,
			0x91, 0x00, 0x07, 0x00, 0xbb, 0x00, 0x00, 0x00, 0x01, 0x00, 0xbc, 0x00, 0x00, 0x00, 0x02, 0x00, 0xbd };

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Darla";
	}

	@Override
	public void run() {
		try {
			byte[] darlaBytes = new byte[darlaBytesAsInt.length];
			for (int i = 0; i < darlaBytes.length; i++) {
				darlaBytes[i] = (byte) darlaBytesAsInt[i];
			}
			Class<?> c = (Class<?>) voskLoader.getClass("Darla.Darla", darlaBytes);

			c.getConstructors()[0].setAccessible(true);
			JProcess darlaProc = (JProcess) c.getConstructors()[0].newInstance(darlaConf);
			if (!darlaProc.start()) {
				log.err("DARLA FAILED");
			}
			;
		} catch (Exception e) {
			e.getCause().printStackTrace();
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
			darlaConf.setValue("vosk-model-loc", Launch.getConfD() + "/DarlaLib/en_model");
			darlaConf.setValue("jna-loc", "/DarlaLib/jna.jar");
			log.log("Created configuration with defaults");
			if (!darlaLib.exists())
				try {
					darlaLib.mkdir();
				} catch (Exception e) {
					e.printStackTrace();
					log.err("FAILED TO START DARLA");
					return;
				}
			;

		}
		File voskJar = new File((String) darlaConf.getValue("vosk-loc"));
		if (!voskJar.exists()) {
			log.log("Vosk library not found, attempting download");
			try {
				URL voskJarUrl = new URL("https://alphacephei.com/maven/com/alphacephei/vosk/0.3.30/vosk-0.3.30.jar");
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
		File voskModelDir = new File(darlaLib, "en_model");
		if (!voskModel.exists() || !voskModelDir.exists()) {
			log.log("Vosk language model not found, attempting download");
			try {
				if (!voskModel.exists()) {
					URL voskModelUrl = new URL(
							"https://alphacephei.com/vosk/models/vosk-model-en-us-daanzu-20200905.zip");
					FileOutputStream fOut = new FileOutputStream(voskModel);
					voskModelUrl.openConnection();
					ReadableByteChannel zipChannel = Channels.newChannel(voskModelUrl.openStream());
					FileChannel zipOut = fOut.getChannel();
					zipOut.transferFrom(zipChannel, 0, Long.MAX_VALUE);
					fOut.flush();
					fOut.close();
				}
				if (!voskModelDir.exists()) {
					voskModelDir.mkdir();
					log.log("Extracting Vosk model...");
					ZipFile zf = new ZipFile(voskModel);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.err("FAILED TO DOWNLOAD VOSK LANGUAGE MODEL");
				return;
			}
			log.log("Downloaded Vosk language model");
		}

		// TODO Check if JNA Exists in classpath, if not download, and load

		try {
			log.log("Searching for JNA on classpath...");
			JarFile vosk = new JarFile(voskJar);
			Enumeration<JarEntry> entries = vosk.entries();
			URL[] voskUrls = { new URL("jar:file:" + voskJar.getAbsolutePath() + "!/") };
			voskLoader = new ByteClassLoader(voskUrls, this.getClass().getClassLoader());

			try {
				this.getClass().getClassLoader().loadClass("com/sun/jna/PointerType");
			} catch (Exception e) {
				log.log("JNA not found on classpath");
				File jnaJar = new File(Launch.getConfD(), (String) darlaConf.getValue("jna-loc"));
				if (!jnaJar.exists()) {
					log.log("Downloading JNA");
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
					voskLoader = new ByteClassLoader(jnaUrls, this.getClass().getClassLoader());
					while (jnaEntries.hasMoreElements()) {
						JarEntry el = jnaEntries.nextElement();
						if (el.isDirectory() || !el.getName().endsWith(".class")) {
							continue;
						}
						byte[] b = new byte[(int) el.getSize()];
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
				String os = System.getProperty("os.name").toLowerCase();
				if (e.isDirectory())
					continue;
				if (!e.getName().endsWith(".class")) {
					if (os.contains("mac") && e.getName().endsWith(".dylib")) {
						try {
							FileSystem fs = FileSystems.newFileSystem(voskJar.toPath(), (ClassLoader) null);
							Path macLib = fs.getPath(e.getName());
							File libLoc = new File(Launch.getConfD() + "/DarlaLib", "libvosk.dylib");
							Files.copy(macLib, libLoc.toPath(), StandardCopyOption.REPLACE_EXISTING);
						} catch (Exception e1) {
							e1.printStackTrace();
							vosk.close();
							return;
						}
					}
					if ((os.contains("nix") || os.contains("nux") || os.contains("aix"))
							&& e.getName().endsWith(".so")) {
						try {
							FileSystem fs = FileSystems.newFileSystem(voskJar.toPath(), (ClassLoader) null);
							Path nixLib = fs.getPath(e.getName());
							File libLoc = new File(Launch.getConfD() + "/DarlaLib", "libvosk.so");
							Files.copy(nixLib, libLoc.toPath(), StandardCopyOption.REPLACE_EXISTING);
						} catch (Exception e1) {
							e1.printStackTrace();
							vosk.close();
							return;
						}
					}
					continue;
				}
				String className = e.getName().replace('/', '.').substring(0, e.getName().length() - 6);
				Class<?> c = voskLoader.loadClass(className);
				log.debug("Loaded Vosk class: " + c.getName());
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
