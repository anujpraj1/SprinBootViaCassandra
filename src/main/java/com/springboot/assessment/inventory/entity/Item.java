package com.springboot.assessment.inventory.entity;

import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("item")
public class Item {
	
	@PrimaryKey
	private UUID id;
	
	private String itemId;
	private String locationId;
	private String supply;
	private String demand;
	private String eligibiltyFactor;
	
	public Item(UUID id, String itemId, String locationId, String supply, String demand,
			String eligibiltyFactor) {
		this.id = id;
		this.itemId = itemId;
		this.locationId = locationId;
		this.supply = supply;
		this.demand = demand;
		this.eligibiltyFactor = eligibiltyFactor;
	}
	
	public Item() {}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocation_id(String locationId) {
		this.locationId = locationId;
	}

	public String getSupply() {
		return supply;
	}

	public void setSupply(String supply) {
		this.supply = supply;
	}

	public String getDemand() {
		return demand;
	}

	public void setDemand(String demand) {
		this.demand = demand;
	}

	public String getEligibiltyFactor() {
		return eligibiltyFactor;
	}

	public void setEligibiltyFactor(String eligibiltyFactor) {
		this.eligibiltyFactor = eligibiltyFactor;
	}
}
