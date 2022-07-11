package components;

import helper.WaitTool;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testmanger.UITestManager;

@Getter
@Setter
@Slf4j
public class AmazonTemplateUIComponent extends UIComponent {

    UITestManager testManager;


    @FindBy(id = "nav-hamburger-menu")
    WebElement hamburgerMenu;

    @FindBy(xpath = "//div[contains(text(), 'TV, Appliances, Electronics')]/parent::a")
    WebElement tvApplianceMenuOption;

    @FindBy(xpath = "//a[contains(text(), 'Televisions')]")
    WebElement televisionOption;

    @FindBy(xpath = "//span[@class='a-button-text a-declarative']")
    WebElement sortByDropDown;

    @FindBy(xpath = "//a[@class='a-dropdown-link'][contains(text(), 'High to Low')]")
    WebElement sortByHighToLowOption;

    public AmazonTemplateUIComponent(UITestManager uiTestManager) {
        super(uiTestManager);
        this.testManager = uiTestManager;
        PageFactory.initElements(this.testManager.getDriver(), this);
    }

    public void clickOnHamburgerMenu() {
        hamburgerMenu.click();
    }

    public void clickOnTVAppliancesMenuOption() {
        WaitTool.waitForElementToBeClickable(getDriver(), tvApplianceMenuOption);
        tvApplianceMenuOption.click();
    }

    public void clickOnTelevisionOption() {
        WaitTool.waitForElementToBeVisible(getDriver(), televisionOption);
        televisionOption.click();
    }

}
