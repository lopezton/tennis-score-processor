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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

/**
 * 
 * @author Tony Lopez
 *
 */
@Getter @ToString
public class Game extends Winnable {

	private final Player server;
	private final Player receiver;
	@Getter(AccessLevel.NONE)
	private final GameScore score = new GameScore();
	private final List<Point> points = new ArrayList<>();

	@Override
	public Score getScore() {
		return this.score;
	}

	@Override
	public Player getWinningPlayer() {
		return this.getWinningPlayer(this.points);
	}

	public Point getCurrentPoint() {
		return ListUtils.getLast(this.points);
	}

	public Game(Player server, Player receiver) {
		this.server = server;
		this.receiver = receiver;
	}
	
	public Game(Player server, Player receiver, boolean initialize) {
		this(server, receiver);
		if (initialize) {
			this.initialize();
		}
	}

	@Override
	public void initialize() {
		this.points.add(new Point(server, receiver));
	}

	public Player getNextServer() {
		return this.server;
	}

	public Player getNextReceiver() {
		return this.receiver;
	}

	/**
	 * @param stroke
	 * @param matchRules
	 */
	public void addStroke(Stroke stroke, MatchRules matchRules) {
		this.getCurrentPoint().addStroke(stroke, matchRules);
		if (this.isNotStarted()) {
			this.status = Status.IN_PROGRESS;
		}
	}

	/**
	 * 
	 * @param point
	 * @param matchRules
	 */
	public void addPoint(Point point, MatchRules matchRules) {
		this.validatePoint(point, matchRules);

		this.points.add(point);
		if (this.isNotStarted()) {
			this.setStatus(Status.IN_PROGRESS);
		}
	}

	/**
	 * @param point
	 * @param matchRules
	 */
	private void validatePoint(Point point, MatchRules matchRules) {
		if (point instanceof SimplePoint) {
			Point lastPoint = ListUtils.getLast(this.points);
			if (null != lastPoint) {
				if (lastPoint.isNotStarted()) {
					// last point was added via processor logic and not a real
					// point. Remove it and replace with SimplePoint.
					this.points.remove(this.points.size() - 1);
				} else if (lastPoint.isInProgress()) {
					throw new FrameworkException("Attempted to add point while previous point " +
							"is still in progress.");
				}
			}
		}
	}
}
