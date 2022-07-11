package navigator;

import common.CommonUtils;
import config.TestConfig;
import interfaces.DriverHelper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriverException;
import components.AmazonTemplateUIComponent;
import testmanger.UITestManager;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Slf4j
public class UINavigator implements DriverHelper {

    protected UITestManager testManager;

    public UINavigator(UITestManager testManager) {
        this.testManager = testManager;
    }


    public void navigateToEnv() {
        String url = TestConfig.URL;
        navigateTo(url);
        waitUntilPageLoaded();
    }

    public void navigateToMainMenu() {
        configureBrowserForTest();
        navToProductPage();
    }

    public void configureBrowserForTest() {
        try {
           // if (!isElementInDOMAfterWait("iframe"))
           //     getTestManager().hardFail("Env not Ready");

            waitUntilPageLoaded();
        } catch (WebDriverException e) {
            testManager.hardFail(CommonUtils.getMsgAndCauseFromException(e));
        }
    }

    public void navigateToProductTab() {
        log.info("Navigating to product tab");
        List<String> tabs = new ArrayList<String>(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(1));
    }

    public void navToProductPage() {
        AmazonTemplateUIComponent amazonTemplateComponent = new AmazonTemplateUIComponent(testManager);
        amazonTemplateComponent.clickOnHamburgerMenu();
        amazonTemplateComponent.clickOnTVAppliancesMenuOption();
        amazonTemplateComponent.clickOnTelevisionOption();
        log.info("At Product Page with Title of {} [{}]", getPageTitle(), getDriver().getWindowHandle());
    }

}
