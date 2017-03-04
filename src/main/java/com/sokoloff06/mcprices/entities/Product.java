package com.sokoloff06.mcprices.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * Created by Vitaly Sokolov on 14.02.2017.
 */
@Entity
public class Product {

	@Id
	@GeneratedValue
	private int id;

	private String name;
	private int price;
	private String pic;

	public Product() {
	}

	public Product(String name, int price, String pic) {
		this.name = name;
		this.price = price;
		this.pic = pic;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getPic() {
		return pic;
	}
}
