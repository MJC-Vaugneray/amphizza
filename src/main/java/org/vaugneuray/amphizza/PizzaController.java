package org.vaugneuray.amphizza;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class PizzaController {
    private final PizzaService pizzaService;
    private final OrderRepository orderRepository;

    public PizzaController(PizzaService pizzaService, OrderRepository orderRepository) {
        this.pizzaService = pizzaService;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/api/{pizzaType}/new")
    public Order newOrder(@PathVariable PizzaType pizzaType) {
        return pizzaService.newOrder(pizzaType);
    }

    @PostMapping("/api/{pizzaType}/ready")
    public void pizzaReady(@PathVariable PizzaType pizzaType) {
        pizzaService.pizzaReady(pizzaType);
    }

    @PostMapping("/api/{id}/delivered")
    public Order pizzaDelivered(@PathVariable Long id) {
        return pizzaService.pizzaDelivered(id);
    }

    @GetMapping("/orders")
    public Iterable<Order> orders() {
        return orderRepository.findAll();
    }

    @GetMapping("/pizzas")
    public Iterable<Pizza> pizzas() {
        final var pizzas = Map.of(
                PizzaType.CYCLOPE, new Pizza(PizzaType.CYCLOPE),
                PizzaType.PROVENCAL, new Pizza(PizzaType.PROVENCAL),
                PizzaType.QUATRE_FROMAGES, new Pizza(PizzaType.QUATRE_FROMAGES)
        );
        orderRepository.findAll().forEach(order -> {
            var current = pizzas.get(order.getPizzaType());
            switch (order.getStatus()) {
                case ORDERED:
                    current.incrementDoing();
                    break;
                case DELIVERED:
                    current.incrementReady();
                    break;
                case PICKED_UP:
                    current.incrementDone();
                    break;
            }
        });
        return pizzas.values().stream().sorted(Comparator.comparing(Pizza::getPizzaType)).collect(Collectors.toList());
    }
}
