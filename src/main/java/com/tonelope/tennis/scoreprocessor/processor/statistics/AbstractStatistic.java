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
package com.tonelope.tennis.scoreprocessor.processor.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Stroke;

/**
 * @author Tony Lopez
 *
 */
public abstract class AbstractStatistic {
	
	protected List<Stroke> getStrokes(Match match, Predicate<Stroke> predicate) {
		List<Stroke> strokes = new ArrayList<>();
		
		match.getSets().stream()
			.forEach(set -> set.getGames()
			.forEach(game -> game.getPoints()
			.forEach(point -> point.getStrokes()
				.stream()
				.filter(predicate)
				.forEach(strokes::add))));
		
		return strokes;
	}
}
