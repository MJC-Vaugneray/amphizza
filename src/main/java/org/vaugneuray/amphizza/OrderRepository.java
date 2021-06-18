package org.vaugneuray.amphizza;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    Optional<Order> findFirstByStatusAndPizzaType(OrderStatus orderStatus, PizzaType pizzaType);

    List<Order> findAll();

}
