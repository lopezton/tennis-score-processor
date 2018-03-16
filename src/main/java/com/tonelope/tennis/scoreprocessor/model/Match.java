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
package com.tonelope.tennis.scoreprocessor.model;

import java.util.ArrayList;
import java.util.List;

import com.tonelope.tennis.scoreprocessor.utils.ListUtils;

import lombok.Getter;
import lombok.ToString;

/**
 * <p>
 * The <tt>Match</tt> serves as the main focal point of the score processing
 * framework. At it's core, <tt>Match</tt> is nothing more than a POJO object
 * that is used by the <tt>MatchProcessor</tt> and it's handler objects to
 * determine how updates to this object's internal data should be handled.
 * </p>
 * 
 * <p>
 * While most of the data contained within match is managed by the framework's
 * processing classes, some of the data is expected to be provided by the
 * client. Such examples of this data include the player data or what types of
 * sets, games, or points are to be played (e.g. should a set be win-by-two or a
 * tiebreak set?). This data can be provided manually via constructor,
 * getter/setter logic, or by a factory provider.
 * </p>
 * 
 * <p>
 * <tt>Match</tt> objects may be created directly, but caution should be
 * exercised in doing so as the processor expects all <tt>Match</tt> objects to
 * contain the necessary initial data. For this reason, it is suggested to use
 * <tt>DefaultMatchFactory</tt> or another defined factory pattern to ensure the
 * data provided to the processor is consistent.
 * </p>
 * 
 * @see com.tonelope.tennis.scoreprocessor.processor.MatchFactory
 * @see com.tonelope.tennis.scoreprocessor.processor.DefaultMatchFactory
 * @see com.tonelope.tennis.scoreprocessor.processor.MatchProcessor
 * @author Tony Lopez
 *
 */
@Getter @ToString
public class Match extends Winnable implements HasChildScoringObject<Set> {

	private String id;
	private final List<Player> players;
	private final Player startingServer;
	private final Player startingReceiver;
	private final MatchRules matchRules;
	private final MatchScore score = new MatchScore();
	private final List<Set> sets = new ArrayList<>();

	public Match(List<Player> players, MatchRules matchRules, boolean initialize) {
		this.players = players;
		this.startingServer = players.get(0);
		this.startingReceiver = players.get(1);
		this.matchRules = matchRules;
		if (initialize) {
			this.initialize();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tonelope.tennis.scoreprocessor.model.Winnable#getWinningPlayer()
	 */
	@Override
	public Player getWinningPlayer() {
		return this.getWinningPlayer(this.sets);
	}

	/**
	 * <p>
	 * Returns the current set of this match object. The current set is
	 * equivalent to the last set contained within this object's <tt>sets</tt>
	 * object.
	 * </p>
	 * 
	 * @return the current <tt>Set</tt> object.
	 */
	public Set getCurrentSet() {
		return ListUtils.getLast(this.sets);
	}

	/**
	 * <p>
	 * Returns the current game of this match object. The current game is
	 * equivalent to the last game contained within the <tt>Set</tt> returned by
	 * this object's <tt>getCurrentSet</tt> method.
	 * </p>
	 * 
	 * @return the current <tt>Game</tt> object.
	 */
	public Game getCurrentGame() {
		return this.getCurrentSet().getCurrentGame();
	}

	/**
	 * <p>
	 * Returns the current point of this match object. The current point is
	 * equivalent to the last point contained within the <tt>Game</tt> returned
	 * by this object's <tt>getCurrentGame</tt> method.
	 * </p>
	 * 
	 * @return the current <tt>Game</tt> object.
	 */
	public Point getCurrentPoint() {
		return this.getCurrentGame().getCurrentPoint();
	}

	/**
	 * <p>
	 * Determines whether or not this match is currently in a final set. This is
	 * determined by checking to see if the number of sets contained within this
	 * object's <tt>sets</tt> is equal to the maximum number of sets defined
	 * within this object's <tt>MatchRules</tt>.
	 * </p>
	 * 
	 * @return true if the match is currently within the final set, false
	 *         otherwise.
	 */
	public boolean isCurrentlyInFinalSet() {
		if (null == this.sets || this.sets.isEmpty()) {
			return false;
		}
		return this.matchRules.getNumberOfSets() == this.sets.size();
	}

	@Override
	public void initialize() {
		this.sets.add(new Set(this.matchRules, this.startingServer, this.startingReceiver, true));
	}

	/**
	 * <p>
	 * Adds the provided <tt>stroke</tt> object to the current point of this
	 * match.
	 * </p>
	 * 
	 * @param stroke
	 *            the stroke object to add.
	 * @see com.tonelope.tennis.scoreprocessor.model.Match#getCurrentPoint()
	 */
	public void addStroke(Stroke stroke) {
		this.getCurrentPoint().addStroke(stroke, this.matchRules);
	}

	/**
	 * <p>
	 * Adds the provided <tt>point</tt> object to the current game of this
	 * match.
	 * </p>
	 * 
	 * @param point
	 *            the point object to add.
	 * @see com.tonelope.tennis.scoreprocessor.model.Match#getCurrentGame()
	 */
	public void addPoint(Point point) {
		this.getCurrentGame().addPoint(point, this.matchRules);
	}

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.model.HasChildScoringObject#getChildScoringObjects()
	 */
	@Override
	public List<Set> getChildScoringObjects() {
		return this.getSets();
	}
}
