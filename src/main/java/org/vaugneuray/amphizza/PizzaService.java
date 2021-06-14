package org.vaugneuray.amphizza;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PizzaService {

    private final PizzaRepository pizzaRepository;

    private final OrderRepository orderRepository;

    public PizzaService(PizzaRepository pizzaRepository, OrderRepository orderRepository) {
        this.pizzaRepository = pizzaRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order newOrder(PizzaType pizzaType) {
      pizzaRepository.findOneByPizzaType(pizzaType)
                .or(() ->  Optional.of(new Pizza(pizzaType)))
                .map(Pizza::incrementDoingNumber)
                .ifPresent(pizzaRepository::save);
      return orderRepository.save(new Order(pizzaType));
    }

    @Transactional
    public void pizzaReady(PizzaType pizzaType) {
        final var order = orderRepository.findFirstByStatusAndPizzaType(OrderStatus.ORDERED, pizzaType).orElseThrow(() -> new RuntimeException("No order in progress"));
        order.ready();
        final var entity = pizzaRepository.findOneByPizzaType(pizzaType)
                .map(Pizza::pizzaReady)
                .map(pizzaRepository::save)
                .orElseThrow();
        pizzaRepository.save(entity);
        orderRepository.save(order);
    }

    @Transactional
    public void pizzaDelivered(PizzaType pizzaType) {
        final var order = orderRepository.findFirstByStatusAndPizzaType(OrderStatus.DELIVERED, pizzaType).orElseThrow(() -> new RuntimeException("No order ready"));
        order.pickedUp();
        orderRepository.save(order);
    }
}