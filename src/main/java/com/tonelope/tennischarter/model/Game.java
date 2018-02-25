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
public class Game extends Winnable {

	private Player server;
	private Player receiver;
	private GameScore score;
	private final List<Point> points = new ArrayList<>();
	
	@Override
	public Player getWinningPlayer() {
		return this.getWinningPlayer(this.points);
	}

	public Game(Player server, Player receiver, boolean initialize) {
		this(server, receiver, new GameScore());
		if (initialize) {
			this.initialize();
		}
	}

	@Override
	public void initialize() {
		this.points.add(new Point(server, receiver));
	}

	public Player getNextServer() {
		return this.server;
	}
	
	public Player getNextReceiver() {
		return this.receiver;
	}
}
