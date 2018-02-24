package com.tonelope.tennischarter.processor.scoring.set;

import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Set;
import com.tonelope.tennischarter.model.Winnable;

public class DefaultSetCompletionStrategy extends SetCompletionStrategy<Set> {
	
	@Override
	public boolean test(Winnable scoringObject, Match match) {
		if (!Set.class.isAssignableFrom(scoringObject.getClass())) {
			return false;
		}
		
		return this.isInFinalSetTiebreak(match);
	}

	@Override
	protected boolean isComplete(Match match, Integer p1Games, Integer p2Games) {
		final int gamesNeeded = match.getMatchRules().getNumberOfGamesPerSet();
		final int tiebreakGamesNeeded = gamesNeeded + 1;
		if ((p1Games == gamesNeeded && p2Games < gamesNeeded - 1)) {
			return true;
		} else if (p2Games == gamesNeeded && p1Games < gamesNeeded - 1) {
			return true;
		} else if (p1Games == tiebreakGamesNeeded || p2Games == tiebreakGamesNeeded) {
			return true;
		}
		return false;
	}

}
