package org.afob.limit;

import org.afob.execution.ExecutionClient;
import org.afob.prices.PriceListener;

import java.math.BigDecimal;

public class LimitOrderAgent implements PriceListener {

    // List to hold all limit orders
    private final List<LimitOrder> orders;
    private final ExecutionClient executionClient;

    // Constructor to initialize the agent with the execution client
    public LimitOrderAgent(ExecutionClient executionClient) {
        this.executionClient = executionClient;
        this.orders = new ArrayList<>();
    }

    // Add a new limit order (buy/sell) for a specific product
    public void addOrder(boolean isBuyOrder, String productId, int amount, double limitPrice) {
        LimitOrder order = new LimitOrder(isBuyOrder, productId, amount, limitPrice);
        orders.add(order);
        System.out.println("Added order: " + order);
    }

    // This method will be called with market price updates
    @Override
    public void priceTick(String productId, BigDecimal marketPrice){
        // Iterate over the list of orders to check if any order can be executed
        List<LimitOrder> executedOrders = new ArrayList<>();
        for (LimitOrder order : orders) {
            if (order.getProductId().equals(productId)) {
                // Check if the limit price condition is met
                if (order.isBuyOrder() && marketPrice <= order.getLimitPrice()) {
                    // Buy order: Execute if market price is <= limit price
                    executionClient.buy(productId, order.getAmount());
                    executedOrders.add(order);
                    System.out.println("Executed Buy Order for " + order.getAmount() + " shares of " + productId + " at $" + marketPrice);
                } else if (!order.isBuyOrder() && marketPrice >= order.getLimitPrice()) {
                    // Sell order: Execute if market price is >= limit price
                    executionClient.sell(productId, order.getAmount());
                    executedOrders.add(order);
                    System.out.println("Executed Sell Order for " + order.getAmount() + " shares of " + productId + " at $" + marketPrice);
                }
            }
        }
        // Remove executed orders from the list
        orders.removeAll(executedOrders);
    }

}
