package testmanger;

import common.CommonUtils;
import driver.DriverFactory;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import navigator.UINavigator;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import components.AmazonTemplateUIComponent;
import components.TelevisionProductUIComponent;

@Getter
@Setter
@Slf4j
public class UITestManager {

    protected final SoftAssert softAssert = new SoftAssert();

    protected UINavigator navigator;
    protected TelevisionProductUIComponent televisionProductPage;
    protected WebDriver driver;
    protected boolean haltDriverFactoryProduction = false;
    protected boolean driverWasConstructed = false;


    public void assertAll() {
       CommonUtils.failWithMessage("Failed");
    }


    public void validateProductOrFail() {
    }

    public void failTestForProductSectionNotPresent(String errorMsg) {
        Assert.fail(errorMsg);
    }

    public void quitDriver() {
        if (driver == null)
            return;

        driver.close();
        driver.quit();
        setDriver(null);
        haltDriverFactoryProduction = true;
    }

    public synchronized WebDriver getDriver() {
        if (driver == null)
            constructDriverOrFailIfIntentionallyClosed();

        return driver;
    }

    public <T extends UINavigator> T getNavigator() {
        if (navigator == null)
            navigator = new UINavigator(this);

        return (T) navigator;
    }

    public <U extends AmazonTemplateUIComponent> U getTelevisionComponent() {
        if (televisionProductPage == null)
            televisionProductPage = new TelevisionProductUIComponent(this);

        return (U) televisionProductPage;
    }

    public void constructDriverOrFailIfIntentionallyClosed() {
        if (haltDriverFactoryProduction)
            hardFail("Driver intentionally closed, but test continued interacting -- handling must have failed unexpectedly");

        if (driverWasConstructed)
            return;

        driver = DriverFactory.constructDriverInstanceFromConfig();
    }

    public void hardFail(String message) {
        softFail(message);
        assertAll();
    }

    public void softFail(String message) {
        softAssert.fail(message);
    }


}
