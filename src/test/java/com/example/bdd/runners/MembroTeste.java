package com.example.bdd.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features", tags = "@MembroTeste", glue = "com.example.bdd.steps", monochrome = false, dryRun = false, plugin = {"pretty", "html:target/cucumber-membro-report.html"})
public class MembroTeste { }
