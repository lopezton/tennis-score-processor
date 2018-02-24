package com.tonelope.tennischarter.processor.scoring;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;

import com.tonelope.tennischarter.model.FrameworkException;
import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Winnable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
