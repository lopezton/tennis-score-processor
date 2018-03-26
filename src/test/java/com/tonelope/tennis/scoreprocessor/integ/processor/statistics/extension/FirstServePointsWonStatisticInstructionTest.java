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
package com.tonelope.tennis.scoreprocessor.integ.processor.statistics.extension;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tonelope.tennis.scoreprocessor.integ.processor.AbstractProcessingTests;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.SimplePoint;
import com.tonelope.tennis.scoreprocessor.model.Stroke;
import com.tonelope.tennis.scoreprocessor.model.StrokeType;
import com.tonelope.tennis.scoreprocessor.processor.MatchProcessor;
import com.tonelope.tennis.scoreprocessor.processor.statistics.SimplePercentageStatistic;
import com.tonelope.tennis.scoreprocessor.processor.statistics.extension.FirstServePointsWonStatisticInstruction;

/**
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FirstServePointsWonStatisticInstructionTest extends AbstractProcessingTests {

	@Test
	public void t1_defaultScenario() {
		MatchProcessor matchProcessor = this.createNewMatch();
		Match match = matchProcessor.getMatch();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		matchProcessor.addStatisticInstruction(new FirstServePointsWonStatisticInstruction(player1));
		
		this.hitFirstServeAce(match, player1);
		Assert.assertEquals(1, matchProcessor.getStatistics().size());
		SimplePercentageStatistic statistic = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(1, statistic.getNumerator());
		Assert.assertEquals(1, statistic.getDenominator());
		Assert.assertEquals(1.0, statistic.getPercentage(), 0);
		
		this.hitDoubleFault(match, player1);
		Assert.assertEquals(1, matchProcessor.getStatistics().size());
		statistic = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(1, statistic.getNumerator());
		Assert.assertEquals(1, statistic.getDenominator());
		Assert.assertEquals(1.0, statistic.getPercentage(), 0);
		
		this.hitFirstServeAce(match, player1);
		Assert.assertEquals(1, matchProcessor.getStatistics().size());
		statistic = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(2, statistic.getNumerator());
		Assert.assertEquals(2, statistic.getDenominator());
		Assert.assertEquals(1.0, statistic.getPercentage(), 0);
		
		matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		Assert.assertEquals(1, matchProcessor.getStatistics().size());
		statistic = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(2, statistic.getNumerator());
		Assert.assertEquals(3, statistic.getDenominator());
		Assert.assertEquals(0.667, statistic.getPercentage(), .001);
		
		this.hitFirstServeAce(match, player1);
		Assert.assertEquals(1, matchProcessor.getStatistics().size());
		statistic = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(3, statistic.getNumerator());
		Assert.assertEquals(4, statistic.getDenominator());
		Assert.assertEquals(0.75, statistic.getPercentage(), 0);
		
		matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, false));
		matchProcessor.update(new Stroke(player1, StrokeType.FOREHAND_VOLLEY, false, true));
		Assert.assertEquals(1, matchProcessor.getStatistics().size());
		statistic = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(4, statistic.getNumerator());
		Assert.assertEquals(5, statistic.getDenominator());
		Assert.assertEquals(0.8, statistic.getPercentage(), 0);
	}
	
	@Test
	public void t1_simplePoint() {
		MatchProcessor matchProcessor = this.createNewMatch();
		Match match = matchProcessor.getMatch();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		matchProcessor.addStatisticInstruction(new FirstServePointsWonStatisticInstruction(player1));
		
		this.hitFirstServeAce(match, player1);
		Assert.assertEquals(1, matchProcessor.getStatistics().size());
		SimplePercentageStatistic statistic = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(1, statistic.getNumerator());
		Assert.assertEquals(1, statistic.getDenominator());
		Assert.assertEquals(1.0, statistic.getPercentage(), 0);
		
		this.hitDoubleFault(match, player1);
		Assert.assertEquals(1, matchProcessor.getStatistics().size());
		statistic = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(1, statistic.getNumerator());
		Assert.assertEquals(1, statistic.getDenominator());
		Assert.assertEquals(1.0, statistic.getPercentage(), 0);
		
		this.hitFirstServeAce(match, player1);
		Assert.assertEquals(1, matchProcessor.getStatistics().size());
		statistic = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(2, statistic.getNumerator());
		Assert.assertEquals(2, statistic.getDenominator());
		Assert.assertEquals(1.0, statistic.getPercentage(), 0);
		
		matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		Assert.assertEquals(1, matchProcessor.getStatistics().size());
		statistic = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(2, statistic.getNumerator());
		Assert.assertEquals(3, statistic.getDenominator());
		Assert.assertEquals(.667, statistic.getPercentage(), 0.001);
		
		this.hitFirstServeAce(match, player1);
		Assert.assertEquals(1, matchProcessor.getStatistics().size());
		statistic = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(3, statistic.getNumerator());
		Assert.assertEquals(4, statistic.getDenominator());
		Assert.assertEquals(.75, statistic.getPercentage(), 0);
		
		matchProcessor.update(new SimplePoint(player1, player2, player1));
		Assert.assertEquals(1, matchProcessor.getStatistics().size());
		statistic = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(3, statistic.getNumerator());
		Assert.assertEquals(4, statistic.getDenominator());
		Assert.assertEquals(.75, statistic.getPercentage(), 0);
	}
}
