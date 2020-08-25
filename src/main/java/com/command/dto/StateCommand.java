package com.command.dto;

/**
 * The Class TopCommand represents the top command.
 */
public class StateCommand {

	/** The US state this command pertains to */
	private String state;

	/** The sound command. */
	private String command;

	/** The start process time. */
	private Long startProcessTime;

	/** The stop process time. */
	private Long stopProcessTime;

	public StateCommand() {
		super();
	}

	
	public StateCommand(String state, String command) {
		super();
		this.state = state;
		this.command = command;
	}


	public StateCommand(String state, String command, Long startProcessTime, Long stopProcessTime) {
		super();
		this.state = state;
		this.command = command;
		this.startProcessTime = startProcessTime;
		this.stopProcessTime = stopProcessTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getStartProcessTime() {
		return startProcessTime;
	}

	public void setStartProcessTime(Long startProcessTime) {
		this.startProcessTime = startProcessTime;
	}

	public Long getStopProcessTime() {
		return stopProcessTime;
	}

	public void setStopProcessTime(Long stopProcessTime) {
		this.stopProcessTime = stopProcessTime;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

}
