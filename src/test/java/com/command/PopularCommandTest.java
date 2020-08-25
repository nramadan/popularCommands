package com.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.util.Hash;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.PivotField;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.FacetPivotFieldEntry;
import org.springframework.data.solr.core.query.result.FacetQueryEntry;
import org.springframework.test.context.junit4.SpringRunner;

import com.command.controller.CommandController;
import com.command.dto.Command;
import com.command.repository.CommandRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PopularCommandTest {

	@Autowired
	CommandController controller;
	
	@Autowired
	CommandRepository repo;
	
	@Test
	public void testSaveCommands() throws JsonProcessingException {
		Command command = new Command();
		command.setCommand("Games Of Thorne");
		command.setSpeaker("Fred Zhang");
		
		Map<String, List<Command>> commands = new HashMap<>();
		commands.put("alabama", Collections.singletonList(command));
		
		String reqStr = new ObjectMapper().writeValueAsString(commands);
		
		System.out.println(controller.saveCommands(reqStr));
		System.out.println(reqStr);
	}
	
	
	@Test
	public void mapJsonToObj() throws IOException {
		String json = "{\"alabama\":[{\"speaker\":\"Fred Zhang\",\"command\":\"Games Of Thorne\"}]}";
		
		
		Map<String, List<Command>> commands = 
				new ObjectMapper().readValue(json, new TypeReference<Map<String, List<Command>>>() {});
		
		System.out.println(commands.get("alabama"));
	}
	
	
	@Test
	public void testFindByStateAndCommand() throws JsonProcessingException {
		List<Command> commands = repo.findByStateAndCommand("florida", "Turn off the TV");
		
		for (Command command : commands) {
			System.out.println(new ObjectMapper().writeValueAsString(command));
		}
	}

}
