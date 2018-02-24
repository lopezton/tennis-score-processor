package com.tonelope.tennischarter.processor;

import com.tonelope.tennischarter.dao.MatchRepository;
import com.tonelope.tennischarter.model.FrameworkException;
import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.MatchRules;
import com.tonelope.tennischarter.model.PlayerConfig;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter @Setter
public class DefaultMatchFactory implements MatchFactory {
	
	private final MatchRepository matchRepository;
	
	public Match create(MatchRules matchRules, PlayerConfig playerConfig) {
		if (null == matchRules) {
			throw new FrameworkException("Missing configuration to create match: matchRules");
		}
		if (null == playerConfig) {
			throw new FrameworkException("Missing configuration to create match: playerConfig");
		}
		if (null == playerConfig.getPlayers()) {
			throw new FrameworkException("Missing configuration to create match: playerConfig.players");
		}
		if (null == playerConfig.getStartingServer()) {
			throw new FrameworkException("Missing configuration to create match: playerConfig.startingServer");
		}
		if (!playerConfig.getPlayers().contains(playerConfig.getStartingServer())) {
			throw new FrameworkException("Starting server is in the list of players");
		}
		if (playerConfig.getPlayers().size() != 2 && playerConfig.getPlayers().size() != 4) {
			throw new FrameworkException("Incorrect number of players for match.");
		}
		Match match = new Match();
		match.setMatchRules(matchRules);
		match.setPlayers(playerConfig.getPlayers());
		match.setStartingServer(playerConfig.getStartingServer());
		match.setStartingReceiver(playerConfig.getStartingReceiver());
		return this.matchRepository.save(match);
	}
}
