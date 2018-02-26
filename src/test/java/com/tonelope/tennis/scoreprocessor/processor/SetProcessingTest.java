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

import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Player;
import com.tonelope.tennis.scoreprocessor.model.Status;

/**
 * 
 * @author Tony Lopez
 *
 */
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
