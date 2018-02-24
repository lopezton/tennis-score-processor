package com.tonelope.tennischarter.processor;

import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Stroke;

public interface MatchProcessor {

	Match addStrokeToMatch(Match match, Stroke stroke);
}
