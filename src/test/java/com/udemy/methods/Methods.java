package com.udemy.methods;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udemy.driver.BaseTest;
import com.udemy.info.ElementInfo;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Methods extends BaseTest {
    //private final static String ELEMENTS_PATH = "src/test/resources/elements/elements.json";
    private final static String ELEMENTS_JSON_PATH = "element";
    private final static String ANDROID = "android";
    private final static String IOS = "ios";
    public Map<String, Object> elementsMap;
    public Logger logger;
    protected MobileElement mobileElement;

    public Methods() {
        logger = Logger.getLogger(Methods.class);
        initByMap(getFileList());
    }

    public void waitBySeconds(long seconds) {
        try {
            Thread.sleep(seconds * 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void click(String keyword) {
        String device = getDeviceInfo(keyword);
        if (device.equals(ANDROID) || device.equals(IOS)) {
            findElementMobile(keyword).click();
            logger.info(keyword + " ANDROID elementine tıklandı.");
       }
        else {
            logger.error(keyword + " elementine tıklanamadı.");
        }
    }

    public MobileElement findElementMobile(String keyword) {
        By by = getByTypeWithMap(keyword);
        return (MobileElement) mobileWait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public MobileElement findElementMobile(By element) {
        return (MobileElement) mobileWait.until(ExpectedConditions.presenceOfElementLocated(element));
    }

    public List<MobileElement> findElementsMobile(String keyword) {
        By by = getByTypeWithMap(keyword);
        mobileWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        return appiumDriver.findElements(by);
    }

    public void randomElementClick(String keyword) {
        Random random = new Random();
        int randomInt;
        if (getDeviceInfo(keyword).equals(ANDROID) || getDeviceInfo(keyword).equals(IOS)) {
            List<MobileElement> elements = findElementsMobile(keyword);
            randomInt = random.nextInt(elements.size());
            mobileElement = elements.get(randomInt);
        } else {
            logger.error(keyword + " elementine tıklanamadı.");
        }
    }

    public void selectRandom(String keyword) {
        Random random = new Random();
        int randomInt;
        List<MobileElement> elemets = findElementsMobile(keyword);
        randomInt = random.nextInt(elemets.size());
        mobileElement = elemets.get(randomInt);
    }

    public void swipeUp(int repeat) {
        try {
            Dimension dims = appiumDriver.manage().window().getSize();
            PointOption pointOptionStart, pointOptionEnd;
            int edgeBorder = 2;
            final int PRESS_TIME = 1200; // ms
            pointOptionStart = PointOption.point(dims.width / 2, dims.height / 3);
            pointOptionEnd = PointOption.point(dims.width / 2, edgeBorder);
            for (int i = 0; i < repeat; i++) {
                new TouchAction(appiumDriver).press(pointOptionStart)
                        .waitAction(WaitOptions.waitOptions(Duration.ofMillis(PRESS_TIME)))
                        .moveTo(pointOptionEnd).release().perform();
                waitBySeconds(2);
            }
            logger.info(repeat + " defa  sayfa scroll edildi.");
        } catch (Exception e) {
            logger.error("Sayfa scroll edilemedi hata meydana geldi:" + e);
        }
    }

    public void swipeRight() {
        Dimension size = appiumDriver.manage().window().getSize();
        int startx = (int) (size.width * 0.9);
        int endx = (int) (size.width * 0.20);
        int starty = size.height / 2;
        new TouchAction(appiumDriver).press(PointOption.point(startx, starty))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point(endx,starty)).release().perform();
    }

    public void swipeLeft() {
        Dimension size = appiumDriver.manage().window().getSize();
        int startx = (int) (size.width * 0.8);
        int endx = (int) (size.width * 0.20);
        int starty = size.height / 2;
        new TouchAction(appiumDriver).press(PointOption.point(startx, starty))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(PointOption.point(endx,starty)).release();
    }

    public void sendKeys(String keyword, String text) {
        if (getDeviceInfo(keyword).equals(ANDROID) || getDeviceInfo(keyword).equals(IOS)) {
            findElementMobile(keyword).clear();
            findElementMobile(keyword).sendKeys(text);
            logger.info(keyword + " elementine " + text + " değeri girildi.");
        }
    }

    public void sendKeysEnter(String keyword, String text) {
        if (getDeviceInfo(keyword).equals(ANDROID) || getDeviceInfo(keyword).equals(IOS)) {
            findElementMobile(keyword).clear();
            findElementMobile(keyword).sendKeys(text);
            ((AndroidDriver<MobileElement>) appiumDriver).pressKey(new KeyEvent(AndroidKey.ENTER));
            logger.info(keyword + " elementine " + text + " değeri girildi.");
        }
    }

    public String getTextMobile(String keyword) {
        By by = getByTypeWithMap(keyword);
        mobileWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        return appiumDriver.findElement(by).getText();
    }

    public boolean isElementContainText(String key, String text) {
        waitBySeconds(9);
        boolean degisken = false;
        try {
            findElementMobile(key).getText().contains(text);
            logger.info(key + " elementi görünür durumdadır.");
            degisken = true;
        } catch (Exception e) {
            logger.info(key + " elementi görünür değil.");
            degisken = false;
        }
        return degisken;
    }

    public void selectTextMobileDrop(String keyword, String text) {
        By by = getByTypeWithMap(keyword);
        mobileWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
        Select drpCountry = new Select(appiumDriver.findElement(by));
        drpCountry.selectByVisibleText(text);
    }

    public By getByTypeWithMap(String keyword) {
        ElementInfo elements = (ElementInfo) elementsMap.get(keyword);
        Map<String, By> map = initByMap(elements.getLocatorValue());
        return map.getOrDefault(elements.getLocatorType(), null);
    }

    public Map<String, By> initByMap(String locatorValue) {
        Map<String, By> map = new HashMap<>();
        map.put("id", By.id(locatorValue));
        map.put("css", By.cssSelector(locatorValue));
        map.put("xpath", By.xpath(locatorValue));
        map.put("class", By.className(locatorValue));
        map.put("linktext", By.linkText(locatorValue));
        map.put("name", By.name(locatorValue));
        map.put("partial", By.partialLinkText(locatorValue));
        map.put("tagname", By.tagName(locatorValue));
        return map;
    }

    public void initByMap(File[] fileList) {
        elementsMap = new ConcurrentHashMap<>();
        Type elementType = new TypeToken<List<ElementInfo>>() {
        }.getType();
        Gson gson = new Gson();
        List<ElementInfo> elementInfoList;
        for (File file : fileList) {
            try {
                elementInfoList = gson.fromJson(new FileReader(file), elementType);
                elementInfoList.parallelStream().forEach(elementInfo -> elementsMap.put(elementInfo.getKeyword(), elementInfo));
            } catch (FileNotFoundException e) {
            }
        }
    }

    public File[] getFileList() {
        File[] fileList = new File(this.getClass().getClassLoader().getResource(ELEMENTS_JSON_PATH).getFile()).listFiles(pathname -> !pathname.isDirectory() && pathname.getName().endsWith(".json"));
        //fileList = fileList + iki array birleştirme bak
        if (fileList == null) {
            throw new NullPointerException("Belirtilen dosya bulunamadı.");
        }
        return fileList;
    }

    public void mouseMove(int x, int y) {
        Actions action = new Actions(appiumDriver);
        action.moveByOffset(x, y).perform();
    }

    public void mouseHover(String keyword) {
        Actions actions = new Actions(appiumDriver);
        actions.moveToElement(findElementMobile(keyword)).perform();
    }

    public void mouseHover(MobileElement mebElement) {
        Actions actions = new Actions(appiumDriver);
        actions.moveToElement(mebElement).perform();
    }

    public void swipeUpAccordingToPhoneSize() {
        if (appiumDriver instanceof AndroidDriver) {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 50) / 100;
            int swipeEndHeight = (height * 90) / 100;
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
        } else {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 40) / 100;
            int swipeEndHeight = (height * 90) / 100;
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
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
        } else {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 90) / 100;
            int swipeEndHeight = (height * 40) / 100;
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
        }
    }

    public void goBackPage() {
        appiumDriver.navigate().back();
        logger.info("Bir önceki sayfaya gidildi.");
    }

    public void goForwardPage() {
        appiumDriver.navigate().forward();
        logger.info("Bir sonraki sayfaya gidildi.");
    }

    public void refreshPage() {
        appiumDriver.navigate().refresh();
        logger.info("Sayfa yenilendi.");
    }

    public void goToUrl(String url) {
        appiumDriver.get(url);
        logger.info(url + " adresine gidildi.");
    }

    public List<MobileElement> getElements(String keyword) {
        By by = getByTypeWithMap(keyword);
        List<MobileElement> l1 = appiumDriver.findElements(by);
        return l1;
    }

    public void randomElementClicker(List<MobileElement> list) {
        Random rand = new Random();
        list.get(rand.nextInt(list.size())).click();
    }

    public boolean isElementVisible(String keyword) {
        if (getDeviceInfo(keyword).equals(ANDROID) || getDeviceInfo(keyword).equals(IOS)) {
            try {
                mobileElement = findElementMobile(keyword);
                logger.info(keyword + " elementi görünür.");
                waitBySeconds(1);
                return true;
            } catch (Exception e) {
                logger.warn(keyword + " element görünür değil yada bulunamadı." + e);
                return false;
            }
        }
        return false;
    }
    //sonra returnlere bak!!!

    public void clickWithText(String keyword, String text) {
        click(String.valueOf(By.xpath("//" + keyword + "[text()='" + text + "']")));
    }

    public void clickIfExist(String keyword) {
        if (isElementVisible(keyword)) {
            click(keyword);
        }
    }

    public void scrollWithAction(List<MobileElement> list) {
        Actions actions = new Actions(appiumDriver);
        actions.moveToElement(mobileElement).build().perform();
    }

    public String getDeviceInfo(String keyword) {
        ElementInfo elements = (ElementInfo) elementsMap.get(keyword);
        return elements.getDeviceInfo();
    }

    public void saveValue(String keyword, String value) {
    elementsMap.put(keyword,findElementMobile(value).getText());
    }

}
