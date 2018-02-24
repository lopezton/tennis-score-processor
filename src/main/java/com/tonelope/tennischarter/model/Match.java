package com.tonelope.tennischarter.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class Match extends Winnable {

	private String id;
	private List<Player> players;
	private Player startingServer;
	private Player startingReceiver;
	private MatchRules matchRules;
	private final List<Set> sets = new ArrayList<>();
	
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
