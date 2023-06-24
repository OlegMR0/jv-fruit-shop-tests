package fruit.shop.service.impl;

import static org.junit.Assert.assertThrows;

import fruit.shop.db.Storage;
import fruit.shop.model.FruitTransaction;
import fruit.shop.service.TransactionHandler;
import fruit.shop.service.strategy.OperationHandler;
import fruit.shop.service.strategy.OperationStrategy;
import fruit.shop.service.strategy.impl.BalanceHandler;
import fruit.shop.service.strategy.impl.OperationStrategyImpl;
import fruit.shop.service.strategy.impl.PurchaseHandler;
import fruit.shop.service.strategy.impl.ReturnHandler;
import fruit.shop.service.strategy.impl.SupplyHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TransactionHandlerImplTest {
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
    void parseStorage_nullTransaction_NotOk() {
        FruitTransaction firstTransaction = new FruitTransaction();
        firstTransaction.setFruit("banana");
        firstTransaction.setOperation(FruitTransaction.Operation.BALANCE);
        firstTransaction.setValue(200);
        FruitTransaction secondTransaction = new FruitTransaction();
        List<FruitTransaction> transactions = List.of(firstTransaction, secondTransaction);
        assertThrows(RuntimeException.class,
                () -> transactionHandler.parseStorage(transactions));
    }
}
