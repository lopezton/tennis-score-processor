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

import lombok.Getter;

/**
 * @author Tony Lopez
 *
 */
@Getter
public class SimplePoint extends Point {

	private final Player winningPlayer;

	public SimplePoint(Player server, Player receiver, Player winningPlayer) {
		super(server, receiver);
		this.winningPlayer = winningPlayer;
		this.setStatus(Status.COMPLETE);
	}

	/**
	 * <p>Always returns true.</p>
	 */
	@Override
	public boolean isSimple() {
		return true;
	}
}
