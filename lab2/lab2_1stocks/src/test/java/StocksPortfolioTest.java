import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class StocksPortfolioTest {

    @Test
    void getTotalValue() {
        IStockmarketService mockMarket = mock(IStockmarketService.class);

        StocksPortfolio portfolioTest = new StocksPortfolio(mockMarket);

        double microStockValue = 262.49;
        int microStockQty = 10;
        double edpStockValue = 4.28;
        int edpStockQty = 42;
        when(mockMarket.lookUpPrice("Microsoft")).thenReturn(microStockValue);
        when(mockMarket.lookUpPrice("EDP")).thenReturn(edpStockValue);

        portfolioTest.addStock(new Stock("Microsoft", microStockQty));
        portfolioTest.addStock(new Stock("EDP", edpStockQty));

        double realTotal = microStockValue * microStockQty + edpStockValue * edpStockQty;

         assertEquals(realTotal, portfolioTest.getTotalValue());

    }
}