package com.example.scraper.talent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.example.shell.Company;
import com.example.shell.Job;
import com.example.shell.Scraper;
import com.example.shell.SiteType;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;

public class Talent extends Company implements Scraper<Job> {
    public Talent(Integer id, String url, String title, String logo, SiteType type) {
        super(id, url, title, logo, type);
    }

    public Collection<Job> getJobsData(int jobLimit, String search) throws IOException {
        System.setProperty("webdriver.chrome.driver",
                "/Users/williamssissoko/Desktop/MDX/ADVANCED-WEB-CLASS/coursework1/salary-comp/scraper-job/src/main/java/com/example/scraper/chromedriver");
        ChromeDriver driver = new ChromeDriver();
        Collection<Job> companies = new ArrayList<Job>();

        try {
            driver.get(this.getUrl() + "/jobs");

            driver.findElement(By.cssSelector("#nv-k")).sendKeys(search + Keys.ENTER);
            String pageSource = driver.getPageSource();
            companies = mapJobsData(jobLimit, pageSource, search);
        } catch (Exception e) {
            System.out.println("Error while getting ChromeDriver = " + e);
        }

        return companies;
    }

    public Collection<Job> mapJobsData(int jobLimit, String input, String type) throws IOException {
        Collection<Job> jobList = new ArrayList<Job>();
        Document doc = Jsoup.parse(input);

        for (int i = 0; i < jobLimit; i++) {
            String companyName = doc.select("div.card__job-empname-label").get(i).text();
            String jobTitle = doc.select("h2.card__job-title").get(i).text();

            String location = doc.select("div.card__job-location").get(i).text();
            String salary = doc.selectXpath("//div[@class='sideCard--stats--mainNumber timeBased']").text();
            String logo = doc.select("img[class='card__job-logo']").get(i).attr("src");

            String url = this.getUrl() + doc.select("div.link-job-wrap").get(i).attr("data-link");
            String shortDescription = doc.selectXpath("//p[@class='card__job-snippet']").get(i).text();
            String jobType = type;

            Job job = new Job(companyName, jobTitle, salary, logo, url, location, shortDescription, jobType);
            jobList.add(job);
        }
        return jobList;
    }

    public void init() {
    }
}
