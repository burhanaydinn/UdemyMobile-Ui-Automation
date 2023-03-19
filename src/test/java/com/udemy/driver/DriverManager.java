package com.udemy.driver;

import com.udemy.imp.ConfigReader;
import com.thoughtworks.gauge.ExecutionContext;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DriverManager {
    public static AppiumDriver appiumDriver;
    public static WebDriver webDriver;
    public static JavascriptExecutor getJSExecutor;
    public static WebDriverWait mobileWait;
    public static WebDriverWait webWait;
    public static Properties properties;
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final boolean IS_WINDOWS = (OS.contains("win"));
    private static final boolean IS_MAC = (OS.contains("mac"));
    private static final boolean IS_LINUX = (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    public static boolean IS_CHROME;
    public static boolean IS_EDGE;
    public static boolean IS_FIREFOX;
    public static boolean IS_ANDROID;
    public static boolean IS_IOS;

    private final String WEB_URL = "https://www.udemy.com/";
    private final Logger logger;

    public DriverManager() {
        String log4jConfigFile = System.getProperty("user.dir") + File.separator + "src/test/resources/log4j.properties";
        PropertyConfigurator.configure(log4jConfigFile);
        logger = Logger.getLogger(DriverManager.class);
    }

    protected void initDriver(ExecutionContext context) throws MalformedURLException {
        initilazeTest(context);
    }

    private void initilazeTest(ExecutionContext context) throws MalformedURLException {
        setTestType(context);
        setDriver();
    }

    private void setDriver() throws MalformedURLException {
        if(IS_ANDROID){
            setupAndroid();
        } else if(IS_IOS){
            setupIos();
        }
    }

    private void setTestType(ExecutionContext context){
        List<String> list = context.getCurrentScenario().getTags();
        list = list.stream().map(String::toLowerCase).collect(Collectors.toList());
        if (list.isEmpty()) {
            list = context.getCurrentSpecification().getTags();
            list = list.stream().map(String::toLowerCase).collect(Collectors.toList());
        }
        if (list.contains("android") || list.contains("androıd")) {
            IS_ANDROID = true;
        } else {
            logger.error("Senaryoda tag bulunamadi veya desteklenmeyen bir tag girildi.");
            Assertions.fail("Senaryoda tag bulunamadı veya desteklenmeyen bir tag girildi.");
        }
    }

    public void closeDriver() {
        if (appiumDriver != null) {
            appiumDriver.quit();
            logger.info("Appium driver kapatıldı.");
        }
        IS_ANDROID = false;
    }

    protected void setupAndroid() throws MalformedURLException {
        properties = ConfigReader.getProperties();
        DesiredCapabilities desiredCapabilitiesAndroid = new DesiredCapabilities();
        desiredCapabilitiesAndroid.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        desiredCapabilitiesAndroid.setCapability(MobileCapabilityType.DEVICE_NAME, "android");
        desiredCapabilitiesAndroid.setCapability(MobileCapabilityType.UDID, "emulator-5554");
        desiredCapabilitiesAndroid.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.udemy.android");
        desiredCapabilitiesAndroid.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, "com.udemy.android.SplashActivity");
        desiredCapabilitiesAndroid.setCapability(MobileCapabilityType.NO_RESET, false);
        desiredCapabilitiesAndroid.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 350);
        desiredCapabilitiesAndroid.setCapability("unicodeKeyboard", true);
        desiredCapabilitiesAndroid.setCapability("resetKeyboard", true);
        URL url = new URL("http://0.0.0.0:4723/wd/hub");
        appiumDriver = new AndroidDriver(url, desiredCapabilitiesAndroid);
        mobileWait =  new WebDriverWait(appiumDriver, 20);
        mobileWait.withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(400))
                .ignoring(NoSuchElementException.class);
        logger.info("Android emulator başlatılıyor.");
    }

    protected void setupIos() throws MalformedURLException {
        properties = ConfigReader.getProperties();
        DesiredCapabilities desiredCapabilitiesIos = new DesiredCapabilities();
        desiredCapabilitiesIos.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
        desiredCapabilitiesIos.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        desiredCapabilitiesIos.setCapability(MobileCapabilityType.UDID, "ıos-0000");
        desiredCapabilitiesIos.setCapability(MobileCapabilityType.DEVICE_NAME, "example");
        desiredCapabilitiesIos.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12.4");
        desiredCapabilitiesIos.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "bundleIdHere");
        desiredCapabilitiesIos.setCapability(MobileCapabilityType.NO_RESET, true);
        desiredCapabilitiesIos.setCapability(MobileCapabilityType.FULL_RESET, false);
        desiredCapabilitiesIos.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 300);
        mobileWait =  new WebDriverWait(webDriver, 15);
        mobileWait.withTimeout(30, TimeUnit.SECONDS).pollingEvery(1000, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
        URL url = new URL("http://127.0.0.1:4723/wd/hub");
        appiumDriver = new IOSDriver<>(url, desiredCapabilitiesIos);
        logger.info("IOS emulator başlatılıyor.");
    }
}

