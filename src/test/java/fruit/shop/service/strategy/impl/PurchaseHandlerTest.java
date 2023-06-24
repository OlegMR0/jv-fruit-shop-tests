package fruit.shop.service.strategy.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import fruit.shop.db.Storage;
import fruit.shop.model.FruitTransaction;
import fruit.shop.service.TransactionHandler;
import fruit.shop.service.impl.TransactionHandlerImpl;
import fruit.shop.service.strategy.OperationHandler;
import fruit.shop.service.strategy.OperationStrategy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PurchaseHandlerTest {
    private static OperationStrategy strategy;
    private static TransactionHandler transactionHandler;

    @BeforeAll
    static void beforeAll() {

        Map<FruitTransaction.Operation, OperationHandler> fruitMap = new HashMap<>();
        fruitMap.put(FruitTransaction.Operation.PURCHASE, new PurchaseHandler());
        fruitMap.put(FruitTransaction.Operation.BALANCE, new BalanceHandler());
        fruitMap.put(FruitTransaction.Operation.RETURN, new ReturnHandler());
        fruitMap.put(FruitTransaction.Operation.SUPPLY, new SupplyHandler());
        strategy = new OperationStrategyImpl(fruitMap);
        transactionHandler = new TransactionHandlerImpl(strategy);
    }

    @AfterEach
    void clearStorage() {
        Storage.FRUITS.clear();
    }

    @Test
    void parseStorage_purchaseBanana_Ok() {
        FruitTransaction firstTransaction = new FruitTransaction();
        firstTransaction.setFruit("banana");
        firstTransaction.setOperation(FruitTransaction.Operation.BALANCE);
        firstTransaction.setValue(200);
        FruitTransaction secondTransaction = new FruitTransaction();
        secondTransaction.setFruit("banana");
        secondTransaction.setOperation(FruitTransaction.Operation.PURCHASE);
        secondTransaction.setValue(30);
        List<FruitTransaction> transactions = List.of(firstTransaction, secondTransaction);
        transactionHandler.parseStorage(transactions);
        boolean correct = Storage.FRUITS.get("banana").equals(170);
        assertTrue(correct);
    }

    @Test
    void parseStorage_purchaseBanana_NotOk() {
        FruitTransaction secondTransaction = new FruitTransaction();
        secondTransaction.setFruit("banana");
        secondTransaction.setOperation(FruitTransaction.Operation.PURCHASE);
        secondTransaction.setValue(30);
        List<FruitTransaction> transactions = List.of(secondTransaction);
        assertThrows(RuntimeException.class,
                () -> transactionHandler.parseStorage(transactions));
    }

    @Test
    void parseStorage_emptyStoragePurchase_NotOk() {
        FruitTransaction firstTransaction = new FruitTransaction();
        firstTransaction.setFruit("cherry");
        firstTransaction.setOperation(FruitTransaction.Operation.PURCHASE);
        firstTransaction.setValue(5);
        List<FruitTransaction> transactions = List.of(firstTransaction);
        assertThrows(RuntimeException.class,
                () -> transactionHandler.parseStorage(transactions));
    }
}
