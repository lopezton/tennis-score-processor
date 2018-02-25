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

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SetProcessingTest extends AbstractProcessingTests {

	@Test
	public void t1_winSet() {
		Match match = this.matchRepository.save(this.createNewMatch());
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		this.winServiceGame(match, player1);
		this.loseServiceGame(match, player2);
		
		Assert.assertEquals(2, match.getSets().size());
		Assert.assertEquals(6, match.getSets().get(0).getGames().size());
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
	}
	
	@Test
	public void t2_winSet_tiebreak() {
		Match match = this.matchRepository.save(this.createNewMatch());
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
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
		
		Assert.assertEquals(2, match.getSets().size());
		Assert.assertEquals(13, match.getSets().get(0).getGames().size());
		for(int i = 0; i < 12; i++) {
			Assert.assertEquals(4, match.getSets().get(0).getGames().get(i).getPoints().size());
			Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getGames().get(i).getStatus());
		}
		Assert.assertEquals(Status.COMPLETE, match.getSets().get(0).getStatus());
	}
}
