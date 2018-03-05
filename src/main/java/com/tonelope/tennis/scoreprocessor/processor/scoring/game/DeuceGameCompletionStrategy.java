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
import com.tonelope.tennis.scoreprocessor.model.GameScore;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.PointValue;
import com.tonelope.tennis.scoreprocessor.model.ScoringObject;
import com.tonelope.tennis.scoreprocessor.model.Set;
import com.tonelope.tennis.scoreprocessor.model.Winnable;

/**
 * 
 * @author Tony Lopez
 *
 */
public class DeuceGameCompletionStrategy extends GameCompletionStrategy<Game> {

	@Override
	protected boolean isComplete(GameScore score) {
		return PointValue.GAME.equals(score.getServerScore()) || PointValue.GAME.equals(score.getReceiverScore());
	}

	@Override
	public boolean test(Winnable scoringObject, Match match) {
		if (!Game.class.isAssignableFrom(scoringObject.getClass())) {
			return false;
		}
		
		Set currentSet = match.getCurrentSet();
		if (currentSet.getGames().size() < 13 || this.isFinalSetWinByTwo(currentSet, match)) {
			return !match.getMatchRules().isNoAdScoring();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionStrategy#updateScore(com.tonelope.tennis.scoreprocessor.model.ScoringObject, com.tonelope.tennis.scoreprocessor.model.Match, com.tonelope.tennis.scoreprocessor.model.Player)
	 */
	@Override
	public void updateScore(ScoringObject scoringObject, Match match, Player winningPlayer) {
		// TODO Auto-generated method stub
		
	}
}
