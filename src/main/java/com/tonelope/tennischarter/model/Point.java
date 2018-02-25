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

import com.tonelope.tennischarter.utils.ListUtils;

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
public class Point extends Winnable {

	private Player server;
	private Player receiver;
	private final List<Stroke> strokes = new ArrayList<>();
	
	@Override
	public Player getWinningPlayer() {
		if (!this.isCompleted()) {
			return null;
		}
		
		Stroke stroke = ListUtils.getLast(this.strokes);
		
		// Winner
		if (!stroke.isOut()) {
			return stroke.getPlayer();
		}
		
		// Forced/Unforced Error
		if (!stroke.isServe()) {
			return this.strokes.get(this.strokes.size() - 2).getPlayer();
		} 

		// Double fault is only remaining option.
		return this.receiver;
	}

	@Override
	public void initialize() {
		
	}
}
