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
package com.tonelope.tennis.scoreprocessor.processor;

import com.tonelope.tennis.scoreprocessor.model.FrameworkException;
import com.tonelope.tennis.scoreprocessor.model.Game;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.MatchEventType;
import com.tonelope.tennis.scoreprocessor.model.MatchRules;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.Point;
import com.tonelope.tennis.scoreprocessor.model.Set;
import com.tonelope.tennis.scoreprocessor.model.Status;
import com.tonelope.tennis.scoreprocessor.model.Stroke;
import com.tonelope.tennis.scoreprocessor.model.TiebreakGame;
import com.tonelope.tennis.scoreprocessor.model.Winnable;
import com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionHandlerResolver;

/**
 * <p>
 * Handles the processing for stroke objects for a standard singles match.
 * </p>
 * 
 * <p>
 * While most of the logic here is considered typical for standard tennis
 * matches, it is recommended that if changes need to be made they can be done
 * by creating a new implementation of <tt>MatchStrategy</tt>. All logic within
 * this implementation is able to be overridden via careful implementation of
 * equivalent Spring beans. While this approach is achievable it is not
 * recommended over creating a new implementation.
 * </p>
 * 
 * @author Tony Lopez
 *
 */
public class SinglesMatchStrategy extends AbstractMatchStrategy {

	public SinglesMatchStrategy() {
		this(null);
	}

	public SinglesMatchStrategy(ScoreCompletionHandlerResolver scoreCompletionHandlerResolver) {
		super(scoreCompletionHandlerResolver);
	}

	/**
	 * <p>
	 * Creates a new instance of <tt>Game</tt> or <tt>TiebreakGame</tt>
	 * depending on the match status and <tt>MatchRules</tt> logic.
	 * </p>
	 * 
	 * @param matchRules
	 *            the match rulesets to apply
	 * @param match
	 *            the match object
	 * @return a new <tt>Game</tt> instance
	 */
	private Game createNextGame(MatchRules matchRules, Match match) {
		Set currentSet = match.getCurrentSet();
		Game currentGame = currentSet.getCurrentGame();

		if ((!matchRules.isFinalSetTiebreakDisabled() && match.isCurrentlyInFinalSet())
				&& currentSet.isNextGameTiebreakEligible()
				|| (!match.isCurrentlyInFinalSet() && currentSet.isNextGameTiebreakEligible())) {
			return new TiebreakGame(currentGame.getReceiver(), currentGame.getServer(), true);
		} else {
			return new Game(currentGame.getReceiver(), currentGame.getServer(), true);
		}
	}

	/**
	 * <p>
	 * Determines if <tt>scoringObject</tt> is complete or not by using a
	 * registered <tt>ScoreCompletionHandler</tt> that identifies with the
	 * current score of <tt>match</tt>.
	 * </p>
	 * 
	 * @param scoringObject
	 *            the <tt>ScoringObject</tt> to check
	 * @param match
	 *            the match object
	 * @return true if the <tt>scoringObject</tt> is in a completed status,
	 *         false otherwise
	 */
	private boolean isComplete(Winnable scoringObject, Match match) {
		return this.scoreCompletionHandlerResolver.resolve(scoringObject, match);
	}

	private Match process(Match match) {

		Set currentSet = match.getCurrentSet();
		Game currentGame = currentSet.getCurrentGame();
		Point currentPoint = currentGame.getCurrentPoint();

		if (this.isComplete(currentPoint, match)) {
			this.executeMatchEvents(MatchEventType.ON_POINT_COMPLETION, match);
			if (this.isComplete(currentGame, match)) {
				this.executeMatchEvents(MatchEventType.ON_GAME_COMPLETION, match);
				if (this.isComplete(currentSet, match)) {
					this.executeMatchEvents(MatchEventType.ON_SET_COMPLETION, match);
					if (this.isComplete(match, match)) {
						this.executeMatchEvents(MatchEventType.ON_MATCH_COMPLETION, match);
					} else {
						Player server = currentGame.getServer().getOpposingPlayer(match.getPlayers());
						match.getSets().add(new Set(match.getMatchRules(), server, currentGame.getServer(), true));
					}
				} else {
					currentSet.getGames().add(this.createNextGame(match.getMatchRules(), match));
				}
			} else {
				currentGame.getPoints().add(new Point(currentGame.getNextServer(), currentGame.getNextReceiver()));
			}
		}

		return match;
	}

	/**
	 * <p>
	 * Adds <tt>point</tt> into the <tt>match</tt> object.
	 * </p>
	 * 
	 * <p>
	 * Handles all processing logic that needs to occur prior and after to the
	 * point being added to the match object including:
	 * </p>
	 * 
	 * <ul>
	 * <li>Adding the point to the current point</li>
	 * <li>If the point ends the current game, adds a new set or tiebreak to the
	 * match</li>
	 * <li>If the point ends the set or tiebreak, adds a new set to the
	 * match</li>
	 * <li>If the point ends the set or tiebreak and it is the last in the
	 * match, completes the match</li>
	 * <li>Updates the scoring objects contained within the match object</li>
	 * <li>Executes registered events</li>
	 * </ul>
	 * 
	 * <p>
	 * Events can be registered with this instance of
	 * <tt>SinglesMatchStrategy</tt> that occur after the completion of a point,
	 * game, tiebreak, set, or match.
	 * </p>
	 * 
	 * @param match
	 *            the match object
	 * @param point
	 *            the point object to add to <tt>match</tt>
	 * @return the match object
	 */
	@Override
	public Match update(Match match, Point point) {
		this.validateAndPrepare(match);
		match.addPoint(point);
		return this.process(match);
	}

	/**
	 * <p>
	 * Adds <tt>stroke</tt> into the <tt>match</tt> object.
	 * </p>
	 * 
	 * <p>
	 * Handles all processing logic that needs to occur prior and after to the
	 * stroke being added to the match object including:
	 * </p>
	 * 
	 * <ul>
	 * <li>Adding the stroke to the current point</li>
	 * <li>Determines whether the stroke is a winning, losing, or continuation
	 * of the current point</li>
	 * <li>If the stroke ends the current point, adds a new game to the
	 * match</li>
	 * <li>If the stroke ends the current game, adds a new set or tiebreak to
	 * the match</li>
	 * <li>If the stroke ends the set or tiebreak, adds a new set to the
	 * match</li>
	 * <li>If the stroke ends the set or tiebreak and it is the last in the
	 * match, completes the match</li>
	 * <li>Updates the scoring objects contained within the match object</li>
	 * <li>Executes registered events</li>
	 * </ul>
	 * 
	 * <p>
	 * Events can be registered with this instance of
	 * <tt>SinglesMatchStrategy</tt> that occur after the completion of a point,
	 * game, tiebreak, set, or match.
	 * </p>
	 * 
	 * @param match
	 *            the match object
	 * @param stroke
	 *            the stroke to add into the match object
	 * @return the match object
	 */
	@Override
	public Match update(Match match, Stroke stroke) {
		this.validateAndPrepare(match);
		match.addStroke(stroke);
		return this.process(match);
	}

	/**
	 * <p>
	 * Validates <tt>match</tt> is as expected and performs any preparatory
	 * steps needed for the <tt>match</tt> object.
	 * </p>
	 * 
	 * @param match
	 *            the match object
	 */
	private void validateAndPrepare(Match match) {
		if (!match.isInProgress() && !match.isNotStarted()) {
			throw new FrameworkException(
					"Updating score when match status is " + match.getStatus() + " is not supported.");
		}

		if (match.isNotStarted()) {
			match.setStatus(Status.IN_PROGRESS);
		}
	}
}
