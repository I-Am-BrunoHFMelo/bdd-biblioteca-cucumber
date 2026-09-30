package com.example.bdd.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features", tags = "@LivroTeste", glue = "com.example.bdd.steps", monochrome = false, dryRun = false, plugin = {"pretty", "html:target/cucumber-livro-report.html", "json:target/cucumber-json/livro.json"})
public class LivroTeste { }
