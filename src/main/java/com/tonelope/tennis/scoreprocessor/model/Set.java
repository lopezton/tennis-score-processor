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
 * 
 * @author Tony Lopez
 *
 */
@Getter @ToString
public class Set extends Winnable {

	private final MatchRules matchRules;
	private final Player startingServer;
	private final Player startingReceiver;
	private final SetScore score = new SetScore();
	private final List<Game> games = new ArrayList<>();
	
	public Set(MatchRules matchRules, Player startingServer, Player startingReceiver, boolean initialize) {
		this(matchRules, startingServer, startingReceiver);
		if (initialize) {
			this.initialize();
		}
	}
	
	public Set(MatchRules matchRules, Player startingServer, Player startingReceiver) {
		this.matchRules = matchRules;
		this.startingServer = startingServer;
		this.startingReceiver = startingReceiver;
	}
	
	public Game getCurrentGame() {
		return ListUtils.getLast(this.games);
	}
	
	@Override
	public Player getWinningPlayer() {
		return this.getWinningPlayer(this.games);
	}

	@Override
	public void initialize() {
		this.games.add(new Game(startingServer, startingReceiver, true));
	}

	public boolean isNextGameTiebreakEligible() {
		return this.getGames().size() == (2 * this.matchRules.getNumberOfGamesPerSet());
	}

	public void addStroke(Stroke stroke, MatchRules matchRules) {
		this.getCurrentGame().addStroke(stroke, matchRules);
		if (this.isNotStarted()) {
			this.status = Status.IN_PROGRESS;
		}
	}
}
