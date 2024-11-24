import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

public class FitPeoAssigment1  {
    public static void main(String[] args) {
        WebDriver driver = null;
        SoftAssert s = new SoftAssert();

        try {
            driver = new ChromeDriver();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            driver.get("https://fitpeo.com/");
            driver.manage().window().maximize();

            try {
                driver.findElement(By.xpath("//div[text()='Revenue Calculator']")).click();
            } catch (Exception e) {
                throw new Exception("Failed to click on 'Revenue Calculator' button: " + e.getMessage());
            }

            JavascriptExecutor js = (JavascriptExecutor) driver;

            WebElement targetElement;
            try {
                targetElement = driver.findElement(By.xpath("//h4[text()='Medicare Eligible Patients']"));
                js.executeScript("arguments[0].scrollIntoView(true);", targetElement);
            } catch (Exception e) {
                throw new Exception("Failed to locate or scroll to 'Medicare Eligible Patients': " + e.getMessage());
            }

            Actions actions = new Actions(driver);
            try {
            	//try to set the value of slidebar
                WebElement slidebar = driver.findElement(By.cssSelector(".MuiSlider-thumb"));
                actions.dragAndDropBy(slidebar, 93, 0).build().perform();
            } catch (Exception e) {
                throw new Exception("Failed to interact with the slider: " + e.getMessage());
            }

            WebElement input;
            
            // send the input to the slide bar input and update the slidebar
            try {
                input = driver.findElement(By.cssSelector("input[type='number']"));
                actions.click(input).keyDown(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).keyUp(Keys.CONTROL).build().perform();
                int sendValue = 560;
                input.sendKeys(String.valueOf(sendValue));

                String value = driver.findElement(By.cssSelector("input[type='range']")).getAttribute("aria-valuenow");
                int inputValue = Integer.parseInt(value);
                //System.out.println(inputValue);

                s.assertEquals(sendValue, inputValue, "Input value does not match slider value.");
            } catch (Exception e) {
                throw new Exception("Failed to input or validate patient count: " + e.getMessage());
            }

            try {
            	
            	// check mark all the mention Checkbox;
                WebElement check1 = driver.findElement(By.xpath("//p[text()='CPT-99091']"));
                js.executeScript("arguments[0].scrollIntoView(true);", check1);

                driver.findElement(By.xpath("//p[text()='CPT-99091']/following-sibling::label//input[@type='checkbox']")).click();
                driver.findElement(By.xpath("//p[text()='CPT-99453']/following-sibling::label//input[@type='checkbox']")).click();
                driver.findElement(By.xpath("//p[text()='CPT-99454']/following-sibling::label//input[@type='checkbox']")).click();

                WebElement check3 = driver.findElement(By.xpath("//p[text()='CPT-99474']"));
                js.executeScript("arguments[0].scrollIntoView(true);", check3);
                driver.findElement(By.xpath("//p[text()='CPT-99474']/following-sibling::label//input[@type='checkbox']")).click();
            } catch (Exception e) {
                throw new Exception("Failed to interact with CPT checkboxes: " + e.getMessage());
            }

            try {
                js.executeScript("arguments[0].scrollIntoView(true);", targetElement);
                actions.click(input).keyDown(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).keyUp(Keys.CONTROL).build().perform();
                int patientPerMonth = 820;
                input.sendKeys(String.valueOf(patientPerMonth));

                String totalValue = driver.findElement(By.cssSelector(".MuiToolbar-root p.css-1xroguk:nth-of-type(4)")).getText();
                String numericValue = totalValue.replaceAll("[^0-9]", "");
                int totalExpected = Integer.parseInt(numericValue);

                s.assertEquals(110700, totalExpected, "Total reimbursement does not match the expected value.");
            } catch (Exception e) {
                throw new Exception("Failed to calculate or validate the total reimbursement: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Exception occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
