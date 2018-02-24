package com.tonelope.tennischarter.model;

import lombok.Data;

@Data
public abstract class ScoringObject {

	protected Status status = Status.NOT_STARTED;
	
	public boolean isNotStarted() {
		return this.checkStatus(Status.NOT_STARTED);
	}
	
	public boolean isInProgress() {
		return this.checkStatus(Status.IN_PROGRESS);
	}
	
	public boolean isCompleted() {
		return this.checkStatus(Status.COMPLETE);
	}

	private boolean checkStatus(Status complete) {
		return complete.equals(this.status);
	}
}
