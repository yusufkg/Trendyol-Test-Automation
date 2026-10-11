package com.yusuf.qa.runners;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/resources/features", glue={"com.yusuf.qa.steps", "com.yusuf.qa.support"}, tags="@trendyol", plugin={"pretty", "html:target/trendyolit-report.html", "json:target/trendyolit-report.json"})
public class TrendyolIT {}
