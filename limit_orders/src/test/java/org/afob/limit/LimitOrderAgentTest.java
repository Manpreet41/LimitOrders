package org.afob.limit;

import org.afob.execution.ExecutionClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class LimitOrderAgentTest {

    private ExecutionClient executionClient;
    private LimitOrderAgent limitOrderAgent;

    @Before
    public void setUp() {
        executionClient = Mockito.mock(ExecutionClient.class);
        limitOrderAgent = new LimitOrderAgent(executionClient);
    }

    @Test
    public void testAddBuyOrder() throws ExecutionClient.ExecutionException {
        // buy order for AAPL at a limit price of $150
        limitOrderAgent.addOrder(true, "AAPL", 1000, 150);

        verify(executionClient, never()).buy(anyString(), anyInt());
    }

    @Test
    public void testAddSellOrder() throws ExecutionClient.ExecutionException {
        // sell order for AAPL at a limit price of $180
        limitOrderAgent.addOrder(false, "AAPL", 500, 180);

        // No execution expected yet, as no price tick occurred
        verify(executionClient, never()).sell(anyString(), anyInt());
    }

    @Test
    public void testExecuteBuyOrderBelowLimit() throws ExecutionClient.ExecutionException {
        // buy order for AAPL at a limit price of $150
        limitOrderAgent.addOrder(true, "AAPL", 1000, 150);

        limitOrderAgent.priceTick("AAPL", BigDecimal.valueOf(149));

        verify(executionClient, times(1)).buy("AAPL", 1000);
    }

    @Test
    public void testExecuteBuyOrderAtLimit() throws ExecutionClient.ExecutionException {

        limitOrderAgent.addOrder(true, "AAPL", 1000, 150);

        limitOrderAgent.priceTick("AAPL", BigDecimal.valueOf(150));

        // Verify that the buy order was executed
        verify(executionClient, times(1)).buy("AAPL", 1000);
    }

    @Test
    public void testDoNotExecuteBuyOrderAboveLimit() throws ExecutionClient.ExecutionException {

        limitOrderAgent.addOrder(true, "AAPL", 1000, 150);
        limitOrderAgent.priceTick("AAPL", BigDecimal.valueOf(151));
        verify(executionClient, never()).buy("AAPL", 1000);
    }

    @Test
    public void testExecuteSellOrderAboveLimit() throws ExecutionClient.ExecutionException {

        limitOrderAgent.addOrder(false, "AAPL", 500, 180);
        limitOrderAgent.priceTick("AAPL", BigDecimal.valueOf(181));

        verify(executionClient, times(1)).sell("AAPL", 500);
    }

}
