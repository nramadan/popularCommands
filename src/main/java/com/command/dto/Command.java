package com.command.dto;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@SolrDocument(collection = "stateCommands")

public class Command {
	@JsonIgnore
	@Id
	@Indexed
	String id;
	
	@Indexed(name = "state")
	String state;
	
	@Indexed(name = "speaker")
	String speaker;

	@Indexed(name = "command")
	@JsonProperty("command")
	String command;

	/** The start process time. */
	@Indexed(name = "createdDt")
	private Long createdDt;

	public Command() {
		super();
	}

	public Command(String state, String speaker, String command) {
		super();
		this.createdDt = Instant.now().getEpochSecond();
		this.state = state;
		this.speaker = speaker;
		this.command = command;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public Long getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Long createdDt) {
		this.createdDt = createdDt;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

}
