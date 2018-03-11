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
package com.tonelope.tennis.scoreprocessor.integ.processor.singles;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.tonelope.tennis.scoreprocessor.integ.processor.AbstractProcessingTests;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.Status;
import com.tonelope.tennis.scoreprocessor.model.Stroke;
import com.tonelope.tennis.scoreprocessor.model.StrokeType;
import com.tonelope.tennis.scoreprocessor.processor.MatchProcessor;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GameProcessingTest extends AbstractProcessingTests {

	@Test
	public void t1_winGame_fourServiceWinners() {
		MatchProcessor matchProcessor = this.createNewMatch();
		Match match = matchProcessor.getMatch();
		Player player1 = match.getPlayers().get(0);
		
		this.winServiceGame(match, player1);
		
		Assert.assertEquals(1, match.getSets().size());
		Assert.assertEquals(2, match.getSets().get(0).getGames().size());
		Assert.assertEquals(4, match.getSets().get(0).getGames().get(0).getPoints().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getGames().get(0).getStatus());
	}
	
	@Test
	public void t2_winGame_deuce() {
		MatchProcessor matchProcessor = this.createNewMatch();
		Match match = matchProcessor.getMatch();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		
		Assert.assertEquals(1, match.getSets().size());
		Assert.assertEquals(2, match.getSets().get(0).getGames().size());
		Assert.assertEquals(8, match.getSets().get(0).getGames().get(0).getPoints().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getGames().get(0).getStatus());
	}
	
	@Test
	public void t3_winGame_noAdScoring_serverWins() {
		MatchProcessor matchProcessor = this.createNewMatch();
		Match match = matchProcessor.getMatch();
		match.getMatchRules().setNoAdScoring(true);
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		
		Assert.assertEquals(1, match.getSets().size());
		Assert.assertEquals(2, match.getSets().get(0).getGames().size());
		Assert.assertEquals(player1, match.getSets().get(0).getGames().get(0).getWinningPlayer());
		Assert.assertEquals(7, match.getSets().get(0).getGames().get(0).getPoints().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getGames().get(0).getStatus());
	}
	
	@Test
	public void t4_winGame_noAdScoring_receiverWins() {
		MatchProcessor matchProcessor = this.createNewMatch();
		Match match = matchProcessor.getMatch();
		match.getMatchRules().setNoAdScoring(true);
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.matchProcessor.update(new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.matchProcessor.update(new Stroke(player2, StrokeType.FOREHAND, false, true));
		
		Assert.assertEquals(1, match.getSets().size());
		Assert.assertEquals(2, match.getSets().get(0).getGames().size());
		Assert.assertEquals(player2, match.getSets().get(0).getGames().get(0).getWinningPlayer());
		Assert.assertEquals(7, match.getSets().get(0).getGames().get(0).getPoints().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getGames().get(0).getStatus());
	}
}
