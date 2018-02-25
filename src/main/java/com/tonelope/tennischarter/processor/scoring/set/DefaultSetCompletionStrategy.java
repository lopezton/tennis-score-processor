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
package com.tonelope.tennischarter.processor.scoring.set;

import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Set;
import com.tonelope.tennischarter.model.Winnable;

/**
 * 
 * @author Tony Lopez
 *
 */
public class DefaultSetCompletionStrategy extends SetCompletionStrategy<Set> {
	
	@Override
	public boolean test(Winnable scoringObject, Match match) {
		if (!Set.class.isAssignableFrom(scoringObject.getClass())) {
			return false;
		}
		
		return this.isInFinalSetTiebreak(match);
	}

	@Override
	protected boolean isComplete(Match match, Integer p1Games, Integer p2Games) {
		final int gamesNeeded = match.getMatchRules().getNumberOfGamesPerSet();
		final int tiebreakGamesNeeded = gamesNeeded + 1;
		if ((p1Games == gamesNeeded && p2Games < gamesNeeded - 1)) {
			return true;
		} else if (p2Games == gamesNeeded && p1Games < gamesNeeded - 1) {
			return true;
		} else if (p1Games == tiebreakGamesNeeded || p2Games == tiebreakGamesNeeded) {
			return true;
		}
		return false;
	}

}
