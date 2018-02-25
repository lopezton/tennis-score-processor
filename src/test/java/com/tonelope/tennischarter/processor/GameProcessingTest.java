package com.tonelope.tennischarter.processor;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.test.context.junit4.SpringRunner;

import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.Player;
import com.tonelope.tennischarter.model.Status;
import com.tonelope.tennischarter.model.Stroke;
import com.tonelope.tennischarter.model.StrokeType;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GameProcessingTest extends AbstractProcessingTests {

	@Test
	public void t1_winGame_fourServiceWinners() {
		Match match = this.matchRepository.save(this.createNewMatch());
		Player player1 = match.getPlayers().get(0);
		
		this.winServiceGame(match, player1);
		
		Assert.assertEquals(1, match.getSets().size());
		Assert.assertEquals(2, match.getSets().get(0).getGames().size());
		Assert.assertEquals(4, match.getSets().get(0).getGames().get(0).getPoints().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getGames().get(0).getStatus());
	}
	
	@Test
	public void t2_winGame_deuce() {
		Match match = this.matchRepository.save(this.createNewMatch());
		String matchId = match.getId();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		
		Assert.assertEquals(1, match.getSets().size());
		Assert.assertEquals(2, match.getSets().get(0).getGames().size());
		Assert.assertEquals(8, match.getSets().get(0).getGames().get(0).getPoints().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getGames().get(0).getStatus());
	}
	
	@Test
	public void t3_winGame_noAdScoring() {
		Match match = this.matchRepository.save(this.createNewMatch());
		match.getMatchRules().setNoAdScoring(true);
		String matchId = match.getId();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FOREHAND, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		
		Assert.assertEquals(1, match.getSets().size());
		Assert.assertEquals(2, match.getSets().get(0).getGames().size());
		Assert.assertEquals(7, match.getSets().get(0).getGames().get(0).getPoints().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getGames().get(0).getStatus());
	}
}
