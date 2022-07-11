package components;

import interfaces.DriverHelper;
import interfaces.IManagedUITest;
import navigator.UINavigator;
import testmanger.UITestManager;

public abstract class UIComponent implements DriverHelper, IManagedUITest {

    protected UITestManager testManager;
    protected UINavigator navigator;
    protected String componentName = getClass().getSimpleName();

    public UIComponent(UITestManager testManager) {
        this.testManager = testManager;
        this.navigator = testManager.getNavigator();
    }

}
