package driver;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public class DriverManager {

	private DriverFactory driverFactory = DriverFactory.getInstance();

	public void initDriver() {
	}

	public WebDriver getDriver() {
		if (driverFactory.getDriver() == null)
			initDriver();

		return driverFactory.getDriver();
	}

	public void quitDriver() {
		driverFactory.quitDriver();
	}
}
