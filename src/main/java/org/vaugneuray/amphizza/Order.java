package org.vaugneuray.amphizza;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("pizza_order")
public class Order {
    @Id
    private Long id;

    private OrderStatus status = OrderStatus.ORDERED;

    private final PizzaType pizzaType;

    public OrderStatus getStatus() {
        return status;
    }

    public PizzaType getPizzaType() {
        return pizzaType;
    }

    public Order(PizzaType pizzaType) {
        this.pizzaType = pizzaType;
    }

    public Order ready() {
        this.status = OrderStatus.DELIVERED;
        return this;
    }


    public Order unready() {
        this.status = OrderStatus.ORDERED;
        return this;
    }

    public Order pickedUp() {
        this.status = OrderStatus.PICKED_UP;
        return this;
    }

    public long getId() {
        return this.id;
    }

}
