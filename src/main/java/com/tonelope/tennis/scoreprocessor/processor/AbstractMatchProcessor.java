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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tonelope.tennis.scoreprocessor.model.FrameworkException;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.MatchEventType;
import com.tonelope.tennis.scoreprocessor.processor.scoring.DefaultScoreCompletionStrategyResolver;
import com.tonelope.tennis.scoreprocessor.processor.scoring.ScoreCompletionStrategyResolver;

import lombok.Getter;

/**
 * 
 * @author Tony Lopez
 *
 */
@Getter
public abstract class AbstractMatchProcessor implements MatchProcessor {

	public static final Logger LOG = LoggerFactory.getLogger(AbstractMatchProcessor.class);
	
	protected final ScoreCompletionStrategyResolver scoreCompletionStrategyResolver;
	protected final Map<MatchEventType, List<Consumer<Match>>> events = new HashMap<>();
	
	AbstractMatchProcessor(ScoreCompletionStrategyResolver scoreCompletionStrategyResolver) {
		if (null != scoreCompletionStrategyResolver) {
			this.scoreCompletionStrategyResolver = scoreCompletionStrategyResolver;
		} else {
			this.scoreCompletionStrategyResolver = new DefaultScoreCompletionStrategyResolver();
		}
	}
	
	public void registerEvent(MatchEventType event, Consumer<Match> method) {
		this.events.putIfAbsent(event, new ArrayList<>());
		this.events.get(event).add(method);
	}
	
	protected void executeMatchEvents(MatchEventType type, Match match) {
		List<Consumer<Match>> eventMethods = this.events.get(type);
		if (null != eventMethods && !eventMethods.isEmpty()) {
			LOG.debug("Executing events for {}.", type);
			for(Consumer<Match> eventMethod : eventMethods) {
				try {
					eventMethod.accept(match);
				} catch (Exception e) {
					throw new FrameworkException("Failed to execute an event", e);
				}
			}
		}
	}
}
