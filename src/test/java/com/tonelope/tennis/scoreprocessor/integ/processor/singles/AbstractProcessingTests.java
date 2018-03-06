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

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Assert;

import com.tonelope.tennis.scoreprocessor.model.Game;
import com.tonelope.tennis.scoreprocessor.model.GameScore;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.MatchRules;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.PlayerConfig;
import com.tonelope.tennis.scoreprocessor.model.PointValue;
import com.tonelope.tennis.scoreprocessor.model.Set;
import com.tonelope.tennis.scoreprocessor.model.Stroke;
import com.tonelope.tennis.scoreprocessor.model.StrokeType;
import com.tonelope.tennis.scoreprocessor.model.TiebreakGame;
import com.tonelope.tennis.scoreprocessor.model.TiebreakScore;
import com.tonelope.tennis.scoreprocessor.processor.DefaultMatchFactory;
import com.tonelope.tennis.scoreprocessor.processor.MatchFactory;
import com.tonelope.tennis.scoreprocessor.processor.MatchProcessor;
import com.tonelope.tennis.scoreprocessor.processor.SinglesMatchProcessor;

/**
 * 
 * @author Tony Lopez
 *
 */
public class AbstractProcessingTests {
	
	protected MatchFactory matchFactory = new DefaultMatchFactory();
	protected MatchProcessor matchProcessor = new SinglesMatchProcessor();
	
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
		if (!this.isPlayerCurrentServer(match, player)) {
			Assert.fail("Attempted to serve ace as " + player + ", but this player is not the current server.");
		}
		
		this.matchProcessor.update(match, new Stroke(player, StrokeType.FIRST_SERVE, false, true));
	}
	
	private boolean isPlayerCurrentServer(Match match, Player player) {
		Player currentServer = match.getStartingServer();
		if (!match.isNotStarted()) {
			if (!(match.getCurrentSet().getCurrentGame() instanceof TiebreakGame)) {
				currentServer = match.getCurrentSet().getCurrentGame().getCurrentPoint().getServer();
			} else {
				// TODO
				return true;
			}
		}
		if (!currentServer.equals(player)) {
			return false;
		}
		return true;
	}
	
	protected void loseServiceGame(Match match, Player player) {
		for(int i = 0; i < 4; i++) {
			this.hitDoubleFault(match, player);
		}
	}
	
	protected void hitDoubleFault(Match match, Player player) {
		this.missFirstServe(match, player);
		this.missSecondServe(match, player);
	}
	
	protected void missFirstServe(Match match, Player player) {
		if (!this.isPlayerCurrentServer(match, player)) {
			Assert.fail("Attempted to miss first serve as " + player + ", but this player is not the current server.");
		}
		
		this.matchProcessor.update(match, new Stroke(player, StrokeType.FIRST_SERVE, true, false));
	}
	
	protected void missSecondServe(Match match, Player player) {
		if (!this.isPlayerCurrentServer(match, player)) {
			Assert.fail("Attempted to miss second serve as " + player + ", but this player is not the current server.");
		}
		
		this.matchProcessor.update(match, new Stroke(player, StrokeType.SECOND_SERVE, true, false));
	}
	
	protected void validateGameScore(Game game, String p1, String p2) {
		this.validateGameScore(game, PointValue.getByValue(p1), PointValue.getByValue(p2));
	}
	
	protected void validateGameScore(Game game, PointValue p1, PointValue p2) {
		Assert.assertEquals(p1, ((GameScore) game.getScore()).getServerScore());
		Assert.assertEquals(p2, ((GameScore) game.getScore()).getReceiverScore());
		Assert.assertEquals(p1.getValue() + GameScore.SEPARATOR + p2.getValue(), game.getScore().toString());
	}
	
	protected void validateSetScore(Set set, int p1, int p2) {
		Assert.assertEquals(p1, set.getScore().getStartingServerScore());
		Assert.assertEquals(p2, set.getScore().getStartingReceiverScore());
	}
	
	protected void validateTiebreakScore(TiebreakGame tiebreak, int p1, int p2) {
		Assert.assertEquals(p1, ((TiebreakScore) tiebreak.getScore()).getServerScore());
		Assert.assertEquals(p2, ((TiebreakScore) tiebreak.getScore()).getReceiverScore());
		Assert.assertEquals("(" + p1 + "-" + p2 + ")", tiebreak.getScore().toString());
	}
	
	protected void winServiceGame(Match match, Player player) {
		for(int i = 0; i < 4; i++) {
			this.hitFirstServeAce(match, player);
		}
	}
	
	protected void winSet(Match match, Player player) {
		Player losingPlayer = match.getPlayers().stream().filter(p -> !p.equals(player)).collect(Collectors.toList()).get(0);
		
		Game currentGame = match.getCurrentSet().getCurrentGame();
		
		if (!currentGame.getServer().equals(player)) {
			this.loseServiceGame(match, losingPlayer);
		}
		
		this.winServiceGame(match, player);
		this.loseServiceGame(match, losingPlayer);
		this.winServiceGame(match, player);
		this.loseServiceGame(match, losingPlayer);
		this.winServiceGame(match, player);
		
		if (currentGame.getServer().equals(player)) {
			this.loseServiceGame(match, losingPlayer);
		}
	}
	
	protected void winTiebreak(Match match, Player player) {
		Player losingPlayer = match.getPlayers().stream().filter(p -> !p.equals(player)).collect(Collectors.toList()).get(0);
		
		Game currentGame = match.getCurrentSet().getCurrentGame();
		if (!(currentGame instanceof TiebreakGame)) {
			Assert.fail("Attempted to win tiebreak, but current game is not a tiebreak. Current Game = " + currentGame);
		}
		
		if (!currentGame.getServer().equals(player)) {
			this.hitDoubleFault(match, losingPlayer);
			this.hitFirstServeAce(match, player);
		}
		
		this.hitFirstServeAce(match, player);
		
		this.hitDoubleFault(match, losingPlayer);
		
		this.hitDoubleFault(match, losingPlayer);
		
		this.hitFirstServeAce(match, player);
		
		this.hitFirstServeAce(match, player);
		
		if (currentGame.getServer().equals(player)) {
			this.hitDoubleFault(match, losingPlayer);
			this.hitDoubleFault(match, losingPlayer);
		}
	}
}
