package Crawler;

import Helper.Logger;
import Models.Exercise;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExerciseDetailCrawler {
    private WebDriver driver;

    public ExerciseDetailCrawler(WebDriver driver) {
        this.driver = driver;
    }


    public Exercise Crawl(String exerciseUrl) throws IOException {

        try {
            Exercise result = new Exercise();

            String[] nameSplit = exerciseUrl.split("/");
            String name = nameSplit[nameSplit.length - 1];
            result.setName(name);
            driver.get(exerciseUrl);
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            WebElement exerciseDescription = driver.findElement(By.xpath("//div[@class='exercise_description']"));
            String innerHtml = exerciseDescription.getAttribute("innerText");

            String[] splits = innerHtml.split("\n");
            List<String> processingList = new ArrayList<String>();
            for (int i = 0; i < splits.length; i++) {
                String processString = splits[i]
                        .replace("\n", "")
                 .trim();

                if (processString.length() > 1)
                    processingList.add(processString);
            }

            for (int i = 0; i < processingList.size(); i++) {
                String processItem = processingList.get(i);
                if (processItem.equals("Execution")) {
                    result.setExecution(processingList.get(i + 1));
                } else if (processItem.equals("Notes")) {
                    result.setNote(processingList.get(i + 1));
                } else if (processItem.equals("Purpose")) {
                    result.setPurpose(processingList.get(i + 1));
                } else if (processItem.equals("Variations")) {
                    result.setVariations(processingList.get(i + 1));
                } else if (processItem.equals("Programming")) {
                    result.setProgramming(processingList.get(i + 1));
                } else {
                    result.setOverview(processItem);
                }
            }
            return result;
        } catch (Exception exception) {
            Logger.Log(String.format("%s : %s",
                    exerciseUrl,
                    exception.getMessage()));
            Logger.Log("-----------------------------------------");
            return null;
        }
    }
}
