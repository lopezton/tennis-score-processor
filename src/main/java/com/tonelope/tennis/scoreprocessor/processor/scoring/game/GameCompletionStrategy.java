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
import com.tonelope.tennis.scoreprocessor.model.Score;
import com.tonelope.tennis.scoreprocessor.model.ScoringObject;
import com.tonelope.tennis.scoreprocessor.model.Set;
import com.tonelope.tennis.scoreprocessor.model.Status;
import com.tonelope.tennis.scoreprocessor.model.Winnable;
import com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionStrategy;

/**
 * 
 * @author Tony Lopez
 *
 */
public abstract class GameCompletionStrategy<T extends Winnable> implements ScoreCompletionStrategy<T> {

	@Override
	public boolean apply(T scoringObject, Match match) {
		if (this.isComplete(scoringObject.getScore())) {
			scoringObject.setStatus(Status.COMPLETE);
			this.updateScore(match.getCurrentSet(), match, scoringObject.getWinningPlayer());
			return true;
		}
		return false;
	}

	protected boolean isFinalSetWinByTwo(Set currentSet, Match match) {
		return currentSet.getGames().size() >= 13 && 
				match.isCurrentlyInFinalSet() && 
				match.getMatchRules().isFinalSetTiebreakDisabled();
	}
	
	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionStrategy#updateScore(com.tonelope.tennis.scoreprocessor.model.ScoringObject, com.tonelope.tennis.scoreprocessor.model.Match, com.tonelope.tennis.scoreprocessor.model.Player)
	 */
	@Override
	public void updateScore(ScoringObject scoringObject, Match match, Player winningPlayer) {
		Set set = (Set) scoringObject;
		if (set.getStartingServer().equals(winningPlayer)) {
			set.getScore().setStartingServerScore(set.getScore().getStartingServerScore() + 1);
		} else {
			set.getScore().setStartingReceiverScore(set.getScore().getStartingReceiverScore() + 1);
		}
	}
	
	protected abstract boolean isComplete(Score score);
}
