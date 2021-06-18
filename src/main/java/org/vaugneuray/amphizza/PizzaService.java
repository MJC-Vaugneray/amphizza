package org.vaugneuray.amphizza;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Service
public class PizzaService {
    private final OrderRepository orderRepository;

    public PizzaService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Order newOrder(PizzaType pizzaType) {
      return orderRepository.save(new Order(pizzaType));
    }

    @Transactional
    public void pizzaReady(PizzaType pizzaType) {
        final var order = orderRepository.findFirstByStatusAndPizzaType(OrderStatus.ORDERED, pizzaType).orElseThrow(() -> new RuntimeException("No order in progress"));
        order.ready();
        orderRepository.save(order);
    }

    @Transactional
    public Order pizzaDelivered(Long orderId) {
        final var order = orderRepository.findById(orderId / 10L)
                .filter(o -> o.getStatus() != OrderStatus.PICKED_UP)
                .orElseThrow(() -> new HttpException("No order found"));
        if(order.getStatus() == OrderStatus.ORDERED) {
            this.orderRepository.findFirstByStatusAndPizzaType(OrderStatus.DELIVERED, order.getPizzaType())
                    .map(Order::unready)
                            .ifPresent(this.orderRepository::save);

        }
        order.pickedUp();
        return orderRepository.save(order);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class HttpException extends RuntimeException {
        public HttpException(String message) {
            super(message);
        }
    }
}
