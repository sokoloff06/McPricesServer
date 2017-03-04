package com.sokoloff06.mcprices.parsing;

import com.sokoloff06.mcprices.client.LastUpdateRepository;
import com.sokoloff06.mcprices.client.ProductsRepository;
import com.sokoloff06.mcprices.entities.LastUpdate;
import com.sokoloff06.mcprices.entities.Product;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/*
* Created by Vitaly Sokolov on 14.02.2017.
*/



@Controller
@RequestMapping("update")
public class ParsingController {


	@Autowired
	ProductsRepository productsRepository;
	@Autowired
	LastUpdateRepository lastUpdateRepository;

	public static final String BASE_URL = "https://mcdonalds.ru";
	public static final String PRODUCTS_URL = BASE_URL + "/products";
	public static final String PRODUCT_LINK_CLASS = "products__item-link";
	public static final String PRODUCT_PRICE_CLASS = "product__show-price";

	@Scheduled(cron = "0 0 0 * * *")
	@GetMapping
	public String doUpdate(){


		// Retrieves product info from McDonald's website
		HashMap<String, Product> newProductsMap = getProducts();
		Collection<Product> newProducts = newProductsMap.values();

		// Runs through up-to-date products dataset to update those in DB or delete obsolete
		for (Product newProduct : newProducts){

			String newProductName = newProduct.getName();
			int newProductPrice = newProduct.getPrice();

			Product dbProduct = productsRepository.findByName(newProductName);

			// If new product has been added to website
			if (dbProduct == null){
				renewDB(newProducts);
			}
			else{
				int dbProductPrice = dbProduct.getPrice();
				// If product's price has changed
				if(dbProductPrice != newProductPrice){
					renewDB(newProducts);
				}
			}
		}

		// Runs through DB products to find those which were deleted from website
		List<Product> dbProducts = productsRepository.findAll();
		for (Product dbProduct : dbProducts){
			Product newProduct = newProductsMap.get(dbProduct);
			//If new product has been deleted from website
			if(newProduct == null){
				renewDB(newProducts);
			}
		}
		//return productsRepository.findAll();
		return "redirect:/products";
	}

	// Drops and recreates table
	private void renewDB(Collection<Product> newProducts) {
		productsRepository.deleteAll();
		productsRepository.save(newProducts);

		long currentTime = System.currentTimeMillis();
		Timestamp currentTimestamp = new Timestamp(currentTime);
		LastUpdate lastUpdate = new LastUpdate(1, currentTimestamp);

		lastUpdateRepository.deleteAll();
		lastUpdateRepository.save(lastUpdate);
	}

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
				title.trim();
				// Gets link to product's details
				String link = e.attr("href");
				//Gets picture source
				Elements inner = e.getElementsByClass("products__item-image");
				String picSrc = inner.first().attr("data-src");

				// Adds product to HashMap
				productsByName.put(title, new Product(title, getPrice(BASE_URL + link), picSrc));

				// For tests:
				Product testShow = productsByName.get(title);

				System.out.println(
						"Name: " + testShow.getName() + "\n" +
								"Link: " + link + "\n" +
								"Price: " + testShow.getPrice() + "\n" +
								"Image source: " + testShow.getPic()
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
}
