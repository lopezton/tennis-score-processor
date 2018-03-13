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

import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.MatchRules;
import com.tonelope.tennis.scoreprocessor.model.PlayerConfig;

/**
 * <p>
 * Serves as the factory provider for <tt>Match</tt> objects.
 * </p>
 * 
 * <p>
 * Implementations of this class should be responsible for validating match
 * objects have all of the necessary information required for processing by the
 * appropriate strategies and handlers.
 * </p>
 * 
 * @see com.tonelope.tennis.scoreprocessor.processor.DefaultMatchFactory
 * @see com.tonelope.tennis.scoreprocessor.model.Match
 * @author Tony Lopez
 *
 */
public interface MatchFactory {

	/**
	 * <p>
	 * Creates an instantiates a new <tt>Match</tt> object with the provided
	 * parameters.
	 * </p>
	 * 
	 * @param matchRules
	 *            the matchRules to provide to the <tt>match</tt> object.
	 * @param playerConfig
	 *            the player configurations for the match
	 * @return the new <tt>Match</tt> object.
	 */
	Match create(MatchRules matchRules, PlayerConfig playerConfig);
}
