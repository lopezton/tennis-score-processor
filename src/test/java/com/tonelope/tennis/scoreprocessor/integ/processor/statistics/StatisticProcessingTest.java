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
package com.tonelope.tennis.scoreprocessor.integ.processor.statistics;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tonelope.tennis.scoreprocessor.integ.processor.AbstractProcessingTests;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.processor.statistics.SimplePercentageResult;
import com.tonelope.tennis.scoreprocessor.processor.statistics.extension.FirstServeInStatistic;

/**
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatisticProcessingTest extends AbstractProcessingTests {

	@Test
	public void t1_firstServeIn_getResult() {
		this.createNewMatch();
		final Match match = this.matchProcessor.getMatch();
		final Player player1 = match.getStartingServer();
		
		this.hitFirstServeAce(match, player1);
		this.hitFirstServeAce(match, player1);
		this.hitFirstServeAce(match, player1);
		this.hitDoubleFault(match, player1);
		this.hitDoubleFault(match, player1);
		this.hitDoubleFault(match, player1);
		this.hitFirstServeAce(match, player1);
		this.hitFirstServeAce(match, player1);
		
		SimplePercentageResult result = 
				this.matchProcessor.evaluateStatistic(new FirstServeInStatistic(player1));
		Assert.assertEquals(5, result.getNumerator());
		Assert.assertEquals(8, result.getDenominator());
		Assert.assertEquals("5/8 (62%)", result.toString());
	}
}
