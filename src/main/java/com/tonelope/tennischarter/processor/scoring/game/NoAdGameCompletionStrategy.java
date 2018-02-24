package com.tonelope.tennischarter.processor.scoring.game;

import com.tonelope.tennischarter.model.Game;
import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Set;
import com.tonelope.tennischarter.model.Winnable;
import com.tonelope.tennischarter.utils.ListUtils;

public class NoAdGameCompletionStrategy extends GameCompletionStrategy<Game> {

	@Override
	protected boolean isComplete(Integer player1Pts, Integer player2Pts) {
		if (player1Pts == 4 || player2Pts == 4) {
			return true;
		}
		return false;
	}

	@Override
	public boolean test(Winnable scoringObject, Match match) {
		if (!Game.class.isAssignableFrom(scoringObject.getClass())) {
			return false;
		}
		
		Set currentSet = ListUtils.getLast(match.getSets());
		if (currentSet.getGames().size() < 13 || this.isFinalSetWinByTwo(currentSet, match)) {
			return match.getMatchRules().isNoAdScoring();
		}
		return false;
	}
}
