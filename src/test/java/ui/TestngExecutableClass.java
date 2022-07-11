package ui;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

public class TestngExecutableClass {

    public static void main(String[] args) {
        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
        testng.setTestClasses(new Class[] { TestUI.class });
        testng.addListener(tla);
        testng.run();
    }
}

