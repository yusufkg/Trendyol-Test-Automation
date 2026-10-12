package com.yusuf.qa.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/e-commerce.feature",
    glue = {"com.yusuf.qa.steps", "com.yusuf.qa.support"},
    tags = "not @planned",
    plugin = {"pretty"}
)
public class EcommerceIT {}
