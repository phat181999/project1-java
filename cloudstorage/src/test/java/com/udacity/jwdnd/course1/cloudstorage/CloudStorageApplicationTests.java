package com.udacity.jwdnd.course1.cloudstorage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginPageTitle() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void testSignUpAndLogin() {
        String firstName = "John";
        String lastName = "Doe";
        String userName = "john_doe";
        String password = "password123";

        doSignUp(firstName, lastName, userName, password);
        doLogin(userName, password);

        Assertions.assertTrue(driver.getTitle().contains("Home"));
    }

    private void doSignUp(String firstName, String lastName, String userName, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:" + this.port + "/signup");

        wait.until(ExpectedConditions.titleContains("Sign Up"));

        driver.findElement(By.id("inputFirstName")).sendKeys(firstName);
        driver.findElement(By.id("inputLastName")).sendKeys(lastName);
        driver.findElement(By.id("inputUsername")).sendKeys(userName);
        driver.findElement(By.id("inputPassword")).sendKeys(password);
        driver.findElement(By.id("buttonSignUp")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("success-msg")));
    }

    private void doLogin(String userName, String password) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:" + this.port + "/login");

        WebElement loginUserName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        loginUserName.sendKeys(userName);

        WebElement loginPassword = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        loginPassword.sendKeys(password);

        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        loginButton.click();

        wait.until(ExpectedConditions.titleContains("Home"));
    }

    @Test
    public void testUploadFile() {
        String userName = "john_doe";
        String password = "password123";

        doLogin(userName, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        String fileName = "test-file.txt";
        File file = new File(fileName);
        String filePath = file.getAbsolutePath();

        WebElement fileUploadInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        fileUploadInput.sendKeys(filePath);

        WebElement uploadButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("uploadButton")));
        uploadButton.click();

        Assertions.assertTrue(wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("success-msg"), "File uploaded successfully")));
    }

    @Test
    public void testLogout() {
        String userName = "john_doe";
        String password = "password123";

        doLogin(userName, password);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout-button")));
        logoutButton.click();

        Assertions.assertTrue(wait.until(ExpectedConditions.titleContains("Login")));
    }
}
