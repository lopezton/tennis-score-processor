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

import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Score;
import com.tonelope.tennis.scoreprocessor.model.TiebreakGame;
import com.tonelope.tennis.scoreprocessor.model.TiebreakScore;
import com.tonelope.tennis.scoreprocessor.model.Winnable;

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

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.game.GameCompletionStrategy#isComplete(com.tonelope.tennis.scoreprocessor.model.GameScore)
	 */
	@Override
	protected boolean isComplete(Score score) {
		TiebreakScore tiebreakScore = (TiebreakScore) score;
		if (tiebreakScore.getServerScore() == 7 && tiebreakScore.getReceiverScore() <= 5) {
			return true;
		} else if (tiebreakScore.getReceiverScore() == 7 && tiebreakScore.getServerScore() <= 5) {
			return true;
		} else if (tiebreakScore.getServerScore() == 7 && tiebreakScore.getReceiverScore() == 7) {
			// TODO Handle scores > 7
			tiebreakScore.setServerScore(5);
			tiebreakScore.setReceiverScore(5);
		} else if (tiebreakScore.getServerScore() == 7 && tiebreakScore.getReceiverScore() == 5) {
			return true;
		} else if (tiebreakScore.getReceiverScore() == 7 && tiebreakScore.getServerScore() == 5) {
			return true;
		}
		return false;
	}

}
