package common;

import config.OS;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Paths;

/**
 * Objective:
 * House String Constants here that may be referenced heavily within the Stack
 * Example: API routes, Tenant URLs across the pipeline, entity nomenclature, etc.
 */
@Slf4j
public class SystemContextConstants {

	// Disable instantiation external to hierarchy:)
	protected SystemContextConstants() {
	}

	public static String NEW_LINE;
	public static String TAB;
	public static String STACK_BASE_DIR = "";

	static {
		setConstantsForOS();

		NEW_LINE = System.lineSeparator();
		TAB = "\t";
	}

	private static void setConstantsForOS() {
		if (OS.isLinux() || OS.isMac())
			STACK_BASE_DIR = new File(System.getProperty("user.dir")).getParent() + "/";
		else {
			STACK_BASE_DIR = Paths.get("").toAbsolutePath().toString().replaceAll("(.*)kiosk-test-automation(.*)", "$1kiosk-test-automation\\\\");
		}
	}


	// ======
	// System Map
	// ======

	public static final String STACK_RESOURCE_DIR = "/resources/";
	public static final String STACK_SRC_MAIN_DIR = "/src/main/";
	public static final String STACK_SRC_TEST_DIR = "/src/test/";
	public static final String STACK_SRC_MAIN_RESOURCE_DIR = STACK_SRC_MAIN_DIR + STACK_RESOURCE_DIR;
	public static final String STACK_SRC_TEST_RESOURCE_DIR = STACK_SRC_TEST_DIR + STACK_RESOURCE_DIR;
	public static final String STACK_MAIN_RESOURCE_DIR = STACK_BASE_DIR + STACK_SRC_MAIN_RESOURCE_DIR;
	public static final String STACK_TEST_RESOURCE_DIR = STACK_BASE_DIR + STACK_SRC_TEST_RESOURCE_DIR;


	//======
	// Common Files
	//======

	public static final String BASE_PROPERTIES_FILE = "test_config.properties";


	//======
	// Other
	//======


	public static final String DRIVER_PATH = "src/main/resources/drivers";
	public static final String DRIVER_PATH_WINDOWS = DRIVER_PATH + "/windows";
	public static final String CHROME_DRIVER_PATH_WINDOWS = DRIVER_PATH_WINDOWS + "/chromedriver.exe";

}
