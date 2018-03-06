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
import lombok.Setter;

/**
 * 
 * @author Tony Lopez
 *
 */
@Getter @Setter
public class SetScore extends Score {

	public static final String SEPARATOR = "-";
	private int startingServerScore = 0;
	private int startingReceiverScore = 0;
	private TiebreakScore tiebreakScore;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(startingServerScore).append(SEPARATOR).append(startingReceiverScore);
		if (null != tiebreakScore) {
			sb.append(tiebreakScore.toString());
		}
		
		return sb.toString();
	}

}
