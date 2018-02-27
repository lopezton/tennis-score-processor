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
import com.tonelope.tennis.scoreprocessor.model.Status;
import com.tonelope.tennis.scoreprocessor.model.TiebreakGame;
import com.tonelope.tennis.scoreprocessor.model.TiebreakScore;
import com.tonelope.tennis.scoreprocessor.model.Winnable;
import com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionStrategy;

/**
 * 
 * @author Tony Lopez
 *
 */
public class TiebreakGameCompletionStrategy implements ScoreCompletionStrategy<TiebreakGame> {

	@Override
	public boolean test(Winnable scoringObject, Match match) {
		return TiebreakGame.class.isAssignableFrom(scoringObject.getClass());
	}

	private boolean isComplete(TiebreakScore score) {
		if (score.getServerScore() == 7 && score.getReceieverScore() <= 5) {
			return true;
		} else if (score.getReceieverScore() == 7 && score.getServerScore() <= 5) {
			return true;
		} else if (score.getServerScore() == 7 && score.getReceieverScore() == 7) {
			// TODO Handle scores > 7
			score.setServerScore(5);
			score.setReceieverScore(5);
		} else if (score.getServerScore() == 7 && score.getReceieverScore() == 5) {
			return true;
		} else if (score.getReceieverScore() == 7 && score.getServerScore() == 5) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionStrategy#apply(com.tonelope.tennis.scoreprocessor.model.Winnable, com.tonelope.tennis.scoreprocessor.model.Match)
	 */
	@Override
	public boolean apply(TiebreakGame scoringObject, Match match) {
		if (this.isComplete((TiebreakScore) scoringObject.getScore())) {
			scoringObject.setStatus(Status.COMPLETE);
			return true;
		}
		return false;
	}

}
