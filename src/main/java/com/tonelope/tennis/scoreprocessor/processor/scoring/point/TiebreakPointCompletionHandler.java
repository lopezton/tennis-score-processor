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
import com.tonelope.tennis.scoreprocessor.model.TiebreakGame;
import com.tonelope.tennis.scoreprocessor.model.TiebreakScore;

/**
 * @author Tony Lopez
 *
 */
public class TiebreakPointCompletionHandler extends PointCompletionHandler {

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionHandler#test(com.tonelope.tennis.scoreprocessor.model.Winnable, com.tonelope.tennis.scoreprocessor.model.Match)
	 */
	@Override
	public boolean test(Point scoringObject, Match match) {
		return match.getCurrentSet().getCurrentGame() instanceof TiebreakGame;
	}

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.point.PointCompletionHandler#updateScore(com.tonelope.tennis.scoreprocessor.model.Point, com.tonelope.tennis.scoreprocessor.model.Match, com.tonelope.tennis.scoreprocessor.model.Player)
	 */
	@Override
	public void updateScore(Point scoringObject, Match match, Player winningPlayer) {
		TiebreakGame tiebreakGame = (TiebreakGame) match.getCurrentSet().getCurrentGame();
		// TODO Remove cast
		TiebreakScore score = (TiebreakScore) tiebreakGame.getScore();
		if (winningPlayer.equals(tiebreakGame.getServer())) {
			score.setServerScore(score.getServerScore() + 1);
		} else {
			score.setReceiverScore(score.getReceiverScore() + 1);
		}
	}

}
