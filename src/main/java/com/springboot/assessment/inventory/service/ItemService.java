package com.springboot.assessment.inventory.service;

import java.util.List;
import java.util.Map;

import com.springboot.assessment.inventory.entity.Item;

public interface ItemService {

	public List<Map<String, Object>> getItemAvailabilty(List<Map<String, String>> requestData);

	public List<Map<String, String>> addItem(List<Item> item);

	public List<Item> getItems();

	public Map<String, String> updateItem(Item item, String itemId);

	public Map<String, String> deleteItem(String itemId);

}
