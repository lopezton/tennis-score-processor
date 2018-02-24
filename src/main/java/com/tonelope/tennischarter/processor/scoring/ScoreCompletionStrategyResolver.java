package com.tonelope.tennischarter.processor.scoring;

import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Winnable;

public interface ScoreCompletionStrategyResolver {

	<T extends Winnable> boolean resolve(T scoringObject, Match match);
}
