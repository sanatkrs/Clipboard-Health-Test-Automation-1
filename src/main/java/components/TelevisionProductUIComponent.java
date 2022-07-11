package components;

import helper.WaitTool;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import testmanger.UITestManager;

@Slf4j
public class TelevisionProductUIComponent extends AmazonTemplateUIComponent {

    public TelevisionProductUIComponent(UITestManager testManager) {
        super(testManager);
        PageFactory.initElements(testManager.getDriver(), this);
    }

    @FindBy(xpath = "//span[contains(text(), 'Samsung')]/parent::a[@class='a-link-normal']")
    WebElement brandSamsung;

    @FindBy(xpath = "//div[@cel_widget_id = 'MAIN-SEARCH_RESULTS-2']")
    WebElement secondProduct;

    @FindBy(css = "#feature-bullets")
    WebElement aboutThisItemSection;

    @FindBy(xpath = "//div[@id='feature-bullets']/h1")
    WebElement aboutThisItemSectionHeader;


    public void selectBrandSamsung() {
        waitUntilPageLoaded();
        brandSamsung.click();
    }

    public void selectHighToLowSorting() {
        sortByDropDown.click();

        getActionsClass(getDriver()).moveToElement(sortByHighToLowOption).build().perform();
        sortByHighToLowOption.click();
    }

    public void selectSecondProduct() {
        waitUntilPageLoaded();
        secondProduct.click();
    }

    public boolean verifyIfAboutThisSectionPresent() {
        getNavigator().navigateToProductTab();
        WaitTool.waitForElementToBeVisible(getDriver(), aboutThisItemSection);

        if (elementIsNotStale(aboutThisItemSection)) {
            scrollToElement(aboutThisItemSection);
            log.info("=========================================================================");
            log.info("About this item header text was ====>>  {}", aboutThisItemSectionHeader.getText());
            log.info("=========================================================================");
            return true;
        }

        return false;
    }

}
