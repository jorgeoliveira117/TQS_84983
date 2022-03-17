import java.util.ArrayList;
import java.util.List;

public class StocksPortfolio {

    private List<Stock> stocks;
    private IStockmarketService stockmarket;

    public StocksPortfolio(IStockmarketService stockmarket){
        this.stockmarket = stockmarket;
        this.stocks = new ArrayList<>();
    }

    public void addStock(Stock stock){
        stocks.add(stock);
    }

    public double getTotalValue(){
        double total = 0;

        for(Stock s : stocks){
            total += s.getQuantity() * stockmarket.lookUpPrice(s.getLabel());
        }

        return total;
    }
}
