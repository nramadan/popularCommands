package com.command.service;

import org.springframework.http.ResponseEntity;

public interface CommandService {
	
	/**
	 * Gets the top state and national commands.
	 *
	 * @return the top commands
	 */
	public ResponseEntity<?> getTopCommands();
	
	/**
	 * Save commands to Solr
	 *
	 * @param stateCommands the state commands
	 */
	void saveCommands(String stateCommands);
	
	public void deleteAll();
	
}
