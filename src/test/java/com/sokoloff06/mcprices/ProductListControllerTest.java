package com.sokoloff06.mcprices;

import com.sokoloff06.mcprices.client.LastUpdateRepository;
import com.sokoloff06.mcprices.client.ProductsRepository;
import com.sokoloff06.mcprices.entities.Product;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Vitaly Sokolov on 14.02.2017.
 */
@RestController
@RequestMapping("products")
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration
public class ProductListControllerTest {

	private static final int LAST_UPDATE_ID = 1;

	@Autowired
	ProductsRepository productProvider;
	@Autowired
	LastUpdateRepository lastUpdateRepository;

	@ResponseBody
	@GetMapping
	@Test
	List<Product> listProducts(){
		System.out.println(productProvider.findAll());
		return productProvider.findAll();
	}

	@ResponseBody
	@GetMapping("last-update")
	Timestamp getLastUpdate(){
		return lastUpdateRepository.findOne(LAST_UPDATE_ID).getTimestamp();
	}

}
