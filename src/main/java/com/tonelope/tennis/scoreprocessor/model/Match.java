package com.tonelope.tennis.scoreprocessor.model;

import java.util.ArrayList;
import java.util.List;

import com.tonelope.tennis.scoreprocessor.utils.ListUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
	
	public Set getCurrentSet() {
		return ListUtils.getLast(this.sets);
	}
	
	public Game getCurrentGame() {
		return this.getCurrentSet().getCurrentGame();
	}
	
	public Point getCurrentPoint() {
		return this.getCurrentGame().getCurrentPoint();
	}
	
	public boolean isCurrentlyInFinalSet() {
		if (null == this.sets || this.sets.isEmpty()) {
			return false;
		}
		return this.matchRules.getNumberOfSets() == this.sets.size();
	}

	@Override
	public void initialize() {
		this.sets.add(new Set(this.matchRules, this.startingServer, this.startingReceiver, true));
	}

	/**
	 * @param stroke
	 */
	public void addStroke(Stroke stroke) {
		this.getCurrentSet().addStroke(stroke, this.matchRules);
	}
}
