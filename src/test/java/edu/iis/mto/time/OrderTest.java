package edu.iis.mto.time;

import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class OrderTest {

    @Test(expected = OrderExpiredException.class)
    public void confirmShouldThrowExceptionOnExpiredOrder() {
        Instant submissionTime = Instant.now();
        Instant confirmationTime = submissionTime.plus(25, ChronoUnit.HOURS);

        FakeClock fakeClock = new FakeClock(submissionTime);
        Order order = new Order(fakeClock);
        order.submit();
        fakeClock.setInstant(confirmationTime);
        order.confirm();
    }
}
