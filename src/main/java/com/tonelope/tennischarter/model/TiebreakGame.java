package com.tonelope.tennischarter.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class TiebreakGame extends Game {

	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Player nextServer;
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Player nextReceiver;
	
	public TiebreakGame(Player server, Player receiver) {
		this(server, receiver, false);
	}
	
	public TiebreakGame(Player server, Player receiver, boolean initialize) {
		super(server, receiver, initialize);
		this.nextServer = server;
		this.nextReceiver = receiver;
	}
	
	@Override
	public Player getNextServer() {
		this.nextServer = this.getNextAfterNumberOfPoints(this.nextServer, this.getServer(), this.getReceiver(), 2);
		return this.nextServer;
	}
	
	@Override
	public Player getNextReceiver() {
		this.nextReceiver = this.getNextAfterNumberOfPoints(this.nextReceiver, this.getReceiver(), this.getServer(), 2);
		return this.nextReceiver;
	}
	
	private Player getNextAfterNumberOfPoints(Player current, Player a, Player b, int idx) {
		if ((this.getPoints().size() + 1) % idx == 0) {
			if (current == a) {
				current = b;
			} else {
				current = a;
			}
		}
		return current;
	}
	
	public static void main(String[] args) {
		Player player1 = new Player("Roger", "Federer");
		Player player2 = new Player("Rafael", "Nadal");
		TiebreakGame game = new TiebreakGame(player1, player2, true);
		for(int i = 0; i <= 12; i++) {
			System.out.println(game.nextServer);
			game.getPoints().add(new Point(game.getNextServer(), game.getNextReceiver()));
		}
	}
}
