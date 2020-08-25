package com.command.dto;

public class CommandProcessTime {
	Long startTime;
	Long endTime;

	public CommandProcessTime() {
		super();
	}

	public CommandProcessTime(Long startTime, Long endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

}
