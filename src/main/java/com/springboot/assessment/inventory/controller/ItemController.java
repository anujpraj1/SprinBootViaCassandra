package com.springboot.assessment.inventory.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.assessment.inventory.entity.Item;
import com.springboot.assessment.inventory.service.ItemService;

@RestController
public class ItemController {

	@Autowired
	ItemService itemService;

	@RequestMapping(method = RequestMethod.GET, value = "/items")
	public List<Item> getAllItems() {
		return itemService.getItems();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/items")
	public List<Map<String,String>> createItem(@RequestBody List<Item> item) {
		return itemService.addItem(item);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/items/{itemid}")
	public Map<String, String> updateItem(@RequestBody Item item, @PathVariable String itemid) {
		return itemService.updateItem(item, itemid);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/items/{itemid}")
	public Map<String, String> deleteItem(@PathVariable String itemid) {
		return itemService.deleteItem(itemid);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/items/getAvailabilty")
	public List<Map<String, Object>> getAvailablity(@RequestBody List<Map<String, String>> requestData) {
		return itemService.getItemAvailabilty(requestData);
	}
}
