package com.sokoloff06.mcprices.parsing;

import com.sokoloff06.mcprices.client.LastUpdateRepository;
import com.sokoloff06.mcprices.client.ProductsRepository;
import com.sokoloff06.mcprices.entities.Product;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Vitaly Sokolov on 14.02.2017.
 */

@Controller
public class ParsingControllerTest {


	@Autowired
	ProductsRepository productProvider;
	@Autowired
	LastUpdateRepository lastUpdateRepository;

	public static final String BASE_URL = "https://mcdonalds.ru";
	public static final String PRODUCTS_URL = BASE_URL + "/products";
	public static final String PRODUCT_LINK_CLASS = "products__item-link";
	public static final String PRODUCT_PRICE_CLASS = "product__show-price";


	private HashMap<String, Product> getProducts(){

		try {

			Connection productListConnection = Jsoup.connect(PRODUCTS_URL);

			// Gets HTML doc
			Document productList = productListConnection.get();

			// Gets elements containing links and namings of all products
			Elements products = productList.body().getElementsByClass(PRODUCT_LINK_CLASS);

			// Map for holding Product objects with title as key and Product objects as values
			HashMap<String, Product> productsByName = new HashMap<>();

			for(Element e : products){

				// Gets product's title
				String title = e.attr("title");
				// Gets link to product's details
				String link = e.attr("href");

				// Adds product to HashMap
				productsByName.put(title, new Product(title, getPrice(BASE_URL + link)));

				// For tests:
				Product testShow = productsByName.get(title);

				System.out.println(
					"Name: " + testShow.getName() + "\n" +
					"Link: " + link + "\n" +
					"Price: " + testShow.getPrice()
				);
			}

			return productsByName;
		} catch (IOException e) {
			e.printStackTrace();
			//TODO: Write to log
			//TODO: Try again immediately, then after 2 hours then notify developer and stick to the schedule
			return null;
		}
	}

	/*
	* Method gets link to product's details, parses price there and returns it
	*/
	private int getPrice(String link) {

		Connection productPageConnection = Jsoup.connect(link);

		try {
			Document productPage = productPageConnection.get();
			Elements products = productPage.getElementsByClass(PRODUCT_PRICE_CLASS);
			// Gets only 1 element with price (There is only one on the page)
			Element e = products.first();
			// Gets text of the element
			String priceString = e.ownText();
			// Finds space to split number and currency
			int whiteSpaceIndex = priceString.indexOf(" ");
			// Deletes extra symbols and only number is left
			priceString = priceString.substring(0, whiteSpaceIndex).trim();
			return Integer.parseInt(priceString);

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Scheduled(cron = "0 0 0 * * *")
	@Test
	public void doUpdate(){
		// Retrieves product info from McDonald's website
		//HashMap<String, Product> products = getProducts();

		// Saves products to database
		//productProvider.save(products.values());
		productProvider.save(new Product("Пирожок", 50));

	}
}
