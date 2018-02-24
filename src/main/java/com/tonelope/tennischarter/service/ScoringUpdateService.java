package com.tonelope.tennischarter.service;

import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Stroke;

public interface ScoringUpdateService {

	Match updateMatch(String matchId, Stroke stroke);

}
