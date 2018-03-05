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
package com.tonelope.tennis.scoreprocessor.processor.scoring.point;

import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.Point;
import com.tonelope.tennis.scoreprocessor.model.ScoringObject;
import com.tonelope.tennis.scoreprocessor.model.TiebreakGame;
import com.tonelope.tennis.scoreprocessor.model.TiebreakScore;
import com.tonelope.tennis.scoreprocessor.model.Winnable;

/**
 * @author Tony Lopez
 *
 */
public class TiebreakPointCompletionStrategy extends PointCompletionStrategy {

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionStrategy#test(com.tonelope.tennis.scoreprocessor.model.Winnable, com.tonelope.tennis.scoreprocessor.model.Match)
	 */
	@Override
	public boolean test(Winnable scoringObject, Match match) {
		return Point.class.isAssignableFrom(scoringObject.getClass()) && 
				match.getCurrentSet().getCurrentGame() instanceof TiebreakGame;
	}

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.point.PointCompletionStrategy#updateScore(com.tonelope.tennis.scoreprocessor.model.Point, com.tonelope.tennis.scoreprocessor.model.Match, com.tonelope.tennis.scoreprocessor.model.Player)
	 */
	@Override
	public void updateScore(ScoringObject scoringObject, Match match, Player winningPlayer) {
		Point point = (Point) scoringObject;
		TiebreakScore score = (TiebreakScore) ((TiebreakGame) match.getCurrentSet().getCurrentGame()).getScore();
		if (winningPlayer.equals(point.getServer())) {
			score.setServerScore(score.getServerScore() + 1);
		} else {
			score.setReceiverScore(score.getReceiverScore() + 1);
		}
	}

}
