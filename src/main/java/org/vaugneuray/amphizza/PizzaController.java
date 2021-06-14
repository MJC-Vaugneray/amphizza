package org.vaugneuray.amphizza;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PizzaController {
    private final PizzaService pizzaService;
    private final PizzaRepository pizzaRepository;
    private final OrderRepository orderRepository;

    public PizzaController(PizzaService pizzaService, PizzaRepository pizzaRepository, OrderRepository orderRepository) {
        this.pizzaService = pizzaService;
        this.pizzaRepository = pizzaRepository;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/{pizzaType}/new")
    public Order newOrder(@PathVariable PizzaType pizzaType) {
        return pizzaService.newOrder(pizzaType);
    }

    @PostMapping("/{pizzaType}/ready")
    public void pizzaReady(@PathVariable PizzaType pizzaType) {
        pizzaService.pizzaReady(pizzaType);
    }

    @PostMapping("/{id}/delivered")
    public void pizzaDelivered(@PathVariable Long id) {
        pizzaService.pizzaDelivered(id);
    }

    @GetMapping("/orders")
    public Iterable<Order> orders() {
        return orderRepository.findAll();
    }

    @GetMapping("/pizzas")
    public Iterable<Pizza> pizzas() {
        return pizzaRepository.findAll();
    }



}
