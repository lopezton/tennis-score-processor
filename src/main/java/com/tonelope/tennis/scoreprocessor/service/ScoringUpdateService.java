package com.tonelope.tennis.scoreprocessor.service;

import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Stroke;

public interface ScoringUpdateService {

	Match updateMatch(String matchId, Stroke stroke);

}
