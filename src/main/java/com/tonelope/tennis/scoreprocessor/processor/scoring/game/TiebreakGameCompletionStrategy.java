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
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.Score;
import com.tonelope.tennis.scoreprocessor.model.TiebreakGame;
import com.tonelope.tennis.scoreprocessor.model.TiebreakScore;

/**
 * 
 * @author Tony Lopez
 *
 */
public class TiebreakGameCompletionStrategy extends GameCompletionStrategy<TiebreakGame> {

	private static final int MINIMUM_PTS_NEEDED = 7;
	
	@Override
	public boolean test(TiebreakGame scoringObject, Match match) {
		return TiebreakGame.class.isAssignableFrom(scoringObject.getClass());
	}

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.game.GameCompletionStrategy#isComplete(com.tonelope.tennis.scoreprocessor.model.GameScore)
	 */
	@Override
	protected boolean isComplete(Score score) {
		TiebreakScore tiebreakScore = (TiebreakScore) score;
		if ((tiebreakScore.getServerScore() >= MINIMUM_PTS_NEEDED || tiebreakScore.getReceiverScore() >= MINIMUM_PTS_NEEDED) && 
				Math.abs(tiebreakScore.getServerScore() - tiebreakScore.getReceiverScore()) > 1) {
			return true;
		}	
		return false;
	}

	@Override
	public void updateScore(TiebreakGame scoringObject, Match match, Player winningPlayer) {
		super.updateScore(scoringObject, match, winningPlayer);
		// TODO Remove casting
		match.getCurrentSet().getScore().setTiebreakScore((TiebreakScore) scoringObject.getScore());
	}
}
