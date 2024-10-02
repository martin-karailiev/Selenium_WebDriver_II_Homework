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
        loginPage.navigate();
        loginPage.submitLoginForm(TestData.STANDARD_USER_USERNAME.getValue(), TestData.STANDARD_USER_PASSWORD.getValue());
        inventoryPage.waitForPageTitle();
    }

    @Test
    public void shouldAddProductsToCart_whenAddToCartIsClicked(){
        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);
        inventoryPage.clickShoppingCartLink();

        Assertions.assertEquals(2, shoppingCartPage.getShoppingCartItems().size(), "Products were not correctly added to the cart.");
    }

    @Test
    public void shouldAddUserDetails_whenCheckoutWithValidInfo(){
        inventoryPage.addProductsByTitle(BACKPACK_TITLE);
        inventoryPage.clickShoppingCartLink();

        shoppingCartPage.clickCheckout();
        checkoutYourInformationPage.fillShippingDetails("John", "Doe", "12345");
        checkoutYourInformationPage.clickContinue();

        Assertions.assertEquals(1, checkoutOverviewPage.getShoppingCartItems().size(), "Product count mismatch.");
        String totalLabel = checkoutOverviewPage.getTotalLabelText();
        Assertions.assertTrue(totalLabel.contains("Total"), "Total amount is not calculated correctly.");
    }

    @Test
    public void shouldCompleteOrder_whenProductsAreAddedAndCheckoutIsConfirmed(){
        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);
        inventoryPage.clickShoppingCartLink();

        shoppingCartPage.clickCheckout();
        checkoutYourInformationPage.fillShippingDetails("John", "Doe", "12345");
        checkoutYourInformationPage.clickContinue();
        checkoutOverviewPage.clickFinish();

        Assertions.assertTrue(checkoutCompletePage.getPageUrl().contains("checkout-complete.html"), "Order was not successfully completed.");
        checkoutCompletePage.navigate();
        Assertions.assertTrue(inventoryPage.getShoppingCartItemsNumber() == 0, "Cart is not empty after order completion.");
    }
}
