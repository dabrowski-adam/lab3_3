package edu.iis.mto.time;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;

public class Order {

    private static final int VALID_PERIOD_HOURS = 24;
    private final Clock clock;
    private State orderState;
    private List<OrderItem> items = new ArrayList<OrderItem>();
    private DateTime subbmitionDate;

    public Order(Clock clock) {
        this.clock = clock;
        orderState = State.CREATED;
    }

    public void addItem(OrderItem item) {
        requireState(State.CREATED, State.SUBMITTED);

        items.add(item);
        orderState = State.CREATED;
    }

    public void submit() {
        requireState(State.CREATED);

        orderState = State.SUBMITTED;
        subbmitionDate = DateTime.parse(clock.instant().toString());
    }

    public void confirm() {
        requireState(State.SUBMITTED);

        DateTime now = DateTime.parse(clock.instant().toString());
        int hoursElapsedAfterSubmittion = Hours.hoursBetween(subbmitionDate, now)
                                               .getHours();
        if (hoursElapsedAfterSubmittion > VALID_PERIOD_HOURS) {
            orderState = State.CANCELLED;
            throw new OrderExpiredException();
        }
    }

    public void realize() {
        requireState(State.CONFIRMED);
        orderState = State.REALIZED;
    }

    State getOrderState() {
        return orderState;
    }

    private void requireState(State... allowedStates) {
        for (State allowedState : allowedStates) {
            if (orderState == allowedState)
                return;
        }

        throw new OrderStateException("order should be in state "
                                      + allowedStates
                                      + " to perform required  operation, but is in "
                                      + orderState);
    }

    public static enum State {
        CREATED,
        SUBMITTED,
        CONFIRMED,
        REALIZED,
        CANCELLED
    }
}
