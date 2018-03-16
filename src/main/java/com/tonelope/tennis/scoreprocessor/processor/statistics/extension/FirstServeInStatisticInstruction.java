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

import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.Stroke;
import com.tonelope.tennis.scoreprocessor.model.StrokeType;
import com.tonelope.tennis.scoreprocessor.processor.statistics.SimplePercentageStatistic;
import com.tonelope.tennis.scoreprocessor.processor.statistics.StatisticInstruction;

import lombok.Getter;

/**
 * <p>
 * Calculates the number of first serves in of total first serves and the
 * percentage value.
 * </p>
 * 
 * @author Tony Lopez
 *
 */
@Getter
public class FirstServeInStatisticInstruction implements StatisticInstruction<Stroke, SimplePercentageStatistic> {

	private final Player player;

	private int firstServesIn;
	private int totalFirstServes;

	public FirstServeInStatisticInstruction(Player player) {
		this.player = player;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tonelope.tennis.scoreprocessor.processor.statistics.
	 * StatisticInstruction#buildResult()
	 */
	@Override
	public SimplePercentageStatistic createResult() {
		return new SimplePercentageStatistic(firstServesIn, totalFirstServes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tonelope.tennis.scoreprocessor.processor.statistics.
	 * StatisticInstruction#evaluate(com.tonelope.tennis.scoreprocessor.model.
	 * ScoringObject)
	 */
	@Override
	public void evaluate(Stroke stroke) {
		if (stroke.isA(StrokeType.FIRST_SERVE) && stroke.getPlayer().equals(player)) {
			totalFirstServes++;
			if (!stroke.isOut()) {
				firstServesIn++;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tonelope.tennis.scoreprocessor.processor.statistics.
	 * StatisticInstruction#reset()
	 */
	@Override
	public void reset() {
		this.firstServesIn = 0;
		this.totalFirstServes = 0;
	}

}
