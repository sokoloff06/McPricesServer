package com.sokoloff06.mcprices.client;

import com.sokoloff06.mcprices.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Vitaly Sokolov on 14.02.2017.
 */

public interface ProductsRepository extends JpaRepository<Product, Integer> {
	Product findByName(String name);
}
