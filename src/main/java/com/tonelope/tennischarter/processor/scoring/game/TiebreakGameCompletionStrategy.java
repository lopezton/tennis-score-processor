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

import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.TiebreakGame;
import com.tonelope.tennischarter.model.Winnable;

/**
 * 
 * @author Tony Lopez
 *
 */
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
