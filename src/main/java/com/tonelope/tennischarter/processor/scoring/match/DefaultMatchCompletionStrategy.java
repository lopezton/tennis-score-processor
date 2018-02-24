package com.tonelope.tennischarter.processor.scoring.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Player;
import com.tonelope.tennischarter.model.Set;
import com.tonelope.tennischarter.model.Status;
import com.tonelope.tennischarter.model.Winnable;

public class DefaultMatchCompletionStrategy implements MatchCompletionStrategy {

	@Override
	public boolean apply(Match scoringObject, Match match) {
		Map<Player, Integer> setsWonMap = new HashMap<>();
		for(Set set : scoringObject.getSets()) {
			Player winningPlayer = set.getWinningPlayer();
			
			if (null == winningPlayer) {
				return false;
			}
			
			setsWonMap.put(winningPlayer, Optional.ofNullable(setsWonMap.get(winningPlayer)).orElse(0) + 1);
			List<Player> players = new ArrayList<>(setsWonMap.keySet());
			int p1Sets = setsWonMap.get(players.get(0));
			int p2Sets = 0;
			if (players.size() > 1) {
				p2Sets = setsWonMap.get(players.get(1));
			}
			// TODO Handle final set win by two
			final int setsNeeded = (int) Math.ceil(((double) match.getMatchRules().getNumberOfSets()) / 2);
			if (p1Sets == setsNeeded || p2Sets == setsNeeded) {
				scoringObject.setStatus(Status.COMPLETE);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean test(Winnable scoringObject, Match match) {
		return Match.class.isAssignableFrom(scoringObject.getClass());
	}

}
