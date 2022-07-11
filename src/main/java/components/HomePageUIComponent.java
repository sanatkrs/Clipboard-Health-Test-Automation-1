package components;

import org.openqa.selenium.support.PageFactory;
import testmanger.UITestManager;

public class HomePageUIComponent extends AmazonTemplateUIComponent {

    public HomePageUIComponent(UITestManager testManager) {
        super(testManager);
        PageFactory.initElements(getDriver(), this);
    }

}
