package Main;

import Constant.Constants;
import Crawler.ExerciseDetailCrawler;
import Crawler.ExerciseLinkCrawler;
import Crawler.ExerciseListCrawler;
import Helper.FileHelper;
import Models.Exercise;
import com.google.gson.Gson;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Program {
    public static void main(String[] args) throws InterruptedException, IOException {
        WebDriverManager.chromedriver().proxy(Constants.PROXY).setup();
        WebDriver driver = new ChromeDriver();

        //crawl exercise list
        System.out.println("Exercise list crawling...");
        ExerciseListCrawler exerciseListCrawler = new ExerciseListCrawler(driver);
        List<String> exerciseListUrls = exerciseListCrawler.Crawl();

        //crawl exercise links
        System.out.println("Exercise links crawling...");

        List<String> exerciseLinks = FileHelper.readAllLines(Constants.EXERCISE_LINKS_OUTPUT_FILE);
        if (FileHelper.isFileExist(Constants.EXERCISE_LINKS_OUTPUT_FILE) == false || exerciseLinks.size()<=1) {
            exerciseLinks = new ArrayList<String>();
            for (String exerciseListUrl : exerciseListUrls) {
                ExerciseLinkCrawler exerciseLinkCrawler = new ExerciseLinkCrawler(driver);
                exerciseLinks.addAll(exerciseLinkCrawler.Crawl(exerciseListUrl));
            }
        }
        List<String> listWithoutDuplicates = exerciseLinks.stream()
                .distinct()
                .collect(Collectors.toList());

        //crawl exercise
        Gson gson = new Gson();
        for (String exerciseLink : listWithoutDuplicates) {
            ExerciseDetailCrawler exerciseDetailCrawler = new ExerciseDetailCrawler(driver);
            System.out.println(String.format("Processing %s", exerciseLink));
            Exercise exercise = exerciseDetailCrawler.Crawl(exerciseLink);
            if (exercise != null) {
                String jsonString = gson.toJson(exercise);
                FileHelper.writeToFile(jsonString,String.format("%s/%s.txt","Data", exercise.getName()),false);
                System.out.println("done");
            }else{System.out.println("fail");}

        }
    }
}
