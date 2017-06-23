package com.appium.framework.core;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import static com.appium.framework.core.AppiumServer.startAppiumServer;
import static com.appium.framework.core.AppiumServer.stopAppiumServer;
import static com.support.framework.support.Property.*;
import static com.support.framework.support.Util.getURLPort;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = "json:target/cucumber.json",
        features = {"classpath:features"},
        glue = {"com.appium.test.stepdefs", "com.appium.framework.stepdefs"},
        tags = {"~@ignore"})
public class AppiumCukes {

    private static final Logger LOG = Logger.getLogger(AppiumCukes.class);

    @BeforeClass
    public static void startAppium() {

        if (PLATFORM_NAME.toString().equalsIgnoreCase("android") && DEVICE_NAME.toString().contains("qa-devicefarm")) {
            LOG.info("### Trying ADB Connect To QA Farm Device ###");
            try {
                Runtime rt = Runtime.getRuntime();
                rt.exec("adb connect " + DEVICE_NAME.toString());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        LOG.info("### Starting Appium Server ####");
        LOG.info("Appium Host: " + getURLPort("url") + " & Port: " + getURLPort("port") + " & Log Level: " + APPIUM_LOG);
        LOG.info("Platform: " + PLATFORM_NAME + " & Test Device: " + DEVICE_NAME);
        LOG.info("Application In Test: " + APP_FILE);
        LOG.info("Keep App State Between Scenarios: " + NO_RESET + " & Compare Image Status: " + COMPARE_IMAGE);
        startAppiumServer();
        LOG.info("### Appium Server Started ###");
    }

    @AfterClass
    public static void stopAppium() {
        LOG.info("### Closing Appium Server ###");
        LOG.info("### Closing Done ###");
        stopAppiumServer();
    }

}