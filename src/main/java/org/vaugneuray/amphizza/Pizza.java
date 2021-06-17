package org.vaugneuray.amphizza;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class Pizza {
    @Id
    private Long id;

    private final PizzaType pizzaType;

    private long doingNumber;

    private long readyNumber;

    public Pizza(PizzaType pizzaType) {
        this.pizzaType = pizzaType;
        this.doingNumber = 0;
        this.readyNumber = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PizzaType getPizzaType() {
        return pizzaType;
    }

    public long getDoingNumber() {
        return doingNumber;
    }

    public Pizza incrementDoingNumber() {
        this.doingNumber += 1;
        return this;
    }

    public Pizza pizzaReady() {
        this.doingNumber -= 1;
        this.readyNumber += 1;
        return this;
    }

    public Pizza pizzaPickedUp() {
        this.readyNumber -= 1;
        return this;
    }
    public long getReadyNumber() {
        return readyNumber;
    }
}
