package com.command.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Pivot;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import com.command.dto.Command;

@Repository
public interface CommandRepository extends SolrCrudRepository<Command, String>{
	
	  @Query(value = "*:*")
	    List<Command> getUsers();
	 
	  /**
  	 * Find top national commands by performing a facet query on command field.
  	 *
  	 * @param page the page
  	 * @return the facet page
  	 */
  	@Query(value = "*:*")
	  @Facet(fields = { "command" }, limit = 3)
	  FacetPage<Command> findTopNationalCommands(Pageable page);

	  /**
  	 * Find top state commands by performing a pivot facet query on state and command fields.
  	 *
  	 * @param page the page
  	 * @return the facet page
  	 */
  	@Query(value = "*:*")
	  @Facet(pivots = @Pivot({"state", "command"}), pivotMinCount = 0)
	  FacetPage<Command> findTopStateCommands(Pageable page);
	  
	  /**
  	 *  Find state command by the specified state and command. 
  	 *
  	 * @param state the state
  	 * @param command the command
  	 * @return the list
  	 */
  	@Query(fields = { "state, speaker, command, createdDt"})
	  List<Command> findByStateAndCommand(String state, String command);
	  
}
