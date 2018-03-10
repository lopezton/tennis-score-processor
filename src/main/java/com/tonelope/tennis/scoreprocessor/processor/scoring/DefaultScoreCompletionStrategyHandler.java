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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tonelope.tennis.scoreprocessor.model.FrameworkException;
import com.tonelope.tennis.scoreprocessor.model.Game;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Point;
import com.tonelope.tennis.scoreprocessor.model.Set;
import com.tonelope.tennis.scoreprocessor.model.TiebreakGame;
import com.tonelope.tennis.scoreprocessor.model.Winnable;
import com.tonelope.tennis.scoreprocessor.processor.scoring.game.DeuceGameCompletionHandler;
import com.tonelope.tennis.scoreprocessor.processor.scoring.game.NoAdGameCompletionHandler;
import com.tonelope.tennis.scoreprocessor.processor.scoring.game.TiebreakGameCompletionHandler;
import com.tonelope.tennis.scoreprocessor.processor.scoring.match.DefaultMatchCompletionHandler;
import com.tonelope.tennis.scoreprocessor.processor.scoring.point.DefaultPointCompletionHandler;
import com.tonelope.tennis.scoreprocessor.processor.scoring.point.TiebreakPointCompletionHandler;
import com.tonelope.tennis.scoreprocessor.processor.scoring.set.DefaultSetCompletionHandler;
import com.tonelope.tennis.scoreprocessor.processor.scoring.set.NoFinalSetTiebreakSetCompletionHandler;

import lombok.Getter;

/**
 * 
 * @author Tony Lopez
 *
 */
@Getter
public class DefaultScoreCompletionStrategyHandler implements ScoreCompletionHandlerResolver {

	private final Map<Class<?>, List<ScoreCompletionHandler<Winnable>>> scoreCompletionHandlers;
	
	public DefaultScoreCompletionStrategyHandler() {
		this(null);
	}
	
	public DefaultScoreCompletionStrategyHandler(Map<Class<?>, List<ScoreCompletionHandler<Winnable>>> scoreCompletionStrategies) {
		if (null != scoreCompletionStrategies) {
			this.scoreCompletionHandlers = scoreCompletionStrategies;
		} else {
			this.scoreCompletionHandlers = this.createDefaultScoreCompletionHandlers();
		}
	}
	
	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> Map<Class<?>, List<T>> createDefaultScoreCompletionHandlers() {
		// TODO Refactor
		Map<Class<?>, List<T>> map = new HashMap<>();
		map.put(Point.class, (List<T>) Stream.of(new DefaultPointCompletionHandler(), new TiebreakPointCompletionHandler()).collect(Collectors.toList()));
		map.put(Game.class, (List<T>) Stream.of(new DeuceGameCompletionHandler(), new NoAdGameCompletionHandler()).collect(Collectors.toList()));
		map.put(TiebreakGame.class, (List<T>) Stream.of(new TiebreakGameCompletionHandler()).collect(Collectors.toList()));
		map.put(Set.class, (List<T>) Stream.of(new DefaultSetCompletionHandler(), new NoFinalSetTiebreakSetCompletionHandler()).collect(Collectors.toList()));
		map.put(Match.class, (List<T>) Stream.of(new DefaultMatchCompletionHandler()).collect(Collectors.toList()));
		return map;
	}

	private ScoreCompletionHandler<Winnable> getHandler(Winnable scoringObject, Match match) {
		List<ScoreCompletionHandler<Winnable>> acceptableHandlers = this.scoreCompletionHandlers.get(scoringObject.getClass()).stream()
				.filter(s -> s.test(scoringObject, match))
				.collect(Collectors.toList());
		
		if (acceptableHandlers.isEmpty()) {
			throw new FrameworkException("Failed to find score completion strategy for " + scoringObject);
		} else if (acceptableHandlers.size() > 1) {
			throw new FrameworkException("Expected to find only (1) score completion strategy but found " + acceptableHandlers.size() + " for " + scoringObject);
		} else {
			return acceptableHandlers.get(0);
		}
	}

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionHandlerResolver#resolve(com.tonelope.tennis.scoreprocessor.model.Winnable, com.tonelope.tennis.scoreprocessor.model.Match)
	 */
	@Override
	public boolean resolve(Winnable scoringObject, Match match) {
		return this.getHandler(scoringObject, match).apply(scoringObject, match);
	}
}
