package com.sokoloff06.mcprices.client;

import com.sokoloff06.mcprices.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ProductListController {

	//TODO: Refactor "controller" lol
	private static final int LAST_UPDATE_ID = 1;

	@Autowired
	ProductsRepository productProvider;
	@Autowired
	LastUpdateRepository lastUpdateRepository;

	@ResponseBody
	@GetMapping
	public List<Product> listProducts(){
		return productProvider.findAll();
	}

	@ResponseBody
	@GetMapping("last-update")
	Timestamp getLastUpdate(){
		return lastUpdateRepository.findOne(LAST_UPDATE_ID).getTimestamp();
	}

}
