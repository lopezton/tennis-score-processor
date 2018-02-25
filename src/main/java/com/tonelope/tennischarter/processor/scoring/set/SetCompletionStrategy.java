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
package com.tonelope.tennischarter.processor.scoring.set;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.tonelope.tennischarter.model.Game;
import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Player;
import com.tonelope.tennischarter.model.Set;
import com.tonelope.tennischarter.model.Status;
import com.tonelope.tennischarter.processor.scoring.ScoreCompletionStrategy;

/**
 * 
 * @author Tony Lopez
 *
 * @param <T>
 */
public abstract class SetCompletionStrategy<T extends Set> implements ScoreCompletionStrategy<T> {

	@Override
	public boolean apply(T scoringObject, Match match) {
		if (scoringObject.getGames().size() < match.getMatchRules().getNumberOfGamesPerSet()) {
			return false;
		}
		
		Map<Player, Integer> gamesWonMap = new HashMap<>();
		for(Game game : scoringObject.getGames()) {
			Player winningPlayer = game.getWinningPlayer();
			
			if (null == winningPlayer) {
				return false;
			}
			
			gamesWonMap.put(winningPlayer, Optional.ofNullable(gamesWonMap.get(winningPlayer)).orElse(0) + 1);
		}
		
		List<Player> players = new ArrayList<>(gamesWonMap.keySet());
		int p1Games = gamesWonMap.get(players.get(0));
		int p2Games = gamesWonMap.size() > 1 ? gamesWonMap.get(players.get(1)) : 0;
		
		boolean isComplete = this.isComplete(match, p1Games, p2Games);
		if (isComplete) {
			scoringObject.setStatus(Status.COMPLETE);
		}
		return isComplete;
	}
	
	protected boolean isInFinalSetTiebreak(Match match) {
		return !(match.isCurrentlyInFinalSet() && match.getMatchRules().isFinalSetTiebreakDisabled());
	}
	
	protected abstract boolean isComplete(Match match, Integer p1Games, Integer p2Games);
}
