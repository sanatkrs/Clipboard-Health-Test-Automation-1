package interfaces;

import navigator.UINavigator;
import org.openqa.selenium.WebDriver;
import testmanger.UITestManager;

public interface IManagedUITest<T extends UINavigator> {

    UITestManager getTestManager();
    void setTestManager(UITestManager testManager);

    default WebDriver getDriver() {
        return getTestManager().getDriver();
    }

    default void setDriver(WebDriver driver) {
        getTestManager().setDriver(driver);
    }

    default T getNavigator() {
        return getTestManager().getNavigator();
    }

    default void setDriver(T navigator) {
        getTestManager().setNavigator(navigator);
    }

}

