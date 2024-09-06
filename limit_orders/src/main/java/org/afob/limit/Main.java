package org.afob.limit;

import org.afob.execution.ExecutionClient;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        ExecutionClient executionClient = new ExecutionClient();
        LimitOrderAgent agent = new LimitOrderAgent(executionClient);

        // Add a buy order for IBM at a limit price of $100
        agent.addOrder(true, "IBM", 1000, 100);

        // Simulate a price tick with a market price of $99 (this should trigger the order)
        agent.priceTick("IBM", BigDecimal.valueOf(99));
    }
}

