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
package com.tonelope.tennis.scoreprocessor.processor.statistics.extension.serve;

import java.util.List;

import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.Point;
import com.tonelope.tennis.scoreprocessor.model.Stroke;
import com.tonelope.tennis.scoreprocessor.model.StrokeType;
import com.tonelope.tennis.scoreprocessor.processor.statistics.SimplePercentageStatistic;
import com.tonelope.tennis.scoreprocessor.processor.statistics.StatisticInstruction;

import lombok.Getter;

/**
 * @author Tony Lopez
 *
 */
@Getter
public class SecondServePointsWonStatisticInstruction implements StatisticInstruction<Point, SimplePercentageStatistic> {

	private final Player player;
	private int pointsWon;
	private int totalPoints;
	
	public SecondServePointsWonStatisticInstruction(Player player) {
		this.player = player;
	}
	
	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.statistics.StatisticInstruction#createResult()
	 */
	@Override
	public SimplePercentageStatistic createResult() {
		return new SimplePercentageStatistic(pointsWon, totalPoints);
	}

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.statistics.StatisticInstruction#evaluate(com.tonelope.tennis.scoreprocessor.model.ScoringObject)
	 */
	@Override
	public void evaluate(Point point) {
		if (point.isSimple()) {
			return;
		}
		
		// iterate over the strokes until first serve is found (in case of let serves)
		List<Stroke> strokes = point.getStrokes();
		for(Stroke stroke: strokes) {
			if (StrokeType.SECOND_SERVE.equals(stroke.getStrokeType())) {
				if (!stroke.isOut()) {
					this.totalPoints++;
				}
				if (point.getWinningPlayer().equals(player)) {
					this.pointsWon++;
				}
				return;
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.statistics.StatisticInstruction#reset()
	 */
	@Override
	public void reset() {
		this.pointsWon = 0;
		this.totalPoints = 0;
	}

}
