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
package com.tonelope.tennis.scoreprocessor.processor;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.junit4.SpringRunner;

import com.tonelope.tennis.scoreprocessor.model.Game;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.PointValue;
import com.tonelope.tennis.scoreprocessor.model.Set;
import com.tonelope.tennis.scoreprocessor.model.Status;
import com.tonelope.tennis.scoreprocessor.model.Stroke;
import com.tonelope.tennis.scoreprocessor.model.StrokeType;
import com.tonelope.tennis.scoreprocessor.model.TiebreakGame;
import com.tonelope.tennis.scoreprocessor.model.TiebreakScore;

/**
 * 
 * @author Tony Lopez
 *
 */
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScoreProcessingTest extends AbstractProcessingTests {

	@Test
	public void t1_game_deuce() {
		Match match = this.matchRepository.save(this.createNewMatch());
		String matchId = match.getId();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		Game currentGame = match.getCurrentSet().getCurrentGame();
		
		this.validateGameScore(currentGame, PointValue.LOVE, PointValue.LOVE);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.validateGameScore(currentGame, PointValue.FIFTEEN, PointValue.LOVE);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.validateGameScore(currentGame, PointValue.THIRTY, PointValue.LOVE);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.LOVE);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.LOVE);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.FIFTEEN);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.FIFTEEN);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.THIRTY);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.THIRTY);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.FORTY);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.FORTY);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.ADVANTAGE);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.FORTY);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.validateGameScore(currentGame, PointValue.ADVANTAGE, PointValue.FORTY);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.validateGameScore(currentGame, PointValue.ADVANTAGE, PointValue.FORTY);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.FORTY);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.FORTY);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.ADVANTAGE);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.ADVANTAGE);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.GAME);
		
		Assert.assertEquals(1, match.getSets().size());
		Assert.assertEquals(2, match.getSets().get(0).getGames().size());
		Assert.assertEquals(12, match.getSets().get(0).getGames().get(0).getPoints().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getGames().get(0).getStatus());
	}
	
	@Test
	public void t2_game_noAd() {
		Match match = this.matchRepository.save(this.createNewMatch());
		match.getMatchRules().setNoAdScoring(true);
		String matchId = match.getId();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		Game currentGame = match.getCurrentSet().getCurrentGame();
		
		this.validateGameScore(currentGame, PointValue.LOVE, PointValue.LOVE);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.validateGameScore(currentGame, PointValue.FIFTEEN, PointValue.LOVE);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.validateGameScore(currentGame, PointValue.THIRTY, PointValue.LOVE);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.LOVE);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.LOVE);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.FIFTEEN);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.FIFTEEN);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.THIRTY);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.THIRTY);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.validateGameScore(currentGame, PointValue.FORTY, PointValue.FORTY);
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.validateGameScore(currentGame, PointValue.GAME, PointValue.FORTY);
		
		Assert.assertEquals(1, match.getSets().size());
		Assert.assertEquals(2, match.getSets().get(0).getGames().size());
		Assert.assertEquals(8, match.getSets().get(0).getGames().get(0).getPoints().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getGames().get(0).getStatus());
	}
	
	@Test
	public void t3_set() {
		Match match = this.matchRepository.save(this.createNewMatch());
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		Set currentSet = match.getCurrentSet();
		
		this.validateSetScore(currentSet, 0, 0);
		
		for(int i = 1; i <= 4; i++) {
			this.winServiceGame(match, player1);
			this.validateSetScore(currentSet, i, i - 1);
			this.winServiceGame(match, player2);
			this.validateSetScore(currentSet, i, i);
		}
		
		this.winServiceGame(match, player1);
		this.validateSetScore(currentSet, 5, 4);
		this.loseServiceGame(match, player2);
		this.validateSetScore(currentSet, 6, 4);
		
		Assert.assertEquals(2, match.getSets().size());
		Assert.assertEquals(10, match.getSets().get(0).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
	}
	
	@Test
	public void t4_set_tiebreak() {
		Match match = this.matchRepository.save(this.createNewMatch());
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		Set currentSet = match.getCurrentSet();
		
		this.validateSetScore(currentSet, 0, 0);
		
		for(int i = 1; i <= 6; i++) {
			this.winServiceGame(match, player1);
			this.validateSetScore(currentSet, i, i - 1);
			this.winServiceGame(match, player2);
			this.validateSetScore(currentSet, i, i);
		}

		this.winServiceGame(match, player1);
		this.validateSetScore(currentSet, 7, 6);
		
		Assert.assertEquals(2, match.getSets().size());
		Assert.assertEquals(13, match.getSets().get(0).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
	}
	
	@Test
	public void t5_set_longTiebreak() {
		Match match = this.matchRepository.save(this.createNewMatch());
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		Set currentSet = match.getCurrentSet();
		
		this.validateSetScore(currentSet, 0, 0);
		
		for(int i = 1; i <= 6; i++) {
			this.winServiceGame(match, player1);
			this.validateSetScore(currentSet, i, i - 1);
			this.winServiceGame(match, player2);
			this.validateSetScore(currentSet, i, i);
		}

		TiebreakGame tiebreak = (TiebreakGame) currentSet.getCurrentGame();
		
		this.hitFirstServeAce(match, player1);
		this.validateTiebreakScore(tiebreak, 1, 0);
		this.hitFirstServeAce(match, player2);
		this.validateTiebreakScore(tiebreak, 1, 1);
		this.hitFirstServeAce(match, player2);
		this.validateTiebreakScore(tiebreak, 1, 2);
		this.hitFirstServeAce(match, player1);
		this.validateTiebreakScore(tiebreak, 2, 2);
		this.hitFirstServeAce(match, player1);
		this.validateTiebreakScore(tiebreak, 3, 2);
		this.hitFirstServeAce(match, player2);
		this.validateTiebreakScore(tiebreak, 3, 3);
		this.hitFirstServeAce(match, player2);
		this.validateTiebreakScore(tiebreak, 3, 4);
		this.hitFirstServeAce(match, player1);
		this.validateTiebreakScore(tiebreak, 4, 4);
		this.hitFirstServeAce(match, player1);
		this.validateTiebreakScore(tiebreak, 5, 4);
		this.hitFirstServeAce(match, player2);
		this.validateTiebreakScore(tiebreak, 5, 5);
		this.hitFirstServeAce(match, player2);
		this.validateTiebreakScore(tiebreak, 5, 6);
		this.hitFirstServeAce(match, player1);
		this.validateTiebreakScore(tiebreak, 6, 6);
		this.hitFirstServeAce(match, player1);
		this.validateTiebreakScore(tiebreak, 7, 6);
		this.hitFirstServeAce(match, player2);
		this.validateTiebreakScore(tiebreak, 7, 7);
		this.hitFirstServeAce(match, player2);
		this.validateTiebreakScore(tiebreak, 7, 8);
		this.hitFirstServeAce(match, player2);
		this.validateTiebreakScore(tiebreak, 7, 9);
		
		Assert.assertEquals(2, match.getSets().size());
		Assert.assertEquals(13, match.getSets().get(0).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
	}

	@Test
	public void t6_match() {
		Match match = this.matchRepository.save(this.createNewMatch());
		Player player1 = match.getPlayers().get(0);
		
		Assert.assertEquals(0, match.getScore().getSetScores().size());
		Assert.assertEquals("", match.getScore().toString());
		this.winSet(match, player1);
		this.winSet(match, player1);
		Assert.assertEquals(2, match.getScore().getSetScores().size());
		Assert.assertEquals("6-0, 6-0", match.getScore().toString());
		
		Assert.assertEquals(2, match.getSets().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
	}

	@Test
	public void t7_match_finalset_tiebreak() {
		Match match = this.matchRepository.save(this.createNewMatch());
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		Assert.assertEquals(0, match.getScore().getSetScores().size());
		Assert.assertEquals("", match.getScore().toString());
		this.winSet(match, player1);
		this.winSet(match, player2);
		
		this.winServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.winServiceGame(match, player2);
		
		// Tiebreak
		this.winTiebreak(match, player1);
		
		Assert.assertEquals(3, match.getScore().getSetScores().size());
		Assert.assertEquals("6-0, 6-0, 7-6(0)", match.getScore().toString());
		
		Assert.assertEquals(3, match.getSets().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
	}
	
	@Test
	public void t8_match_finalset_winByTwo() {
		Match match = this.matchRepository.save(this.createNewMatch());
		match.getMatchRules().setFinalSetTiebreakDisabled(true);
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		Assert.assertEquals(0, match.getScore().getSetScores().size());
		Assert.assertEquals("", match.getScore().toString());
		this.winSet(match, player1);
		this.winSet(match, player2);
		
		this.winServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		
		Assert.assertEquals(3, match.getScore().getSetScores().size());
		Assert.assertEquals("6-0, 6-0, 8-6", match.getScore().toString());
		
		Assert.assertEquals(2, match.getSets().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
	}
}
