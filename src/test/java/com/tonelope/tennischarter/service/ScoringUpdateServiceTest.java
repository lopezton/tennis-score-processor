package com.tonelope.tennischarter.service;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.tonelope.tennischarter.dao.MatchRepository;
import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.MatchRules;
import com.tonelope.tennischarter.model.Player;
import com.tonelope.tennischarter.model.PlayerConfig;
import com.tonelope.tennischarter.model.Status;
import com.tonelope.tennischarter.model.Stroke;
import com.tonelope.tennischarter.model.StrokeType;
import com.tonelope.tennischarter.processor.MatchFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScoringUpdateServiceTest {

	@Autowired
	private MatchRepository matchRepository;
	
	@Autowired
	private ScoringUpdateService scoringUpdateService;
	
	@Autowired
	private MatchFactory matchFactory;
	
	private Match createNewMatch() {
		return this.createNewMatch(null);
	}
	
	private Match createNewMatch(MatchRules matchRules) {
		PlayerConfig playerConfig = new PlayerConfig();
		playerConfig.setPlayers(new ArrayList<Player>());
		playerConfig.getPlayers().add(new Player());
		playerConfig.getPlayers().add(new Player());
		playerConfig.getPlayers().get(0).setFirstName("Roger");
		playerConfig.getPlayers().get(0).setLastName("Federer");
		playerConfig.getPlayers().get(1).setFirstName("Rafael");
		playerConfig.getPlayers().get(1).setLastName("Nadal");
		playerConfig.setStartingServer(playerConfig.getPlayers().get(0));
		playerConfig.setStartingReceiver(playerConfig.getPlayers().get(1));
		
		return this.matchFactory.create(Optional.ofNullable(matchRules).orElse(new MatchRules()), playerConfig);
	}
	
	@Test
	public void t1_winGame_fourServiceWinners() {
		Match match = this.matchRepository.save(this.createNewMatch());
		String matchId = match.getId();
		Player player1 = match.getPlayers().get(0);
		
		this.winServiceGame(matchId, player1);
		
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
	
	@Test
	public void t4_winSet_tiebreak() {
		Match match = this.matchRepository.save(this.createNewMatch());
		String matchId = match.getId();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		
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
		
		Assert.assertEquals(2, match.getSets().size());
		Assert.assertEquals(13, match.getSets().get(0).getGames().size());
		for(int i = 0; i < 12; i++) {
			Assert.assertEquals(4, match.getSets().get(0).getGames().get(i).getPoints().size());
			Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getGames().get(i).getStatus());
		}
		// TODO validate tiebreak
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
	}
	
	@Test
	public void t5_winSet() {
		Match match = this.matchRepository.save(this.createNewMatch());
		String matchId = match.getId();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		
		Assert.assertEquals(2, match.getSets().size());
		Assert.assertEquals(6, match.getSets().get(0).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
	}
	
	@Test
	public void t6_winMatch() {
		Match match = this.matchRepository.save(this.createNewMatch());
		String matchId = match.getId();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		
		Assert.assertEquals(2, match.getSets().size());
		Assert.assertEquals(6, match.getSets().get(0).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
		Assert.assertEquals(6, match.getSets().get(1).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(1).getStatus());
		Assert.assertEquals(Status.COMPLETE, match.getStatus());
	}
	
	@Test
	public void t7_winMatch_finalSet() {
		Match match = this.matchRepository.save(this.createNewMatch());
		String matchId = match.getId();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		
		this.loseServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.loseServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.loseServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		
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
	public void t8_winMatch_finalSetTiebreak() {
		Match match = this.matchRepository.save(this.createNewMatch());
		String matchId = match.getId();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		
		this.loseServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.loseServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.loseServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		
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
		// TODO Tiebreak validation
		Assert.assertEquals(Status.COMPLETE, match.getStatus());
	}
	
	@Test
	public void t9_winMatch_finalSetWinByTwo() {
		Match match = this.matchRepository.save(this.createNewMatch());
		match.getMatchRules().setFinalSetTiebreakDisabled(true);
		String matchId = match.getId();
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		
		this.loseServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.loseServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.loseServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.winServiceGame(matchId, player2);
		this.winServiceGame(matchId, player1);
		this.loseServiceGame(matchId, player2);
		
		Assert.assertEquals(3, match.getSets().size());
		Assert.assertEquals(6, match.getSets().get(0).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
		Assert.assertEquals(6, match.getSets().get(1).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(1).getStatus());
		Assert.assertEquals(14, match.getSets().get(2).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(2).getStatus());
		// TODO Tiebreak validation
		Assert.assertEquals(Status.COMPLETE, match.getStatus());
	}
	
	private void winServiceGame(String matchId, Player player) {
		for(int i = 0; i < 4; i++) {
			this.scoringUpdateService.updateMatch(matchId, new Stroke(player, StrokeType.FIRST_SERVE, false, true));
		}
	}
	
	private void loseServiceGame(String matchId, Player player) {
		for(int i = 0; i < 4; i++) {
			this.scoringUpdateService.updateMatch(matchId, new Stroke(player, StrokeType.FIRST_SERVE, true, false));
			this.scoringUpdateService.updateMatch(matchId, new Stroke(player, StrokeType.SECOND_SERVE, true, false));
		}
	}
}
