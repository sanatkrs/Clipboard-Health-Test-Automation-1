package ui;

import common.UITestBase;
import org.testng.annotations.Test;

public class TestUI extends UITestBase {

    @Test
    public void testChosenProducts() {
        doUIValidationForProduct();
    }
}
