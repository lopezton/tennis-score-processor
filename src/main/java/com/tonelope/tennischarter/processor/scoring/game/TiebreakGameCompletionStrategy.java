package com.tonelope.tennischarter.processor.scoring.game;

import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.TiebreakGame;
import com.tonelope.tennischarter.model.Winnable;

public class TiebreakGameCompletionStrategy extends GameCompletionStrategy<TiebreakGame> {

	@Override
	public boolean test(Winnable scoringObject, Match match) {
		return TiebreakGame.class.isAssignableFrom(scoringObject.getClass());
	}

	@Override
	protected boolean isComplete(Integer player1Pts, Integer player2Pts) {
		if (player1Pts == 7 && player2Pts <= 5) {
			return true;
		} else if (player2Pts == 7 && player1Pts <= 5) {
			return true;
		} else if (player1Pts == 7 && player2Pts == 7) {
			player1Pts = 5;
			player2Pts = 5;
		} else if (player1Pts == 7 && player2Pts == 5) {
			return true;
		} else if (player2Pts == 7 && player1Pts == 5) {
			return true;
		}
		return false;
	}

}
