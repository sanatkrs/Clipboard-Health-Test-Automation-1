package config;

public enum OS {
	TYPE;

	public static String NAME = System.getProperty("os.name").toLowerCase();

	public static boolean isLinux() {
		return (NAME.contains("nix") || NAME.contains("nux") || NAME.contains("aix"));
	}

	public static boolean isMac() {
		return (NAME.contains("mac"));
	}

	public static boolean isWindows() {
		return (NAME.contains("win"));
	}
}