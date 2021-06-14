package org.vaugneuray.amphizza;

import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface PizzaRepository extends PagingAndSortingRepository<Pizza, Long> {
    Optional<Pizza> findOneByPizzaType(PizzaType pizzaType);
}
