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
public class MatchProcessingTest extends AbstractProcessingTests {

	@Test
	public void t1_winMatch_perfect() {
		Match match = this.matchRepository.save(this.createNewMatch());
		Player player1 = match.getPlayers().get(0);
		
		this.winSet(match, player1);
		this.winSet(match, player1);
		
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
		
		this.winSet(match, player1);
		this.winSet(match, player2);
		this.winSet(match, player1);
		
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
		Player player1 = match.getPlayers().get(0);
		Player player2 = match.getPlayers().get(1);
		
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
