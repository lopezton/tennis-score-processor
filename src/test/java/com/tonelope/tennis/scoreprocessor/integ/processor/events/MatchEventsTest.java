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
package com.tonelope.tennis.scoreprocessor.integ.processor.events;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import com.tonelope.tennis.scoreprocessor.integ.processor.AbstractProcessingTests;
import com.tonelope.tennis.scoreprocessor.model.FrameworkException;
import com.tonelope.tennis.scoreprocessor.model.MatchEventType;
import com.tonelope.tennis.scoreprocessor.processor.MatchProcessor;

/**
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MatchEventsTest extends AbstractProcessingTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void t1_eventExecution() {
		MatchProcessor processor = this.createNewMatch();
		
		AtomicInteger pointEventsExecuted = new AtomicInteger();
		AtomicInteger gameEventsExecuted = new AtomicInteger();
		AtomicInteger setEventsExecuted = new AtomicInteger();
		AtomicInteger matchEventsExecuted = new AtomicInteger();
		
		// Register events
		this.matchProcessor.registerEvent(MatchEventType.ON_POINT_COMPLETION, m -> pointEventsExecuted.getAndIncrement());
		this.matchProcessor.registerEvent(MatchEventType.ON_GAME_COMPLETION, m -> gameEventsExecuted.getAndIncrement());
		this.matchProcessor.registerEvent(MatchEventType.ON_SET_COMPLETION, m -> setEventsExecuted.getAndIncrement());
		this.matchProcessor.registerEvent(MatchEventType.ON_MATCH_COMPLETION, m -> matchEventsExecuted.getAndIncrement());
		
		// Execute the processor
		this.winSet(processor.getMatch(), processor.getMatch().getPlayers().get(0));
		this.winSet(processor.getMatch(), processor.getMatch().getPlayers().get(0));
		
		assertEquals(48, pointEventsExecuted.get());
		assertEquals(12, gameEventsExecuted.get());
		assertEquals(2, setEventsExecuted.get());
		assertEquals(1, matchEventsExecuted.get());
	}
	
	@Test
	public void t2_eventExecution_failure() {
		MatchProcessor matchProcessor = this.createNewMatch();
		this.matchProcessor.registerEvent(MatchEventType.ON_POINT_COMPLETION, m -> { throw new RuntimeException(); });
		this.thrown.expect(FrameworkException.class);
		this.thrown.expectMessage("Failed to execute an event");
		this.winServiceGame(matchProcessor.getMatch(), matchProcessor.getMatch().getPlayers().get(0));
	}
}
