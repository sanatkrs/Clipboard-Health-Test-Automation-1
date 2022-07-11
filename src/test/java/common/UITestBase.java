package common;

import lombok.extern.slf4j.Slf4j;
import navigator.UINavigator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import components.TelevisionProductUIComponent;
import testmanger.UITestManager;

@Slf4j
public abstract class UITestBase {

    public static void doUIValidationForProduct() {
        UITestManager testManager = new UITestManager();
        testManager.validateProductOrFail();

        log.info("Now testing");

        try {
            navigateThisProduct(testManager);
            applyFilter(testManager);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            testManager.quitDriver();
        }
    }

    private static void applyFilter(UITestManager testManager) throws InterruptedException {
        log.info("Now Applying filters ");
        TelevisionProductUIComponent UIComponent = testManager.getTelevisionComponent();
        UIComponent.selectBrandSamsung();
        UIComponent.selectHighToLowSorting();
        UIComponent.selectSecondProduct();

        if (!UIComponent.verifyIfAboutThisSectionPresent())
            testManager.failTestForProductSectionNotPresent("About this section was not was not present in the UI");
    }

    public static void navigateThisProduct(UITestManager testManager) {
        log.info("Now testing flow ");

        UINavigator navigator = testManager.getNavigator();
        log.info("Assigned navigator was: [{}]", navigator.getClass().getSimpleName());
        navigator.navigateToEnv();
        navigator.navigateToMainMenu();
    }



}
