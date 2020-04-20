package com.tonelope.tennis.scoreprocessor;

/**
 *
 *
 * @author Tony Lopez
 *
 */
interface FinalSetHandler {

	String getScore();
	
	void update(int idxWinner);
	
	boolean isComplete();
}
