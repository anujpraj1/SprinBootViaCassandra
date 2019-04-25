package com.springboot.assessment.inventory.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import com.springboot.assessment.inventory.entity.Item;

@Repository
public interface ItemRepository extends CassandraRepository<Item, String>{
	@AllowFiltering
	Item findByitemId(String item);
	
	@AllowFiltering
	Item findByItemIdAndLocationId(String itemId, String locationId);
}
