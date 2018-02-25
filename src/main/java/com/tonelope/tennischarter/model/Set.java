package com.tonelope.tennischarter.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @ToString
public class Set extends Winnable {

	private Player startingServer;
	private Player startingReceiver;
	private SetScore score;
	private final List<Game> games = new ArrayList<>();
	
	public Set(Player startingServer, Player startingReceiver, boolean initialize) {
		this(startingServer, startingReceiver, new SetScore());
		if (initialize) {
			this.initialize();
		}
	}
	
	@Override
	public Player getWinningPlayer() {
		return this.getWinningPlayer(this.games);
	}

	@Override
	public void initialize() {
		this.games.add(new Game(startingServer, startingReceiver, true));
	}
}
