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
package com.tonelope.tennischarter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tonelope.tennischarter.dao.MatchRepository;
import com.tonelope.tennischarter.model.Game;
import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Point;
import com.tonelope.tennischarter.model.Set;
import com.tonelope.tennischarter.model.TiebreakGame;
import com.tonelope.tennischarter.processor.DefaultMatchFactory;
import com.tonelope.tennischarter.processor.MatchFactory;
import com.tonelope.tennischarter.processor.MatchProcessor;
import com.tonelope.tennischarter.processor.SinglesMatchProcessor;
import com.tonelope.tennischarter.processor.scoring.DefaultScoreCompletionStrategyResolver;
import com.tonelope.tennischarter.processor.scoring.ScoreCompletionStrategy;
import com.tonelope.tennischarter.processor.scoring.ScoreCompletionStrategyResolver;
import com.tonelope.tennischarter.processor.scoring.game.DeuceGameCompletionStrategy;
import com.tonelope.tennischarter.processor.scoring.game.NoAdGameCompletionStrategy;
import com.tonelope.tennischarter.processor.scoring.game.TiebreakGameCompletionStrategy;
import com.tonelope.tennischarter.processor.scoring.match.DefaultMatchCompletionStrategy;
import com.tonelope.tennischarter.processor.scoring.point.DefaultPointCompletionStrategy;
import com.tonelope.tennischarter.processor.scoring.set.DefaultSetCompletionStrategy;
import com.tonelope.tennischarter.processor.scoring.set.NoFinalSetTiebreakSetCompletionStrategy;

/**
 * 
 * @author Tony Lopez
 *
 */
@Configuration
public class DefaultFrameworkConfiguration {

	@Autowired
	private MatchRepository matchRepository;
	
	@Bean
	public MatchFactory matchFactory() {
		return new DefaultMatchFactory(this.matchRepository);
	}
	
	@Bean
	public MatchProcessor singlesMatchProcessor(ScoreCompletionStrategyResolver resolver) {
		return new SinglesMatchProcessor(resolver);
	}
	
	@Bean
	public ScoreCompletionStrategyResolver defaultScoreCompletionStrategyResolver(ApplicationContext context) {
		return new DefaultScoreCompletionStrategyResolver(context);
	}
	
	@Bean
	public ScoreCompletionStrategy<Point> defaultPointCompletionStrategy() {
		return new DefaultPointCompletionStrategy();
	}
	
	@Bean
	public ScoreCompletionStrategy<Game> deuceGameCompletionStrategy() {
		return new DeuceGameCompletionStrategy();
	}
	
	@Bean
	public ScoreCompletionStrategy<Game> noAdGameCompletionStrategy() {
		return new NoAdGameCompletionStrategy();
	}
	
	@Bean
	public ScoreCompletionStrategy<Set> defaultSetCompletionStrategy() {
		return new DefaultSetCompletionStrategy();
	}
	
	@Bean
	public ScoreCompletionStrategy<Match> defaultMatchCompletionStrategy() {
		return new DefaultMatchCompletionStrategy();
	}
	
	@Bean
	public ScoreCompletionStrategy<TiebreakGame> tiebreakGameCompletionStrategy() {
		return new TiebreakGameCompletionStrategy();
	}
	
	@Bean
	public ScoreCompletionStrategy<Set> noFinalSetTiebreakSetCompletionStrategy() {
		return new NoFinalSetTiebreakSetCompletionStrategy();
	}
}
