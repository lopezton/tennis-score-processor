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
package com.tonelope.tennis.scoreprocessor.processor.scoring;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tonelope.tennis.scoreprocessor.model.FrameworkException;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Winnable;
import com.tonelope.tennis.scoreprocessor.processor.scoring.game.DeuceGameCompletionStrategy;
import com.tonelope.tennis.scoreprocessor.processor.scoring.game.NoAdGameCompletionStrategy;
import com.tonelope.tennis.scoreprocessor.processor.scoring.game.TiebreakGameCompletionStrategy;
import com.tonelope.tennis.scoreprocessor.processor.scoring.match.DefaultMatchCompletionStrategy;
import com.tonelope.tennis.scoreprocessor.processor.scoring.point.DefaultPointCompletionStrategy;
import com.tonelope.tennis.scoreprocessor.processor.scoring.point.TiebreakPointCompletionStrategy;
import com.tonelope.tennis.scoreprocessor.processor.scoring.set.DefaultSetCompletionStrategy;
import com.tonelope.tennis.scoreprocessor.processor.scoring.set.NoFinalSetTiebreakSetCompletionStrategy;

import lombok.Getter;

/**
 * 
 * @author Tony Lopez
 *
 */
@Getter
public class DefaultScoreCompletionStrategyResolver implements ScoreCompletionStrategyResolver {

	private final List<ScoreCompletionStrategy<?>> scoreCompletionStrategies;
	
	public DefaultScoreCompletionStrategyResolver() {
		this(null);
	}
	
	public DefaultScoreCompletionStrategyResolver(List<ScoreCompletionStrategy<?>> scoreCompletionStrategies) {
		if (null != scoreCompletionStrategies) {
			this.scoreCompletionStrategies = scoreCompletionStrategies;
		} else {
			this.scoreCompletionStrategies = this.createDefaultScoreCompletionStrategies();
		}
	}
	
	/**
	 * @return
	 */
	private List<ScoreCompletionStrategy<?>> createDefaultScoreCompletionStrategies() {
		List<ScoreCompletionStrategy<?>> list = new ArrayList<>();
		list.add(new DefaultPointCompletionStrategy());
		list.add(new TiebreakPointCompletionStrategy());
		list.add(new DeuceGameCompletionStrategy());
		list.add(new NoAdGameCompletionStrategy());
		list.add(new TiebreakGameCompletionStrategy());
		list.add(new DefaultSetCompletionStrategy());
		list.add(new NoFinalSetTiebreakSetCompletionStrategy());
		list.add(new DefaultMatchCompletionStrategy());
		return list;
	}

	@Override
	public <T extends Winnable> boolean resolve(T scoringObject, Match match) {
		return this.getStrategy(scoringObject, match).apply(scoringObject, match);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T extends Winnable> ScoreCompletionStrategy<T> getStrategy(T scoringObject, Match match) {
		List<ScoreCompletionStrategy> acceptableStrategies = this.scoreCompletionStrategies.stream()
				.filter(s -> s.test(scoringObject, match))
				.collect(Collectors.toList());
		
		if (acceptableStrategies.isEmpty()) {
			throw new FrameworkException("Failed to find score completion strategy for " + scoringObject);
		} else if (acceptableStrategies.size() > 1) {
			throw new FrameworkException("Expected to find only (1) score completion strategy but found " + acceptableStrategies.size() + " for " + scoringObject);
		} else {
			return acceptableStrategies.get(0);
		}
	}
}
