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
package com.tonelope.tennischarter.processor.scoring.game;

import com.tonelope.tennischarter.model.Game;
import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Set;
import com.tonelope.tennischarter.model.Winnable;
import com.tonelope.tennischarter.utils.ListUtils;

/**
 * 
 * @author Tony Lopez
 *
 */
public class DeuceGameCompletionStrategy extends GameCompletionStrategy<Game> {

	@Override
	protected boolean isComplete(Integer player1Pts, Integer player2Pts) {
		if (player1Pts == 4 && player2Pts <= 2) {
			return true;
		} else if (player2Pts == 4 && player1Pts <= 2) {
			return true;
		} else if (player1Pts == 4 && player2Pts == 4) {
			player1Pts = 3;
			player2Pts = 3;
		} else if (player1Pts == 5 && player2Pts == 3) {
			return true;
		} else if (player2Pts == 5 && player1Pts == 3) {
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
			return !match.getMatchRules().isNoAdScoring();
		}
		return false;
	}

}
