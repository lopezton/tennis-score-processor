package com.tonelope.tennischarter.processor;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tonelope.tennischarter.dao.MatchRepository;
import com.tonelope.tennischarter.model.Game;
import com.tonelope.tennischarter.model.Match;
import com.tonelope.tennischarter.model.MatchRules;
import com.tonelope.tennischarter.model.Player;
import com.tonelope.tennischarter.model.PlayerConfig;
import com.tonelope.tennischarter.model.Set;
import com.tonelope.tennischarter.model.Stroke;
import com.tonelope.tennischarter.model.StrokeType;
import com.tonelope.tennischarter.model.TiebreakGame;
import com.tonelope.tennischarter.service.ScoringUpdateService;
import com.tonelope.tennischarter.utils.ListUtils;

@SpringBootTest
public class AbstractProcessingTests {

	@Autowired
	protected MatchRepository matchRepository;
	
	@Autowired
	protected ScoringUpdateService scoringUpdateService;
	
	@Autowired
	protected MatchFactory matchFactory;
	
	protected Match createNewMatch() {
		return this.createNewMatch(null);
	}
	
	protected Match createNewMatch(MatchRules matchRules) {
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
	
	protected void hitFirstServeAce(Match match, Player player) {
		this.scoringUpdateService.updateMatch(match.getId(), new Stroke(player, StrokeType.FIRST_SERVE, false, true));
	}
	
	protected void missFirstServe(Match match, Player player) {
		this.scoringUpdateService.updateMatch(match.getId(), new Stroke(player, StrokeType.FIRST_SERVE, true, false));
	}
	
	protected void missSecondServe(Match match, Player player) {
		this.scoringUpdateService.updateMatch(match.getId(), new Stroke(player, StrokeType.SECOND_SERVE, true, false));
	}
	
	protected void winServiceGame(Match match, Player player) {
		if (!this.isPlayerCurrentServer(match, player)) {
			Assert.fail("Attempted to win service game as " + player + ", but this player is not the current server.");
		}
		
		for(int i = 0; i < 4; i++) {
			this.hitFirstServeAce(match, player);
		}
	}
	
	protected void loseServiceGame(Match match, Player player) {
		if (!this.isPlayerCurrentServer(match, player)) {
			Assert.fail("Attempted to lose service game as " + player + ", but this player is not the current server.");
		}
		
		for(int i = 0; i < 4; i++) {
			this.missFirstServe(match, player);
			this.missSecondServe(match, player);
		}
	}
	
	private boolean isPlayerCurrentServer(Match match, Player player) {
		Player currentServer = match.getStartingServer();
		Set currentSet = ListUtils.getLast(match.getSets());
		if (null != currentSet) {
			Game currentGame = ListUtils.getLast(currentSet.getGames());
			currentServer = currentGame.getServer();
		}
		if (!currentServer.equals(player)) {
			return false;
		}
		return true;
	}
	
	protected void winTiebreak(Match match, Player player) {
		Player winningPlayer = match.getPlayers().stream().filter(p -> p.equals(player)).collect(Collectors.toList()).get(0);
		Player losingPlayer = match.getPlayers().stream().filter(p -> !p.equals(player)).collect(Collectors.toList()).get(0);
		
		Set currentSet = ListUtils.getLast(match.getSets());
		Game currentGame = ListUtils.getLast(currentSet.getGames());
		if (!(currentGame instanceof TiebreakGame)) {
			Assert.fail("Attempted to win tiebreak, but current game is not a tiebreak. Current Game = " + currentGame);
		}
		
		if (!currentGame.getServer().equals(player)) {
			this.missFirstServe(match, losingPlayer);
			this.missSecondServe(match, losingPlayer);
			
			this.hitFirstServeAce(match, winningPlayer);
		}
		
		this.hitFirstServeAce(match, winningPlayer);
		
		this.missFirstServe(match, losingPlayer);
		this.missSecondServe(match, losingPlayer);
		
		this.missFirstServe(match, losingPlayer);
		this.missSecondServe(match, losingPlayer);
		
		this.hitFirstServeAce(match, winningPlayer);
		
		this.hitFirstServeAce(match, winningPlayer);
		
		if (currentGame.getServer().equals(player)) {
			this.missFirstServe(match, losingPlayer);
			this.missSecondServe(match, losingPlayer);
			
			this.missFirstServe(match, losingPlayer);
			this.missSecondServe(match, losingPlayer);
		}
	}
}
