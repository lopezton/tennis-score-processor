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
import static org.junit.Assert.assertThat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tonelope.tennis.scoreprocessor.integ.processor.AbstractProcessingTests;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.processor.MatchProcessor;
import com.tonelope.tennis.scoreprocessor.processor.statistics.CommonStatisticProcessor;
import com.tonelope.tennis.scoreprocessor.processor.statistics.CountStatistic;
import com.tonelope.tennis.scoreprocessor.processor.statistics.extension.serve.DoubleFaultStatisticInstruction;

/**
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DoubleFaultStatisticInstructionTest extends AbstractProcessingTests {

	@Test
	public void t1_defaultScenario() {
		MatchProcessor matchProcessor = this.createNewMatch();
		Match match = matchProcessor.getMatch();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		matchProcessor.addStatisticInstruction(new DoubleFaultStatisticInstruction(player1));
		assertThat(matchProcessor.getStatisticProcessor().getInstructions().size(), is(1));
		
		for (int i = 1; i <=4; i++) {
			this.hitDoubleFault(match, player1);
			CountStatistic stat = (CountStatistic) matchProcessor.getStatistics().get(0);
			assertThat(stat.getCount(), is(i));
		}
		
		for (int i = 1; i <=4; i++) {
			this.hitFirstServeAce(match, player2);
			CountStatistic stat = (CountStatistic) matchProcessor.getStatistics().get(0);
			assertThat(stat.getCount(), is(4));
		}
		
		this.hitDoubleFault(match, player1);
		CountStatistic stat = (CountStatistic) matchProcessor.getStatistics().get(0);
		assertThat(stat.getCount(), is(5));
	}
	
	@Test
	public void t2_commonStatisticProcessor() {
		MatchProcessor matchProcessor = this.createNewMatch();
		CommonStatisticProcessor statProcessor = matchProcessor.getStatisticProcessor().toCommonStatisticProcessor();
		Match match = matchProcessor.getMatch();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		for (int i = 1; i <=4; i++) {
			this.hitDoubleFault(match, player1);
			assertThat(statProcessor.getNumberOfDoubleFaults(player1).getCount(), is(i));
		}
		
		for (int i = 1; i <=4; i++) {
			this.hitFirstServeAce(match, player2);
			assertThat(statProcessor.getNumberOfDoubleFaults(player1).getCount(), is(4));
		}
		
		this.hitDoubleFault(match, player1);
		assertThat(statProcessor.getNumberOfDoubleFaults(player1).getCount(), is(5));
	}
}
