/**
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tonelope.tennis.scoreprocessor.processor.scoring.match;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.ScoringObject;
import com.tonelope.tennis.scoreprocessor.model.Set;
import com.tonelope.tennis.scoreprocessor.model.Status;
import com.tonelope.tennis.scoreprocessor.model.Winnable;

/**
 * 
 * @author Tony Lopez
 *
 */
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

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionStrategy#updateScore(com.tonelope.tennis.scoreprocessor.model.ScoringObject, com.tonelope.tennis.scoreprocessor.model.Match, com.tonelope.tennis.scoreprocessor.model.Player)
	 */
	@Override
	public void updateScore(ScoringObject scoringObject, Match match, Player winningPlayer) {
		
	}

}
