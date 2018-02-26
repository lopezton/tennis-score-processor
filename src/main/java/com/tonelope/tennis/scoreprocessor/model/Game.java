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
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @ToString
public class Game extends Winnable {

	private Player server;
	private Player receiver;
	private final GameScore score = new GameScore();
	private final List<Point> points = new ArrayList<>();
	
	@Override
	public Player getWinningPlayer() {
		return this.getWinningPlayer(this.points);
	}

	public Game(Player server, Player receiver, boolean initialize) {
		this(server, receiver);
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
