package com.tonelope.tennischarter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tonelope.tennischarter.dao.MatchRepository;
import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Stroke;
import com.tonelope.tennischarter.processor.MatchProcessorResolver;

import lombok.Getter;
import lombok.Setter;

@Service
@Getter @Setter
public class LocalScoringUpdateService implements ScoringUpdateService {

	private final MatchProcessorResolver matchProcessorResolver;
	private final MatchRepository matchRepository;
	
	@Autowired
	public LocalScoringUpdateService(MatchRepository matchRepository,
			MatchProcessorResolver matchProcessorResolver) {
		this.matchRepository = matchRepository;
		this.matchProcessorResolver = matchProcessorResolver;
	}
	
	@Override
	public Match updateMatch(String matchId, Stroke stroke) {
		Match match = this.matchRepository.findById(matchId);
		this.matchProcessorResolver.get(match).addStrokeToMatch(match, stroke);
		return this.matchRepository.save(match);
	}
}