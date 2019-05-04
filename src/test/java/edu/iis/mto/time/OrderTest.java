package edu.iis.mto.time;

import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

    @Test
    public void confirmShouldSucceedForValidOrder() {
        Instant submissionTime = Instant.now();
        Instant confirmationTime = submissionTime.plus(5, ChronoUnit.HOURS);

        FakeClock fakeClock = new FakeClock(submissionTime);
        Order order = new Order(fakeClock);
        order.submit();
        fakeClock.setInstant(confirmationTime);
        order.confirm();

        assertThat(order.getOrderState(), is(Order.State.SUBMITTED));
    }
}
