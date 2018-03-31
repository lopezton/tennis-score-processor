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
package com.tonelope.tennis.scoreprocessor.integ.processor.statistics.extension.serve;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tonelope.tennis.scoreprocessor.integ.processor.AbstractProcessingTests;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.processor.MatchProcessor;
import com.tonelope.tennis.scoreprocessor.processor.statistics.CommonStatisticProcessor;
import com.tonelope.tennis.scoreprocessor.processor.statistics.SimplePercentageStatistic;
import com.tonelope.tennis.scoreprocessor.processor.statistics.extension.serve.ServiceGamesWonStatisticInstruction;

/**
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServiceGamesWonStatisticInstructionTest extends AbstractProcessingTests {

	@Test
	public void t1_defaultScenario() {
		MatchProcessor matchProcessor = this.createNewMatch();
		Match match = matchProcessor.getMatch();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		matchProcessor.addStatisticInstruction(new ServiceGamesWonStatisticInstruction(player1));
		assertThat(matchProcessor.getStatisticProcessor().getInstructions().size(), is(1));
		
		this.winServiceGame(match, player1);
		SimplePercentageStatistic stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		assertThat(stat.getNumerator(), is(1));
		assertThat(stat.getDenominator(), is(1));
		assertThat(stat.getPercentage(), is(1.0));
		
		this.winServiceGame(match, player2);
		
		this.winServiceGame(match, player1);
		stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		assertThat(stat.getNumerator(), is(2));
		assertThat(stat.getDenominator(), is(2));
		assertThat(stat.getPercentage(), is(1.0));
		
		this.winServiceGame(match, player2);
		
		this.winServiceGame(match, player1);
		stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		assertThat(stat.getNumerator(), is(3));
		assertThat(stat.getDenominator(), is(3));
		assertThat(stat.getPercentage(), is(1.0));
		
		this.winServiceGame(match, player2);
		
		this.winServiceGame(match, player1);
		stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		assertThat(stat.getNumerator(), is(4));
		assertThat(stat.getDenominator(), is(4));
		assertThat(stat.getPercentage(), is(1.0));
		
		this.winServiceGame(match, player2);
		
		this.loseServiceGame(match, player1);
		stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		assertThat(stat.getNumerator(), is(4));
		assertThat(stat.getDenominator(), is(5));
		assertThat(stat.getPercentage(), is(0.8));
		
		this.loseServiceGame(match, player2);
		
		this.winServiceGame(match, player1);
		stat = (SimplePercentageStatistic) matchProcessor.getStatistics().get(0);
		assertThat(stat.getNumerator(), is(5));
		assertThat(stat.getDenominator(), is(6));
		assertEquals(0.833, stat.getPercentage(), 0.001);
		
		this.loseServiceGame(match, player2);
	}
	
	@Test
	public void t2_commonProcessor() {
		MatchProcessor matchProcessor = this.createNewMatch();
		CommonStatisticProcessor statProcessor = matchProcessor.getStatisticProcessor().toCommonStatisticProcessor();
		Match match = matchProcessor.getMatch();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.winServiceGame(match, player1);
		SimplePercentageStatistic stat = statProcessor.getServiceGamesWon(player1);
		assertThat(stat.getNumerator(), is(1));
		assertThat(stat.getDenominator(), is(1));
		assertThat(stat.getPercentage(), is(1.0));
		
		this.winServiceGame(match, player2);
		
		this.winServiceGame(match, player1);
		stat = statProcessor.getServiceGamesWon(player1);
		assertThat(stat.getNumerator(), is(2));
		assertThat(stat.getDenominator(), is(2));
		assertThat(stat.getPercentage(), is(1.0));
		
		this.winServiceGame(match, player2);
		
		this.winServiceGame(match, player1);
		stat = statProcessor.getServiceGamesWon(player1);
		assertThat(stat.getNumerator(), is(3));
		assertThat(stat.getDenominator(), is(3));
		assertThat(stat.getPercentage(), is(1.0));
		
		this.winServiceGame(match, player2);
		
		this.winServiceGame(match, player1);
		stat = statProcessor.getServiceGamesWon(player1);
		assertThat(stat.getNumerator(), is(4));
		assertThat(stat.getDenominator(), is(4));
		assertThat(stat.getPercentage(), is(1.0));
		
		this.winServiceGame(match, player2);
		
		this.loseServiceGame(match, player1);
		stat = statProcessor.getServiceGamesWon(player1);
		assertThat(stat.getNumerator(), is(4));
		assertThat(stat.getDenominator(), is(5));
		assertThat(stat.getPercentage(), is(0.8));
		
		this.loseServiceGame(match, player2);
		
		this.winServiceGame(match, player1);
		stat = statProcessor.getServiceGamesWon(player1);
		assertThat(stat.getNumerator(), is(5));
		assertThat(stat.getDenominator(), is(6));
		assertEquals(0.833, stat.getPercentage(), 0.001);
		
		this.loseServiceGame(match, player2);
	}
}
