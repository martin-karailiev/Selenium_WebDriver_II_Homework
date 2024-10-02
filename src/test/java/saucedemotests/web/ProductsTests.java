package saucedemotests.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import saucedemotests.core.SauceDemoBaseWebTest;
import saucedemotests.enums.TestData;

public class ProductsTests extends SauceDemoBaseWebTest {
    public final String BACKPACK_TITLE = "Sauce Labs Backpack";
    public final String SHIRT_TITLE = "Sauce Labs Bolt T-Shirt";

    @BeforeEach
    public void beforeTest(){
        // Authenticate with Standard user
    }

    @Test
    public void productAddedToShoppingCart_when_addToCart(){
        loginPage.navigate();
        loginPage.submitLoginForm(TestData.STANDARD_USER_USERNAME.getValue(), TestData.STANDARD_USER_PASSWORD.getValue());
        inventoryPage.waitForPageTitle();

        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);

        inventoryPage.clickShoppingCartLink();

        Assertions.assertEquals(2, shoppingCartPage.getShoppingCartItems().size(), "Продуктите не са добавени правилно в количката.");
    }

    @Test
    public void userDetailsAdded_when_checkoutWithValidInformation(){
        loginPage.navigate();
        loginPage.submitLoginForm(TestData.STANDARD_USER_USERNAME.getValue(), TestData.STANDARD_USER_PASSWORD.getValue());
        inventoryPage.waitForPageTitle();

        inventoryPage.addProductsByTitle(BACKPACK_TITLE);
        inventoryPage.clickShoppingCartLink();

        shoppingCartPage.clickCheckout();

        checkoutYourInformationPage.fillShippingDetails("John", "Doe", "12345");
        checkoutYourInformationPage.clickContinue();

        Assertions.assertEquals(1, checkoutOverviewPage.getShoppingCartItems().size(), "Броят на продуктите не съвпада.");

        String totalLabel = checkoutOverviewPage.getTotalLabelText();
        Assertions.assertTrue(totalLabel.contains("Total"), "Общата сума не е изчислена правилно.");
    }

    @Test
    public void orderCompleted_when_addProduct_and_checkout_withConfirm(){
        loginPage.navigate();
        loginPage.submitLoginForm(TestData.STANDARD_USER_USERNAME.getValue(), TestData.STANDARD_USER_PASSWORD.getValue());
        inventoryPage.waitForPageTitle();

        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);
        inventoryPage.clickShoppingCartLink();

        shoppingCartPage.clickCheckout();
        checkoutYourInformationPage.fillShippingDetails("John", "Doe", "12345");
        checkoutYourInformationPage.clickContinue();

        checkoutOverviewPage.clickFinish();

        Assertions.assertTrue(checkoutCompletePage.getPageUrl().contains("checkout-complete.html"), "Поръчката не е завършена успешно.");

        checkoutCompletePage.navigate();
        Assertions.assertTrue(inventoryPage.getShoppingCartItemsNumber() == 0, "Количката не е празна след завършване на поръчката.");
    }
}