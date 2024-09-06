package org.afob.limit;

public class LimitOrder {
    private final boolean isBuyOrder;
    private final String productId;
    private final int amount;
    private final double limitPrice;

    // Constructor to initialize the order
    public LimitOrder(boolean isBuyOrder, String productId, int amount, double limitPrice) {
        this.isBuyOrder = isBuyOrder;
        this.productId = productId;
        this.amount = amount;
        this.limitPrice = limitPrice;
    }

    public boolean isBuyOrder() {
        return isBuyOrder;
    }

    public String getProductId() {
        return productId;
    }

    public int getAmount() {
        return amount;
    }

    public double getLimitPrice() {
        return limitPrice;
    }

    @Override
    public String toString() {
        return (isBuyOrder ? "Buy" : "Sell") + " Order: " + amount + " shares of " + productId + " at $" + limitPrice;
    }
}
