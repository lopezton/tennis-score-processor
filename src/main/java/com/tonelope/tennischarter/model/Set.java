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
package com.tonelope.tennischarter.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * @author Tony Lopez
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter @ToString
public class Set extends Winnable {

	private Player startingServer;
	private Player startingReceiver;
	private final SetScore score = new SetScore();
	private final List<Game> games = new ArrayList<>();
	
	public Set(Player startingServer, Player startingReceiver, boolean initialize) {
		this(startingServer, startingReceiver);
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
