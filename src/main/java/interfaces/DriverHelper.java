package interfaces;

import common.CommonUtils;
import helper.JsoupHelper;
import helper.WaitTool;
import navigator.UINavigator;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface DriverHelper<T extends UINavigator>
        extends IManagedUITest<T>{

    Logger logger = LoggerFactory.getLogger(DriverHelper.class);

    default void navigateTo(String url) {
        logger.info("[Navigating to {}][{}]", url, getDriver().getWindowHandle());
        getDriver().get(url);
    }

    default void scrollDown(int amountInPixels) {
        executeJavascript("window.scrollBy(0, arguments[0]);", amountInPixels);
    }

    default void scrollToElement(WebElement element) {
        executeJavascript("arguments[0].scrollIntoView(true);", element);
    }

    default void scrollToTop() {
        executeJavascript("window.scrollTo(0, 0);");
    }

    default Object executeJavascript(String javascriptToExecute, Object... arguments) {
        try {
            return getJavascriptExecutor().executeScript(javascriptToExecute, arguments);
        } catch (WebDriverException e) {
            String message = String.format(
                    "Encountered exception when executing Javascript, error was %s, against: [%s]",
                    CommonUtils.getMsgAndCauseFromException(e),
                    javascriptToExecute);
        }

        return null;
    }

    default void waitUntilPageLoaded() {
        logger.debug("waitUntilPageLoaded called by {}", Thread.currentThread().getStackTrace()[2].getMethodName());

        long start = System.currentTimeMillis();
        WaitTool.waitUntilPageLoaded(getDriver());

        CommonUtils.logTimeToNowIfGreaterThanHalfSec(
              start,
             "[{}ms] - Wait Time - Page Loaded");
    }

    default JavascriptExecutor getJavascriptExecutor() {
        return (JavascriptExecutor) getDriver();
    }

    default boolean isElementInDOMAfterWait(String uncleanJsoupSelector) {
        return isElementInDOM(uncleanJsoupSelector);
    }

    default boolean isElementInDOM(String uncleanJsoupSelector) {
        String jsoupSelector = StringUtils.replaceAll(uncleanJsoupSelector, " i]", "]");

        boolean isPresent = JsoupHelper.isElementInDOM(getPageDOM(), jsoupSelector);

        if (logger.isDebugEnabled()) {
            String foundMsg = (isPresent) ? "" : "NOT ";
            String msg = String.format("Element %sFOUND with selector of %s", foundMsg, jsoupSelector);
            logger.debug(msg);
        }

        return isPresent;
    }

    default Document getPageDOM() {
        return JsoupHelper.getHTMLStringAsDocument(getPageSource());
    }

    default String getPageSource() {
        return getDriver().getPageSource();
    }

    default Actions getActionsClass(WebDriver driver) {
        return new Actions(driver);
    }

    default boolean elementIsNotStale(WebElement element) {
        try {
            if (element.isDisplayed())
                return true;
        } catch (StaleElementReferenceException e) {

        }

        return false;
    }

    default String getPageTitle() {
        return getDriver().getTitle();
    }


}
