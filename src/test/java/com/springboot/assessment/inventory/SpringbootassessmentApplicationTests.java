package com.springboot.assessment.inventory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.datastax.driver.core.utils.UUIDs;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.assessment.inventory.entity.Item;
import com.springboot.assessment.inventory.repository.ItemRepository;
import com.springboot.assessment.inventory.service.impl.ItemServiceImpl;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = SpringbootassessmentApplication.class)
@WebAppConfiguration
public class SpringbootassessmentApplicationTests {

	@Autowired
	MockMvc mvc;
	@Autowired
	WebApplicationContext webApplicationContext;
	@InjectMocks
	private ItemServiceImpl itemService;
	@MockBean
	ItemRepository itemRepository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void getItems() throws Exception {
		List<Item> mockedList = new ArrayList<>();
		mockedList.add(new Item(UUIDs.timeBased(), "Item1", "BLR", "120", "12,", "0.5"));

		when(itemRepository.findAll()).thenReturn(mockedList);

		String uri = "/items";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Item[] itemList = mapFromJson(content, Item[].class);
		assertTrue(itemList.length > 0);
		verify(itemRepository, times(1)).findAll();
	}

	@Test
	public void createItem() throws Exception {

		List<Item> itemInput = new ArrayList<>();
		Item item = new Item(UUIDs.timeBased(), "Item1", "BLR", "120", "12,", "0.5");
		itemInput.add(item);

		when(itemRepository.save(Mockito.any(Item.class))).thenReturn(new Item());

		String uri = "/items";
		String inputJson = mapToJson(itemInput);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void putItem() throws Exception {

		Item item = new Item(UUIDs.timeBased(), "Item1", "BLR", "120", "12,", "0.5");
		String itemId = "Item1";
		String uri = "/items/" + itemId;

		when(itemRepository.findByitemId(Mockito.anyString())).thenReturn(new Item());

		String inputJson = mapToJson(item);
		MvcResult mvcResult = mvc.perform(
				MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void deleteItem() throws Exception {

		String itemId = "Item1";
		String uri = "/items/" + itemId;

		MvcResult mvcResult = mvc
				.perform(MockMvcRequestBuilders.delete(uri).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void getAvailability() throws Exception {

		String content = "[  \n" + "  {\n" + "	\"itemId\" : \"customI836\",\n" + "    \"locationId\" : \"MKL\"\n"
				+ "  },\n" + "  {\n" + "	\"itemId\" : \"customI2836\",\n" + "    \"locationId\" : \"MKLKKL\"\n"
				+ "  }\n" + "  ]";
		String uri = "/items/getAvailabilty";
		MvcResult mvcResult = mvc
				.perform(
						MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(content))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	protected static <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	protected static String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}
}
