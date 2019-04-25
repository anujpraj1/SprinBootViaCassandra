package com.springboot.assessment.inventory.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.utils.UUIDs;
import com.springboot.assessment.inventory.entity.Item;
import com.springboot.assessment.inventory.repository.ItemRepository;
import com.springboot.assessment.inventory.service.ItemService;

@Service("itemService")
public class ItemServiceImpl implements ItemService {

	@Autowired
	ItemRepository itemRepository;
	//@Autowired
	//MongoTemplate mongoTemplate;

	@Override
	public List<Map<String, Object>> getItemAvailabilty(List<Map<String, String>> requestData) {

		List<Map<String, Object>> availabiltyList = new ArrayList<>();
		for (Map<String, String> mapEntry : requestData) {
			Map<String, Object> availabiltyMap = new HashMap<>();
			String itemId = mapEntry.get("itemId");
			String locationId = mapEntry.get("locationId");
			/*Query query = new Query();
			query.addCriteria(Criteria.where("itemId").is(itemId).and("locationId").is(locationId));
			Item foundItem = mongoTemplate.findOne(query, Item.class);*/
			Item foundItem = itemRepository.findByItemIdAndLocationId(itemId, locationId);
			if (foundItem != null) {
				double eligibiltyFactor = Double.parseDouble(foundItem.getEligibiltyFactor());
				int supply = Integer.parseInt(foundItem.getSupply());
				int demand = Integer.parseInt(foundItem.getDemand());
				int atp = supply - demand;
				float promisable = (float) (atp * eligibiltyFactor);

				availabiltyMap.put("itemId", foundItem.getItemId());
				availabiltyMap.put("locationId", foundItem.getLocationId());
				availabiltyMap.put("supply", supply);
				availabiltyMap.put("demand", demand);
				availabiltyMap.put("atp", atp);
				availabiltyMap.put("promisable", promisable);
			}
			availabiltyList.add(availabiltyMap);
		}

		return availabiltyList;
	}

	@Override
	public List<Map<String, String>> addItem(List<Item> items) {
		List<Map<String, String>> messageList = new ArrayList<>();
		for (Item i : items) {
			Map<String, String> message = new HashMap<>();
			/*String itemId = i.getItemId();
			String locationId = i.getLocationId();
			Query query = new Query();
			query.addCriteria(Criteria.where("itemId").is(itemId).and("locationId").is(locationId));
			Item foundDoc = mongoTemplate.findOne(query, Item.class);*/
			Item foundDoc = itemRepository.findByitemId(i.getItemId());
			if (foundDoc == null) {
				itemRepository.save(new Item(UUIDs.timeBased(),i.getItemId(), i.getLocationId(), i.getSupply(), i.getDemand(), i.getEligibiltyFactor()));
				message.put("itemId",i.getItemId());
				message.put("message", "Item Inserted Successfully");
			}
			else {
				message.put("itemId",i.getItemId());
				message.put("message", "Item was already Present Hence no effect");
			}
			messageList.add(message);
		}
		return messageList;
	}

	@Override
	public List<Item> getItems() {
		return itemRepository.findAll();
	}

	@Override
	public Map<String, String> updateItem(Item item, String itemId) {
		Map<String, String> message = new HashMap<>();
		Item i = itemRepository.findByitemId(itemId);
		if (i != null) {
			itemRepository.delete(i);
			System.out.println("inside updation ");
			itemRepository.save(new Item(UUIDs.timeBased(),item.getItemId(), item.getLocationId(), item.getSupply(), item.getDemand(), item.getEligibiltyFactor()));
			message.put("itemId", item.getItemId());
			message.put("message", "Item Updated Successfully");
		}
		else {
			message.put("itemId", itemId);
			message.put("message", "Item Unavailable");
		}
		return message;
	}

	@Override
	public Map<String, String> deleteItem(String itemId) {
		Map<String, String> message = new HashMap<>();
		Item a = itemRepository.findByitemId(itemId);
		if (a!=null) {
			itemRepository.delete(a);
			message.put("itemId", a.getItemId());
			message.put("message", "Item Deleted Successfully");
		}
		else {
			message.put("itemId", itemId);
			message.put("message", "Item Unavailable");
		}
		return message;
	}

}
