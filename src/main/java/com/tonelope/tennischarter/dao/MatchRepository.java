package com.tonelope.tennischarter.dao;

import com.tonelope.tennischarter.model.Match;

public interface MatchRepository {

	Match findById(String id);
	
	Match save(Match match);
}
