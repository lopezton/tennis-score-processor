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

import com.tonelope.tennis.scoreprocessor.model.GameScore;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.Point;
import com.tonelope.tennis.scoreprocessor.model.PointValue;
import com.tonelope.tennis.scoreprocessor.model.TiebreakGame;
import com.tonelope.tennis.scoreprocessor.model.Winnable;

/**
 * 
 * @author Tony Lopez
 *
 */
public class DefaultPointCompletionStrategy extends PointCompletionStrategy {

	/*
	 * (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionStrategy#test(com.tonelope.tennis.scoreprocessor.model.Winnable, com.tonelope.tennis.scoreprocessor.model.Match)
	 */
	@Override
	public boolean test(Winnable scoringObject, Match match) {
		return Point.class.isAssignableFrom(scoringObject.getClass()) && 
				!(match.getCurrentSet().getCurrentGame() instanceof TiebreakGame);
	}

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.point.PointCompletionStrategy#updateScore(com.tonelope.tennis.scoreprocessor.model.Player)
	 */
	@Override
	protected void updateScore(Point scoringObject, Match match, Player winningPlayer) {
		GameScore score = (GameScore) match.getCurrentSet().getCurrentGame().getScore();
		if (winningPlayer.equals(scoringObject.getServer())) {
			if (score.isDeuce()) {
				score.setServerScore(score.getServerScore().next());
			} else if (PointValue.ADVANTAGE.equals(score.getReceiverScore())) {
				score.setReceiverScore(score.getReceiverScore().previous());
			} else if (PointValue.FORTY.equals(score.getServerScore())) {
				score.setServerScore(PointValue.GAME);
			} else {
				score.setServerScore(score.getServerScore().next());
			}
		} else {
			if (score.isDeuce()) {
				score.setReceiverScore(score.getReceiverScore().next());
			} else if (PointValue.ADVANTAGE.equals(score.getServerScore())) {
				score.setServerScore(score.getServerScore().previous());
			} else if (PointValue.FORTY.equals(score.getReceiverScore())) {
				score.setReceiverScore(PointValue.GAME);
			} else {
				score.setReceiverScore(score.getReceiverScore().next());
			}
		}
	}

}
