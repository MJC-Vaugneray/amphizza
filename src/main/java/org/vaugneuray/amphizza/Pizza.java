package org.vaugneuray.amphizza;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

public class Pizza {

    private final PizzaType pizzaType;

    private long doingNumber;

    private long readyNumber;

    private long doneNumber;

    public Pizza(PizzaType pizzaType) {
        this.pizzaType = pizzaType;
        this.doingNumber = 0;
        this.readyNumber = 0;
        this.doneNumber = 0;
    }

    public PizzaType getPizzaType() {
        return pizzaType;
    }

    public long getDoingNumber() {
        return doingNumber;
    }

    public long getReadyNumber() {
        return readyNumber;
    }

    public long getDoneNumber() {
        return doneNumber;
    }

    public void incrementDoing() {
        this.doingNumber++;
    }

    public void incrementReady() {
        this.readyNumber++;
    }

    public void incrementDone() {
        this.doneNumber++;
    }
}
