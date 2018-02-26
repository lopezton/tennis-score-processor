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
package com.tonelope.tennis.scoreprocessor.processor.scoring.game;

import com.tonelope.tennis.scoreprocessor.model.Game;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Set;
import com.tonelope.tennis.scoreprocessor.model.Winnable;

/**
 * 
 * @author Tony Lopez
 *
 */
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
		
		Set currentSet = match.getCurrentSet();
		if (currentSet.getGames().size() < 13 || this.isFinalSetWinByTwo(currentSet, match)) {
			return match.getMatchRules().isNoAdScoring();
		}
		return false;
	}
}
