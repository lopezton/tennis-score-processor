package com.tonelope.tennischarter.processor.scoring.set;

import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Set;
import com.tonelope.tennischarter.model.Winnable;

public class NoFinalSetTiebreakSetCompletionStrategy extends SetCompletionStrategy<Set> {

	@Override
	public boolean test(Winnable scoringObject, Match match) {
		if (!Set.class.isAssignableFrom(scoringObject.getClass())) {
			return false;
		}
		
		return match.isCurrentlyInFinalSet() && match.getMatchRules().isFinalSetTiebreakDisabled();
	}

	@Override
	protected boolean isComplete(Match match, Integer p1Games, Integer p2Games) {
		final int gamesNeeded = match.getMatchRules().getNumberOfGamesPerSet();
		if ((p1Games == gamesNeeded && p2Games < gamesNeeded - 1)) {
			return true;
		} else if (p2Games == gamesNeeded && p1Games < gamesNeeded - 1) {
			return true;
		} else if (p1Games + p2Games >= (2 * gamesNeeded) && Math.abs(p1Games - p2Games) > 1) {
			return true;
		}
		return false;
	}
}
