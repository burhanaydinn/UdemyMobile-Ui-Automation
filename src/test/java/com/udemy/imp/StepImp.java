package com.udemy.imp;

import com.udemy.methods.Methods;
import com.thoughtworks.gauge.Step;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static com.udemy.driver.BaseTest.logger;
import static com.udemy.driver.DriverManager.appiumDriver;
import static io.appium.java_client.touch.offset.PointOption.point;
import static java.time.Duration.ofMillis;


public class StepImp  {
    protected Methods methods;
    public By by;

    public StepImp() {
        methods = new Methods();
    }

    @Step("<key> li elemente tıklanır.")
    public void click(String keyword) {
        methods.click(keyword);
    }

    @Step("<key> li elementin merkezine double tıklanır.")
    public void pressElementWithKeyTwo(String key) {
        Actions actions = new Actions(appiumDriver);
        actions.moveToElement(methods.findElementMobile(key));
        actions.doubleClick();
        actions.perform();
        appiumDriver.getKeyboard();
    }

    @Step("<key> 'li elemente görünür ise tıklanır.")
    public void clickIfExist(String keyword) {
        methods.clickIfExist(keyword);
    }


    @Step("<second> saniye kadar beklenir.")
    public void waitForsecond(int second) throws InterruptedException {
        methods.waitBySeconds(second);
    }

    @Step({"<key> li elementi bul, temizle ve <text> değerini yaz.",
            "Find element by <key> clear and send keys <text>."})
    public void sendKeysByKey(String key, String text) {
        MobileElement webElement = methods.findElementMobile(key);
        webElement.clear();
        webElement.setValue(text);
    }
    @Step("Find element by <key> and send keys <text> to input")
    public void sendKeysToInput(String key, String text){
        methods.findElementMobile(key).sendKeys(text);
        System.out.println("Text girildi");
    }

    @Step({"Click element by <key> if exist"})
    public void existClickByKey(String key) {
        WebElement element = null;
        try {
            element = methods.findElementMobile(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (element != null) {
            element.click();
        }
    }

    @Step("If <key> displayed. App opened")
    public void checkApp(String key) throws InterruptedException {

        methods.logger.info("Check your app opened");
        Assert.assertTrue("Element is not found in page", methods.findElementMobile(key).isEnabled());
    }

    @Step({"<key> element is displayed"})
    public void existElement(String key) {
        Assert.assertTrue("Element is not found in page", methods.findElementMobile(key).isDisplayed());
        System.out.println("'"+key+"' is displayed");
    }

    @Step({"Click element with coordinate : <x> , <y>"})
    public void clickByKeyWithCoordinate(int x, int y) {
        //TouchAction a2 = new TouchAction(appiumDriver);
        //a2.tap(point(x,y)).perform();

        TouchAction touchAction = new TouchAction(appiumDriver);
        touchAction.tap(PointOption.point(x, y)).perform();
        System.out.println("tap with coordinate");
    }

    @Step({"Find element by <key> and send keys <text> phone"})
    public void searhInputsendKeysByKeyNotClearPhone(String key, String text) {
        MobileElement _me = methods.findElementMobile(key);
        _me.clear();
        for(int i=0 ; i<text.length();i++){
            char c = text.charAt(i);
            _me.sendKeys(String.valueOf(c));
            new TouchAction(appiumDriver).tap(point(_me.getLocation().getX(), 225)).perform();
        }
        System.out.println("'" + text + "' written to '" + key + "' element.");
    }

    @Step({"<key> li elementi bul ve değerini <saveKey> olarak sakla",
            "Find element by <key> and save text <saveKey>"})
    public void saveTextByKey(String key, String saveKey) {
        methods.saveValue(saveKey,key);
    }

    @Step({"<key> li elementi bul ve değerini <saveKey> saklanan değer ile karşılaştır",
            "Find element by <key> and compare saved key <saveKey>"})
    public void equalsSaveTextByKey(String key, String saveKey) {
        //methods.saveValue(saveKey,key);
        Assert.assertEquals(methods.elementsMap.get(saveKey), methods.findElementMobile(key).getText());
    }

    @Step({"Değeri <text> e eşit olan elementli bul ve tıkla",
            "Find element text equals <text> and click"})
    public void clickByText(String text) {
        methods.findElementMobile(By.xpath(".//*[contains(@text,'" + text + "')]")).click();
    }

    @Step({"İçeriği <value> e eşit olan elementli bul ve tıkla",
            "Find element value equals <value> and click"})
    public void clickByValue(String value) {
        By by = By.xpath(".//*[contains(@value,'" + value + "')]");
        methods.findElementMobile(by).click();
    }

    @Step({"<key> li ve değeri <text> e eşit olan elementli bul ve tıkla",
            "Find element by <key> text equals <text> and click"})
    public void clickByIdWithContains(String key, String text) {
        List<MobileElement> elements = methods.getElements(key);
        for (MobileElement element : elements) {
            System.out.print("Text !!!" + element.getText());
            if (element.getText().toLowerCase().contains(text.toLowerCase())) {
                element.click();
                break;
            }
        }
    }

    @Step({"<key> li ve değeri <text> e eşit olan elementli bulana kadar swipe et ve tıkla", "Find element by <key> text equals <text> swipe and click"})
    public void clickByKeyWithSwipe(String key, String text) throws InterruptedException {
        boolean find = false;
        int maxRetryCount = 10;
        while (!find && maxRetryCount > 0) {
            List<MobileElement> elements = methods.getElements(key);
            for (MobileElement element : elements) {
                if (element.getText().contains(text)) {
                    element.click();
                    find = true;
                    break;
                }
            }
            if (!find) {
                maxRetryCount--;
                if (appiumDriver instanceof AndroidDriver) {
                    swipeUpAccordingToPhoneSize();
                    waitForsecond(1);
                } else {
                    swipeDownAccordingToPhoneSize();
                    waitForsecond(1);
                }
            }
        }
    }

    @Step({"<key> li elementi bulana kadar yukarı swipe et",
            "Find element by <key>  swipe "})
    public void findByKeyWithSwipeUp(String key) {
        int maxRetryCount = 10;
        while (maxRetryCount > 0) {

            List<MobileElement> elements = methods.getElements(key);
            if (elements.size() > 0) {
                if (!elements.get(0).isDisplayed()) {
                    maxRetryCount--;
                    swipeUpAccordingToPhoneSize();
                } else {
                    System.out.println(key + " element bulundu. UP");
                    break;
                }
            } else {
                maxRetryCount--;
                swipeUpAccordingToPhoneSize();
            }
        }
    }

    public void swipeUpAccordingToPhoneSize() {
        if (appiumDriver instanceof AndroidDriver) {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;
            System.out.println(width + "  " + height);

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 80) / 100;
            int swipeEndHeight = (height * 20) / 100;
            new TouchAction((AndroidDriver) appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeEndHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(2000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeStartHeight))
                    .release()
                    .perform();
        } else {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            logger.info("swipeStartWidth ios up " +swipeStartWidth);
            int swipeStartHeight = (height * 75) / 100;
            logger.info("swipeStartHeight ios up " +swipeStartHeight);
            int swipeEndHeight = (height * 35) / 100;
            logger.info("swipeEndHeight ios up " +swipeEndHeight);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeEndHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(2000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeStartHeight))
                    .release()
                    .perform();
        }
    }

    public void swipeDownAccordingToPhoneSize() {
        if (appiumDriver instanceof AndroidDriver) {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 90) / 100;
            int swipeEndHeight = (height * 50) / 100;
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        } else {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 90) / 100;
            int swipeEndHeight = (height * 40) / 100;
            // appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        }
    }

    @Step({"<key> li elementi bulana kadar swipe et ve tıkla", "Find element by <key>  swipe and click"})
    public void clickByKeyWithSwipe(String key) {
        int maxRetryCount = 10;
        while (maxRetryCount > 0) {
            List<MobileElement> elements = methods.getElements(key);
            if (elements.size() > 0) {
                if (elements.get(0).isDisplayed() == false) {
                    maxRetryCount--;
                //if (appiumDriver instanceof AndroidDriver) {
                //swipeUpAccordingToPhoneSize();
                //} else {
                    swipeDownAccordingToPhoneSize();
                    //}
                } else {
                    elements.get(0).click();
                    System.out.println(key + " elementine tıklandı");
                    break;
                }
            } else {
                maxRetryCount--;
                //if (appiumDriver instanceof AndroidDriver) {
                //  swipeUpAccordingToPhoneSize();
                //}
                //else {
                swipeDownAccordingToPhoneSize();
                //}
            }

        }
    }

    private int getScreenWidth() {
        return appiumDriver.manage().window().getSize().width;
    }

    private int getScreenHeight() {
        return appiumDriver.manage().window().getSize().height;
    }

    private int getScreenWithRateToPercent(int percent) {
        return getScreenWidth() * percent / 100;
    }

    private int getScreenHeightRateToPercent(int percent) {
        return getScreenHeight() * percent / 100;
    }

    public void swipeDownAccordingToPhoneSize(int startXLocation, int startYLocation, int endXLocation, int endYLocation) {
        startXLocation = getScreenWithRateToPercent(startXLocation);
        startYLocation = getScreenHeightRateToPercent(startYLocation);
        endXLocation = getScreenWithRateToPercent(endXLocation);
        endYLocation = getScreenHeightRateToPercent(endYLocation);

        new TouchAction(appiumDriver)
                .press(PointOption.point(startXLocation, startYLocation))
                .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                .moveTo(PointOption.point(endXLocation, endYLocation))
                .release()
                .perform();
    }

    @Step({"<key> id'li elementi bulana kadar <times> swipe yap ", "Find element by <key>  <times> swipe "})
    public void swipeDownUntilSeeTheElement(String element, int limit) {
        for (int i = 0; i < limit; i++) {
            List<MobileElement> meList = appiumDriver.findElements(By.id(element));
            //  List<MobileElement> meList = findElemenstByKey(element);
            if (meList.size() > 0 &&
                    meList.get(0).getLocation().x <= getScreenWidth() &&
                    meList.get(0).getLocation().y <= getScreenHeight()) {
                break;
            } else {
                swipeDownAccordingToPhoneSize(50, 80, 50, 30);
                break;
            }
        }
    }

    @Step({"<key> li elementi bulana kadar swipe et", "Find element by <key> swipe "})
    public void findByKeyWithSwipe(String key) {
        int maxRetryCount = 25;
        while (maxRetryCount > 0) {
            List<MobileElement> elements = methods.getElements(key);
            if (elements.size() > 0) {
                if (!elements.get(0).isDisplayed()) {
                    maxRetryCount--;
                    swipeDownAccordingToPhoneSize();
                } else {
                    logger.info(key + " element bulundu.");
                    break;
                }
            } else {
                maxRetryCount--;
                swipeDownAccordingToPhoneSize();
            }

        }
    }

    @Step("<yon> yönüne swipe et")
    public void swipe(String yon) {

        Dimension d = appiumDriver.manage().window().getSize();
        int height = d.height;
        int width = d.width;

        if (yon.equals("SAĞ")) {

            int swipeStartWidth = (width * 80) / 100;
            int swipeEndWidth = (width * 10) / 100;

            int swipeStartHeight = height / 2;
            int swipeEndHeight = height / 2;

            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        } else if (yon.equals("SOL")) {

            int swipeStartWidth = (width * 10) / 100;
            int swipeEndWidth = (width * 80) / 100;

            int swipeStartHeight = height / 2;
            int swipeEndHeight = height / 2;

            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();

        }
    }

    @Step({"<key> li elementi bul yoksa <message> mesajını hata olarak göster",
            "Find element by <key> if not exist show error message <message>"})
    public void isElementExist(String key, String message) {
        Assert.assertTrue(message, methods.findElementsMobile(key) != null);
    }

    @Step({"<times> kere aşağıya kaydır", "Swipe times <times>"})
    public void swipe(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            swipeDownAccordingToPhoneSize();
            waitForsecond(1);

            //System.out.println("SWİPE EDİLDİ");
            logger.info(times + " aşağı swipe edildi.");

        }
    }

    @Step({"<times> kere yukarı doğru kaydır", "Swipe up times <times>"})
    public void swipeUP(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            swipeUpAccordingToPhoneSize();
            waitForsecond(1);

            //System.out.println("SWİPE EDİLDİ");
            logger.info(times + " yukarı swipe edildi.");

        }
    }

    @Step({"Swipe up times <times>"})
    public void swipeUPs(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            swipeUpAccordingToPhoneSize();
            waitForsecond(1);
            System.out.println("swipe up");
        }
    }

    @Step("swipe et")
    public void swipeMethod(){
        new TouchAction(appiumDriver).longPress(PointOption.point(330, 800))
                .moveTo(PointOption.point(330, 568))
                .release().perform();
        System.out.println("swipe yapıldı.");
    }

    @Step("Scroll down")
    public void scrollDown() {
        Dimension size = appiumDriver.manage().window().getSize();
        int x = size.getWidth() / 2;
        int starty = (int) (size.getHeight() * 0.60);
        int endy = (int) (size.getHeight() * 0.10);
    }

    @Step({"check <key> word in the page."})
    public void getPageSourceFindWord(String key) {
        Assert.assertTrue(key + " can't find in the page.",
                appiumDriver.getPageSource().contains(key));
        System.out.println(key + " can find in the page");
    }

    @Step("Yeni şifre <text> ve yeni şifre tekrar <textrepeat>  alanlarına tex değerlerini yaz")
    public void writeNewPassword(String text, String textrepeat) {
        WebDriverWait webDriverWait = new WebDriverWait(appiumDriver, 30, 1000);
        MobileElement me = (MobileElement) webDriverWait.until(ExpectedConditions
                .presenceOfElementLocated(By.id("id/editTextPassword"))); //özelleştirilmiş
        me.click();
        me.setValue(text);
        appiumDriver.navigate().back();
        WebDriverWait webDriverWait2 = new WebDriverWait(appiumDriver, 30, 1000);
        MobileElement me2 = (MobileElement) webDriverWait2.until(ExpectedConditions
                .presenceOfElementLocated(
                        By.id("id/editTextPasswordRepeat"))); //özelleştirilmiş
        me2.click();
        me2.setValue(textrepeat);
        appiumDriver.navigate().back();
    }

    @Step("<StartX>,<StartY> oranlarından <EndX>,<EndY> oranlarına <times> kere swipe et")
    public void pointToPointSwipe(int startX, int startY, int endX, int endY, int count) {
        Dimension d = appiumDriver.manage().window().getSize();
        int height = d.height;
        int width = d.width;

        startX = (width * startX) / 100;
        startY = (height * startY) / 100;
        endX = (width * endX) / 100;
        endY = (height * endY) / 100;

        for (int i = 0; i < count; i++) {
            //appiumDriver.swipe(startX, startY, endX, endY, 1000);
        }
    }

    @Step("restart app")
    public void restart() throws InterruptedException {
        appiumDriver.closeApp();
        appiumDriver.launchApp();
        System.out.println("App restarted");
        waitForsecond(5);
        existClickByKey("allowed");
    }

    public boolean isElementPresent(By by) {
        try {
            appiumDriver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Step("pop-up allowed")
    public void closePopupAndCookie() throws InterruptedException {
        waitForsecond(3);
        appiumDriver.context("NATIVE_APP");
        new WebDriverWait(appiumDriver, 30)
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='İZİN VER']")));  //özelleştirilmiş

        if (isElementPresent(By.xpath("//*[@text='İZİN VER']"))) {
            System.out.print("Notification pop-up kapatıldı !!!!!!");
            waitForsecond(3);
            appiumDriver.context("NATIVE_APP");
            appiumDriver.findElement(By.xpath("//*[@text='İZİN VER']")).click();
        } else {
            System.out.print("Pop-up görülmedi");

        }
    }

    @Step("<Key> li elementinin görünürlüğü kontrol edilir.")
    public boolean isElementVisible(String keyword) {
        return methods.isElementVisible(keyword);
    }

    @Step("<repeat> defa scroll edilir.")
    public void swipeUp(int repeat) {
        methods.swipeUp(repeat);
    }

    @Step("<keyword> 'li elemente scroll edilir.")
    public void scrollWithAction(String keyword) {
        methods.scrollWithAction(methods.getElements(keyword));
    }

    @Step("<key> 'li elemente <text> degeri girilir.")
    public void sendKeys(String keyword, String text) {
        methods.sendKeys(keyword, text);
    }

    @Step("<key> 'li elemente <text> degeri girilir ve enter'a basılır.")
    public void sendKeysEnter(String keyword, String text) {
        methods.sendKeysEnter(keyword, text);
    }

    @Step("<key> elementinin üzerinde durulur.")
    public void mouseHover(String keyword) {
        methods.mouseHover(keyword);
    }

    @Step("Bir önceki sayfaya gidilir.")
    public void goBackPage() {
        methods.goBackPage();
    }

    @Step("Sayfayı yeniden yükler.")
    public void refreshPage() {
        methods.refreshPage();
    }

    @Step("Bir sonraki sayfaya gidilir.")
    public void goForwardPage() {
        methods.goForwardPage();
    }

    @Step("<key> adresine gidilir.")
    public void goToUrl(String url) { methods.goToUrl(url); }

    @Step("<keyword> 'lü ürünün favorilerde olduğu doğrulanır.")
    public void getTextMob(String keyword){
        //System.out.println(methods.getTextMobile(keyword));
        Assert.assertTrue("Favorilerde ürün mevcut değil!",methods.getTextMobile(keyword).contains("xxxx"));
    }
    @Step("<keyword> 'li elementin <text> textine tıklar.")
    public void selectTextMobile(String keyword, String text){
        methods.selectTextMobileDrop(keyword,text);
    }

    @Step("Fare <x>,<y> kadar hareket ettirilir.")
    public void mouseMove( int x, int y) {
        methods.mouseMove( x, y);
    }

    @Step("<key> 'li elemente rastgele tıklanır.")
    public void randomClick(String keyword) {
        methods.randomElementClicker(methods.getElements(keyword));
    }

    @Step("<key> li elementin merkezine double tıkla ")
    public void pressElementWithKey2(String key) {
        Actions actions = new Actions(appiumDriver);
        actions.moveToElement(methods.findElementMobile(key));
        actions.doubleClick();
        actions.perform();
        appiumDriver.getKeyboard();
    }

    @Step("Enter tıkla")
    public void keyboardClickEnter() {
        Actions actions=new Actions(appiumDriver);
        actions.sendKeys(Keys.ENTER).build().perform();
        logger.info("ENTER tuşuna basıldı.");
    }

}