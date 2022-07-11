package config;

import common.FileUtil;
import common.SystemContextConstants;
import interfaces.IInitializer;
import lombok.SneakyThrows;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Objective:
 * 	Provide a Class that manages the overall configuration of the system and aid testing
 *
 * Example:
 * Knowing the current System Under Test (SUT), or holding a set of system props that can be passed around
 */

public class TestConfig extends BaseConfig {

	static final Logger logger = LoggerFactory.getLogger(TestConfig.class);
	private static Properties testProperties;

	public static String DRIVER_TIMEOUT_EXPLICIT;
	public static String DRIVER_TIMEOUT_IMPLICIT;
	public static String PLATFORM_NAME;
	public static String URL;
	public static boolean UI_TEST_SLOWS_NAVIGATION;


	static {
		new TestConfigInitializer().init();
	}

	//====
	// Context Initializers
	//====

	static class TestConfigInitializer implements IInitializer {

		private TestConfigInitializer() {}


		public void init() {
			testProperties = new Properties();

			initFromPropertiesFile();

			if (testProperties.isEmpty())
				throwExceptionDueToEmptyProps();
			else
				completeInit();

			if (logger.isDebugEnabled())
				testProperties.forEach((k,v) -> logger.debug("[PROPERTY] {} = {}", k, v));
		}

		private void initFromPropertiesFile() {
			try {
				File propsFile = FileUtil.getFile(SystemContextConstants.BASE_PROPERTIES_FILE);
				try (InputStream inputStream = new FileInputStream(propsFile)) {
					testProperties.load(inputStream);
				}
			} catch (FileNotFoundException e) {
				tryLoadingPropertiesAsResourceStream(e);
			} catch (IOException e) {
				throwExceptionDueToInabilityToInit(e);
			}
		}

		private void throwExceptionDueToEmptyProps() {
			throw new IllegalStateException(String.format("Tried to load from %s but found no properties...", SystemContextConstants.BASE_PROPERTIES_FILE));
		}

		private void completeInit() {
			addSystemPropsToGlobalConfig();

			setPropertiesIfNotKioskAutomationServer();
		}

		private void addSystemPropsToGlobalConfig() {
			Properties systemProperties = System.getProperties();
			systemProperties.forEach(testProperties::put);
		}

		private void setPropertiesIfNotKioskAutomationServer() {
			new DriverConfigInitializer().init();
			UI_TEST_SLOWS_NAVIGATION = Boolean.parseBoolean(testProperties.getProperty(TestConfigConstants.KEY_IF_UI_TEST_FORCE_SLOW_NAVIGATION));
			new DriverConfigInitializer().init();
		}

		@SneakyThrows
		private void tryLoadingPropertiesAsResourceStream(FileNotFoundException e) {
			try (final InputStream in = TestConfig.class.getResourceAsStream("/" + SystemContextConstants.BASE_PROPERTIES_FILE)) {
				if (in == null)
					throwExceptionDueToInabilityToInit(e);

				testProperties.load(in);
			}
		}

		private void throwExceptionDueToInabilityToInit(Exception e) {
			String msg = String.format(
					"Was completely unable to init from %s due to %s",
					SystemContextConstants.BASE_PROPERTIES_FILE,
					e.getMessage());
			throw new IllegalStateException(msg);
		}
	}


	static class DriverConfigInitializer implements IInitializer {

		private DriverConfigInitializer() {
		}


		@Override
		public void init() {
			setGlobalChromeDriverPath();
			setDriverRelatedProperties();
		}

		private void setDriverRelatedProperties() {
			URL = getProperty(TestConfigConstants.KEY_TEST_URL);
			DRIVER_TIMEOUT_IMPLICIT = getProperty(TestConfigConstants.KEY_DRIVER_TIMEOUT_IMPLICIT);
			DRIVER_TIMEOUT_EXPLICIT = getProperty(TestConfigConstants.KEY_DRIVER_TIMEOUT_EXPLICIT);

			PLATFORM_NAME = testProperties.getProperty(TestConfigConstants.KEY_UI_TEST_BROWSER);
		}

		private void setGlobalChromeDriverPath() {
			String chromeDriverPath = SystemContextConstants.CHROME_DRIVER_PATH_WINDOWS;

			chromeDriverPath = setChromeDriverPathFromPropertiesFile(chromeDriverPath);

			logger.info("Setting Chrome Driver path to {}", chromeDriverPath);
			System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, chromeDriverPath);
			testProperties.put(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, chromeDriverPath);

		}

		private String setChromeDriverPathFromPropertiesFile(String chromeDriverPath) {
			if (OS.isMac()) {
				chromeDriverPath = getProperty(TestConfigConstants.KEY_WEBDRIVER_EXECUTABLE_CHROME_MAC);
			} else if (OS.isWindows()) {
				chromeDriverPath = getProperty(TestConfigConstants.KEY_WEBDRIVER_EXECUTABLE_CHROME_WINDOWS);
			} else if (OS.isLinux()) {
				// As it stands, Linux env for Jenkins has a pre-installed driver, therefore is a static
				// path outside the stack
				chromeDriverPath = getProperty(TestConfigConstants.KEY_WEBDRIVER_EXECUTABLE_CHROME_LINUX);
			} else {
				logger.error(
						"Unknown OS identified, unsure of which Driver executable to assign, OS was {}",
						OS.NAME);
			}

			chromeDriverPath = FileUtil.getFilePathFromResource(chromeDriverPath);
			return chromeDriverPath;
		}

		public static String getProperty(String property) {
			if (testProperties.containsKey(property)) {
				return testProperties.getProperty(property);
			} else {
				logger.warn("TestConfig queried for property, but it wasn't present, prop was: " + property);
				return "";
			}
		}
	}
}

