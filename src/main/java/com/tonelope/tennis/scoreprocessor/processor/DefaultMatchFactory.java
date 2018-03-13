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
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.MatchRules;
import com.tonelope.tennis.scoreprocessor.model.PlayerConfig;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * Serves as the default factory provider for <tt>Match</tt> objects.
 * </p>
 * 
 * <p>
 * This class will perform validations when creating a new <tt>Match</tt> entity
 * to ensure that the provided parameters contain all of the necessary
 * information for the scoring processor to correctly process it. If
 * insufficient information has been provided, the
 * </p>
 * 
 * <p>
 * <tt>Match</tt> objects may be created directly, but caution should be
 * exercised in doing so as the processor expects all <tt>Match</tt> objects to
 * contain the necessary initial data. For this reason, it is suggested to use
 * this or another defined factory pattern to ensure the data provided to the
 * processor is consistent.
 * </p>
 * 
 * @see com.tonelope.tennis.scoreprocessor.processor.MatchFactory
 * @see com.tonelope.tennis.scoreprocessor.model.Match
 * @author Tony Lopez
 *
 */
@Getter
@Setter
public class DefaultMatchFactory implements MatchFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tonelope.tennis.scoreprocessor.processor.MatchFactory#create(com.
	 * tonelope.tennis.scoreprocessor.model.MatchRules,
	 * com.tonelope.tennis.scoreprocessor.model.PlayerConfig)
	 */
	public Match create(MatchRules matchRules, PlayerConfig playerConfig) {
		if (null == matchRules) {
			throw new FrameworkException("Missing configuration to create match: matchRules");
		}
		if (null == playerConfig) {
			throw new FrameworkException("Missing configuration to create match: playerConfig");
		}
		if (null == playerConfig.getPlayers()) {
			throw new FrameworkException("Missing configuration to create match: playerConfig.players");
		}
		if (null == playerConfig.getStartingServer()) {
			throw new FrameworkException("Missing configuration to create match: playerConfig.startingServer");
		}
		if (!playerConfig.getPlayers().contains(playerConfig.getStartingServer())) {
			throw new FrameworkException("Starting server is in the list of players");
		}
		if (playerConfig.getPlayers().size() != 2 && playerConfig.getPlayers().size() != 4) {
			throw new FrameworkException("Incorrect number of players for match.");
		}
		return new Match(playerConfig.getPlayers(), matchRules, true);
	}
}
