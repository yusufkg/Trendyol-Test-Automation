package com.yusuf.qa.support;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
public class Hooks {
    @After public void after(Scenario scenario) {
        try {
            ScenarioCleanup.finish(TestContext.driver(), scenario.isFailed(), scenario::attach, scenario::log);
        } finally {
            TestContext.DRIVER.remove();
        }
    }
}
