/*
 * 
 */
package com.command.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.command.dto.Command;
import com.command.dto.CommandProcessTime;
import com.command.dto.StateCommand;
import com.command.repository.CommandRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CommandServiceImpl implements CommandService {

	@Autowired
	CommandRepository repo;

	@Override
	public ResponseEntity<?> getTopCommands() {
		List<Object> response = new ArrayList<>();

		// Top state commands
		Map<String, StateCommand> topStateCommands = getTopStateCommands();

		// Top national commands
		List<String> topNationalCommands = getTopNationalCommands();
		
		Map<String, List<String>> nationalCommandMap = new HashMap<>();
		nationalCommandMap.put("topCommandsNationally", topNationalCommands);

		response.add(topStateCommands);
		response.add(nationalCommandMap);

		return ResponseEntity.ok().body(response);

	}

	
	/**
	 * Gets the top national commands.
	 *
	 * @return the top national commands
	 */
	private List<String> getTopNationalCommands() {

		List<String> commands = new ArrayList<>();

		repo.findTopNationalCommands(PageRequest.of(0, 3)).getFacetResultPage("command").forEach(entry -> {
			commands.add(entry.getValue());
		});

		return commands;

	}
	
	
	/**
	 * Finds the top state commands by navigating the Solr search result pivot tree.
	 *
	 * @return the top command map
	 */
	private Map<String, StateCommand> getTopStateCommands(){
		Pageable pageable = PageRequest.of(0, 20);
		
		// Get the top state sound commands
		FacetPage<Command> page = repo.findTopStateCommands(pageable);
		 
		// Construct a key value map of all states and their commands
		Map<String, StateCommand> topCommandMap = new HashMap<>();
		page.getPivot(page.getFacetPivotFields().iterator().next()).forEach(entry->{
			String state = entry.getValue();
			String topCommand = entry.getPivot().get(0).getValue();
			CommandProcessTime processTime = getCommandProcessTime(state, topCommand);
			
			topCommandMap.put(state, new StateCommand(
					state, topCommand, 
					processTime==null?null:processTime.getStartTime(),
					processTime==null?null:processTime.getEndTime()
					));
			
		});
				
		return topCommandMap;
	}
	
	
	/**
	 * Gets the command process time (start and stop).  
	 * The start process time is when the first instance of the command was detected by the application.
	 * The stop process time is when the last instance of the command that was detected.
	 *
	 * @param state the state
	 * @param command the command
	 * @return the command process time
	 */
	private CommandProcessTime getCommandProcessTime(String state, String command) {
		List<Command> commands = repo.findByStateAndCommand(state, command);
		
		return CollectionUtils.isEmpty(commands)? null: 
			new CommandProcessTime(commands.get(0).getCreatedDt(), commands.get(commands.size()-1).getCreatedDt());
	}


	/**
	 * Extract commands from JSON string (Map JSON commands to objects) then save Save
	 * state commands to DB.
	 *
	 * @param stateCommands the state commands string
	 */
	@Override
	public void saveCommands(String stateCommands) {
		try {

			// Extract commands from string using object mapper and save to DB
			Map<String, List<Command>> commands = new ObjectMapper().readValue(stateCommands, new TypeReference<Map<String, List<Command>>>() {});

			// Extract
			for (Entry<String, List<Command>> entry : commands.entrySet()) {
				for (Command command : entry.getValue()) {
					// Save
					repo.save(new Command(StringUtils.trim(entry.getKey()), 
							StringUtils.trim(command.getSpeaker()),
							StringUtils.trim(command.getCommand())
									));
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void deleteAll() {
		repo.deleteAll();
	}

}
