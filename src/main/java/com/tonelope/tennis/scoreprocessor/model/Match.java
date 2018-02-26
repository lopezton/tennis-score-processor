package com.tonelope.tennis.scoreprocessor.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Getter @Setter @ToString
public class Match extends Winnable {

	private String id;
	private final List<Player> players;
	private final Player startingServer;
	private final Player startingReceiver;
	private final MatchRules matchRules;
	private final MatchScore score = new MatchScore();
	private final List<Set> sets = new ArrayList<>();
	
	public Match(List<Player> players, MatchRules matchRules, boolean initialize) {
		this.players = players;
		this.startingServer = players.get(0);
		this.startingReceiver = players.get(1);
		this.matchRules = matchRules;
		if (initialize) {
			this.initialize();
		}
	}
	
	@Override
	public Player getWinningPlayer() {
		return this.getWinningPlayer(this.sets);
	}
	
	public boolean isCurrentlyInFinalSet() {
		if (null == this.sets || this.sets.isEmpty()) {
			return false;
		}
		return this.matchRules.getNumberOfSets() == this.sets.size();
	}

	@Override
	public void initialize() {
		this.sets.add(new Set(this.startingServer, this.startingReceiver, true));
	}
}
