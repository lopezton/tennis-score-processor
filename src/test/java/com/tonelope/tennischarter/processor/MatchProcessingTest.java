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
public class MatchProcessingTest extends AbstractProcessingTests {

	@Test
	public void t1_winMatch_perfect() {
		Match match = this.matchRepository.save(this.createNewMatch());
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		
		Assert.assertEquals(2, match.getSets().size());
		Assert.assertEquals(6, match.getSets().get(0).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
		Assert.assertEquals(6, match.getSets().get(1).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(1).getStatus());
		Assert.assertEquals(Status.COMPLETE, match.getStatus());
	}
	
	@Test
	public void t2_winMatch_fromFinalSet() {
		Match match = this.matchRepository.save(this.createNewMatch());
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		
		this.loseServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.loseServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.loseServiceGame(match, player1);
		this.winServiceGame(match, player2);
		
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		
		Assert.assertEquals(3, match.getSets().size());
		Assert.assertEquals(6, match.getSets().get(0).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
		Assert.assertEquals(6, match.getSets().get(1).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(1).getStatus());
		Assert.assertEquals(6, match.getSets().get(2).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(2).getStatus());
		Assert.assertEquals(Status.COMPLETE, match.getStatus());
	}
	
	@Test
	public void t3_winMatch_fromFinalSet_tiebreak() {
		Match match = this.matchRepository.save(this.createNewMatch());
		String matchId = match.getId();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		
		this.loseServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.loseServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.loseServiceGame(match, player1);
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
		this.winServiceGame(match, player2);
		
		// Tiebreak
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FIRST_SERVE, true, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.SECOND_SERVE, true, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FIRST_SERVE, true, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.SECOND_SERVE, true, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player1, StrokeType.FIRST_SERVE, false, true));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FIRST_SERVE, true, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.SECOND_SERVE, true, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.FIRST_SERVE, true, false));
		this.scoringUpdateService.updateMatch(matchId, new Stroke(player2, StrokeType.SECOND_SERVE, true, false));
		
		Assert.assertEquals(3, match.getSets().size());
		Assert.assertEquals(6, match.getSets().get(0).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
		Assert.assertEquals(6, match.getSets().get(1).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(1).getStatus());
		Assert.assertEquals(13, match.getSets().get(2).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(2).getStatus());
		Assert.assertEquals(Status.COMPLETE, match.getStatus());
	}
	
	@Test
	public void t4_winMatch_fromFinalSet_winByTwo() {
		Match match = this.matchRepository.save(this.createNewMatch());
		match.getMatchRules().setFinalSetTiebreakDisabled(true);
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		
		this.loseServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.loseServiceGame(match, player1);
		this.winServiceGame(match, player2);
		this.loseServiceGame(match, player1);
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
		this.winServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		
		Assert.assertEquals(3, match.getSets().size());
		Assert.assertEquals(6, match.getSets().get(0).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
		Assert.assertEquals(6, match.getSets().get(1).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(1).getStatus());
		Assert.assertEquals(14, match.getSets().get(2).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(2).getStatus());
		Assert.assertEquals(Status.COMPLETE, match.getStatus());
	}
}
