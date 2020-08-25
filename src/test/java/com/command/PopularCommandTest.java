package com.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.command.controller.CommandController;
import com.command.dto.Command;
import com.command.repository.CommandRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest

public class PopularCommandTest {

	@Autowired
	CommandController controller;
	
	@Autowired
	CommandRepository repo;
	
	@Test
	public void testSaveFirstStateCommands() throws JsonProcessingException {
		/** Delete all commands to start from a clean slate **/
		repo.deleteAll();
		
		List<Command> commands = repo.findByStateAndCommand("alabama", "CNN");
		
		// If removal was successful, then should be pass 
		assertThat(commands).isEmpty();
		
		/** Save the request **/
		
		// Here is the json request string containing state commands to be saved in Solr
		String reqStr = "{   \"Alabama\": [ { \"speaker\": \"Fred Zhang\", \"command\": \"CNN\" }, { \"speaker\": \"Fred Zhang\", \"command\": \"NBC\" }, { \"speaker\": \"Fred Zhang\", \"command\": \"CNN\" } ], \"florida\": [ { \"speaker\": \"Thomas Brown\", \"command\": \"Show me movies\" }, { \"speaker\": \" Alisha Brown\", \"command\": \" Stranger Things\" }, { \"speaker\": \"Marcus Brown\", \"command\": \"Game of Thrones\" }, { \"speaker\": \"Missy Brown\", \"command\": \"Turn off the TV\" }, { \"speaker\": \"Missy Brown\", \"command\": \"Turn off the TV\" } ], \"maryland\": [ { \"speaker\": \"Thomas Black\", \"command\": \"Show me comedies\" }, { \"speaker\": \" Alisha Black\", \"command\": \"Game of thrones\" }, { \"speaker\": \"Marcus Black\", \"command\": \"Game of THrones\" }, { \"speaker\": \"Missy Black\", \"command\": \"Game of Thrones \" }, { \"speaker\": \"Missy Black\", \"command\": \"Turn off the TV\" } ] }";

		// Here is the request json string in pretty format 
		
		/*
		   { 
		   
		    "Alabama": [ 
		               { "speaker": "Fred Zhang", "command": "CNN" }, 
		               { "speaker": "Fred Zhang", "command": "NBC" }, 
		               { "speaker": "Fred Zhang", "command": "CNN" } ], 
			"florida": [ 
			            { "speaker": "Thomas Brown", "command": "Show me movies" }, 
				 		{ "speaker": " Alisha Brown", "command": " Stranger Things" }, 
				   		{ "speaker": "Marcus Brown", "command": "Game of Thrones" }, 
						{ "speaker": "Missy Brown", "command": "Turn off the TV" }, 
						{ "speaker": "Missy Brown", "command": "Turn off the TV" } ], 
			"maryland": [ 
						{ "speaker": "Thomas Black", "command": "Show me comedies" }, 
						{ "speaker": " Alisha Black", "command": "Game of thrones" }, 
						{ "speaker": "Marcus Black", "command": "Game of THrones" }, 
						{ "speaker": "Missy Black", "command": "Game of Thrones " }, 
						{ "speaker": "Missy Black", "command": "Turn off the TV" } ] 
		    }
		 */
		
		//Save commands to Solr
		ResponseEntity<?> response = controller.saveCommands(reqStr);
		
		List<Object> resBody = (ArrayList<Object>)response.getBody();
//		Map<String, List<StateCommand>> stateCommands = (Map<String, List<StateCommand>>) resBody.get(0);
		
		Map<String, List<String>> topNationalCommands = (Map<String, List<String>>) resBody.get(1);
		
		assertThat(topNationalCommands.get("topCommandsNationally").get(0)).isEqualTo("Game Of Thrones");
		assertThat(topNationalCommands.get("topCommandsNationally").get(1)).isEqualTo("Turn Off The Tv");
		assertThat(topNationalCommands.get("topCommandsNationally").get(2)).isEqualTo("Cnn");
		
		System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
		
		// Assert that all the commands have been successfully inserted
		assertThat(repo.findAll()).hasSize(13);
		
	}
	
	/**
	 * Test add new state commands.  ABC will assume 2nd place (knocking off CNN and out ranking "Turn Off the Tv") after adding this state.
	 *
	 * @throws JsonProcessingException the json processing exception
	 */
	@Test
	public void testAddNewStateCommands() throws JsonProcessingException {
		testSaveFirstStateCommands();
		
		/** Save the request **/
		
		// Here is the json request string containing state commands to be saved in Solr
		String reqStr = "{ \"New Jersey\": [ { \"speaker\": \"Fred Zhang\", \"command\": \"ABC\" }, { \"speaker\": \"Fred Zhang\", \"command\": \"ABC\" }, { \"speaker\": \"Fred Zhang\", \"command\": \"ABC\" } ]}";
		
		// Here is the request json string in pretty format 
		
		/*
		   { 
		   
		    "New Jersey": [ 
		               { "speaker": "Fred Zhang", "command": "ABC" }, 
		               { "speaker": "Fred Zhang", "command": "ABC" }, 
		               { "speaker": "Fred Zhang", "command": "ABC" } ]
		    }
		 */
		
		//Save commands to Solr
		ResponseEntity<?> response = controller.saveCommands(reqStr);
		
		List<Object> resBody = (ArrayList<Object>)response.getBody();
		Map<String, List<String>> topNationalCommands = (Map<String, List<String>>) resBody.get(1);
		
		System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
		
		// Assert that all the commands have been successfully inserted
		assertThat(repo.findAll()).hasSize(16);
		
		assertThat(topNationalCommands.get("topCommandsNationally").get(0)).isEqualTo("Game Of Thrones");
		assertThat(topNationalCommands.get("topCommandsNationally").get(1)).isEqualTo("Abc");
		assertThat(topNationalCommands.get("topCommandsNationally").get(2)).isEqualTo("Turn Off The Tv");
		
	}
	
}
