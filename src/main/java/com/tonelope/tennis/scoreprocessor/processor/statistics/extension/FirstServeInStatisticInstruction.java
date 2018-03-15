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
package com.tonelope.tennis.scoreprocessor.processor.statistics.extension;

import java.util.List;

import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.Stroke;
import com.tonelope.tennis.scoreprocessor.model.StrokeType;
import com.tonelope.tennis.scoreprocessor.processor.statistics.AbstractStatistic;
import com.tonelope.tennis.scoreprocessor.processor.statistics.SimplePercentageResult;
import com.tonelope.tennis.scoreprocessor.processor.statistics.TennisStatistic;

import lombok.Getter;

/**
 * @author Tony Lopez
 *
 */
@Getter
public class FirstServeInStatistic extends AbstractStatistic implements TennisStatistic<SimplePercentageResult> {

	private final Player player;
	
	public FirstServeInStatistic(Player player) {
		this.player = player;
	}
	
	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.statistics.TennisStatistic#getResult(com.tonelope.tennis.scoreprocessor.model.Match)
	 */
	@Override
	public SimplePercentageResult getResult(Match match) {
		List<Stroke> firstServes = this.getStrokes(match, s -> s.isA(StrokeType.FIRST_SERVE));
		long in = firstServes.stream().filter(s -> !s.isOut()).count();
		return new SimplePercentageResult(Math.toIntExact(in), firstServes.size());
	}

}
