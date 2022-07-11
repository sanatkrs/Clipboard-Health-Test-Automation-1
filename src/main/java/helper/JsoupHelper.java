package helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.NoSuchElementException;

public class JsoupHelper {

    public static boolean isElementInDOM(Document DOM, String jsoupSelector) {
        try {
            JsoupHelper.getElement(DOM, jsoupSelector);
            return true;
        } catch (NoSuchElementException expected) {
            return false;
        }
    }

    public static Element getElement(Document dom, String selectorPath) throws NoSuchElementException {
        Elements elements = getElements(dom, selectorPath);
        if (elements.isEmpty()) {
            throw new NoSuchElementException("Could not find Element at " + selectorPath);
        }

        return elements.get(0);
    }

    public static Elements getElements(Document dom, String selectorPath) {
        return dom.select(selectorPath);
    }

    public static Document getHTMLStringAsDocument(String htmlString) {
        return Jsoup.parse(htmlString);
    }
}
