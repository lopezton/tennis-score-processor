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

import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.processor.statistics.extension.FirstServeInStatisticInstruction;
import com.tonelope.tennis.scoreprocessor.processor.statistics.extension.FirstServePointsWonStatisticInstruction;
import com.tonelope.tennis.scoreprocessor.processor.statistics.extension.SecondServeInStatisticInstruction;
import com.tonelope.tennis.scoreprocessor.processor.statistics.extension.SecondServePointsWonStatisticInstruction;

/**
 * @author Tony Lopez
 *
 */
public class CommonStatisticProcessor extends StatisticProcessor {

	public CommonStatisticProcessor(Match match) {
		super(match);
	}

	public CommonStatisticProcessor(StatisticProcessor statisticProcessor) {
		this(statisticProcessor.getMatch());
	}
	
	public SimplePercentageStatistic getFirstServesIn(Player player) {
		return (SimplePercentageStatistic) this.getStatistic(new FirstServeInStatisticInstruction(player));
	}
	
	public SimplePercentageStatistic getFirstServePointsWon(Player player) {
		return (SimplePercentageStatistic) this.getStatistic(new FirstServePointsWonStatisticInstruction(player));
	}
	
	public SimplePercentageStatistic getSecondServesIn(Player player) {
		return (SimplePercentageStatistic) this.getStatistic(new SecondServeInStatisticInstruction(player));
	}

	public SimplePercentageStatistic getSecondServePointsWon(Player player) {
		return (SimplePercentageStatistic) this.getStatistic(new SecondServePointsWonStatisticInstruction(player));
	}
}
