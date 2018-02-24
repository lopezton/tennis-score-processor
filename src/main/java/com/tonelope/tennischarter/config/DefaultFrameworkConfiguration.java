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
	
//	@Bean
//	public Map<Class<?>, ScoreCompletionStrategy<?>> defaultScoringCompletionStrategies() {
//		Map<Class<?>, ScoreCompletionStrategy<?>> strategies = new HashMap<>();
//		strategies.put(Point.class, new DefaultPointCompletionStrategy());
//		strategies.put(Game.class, new DeuceGameCompletionStrategy());
//		strategies.put(Set.class, new DefaultSetCompletionStrategy());
//		strategies.put(Match.class, new DefaultMatchCompletionStrategy());
//		return strategies;
//	}
	
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
