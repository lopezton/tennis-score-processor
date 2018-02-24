package com.tonelope.tennischarter.processor;

import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.MatchRules;
import com.tonelope.tennischarter.model.PlayerConfig;

public interface MatchFactory {

	Match create(MatchRules matchRules, PlayerConfig playerConfig);
}
