package com.tonelope.tennischarter.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.tonelope.tennischarter.model.Match;

import lombok.Getter;
import lombok.Setter;

@Repository
@Getter @Setter
public class LocalMatchRepository implements MatchRepository {

	private static Long matchCounter = 0L;
	private Map<String, Match> matches;
	
	public LocalMatchRepository() {
		this.matches = new HashMap<>();
	}

	@Override
	public Match findById(String id) {
		return Optional.ofNullable(this.matches.get(id)).orElseThrow(() -> 
			new RuntimeException("No match found for id=" + id));
	}
	
	@Override
	public Match save(Match match) {
		if (null == match) {
			return null;
		}
		
		if (null == match.getId()) {
			match.setId(String.valueOf(++matchCounter));
		}
		this.matches.put(match.getId(), match);
		return match;
	}
}
