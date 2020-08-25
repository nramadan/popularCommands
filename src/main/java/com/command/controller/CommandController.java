package com.command.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.command.service.CommandService;

@Controller
public class CommandController {
	
	@Autowired
	CommandService service;
	
	@PostMapping(path = "/commands")
	 public synchronized ResponseEntity<?> saveCommands(@RequestBody String stateCommands) {
		
		try {
			// Retrieve list of state commands Object To json mapping 
			
			// Save commands
			service.saveCommands(stateCommands);
			
			// Return top commands
			return  service.getTopCommands();
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Please make sure the state commands area in the proper format.");
		}
		
			
	}
	
	 @RequestMapping("/delete")
	 public String deleteAllDocuments() {
	     try { //delete all documents from solr core
	      service.deleteAll();
	      return "documents deleted succesfully!";
	     }catch (Exception e){
	       return "Failed to delete documents";
	     }
	 }
	 
	
}
