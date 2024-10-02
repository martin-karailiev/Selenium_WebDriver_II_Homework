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
        // Стъпка 1: Логване на потребител
        loginPage.navigate();
        loginPage.submitLoginForm(TestData.STANDARD_USER_USERNAME.getValue(), TestData.STANDARD_USER_PASSWORD.getValue());
        inventoryPage.waitForPageTitle();

        // Стъпка 2: Добавяне на продукти в количката
        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);

        // Стъпка 3: Отиди на страницата с количката
        inventoryPage.clickShoppingCartLink();

        // Стъпка 4: Валидация на броя на продуктите в количката
        Assertions.assertEquals(2, shoppingCartPage.getShoppingCartItems().size(), "Продуктите не са добавени правилно в количката.");
    }

    @Test
    public void userDetailsAdded_when_checkoutWithValidInformation(){
        // Стъпка 1: Логване на потребител
        loginPage.navigate();
        loginPage.submitLoginForm(TestData.STANDARD_USER_USERNAME.getValue(), TestData.STANDARD_USER_PASSWORD.getValue());
        inventoryPage.waitForPageTitle();

        // Стъпка 2: Добавяне на продукт в количката
        inventoryPage.addProductsByTitle(BACKPACK_TITLE);
        inventoryPage.clickShoppingCartLink();

        // Стъпка 3: Отиди на страницата за попълване на информацията за плащане
        shoppingCartPage.clickCheckout();

        // Стъпка 4: Попълване на формата за доставка
        checkoutYourInformationPage.fillShippingDetails("John", "Doe", "12345");
        checkoutYourInformationPage.clickContinue();

        // Стъпка 5: Валидация на броя на продуктите в страницата за преглед на поръчката
        Assertions.assertEquals(1, checkoutOverviewPage.getShoppingCartItems().size(), "Броят на продуктите не съвпада.");

        // Стъпка 6: Валидация на общата сума
        String totalLabel = checkoutOverviewPage.getTotalLabelText();
        Assertions.assertTrue(totalLabel.contains("Total"), "Общата сума не е изчислена правилно.");
    }

    @Test
    public void orderCompleted_when_addProduct_and_checkout_withConfirm(){
        // Стъпка 1: Логване на потребител
        loginPage.navigate();
        loginPage.submitLoginForm(TestData.STANDARD_USER_USERNAME.getValue(), TestData.STANDARD_USER_PASSWORD.getValue());
        inventoryPage.waitForPageTitle();

        // Стъпка 2: Добавяне на продукти в количката
        inventoryPage.addProductsByTitle(BACKPACK_TITLE, SHIRT_TITLE);
        inventoryPage.clickShoppingCartLink();

        // Стъпка 3: Отиди на страницата за попълване на информацията за плащане
        shoppingCartPage.clickCheckout();
        checkoutYourInformationPage.fillShippingDetails("John", "Doe", "12345");
        checkoutYourInformationPage.clickContinue();

        // Стъпка 4: Потвърди поръчката
        checkoutOverviewPage.clickFinish();

        // Стъпка 5: Валидация на успешното завършване на поръчката
        Assertions.assertTrue(checkoutCompletePage.getPageUrl().contains("checkout-complete.html"), "Поръчката не е завършена успешно.");

        // Стъпка 6: Увери се, че количката е празна след поръчката
        checkoutCompletePage.navigate();
        Assertions.assertTrue(inventoryPage.getShoppingCartItemsNumber() == 0, "Количката не е празна след завършване на поръчката.");
    }
}