package com.tonelope.tennis.scoreprocessor;

/**
 *
 *
 * @author Tony Lopez
 *
 */
public abstract class AbstractPoint {

	public static enum Team {
		HOME,
		VISITOR
	}
	
	private final Team winner;
	
	public AbstractPoint(Team winner) {
		this.winner = winner;
	}
	
	public Team getWinner() {
		return this.winner;
	}
}
