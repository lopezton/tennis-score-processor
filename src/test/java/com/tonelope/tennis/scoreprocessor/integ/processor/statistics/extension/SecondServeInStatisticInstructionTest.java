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
import com.tonelope.tennis.scoreprocessor.processor.statistics.CommonStatisticProcessor;
import com.tonelope.tennis.scoreprocessor.processor.statistics.SimplePercentageStatistic;
import com.tonelope.tennis.scoreprocessor.processor.statistics.extension.SecondServeInStatisticInstruction;


/**
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SecondServeInStatisticInstructionTest extends AbstractProcessingTests {

	@Test
	public void t1_defaultScenario() {
		MatchProcessor matchProcessor = this.createNewMatch();
		Match match = matchProcessor.getMatch();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		matchProcessor.addStatisticInstruction(new SecondServeInStatisticInstruction(player1));
		
		this.hitDoubleFault(match, player1);
		SimplePercentageStatistic stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(0, stat.getNumerator());
		Assert.assertEquals(1, stat.getDenominator());
		Assert.assertEquals(0, stat.getPercentage(), 0);
		
		matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, true, false));
		matchProcessor.update(new Stroke(player1, StrokeType.SECOND_SERVE, false, true));
		stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(1, stat.getNumerator());
		Assert.assertEquals(2, stat.getDenominator());
		Assert.assertEquals(.5, stat.getPercentage(), 0);
		
		this.hitFirstServeAce(match, player1);
		stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(1, stat.getNumerator());
		Assert.assertEquals(2, stat.getDenominator());
		Assert.assertEquals(.5, stat.getPercentage(), 0);
		
		matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, true, false));
		matchProcessor.update(new Stroke(player1, StrokeType.SECOND_SERVE, false, false));
		matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, true, false));
		stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(2, stat.getNumerator());
		Assert.assertEquals(3, stat.getDenominator());
		Assert.assertEquals(.667, stat.getPercentage(), .001);
		
		matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, true, false));
		matchProcessor.update(new Stroke(player1, StrokeType.SECOND_SERVE, false, true));
		stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(3, stat.getNumerator());
		Assert.assertEquals(4, stat.getDenominator());
		Assert.assertEquals(.75, stat.getPercentage(), 0);
	}
	
	@Test
	public void t2_simplePoint() {
		MatchProcessor matchProcessor = this.createNewMatch();
		Match match = matchProcessor.getMatch();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		matchProcessor.addStatisticInstruction(new SecondServeInStatisticInstruction(player1));
		
		this.hitDoubleFault(match, player1);
		SimplePercentageStatistic stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(0, stat.getNumerator());
		Assert.assertEquals(1, stat.getDenominator());
		Assert.assertEquals(0, stat.getPercentage(), 0);
		
		matchProcessor.update(new SimplePoint(player1, player2, player1));
		stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(0, stat.getNumerator());
		Assert.assertEquals(1, stat.getDenominator());
		Assert.assertEquals(0, stat.getPercentage(), 0);
		
		this.hitFirstServeAce(match, player1);
		stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(0, stat.getNumerator());
		Assert.assertEquals(1, stat.getDenominator());
		Assert.assertEquals(0, stat.getPercentage(), 0);
		
		matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, true, false));
		matchProcessor.update(new Stroke(player1, StrokeType.SECOND_SERVE, false, false));
		matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, true, false));
		stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(1, stat.getNumerator());
		Assert.assertEquals(2, stat.getDenominator());
		Assert.assertEquals(.5, stat.getPercentage(), 0);
		
		matchProcessor.update(new SimplePoint(player1, player2, player1));
		stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		Assert.assertEquals(1, stat.getNumerator());
		Assert.assertEquals(2, stat.getDenominator());
		Assert.assertEquals(.5, stat.getPercentage(), 0);
	}
	
	@Test
	public void t3_commonStatisticProcessor() {
		MatchProcessor matchProcessor = this.createNewMatch();
		CommonStatisticProcessor statProcessor = matchProcessor.getStatisticProcessor().toCommonStatisticProcessor();
		Match match = matchProcessor.getMatch();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.hitDoubleFault(match, player1);
		SimplePercentageStatistic stat = statProcessor.getSecondServesIn(player1);
		Assert.assertEquals(0, stat.getNumerator());
		Assert.assertEquals(1, stat.getDenominator());
		Assert.assertEquals(0, stat.getPercentage(), 0);
		
		matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, true, false));
		matchProcessor.update(new Stroke(player1, StrokeType.SECOND_SERVE, false, false));
		matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, true, false));
		stat = statProcessor.getSecondServesIn(player1);
		Assert.assertEquals(1, stat.getNumerator());
		Assert.assertEquals(2, stat.getDenominator());
		Assert.assertEquals(.5, stat.getPercentage(), 0);
	}
}
