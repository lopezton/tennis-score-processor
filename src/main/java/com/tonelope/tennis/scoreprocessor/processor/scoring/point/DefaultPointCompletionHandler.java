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

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.tonelope.tennis.scoreprocessor.model.GameScore;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.Point;
import com.tonelope.tennis.scoreprocessor.model.PointValue;
import com.tonelope.tennis.scoreprocessor.model.TiebreakGame;

/**
 * 
 * @author Tony Lopez
 *
 */
public class DefaultPointCompletionHandler extends PointCompletionHandler {

	/*
	 * (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionHandler#test(com.tonelope.tennis.scoreprocessor.model.Winnable, com.tonelope.tennis.scoreprocessor.model.Match)
	 */
	@Override
	public boolean test(Point scoringObject, Match match) {
		return !(match.getCurrentSet().getCurrentGame() instanceof TiebreakGame);
	}

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.point.PointCompletionHandler#updateScore(com.tonelope.tennis.scoreprocessor.model.Player)
	 */
	@Override
	public void updateScore(Point scoringObject, Match match, Player winningPlayer) {
		GameScore score = (GameScore) match.getCurrentSet().getCurrentGame().getScore();
		if (winningPlayer.equals(scoringObject.getServer())) {
			this.updateScore(score, score::setServerScore, score::getServerScore, score::setReceiverScore, score::getReceiverScore);
		} else {
			this.updateScore(score, score::setReceiverScore, score::getReceiverScore, score::setServerScore, score::getServerScore);
		}
	}

	private void updateScore(GameScore score, Consumer<PointValue> p1, Supplier<PointValue> s1, Consumer<PointValue> p2, Supplier<PointValue> s2) {
		if (score.isDeuce()) {
			p1.accept(s1.get().next());
		} else if (PointValue.ADVANTAGE.equals(s2.get())) {
			p2.accept(s2.get().previous());
		} else if (PointValue.FORTY.equals(s1.get())) {
			p1.accept(PointValue.GAME);
		} else {
			p1.accept(s1.get().next());
		}
	}
}
