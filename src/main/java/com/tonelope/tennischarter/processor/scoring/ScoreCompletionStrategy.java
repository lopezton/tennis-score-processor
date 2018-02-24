package com.tonelope.tennischarter.processor.scoring;

import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Winnable;

public interface ScoreCompletionStrategy<T extends Winnable> {

	boolean test(Winnable scoringObject, Match match);
	
	boolean apply(T scoringObject, Match match);
}