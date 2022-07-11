package driver;

import com.epam.healenium.SelfHealingDriver;
import common.CommonUtils;
import common.FileUtil;
import config.OS;
import config.TestConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;

@Slf4j
public class DriverFactory {

	private ThreadLocal<WebDriver> driver = ThreadLocal.withInitial(DriverFactory::constructDriverInstanceFromConfig);

	public static DriverFactory getInstance() {
		return new DriverFactory();
	}

	public WebDriver getDriver() {
		return driver.get();
	}

	public void quitDriver() {
		driver.get().quit();
		driver.remove();
	}

	public static WebDriver constructDriverInstanceFromConfigWithDimensions(Dimension dimension) {
		WebDriver driver = constructDefaultDriverInstanceFromConfig();
		setBrowserDimensions(driver, dimension);
		return driver;
	}

	public static WebDriver constructDriverInstanceFromConfig() {
		WebDriver driver = constructDefaultDriverInstanceFromConfig();
		setBrowserDimensions(driver);

		SelfHealingDriver selfHealDriver = SelfHealingDriver.create(driver);
		return selfHealDriver;
	}

	public static WebDriver constructDefaultDriverInstanceFromConfig() {
		if (OS.isLinux() || OS.isMac())
			makeChromeDriverExecutable();

		return getWebDriverObject(getConfiguredCapabilities());
	}

	public static void setBrowserDimensions(WebDriver driver) {
		driver.manage().window().maximize();
	}

	public static void setBrowserDimensions(WebDriver driver, Dimension dimension) {
		driver.manage().window().setSize(dimension);
	}

	public static WebDriver getWebDriverObject(MutableCapabilities mutableCapabilities) {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver((ChromeOptions) mutableCapabilities);

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Integer.parseInt(TestConfig.DRIVER_TIMEOUT_IMPLICIT)));
		log.info("Driver started successfully: {}", driver.toString());

		return driver;
	}

	public static MutableCapabilities getConfiguredCapabilities() {
		MutableCapabilities capabilities;

		switch (TestConfig.PLATFORM_NAME) {
			case "CHROME":
				capabilities = getHeadedChromeCapabilities();
				break;
			default:
				log.warn("Unable to determine web driver type, defaulting to CHROME");
				capabilities = getHeadedChromeCapabilities();
		}

		log.debug(capabilities.asMap().toString());
		return capabilities;
	}

	public static MutableCapabilities getHeadedChromeCapabilities() {
		ChromeOptions chromeOptions = getCommonOptionBase();
		return chromeOptions;
	}

	public static MutableCapabilities getHeadlessChromeCapabilities() {
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments(
				"--disable-gpu",
				"--headless"
		);
		return chromeOptions;
	}

	public static ChromeOptions getCommonOptionBase() {
		ChromeOptions chromeOptions = new ChromeOptions();

		chromeOptions.addArguments(
				"disable-infobars",
				"--disable-notifications",
				"--disable-dev-shm-usage",
				"--disable-zero-copy",
				"--enable-accelerated-2d-canvas",
				"--enable-low-res-tiling",
				"--enable-strict-powerful-feature-restrictions",
				"--no-sandbox",
				"--num-raster-threads=4",
				"--disable-web-security",
				"–-allow-file-access-from-files",
				"--window-size=1080,1920");

		// Allows the window.chrome reference to work against this driver, which is how we qualify this browser is supported
		chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("test-type"));

		applyExplicitNoProxyToSpeedStartup(chromeOptions);

		return chromeOptions;
	}

	public static void applyExplicitNoProxyToSpeedStartup(ChromeOptions chromeOptions) {
		Proxy proxy = new Proxy();
		proxy.setProxyType(Proxy.ProxyType.MANUAL);
		proxy.setNoProxy("");
		chromeOptions.setCapability("proxy", proxy);
	}

	private static void makeChromeDriverExecutable() {
		String chromeDriverPath = FileUtil.getFilePathFromResource(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY);

		try {
			String[] syntaxToMakeDriverExecutable = {"chmod", "+x", chromeDriverPath};
			Process commandLineStatementRunner = Runtime.getRuntime().exec(syntaxToMakeDriverExecutable);
			commandLineStatementRunner.waitFor();
		} catch (IOException | InterruptedException e) {
			log.error("Could not make ChromeDriverExecutable, initialization likely to fail for {}, with error of {}", chromeDriverPath, CommonUtils.getMsgAndCauseFromException(e));
			e.printStackTrace();
		}
	}
}
