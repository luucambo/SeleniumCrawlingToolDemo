package Crawler;

import Constant.Constants;
import Helper.FileHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseLinkCrawler {
    private WebDriver driver;

    public ExerciseLinkCrawler(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> Crawl(String exerciseListUrl) throws IOException {
        ArrayList<String> result = new ArrayList<String>();

        driver.get(exerciseListUrl);

        List<WebElement> exercises = driver.findElements(By.xpath("//div[@class='exercise_list_image']//child::a"));
        for (WebElement exercise : exercises) {
            result.add(exercise.getAttribute("href"));
        }

        FileHelper.writeToFile(result, Constants.EXERCISE_LINKS_OUTPUT_FILE,true);
        return result;
    }

}
