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

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;

import com.tonelope.tennis.scoreprocessor.model.FrameworkException;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Winnable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Tony Lopez
 *
 */
@RequiredArgsConstructor
@Getter
public class DefaultScoreCompletionStrategyResolver implements ScoreCompletionStrategyResolver {

	private final ApplicationContext context;
	
	@Override
	public <T extends Winnable> boolean resolve(T scoringObject, Match match) {
		return this.getStrategy(scoringObject, match).apply(scoringObject, match);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T extends Winnable> ScoreCompletionStrategy<T> getStrategy(T scoringObject, Match match) {
		List<ScoreCompletionStrategy> acceptableStrategies = this.context.getBeansOfType(ScoreCompletionStrategy.class).values().stream()
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
