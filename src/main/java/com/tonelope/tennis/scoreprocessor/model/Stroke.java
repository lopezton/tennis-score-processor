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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 
 * @author Tony Lopez
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Stroke extends ScoringObject {

	private Player player;
	private StrokeType strokeType;
	private boolean out;
	private boolean winner;
	
	public boolean isServe() {
		return StrokeType.FIRST_SERVE.equals(this.strokeType) || StrokeType.SECOND_SERVE.equals(this.strokeType);
	}
	
	public boolean isOutRallyShot() {
		return this.out && !this.isServe();
	}
	
	public boolean isDoubleFault() {
		return StrokeType.SECOND_SERVE.equals(strokeType) && this.out;
	}
	
	public boolean isServiceAce() {
		return StrokeType.FIRST_SERVE.equals(strokeType) && this.winner;
	}
}
