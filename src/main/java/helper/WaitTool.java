package helper;

import common.CommonUtils;
import config.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.MILLIS;

@Slf4j
public class WaitTool {

    private static FluentWait<WebDriver> wait;
    private static Duration timeout = Duration.parse(TestConfig.DRIVER_TIMEOUT_EXPLICIT);

    public static WebElement waitForElementToBeVisible(WebDriver driver, WebElement element) {
        return getWait(driver).until(ExpectedConditions.visibilityOf(element));
    }

    public static WebElement waitForElementToBeClickable(WebDriver driver, WebElement element) {
        WebElement button = getWait(driver).until(ExpectedConditions.elementToBeClickable(element));

        return button;
    }

    public static FluentWait<WebDriver> getWait(WebDriver driver) {
        if (wait == null) {
            wait = new WebDriverWait(driver, timeout)
                    .pollingEvery(Duration.of(200, MILLIS))
                    .ignoring(NoSuchElementException.class);
        }

        return wait;
    }

    public static void waitUntilPageLoaded(WebDriver driver) {
        long start = System.currentTimeMillis();

        new WebDriverWait(driver, timeout).until(jsReturnsTrueAndDomStopsUpdating("return document.readyState === 'complete'"));

        CommonUtils.logTimeToNowIfGreaterThanHalfSec(
                start,
                "[{}ms] - Wait Time - DOM Ready State = Complete (And no dynamic updates detected)");
    }

    public static ExpectedCondition<Boolean> jsReturnsTrueAndDomStopsUpdating(final String javascript) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    String originalDOM = driver.getPageSource();
                    boolean loadingCompleted = (Boolean) ((JavascriptExecutor) driver).executeScript(javascript);
                    String currentDOM = driver.getPageSource();

                    boolean domIsIdentical = StringUtils.equals(originalDOM, currentDOM);
                    if (!domIsIdentical) {
                        Thread.sleep(50);
                        domIsIdentical = StringUtils.equals(originalDOM, currentDOM);
                    }

                    return domIsIdentical && loadingCompleted;
                } catch (WebDriverException e) {
                    log.debug("Timed out trying to execute Javascript {}", javascript);
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return "Javascript function to return true [" + javascript + "]";
            }
        };
    }

}
