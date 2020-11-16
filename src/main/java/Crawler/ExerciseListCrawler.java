package Crawler;

import Constant.Constants;
import Helper.FileHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseListCrawler {
    private WebDriver driver;

    public ExerciseListCrawler(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> Crawl() throws IOException {
        ArrayList<String> hrefs = null;
        hrefs = FileHelper.readAllLines(Constants.EXERCISE_LIST_OUTPUT_FILE);

        // get exercise list data from catalyst
        if (hrefs.size() <= 1) {
            driver.get("https://www.catalystathletics.com/exercises/");
            List<WebElement> exerciseLists = driver.findElements(By.xpath("//div[@class='exercise_list_image']//child::a"));
            hrefs = new ArrayList<String>();
            for (WebElement exerciseList : exerciseLists) {
                hrefs.add(exerciseList.getAttribute("href"));
            }

            FileHelper.writeToFile(hrefs, Constants.EXERCISE_LIST_OUTPUT_FILE, true);
        }
        return hrefs;
    }
}
