package org.vaugneuray.amphizza;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("pizza_order")
public class Order {
    @Id
    private Long id;

    private OrderStatus status = OrderStatus.ORDERED;

    private final PizzaType pizzaType;

    @ReadOnlyProperty
    private LocalDateTime createdAt;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
