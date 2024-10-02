package saucedemotests.core;

import com.saucedemo.pages.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import testframework.PropertiesManager;
import testframework.core.BaseWebTest;

public class SauceDemoBaseWebTest extends BaseWebTest {
    protected LoginPage loginPage;
    protected InventoryPage inventoryPage;
    protected ShoppingCartPage shoppingCartPage;
    protected CheckoutOverviewPage checkoutOverviewPage;
    protected CheckoutYourInformationPage checkoutYourInformationPage;
    protected CheckoutCompletePage checkoutCompletePage;

    @BeforeEach
    public void beforeTests() {
        // Initialize page objects before each test
        inventoryPage = new InventoryPage();
        loginPage = new LoginPage();
        shoppingCartPage = new ShoppingCartPage();
        checkoutOverviewPage = new CheckoutOverviewPage();
        checkoutYourInformationPage = new CheckoutYourInformationPage();
        checkoutCompletePage = new CheckoutCompletePage();

        // Navigate to base page (SauceDemo homepage)
        driver().get(PropertiesManager.getConfigProperties().getProperty("sauceDemoBaseUrl"));
    }

    @BeforeAll
    public static void beforeAll() {
        // Perform any global setup before all tests, such as initializing configurations
        System.out.println("Starting all tests...");
    }

    @AfterEach
    public void afterTest() {
        // Close the browser after each test
        driver().close();
        System.out.println("Test finished, browser closed.");
    }

    @AfterAll
    public static void afterAll() {
        // Perform global teardown after all tests
        System.out.println("All tests completed.");
    }

    // Extract methods that use multiple pages
    public void authenticateWithUser(String username, String pass) {
        // Log in with the provided username and password
        loginPage.submitLoginForm(username, pass);

        // Wait for the Inventory page to load and verify it
        inventoryPage.waitForPageTitle();
    }
}
