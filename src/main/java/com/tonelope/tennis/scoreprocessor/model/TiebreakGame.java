/**
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tonelope.tennis.scoreprocessor.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Tony Lopez
 *
 */
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
