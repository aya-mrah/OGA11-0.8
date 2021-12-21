package com.oga.stockservice.repository;


import com.oga.stockservice.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface CategoryRepository.
 *
 * @author devrobot
 * @version 1.0
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
