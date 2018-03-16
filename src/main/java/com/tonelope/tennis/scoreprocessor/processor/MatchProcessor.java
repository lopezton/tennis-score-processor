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

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tonelope.tennis.scoreprocessor.model.FrameworkException;
import com.tonelope.tennis.scoreprocessor.model.Game;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.MatchEventType;
import com.tonelope.tennis.scoreprocessor.model.Point;
import com.tonelope.tennis.scoreprocessor.model.ScoringObject;
import com.tonelope.tennis.scoreprocessor.model.Set;
import com.tonelope.tennis.scoreprocessor.model.Stroke;
import com.tonelope.tennis.scoreprocessor.processor.statistics.Statistic;
import com.tonelope.tennis.scoreprocessor.processor.statistics.StatisticInstruction;

import lombok.Getter;

/**
 * <p>
 * Serves as the main entry point to processing the provided <tt>match</tt>
 * object.
 * </p>
 * 
 * <p>
 * On creation, this object will determine the strategy and rules that will be
 * needed in order to process the provided <tt>match</tt> object. Which
 * particular strategy and rules that will be applied will be determined by the
 * appropriate configurations provided within the <tt>match</tt> object.
 * </p>
 * 
 * <p>
 * <tt>MatchProcessor</tt> follows the Facade pattern to abstract the details
 * away regarding which strategy or handlers are applied given a particular set
 * of match configurations. If greater control over which strategies or handlers
 * are needed, they may be used directly. (e.g. use
 * <tt>SinglesMatchStrategy</tt> or <tt>DoublesMatchStrategy</tt> directly.)
 * </p>
 * 
 * @author Tony Lopez
 *
 */
@Getter
public class MatchProcessor {

	public static final Logger LOG = LoggerFactory.getLogger(MatchProcessor.class);

	private final Match match;
	private final MatchStrategy strategy;
	private final List<StatisticInstruction<? extends ScoringObject, ? extends Statistic>> statisticInstructions = new ArrayList<>();

	public MatchProcessor(Match match) {
		if (null == match) {
			throw new FrameworkException("match can not be null.");
		}
		// TODO validate match
		this.match = match;
		this.strategy = this.determineStrategy(match);
	}

	/**
	 * <p>
	 * Appends a <tt>StatisticInstruction</tt> to the list of instructions for
	 * this processor instance.
	 * </p>
	 * 
	 * @param instruction
	 * @return
	 */
	public <S extends ScoringObject> boolean addStatisticInstruction(
			StatisticInstruction<S, ? extends Statistic> instruction) {
		return this.statisticInstructions.add(instruction);
	}

	/**
	 * <p>
	 * Determines the match strategy to be used.
	 * </p>
	 * <ul>
	 * <li>If the number of players equals 2, then <tt>SinglesMatchStrategy</tt>
	 * will be used.</li>
	 * <li>If the number of players equals 4, then <tt>DoublesMatchStrategy</tt>
	 * will be used.</li>
	 * <li>If the number of players is not supported by the available
	 * strategies, then a <tt>FrameworkException</tt> will be thrown.</li>
	 * </ul>
	 * 
	 * @param match
	 *            the match object
	 * @return the strategy determined from <tt>match</tt>, or else a
	 *         <tt>FrameworkException</tt> is thrown.
	 */
	private MatchStrategy determineStrategy(Match match) {
		if (null == match.getPlayers() || match.getPlayers().isEmpty()) {
			throw new FrameworkException("match object contained no player information.");
		} else if (match.getPlayers().size() == 2) {
			return new SinglesMatchStrategy();
		} else if (match.getPlayers().size() == 4) {
			throw new UnsupportedOperationException("Doubles is not yet supported.");
		}
		throw new FrameworkException("Could not determine match strategy from given match information");
	}

	/**
	 * <p>
	 * Retrieves a list of <tt>StatisticInstructions</tt> from this instance's
	 * list of instructions who's type T class matches the provided
	 * <tt>clazz</tt>. This is used to retrieve a specific set of instructions
	 * for a specific type, when only that type is needed.
	 * </p>
	 * 
	 * @param clazz
	 *            the type to check for matching against T
	 * @return the subset of <tt>StatisticInstructions</tt> who's type T class
	 *         matches <tt>class</tt>
	 */
	@SuppressWarnings("unchecked")
	private <T extends ScoringObject> List<StatisticInstruction<T, ? extends Statistic>> getStatisticInstructionsForType(
			Class<?> clazz) {
		List<StatisticInstruction<T, ? extends Statistic>> instructions = new ArrayList<>();
		for (StatisticInstruction<?, ? extends Statistic> instruction : this.statisticInstructions) {
			if (clazz.equals(((ParameterizedType) instruction.getClass().getGenericInterfaces()[0])
					.getActualTypeArguments()[0])) {
				instructions.add((StatisticInstruction<T, Statistic>) instruction);
			}
		}
		return instructions;
	}

	/**
	 * <p>
	 * Executes this instance's set of <tt>StatisticInstructions</tt> over the
	 * current <tt>match</tt> object and delivers a result set.
	 * </p>
	 * 
	 * @return the list containing the statistic results
	 */
	public List<Statistic> getStatistics() {

		// collect the instructions prior to building statistic data
		List<StatisticInstruction<Set, ? extends Statistic>> setInstructions = this
				.getStatisticInstructionsForType(Set.class);
		List<StatisticInstruction<Game, ? extends Statistic>> gameInstructions = this
				.getStatisticInstructionsForType(Game.class);
		List<StatisticInstruction<Point, ? extends Statistic>> pointInstructions = this
				.getStatisticInstructionsForType(Point.class);
		List<StatisticInstruction<Stroke, ? extends Statistic>> strokeInstructions = this
				.getStatisticInstructionsForType(Stroke.class);

		// build the statistic data
		for (Set set : this.match.getSets()) {
			for (Game game : set.getGames()) {
				for (Point point : game.getPoints()) {
					for (Stroke stroke : point.getStrokes()) {
						strokeInstructions.forEach(s -> s.evaluate(stroke));
					}
					pointInstructions.forEach(s -> s.evaluate(point));
				}
				gameInstructions.forEach(s -> s.evaluate(game));
			}
			setInstructions.forEach(s -> s.evaluate(set));
		}

		// build and return the result set
		return this.statisticInstructions.stream().map(StatisticInstruction::getResult).collect(Collectors.toList());
	}

	/**
	 * <p>
	 * Registers <tt>event</tt> to be executed following the completion of the
	 * event defined by <tt>eventType</tt>.
	 * </p>
	 * 
	 * @param eventType
	 *            the event descriptor for after which the provided
	 *            <tt>event</tt> should be executed. Whichever
	 *            <tt>MatchEventType</tt> is provided, <tt>event</tt> will
	 *            executed only after <tt>eventType</tt> has completed.
	 * @param event
	 *            the consuming method to be executed. The provided parameter
	 *            will be the updated <tt>match</tt> object.
	 */
	public void registerEvent(MatchEventType eventType, Consumer<Match> event) {
		this.strategy.registerEvent(eventType, event);
	}

	/**
	 * <p>
	 * Removes the first occurrence of <tt>StatisticInstruction</tt> from the
	 * list of instructions for this processor instance.
	 * </p>
	 * 
	 * <p>
	 * Returns true if the <tt>StatisticInstruction</tt> is found and removed.
	 * </p>
	 * 
	 * @param instruction
	 *            the <tt>StatisticInstruction</tt> to remove
	 * @return true if the <tt>StatisticInstruction</tt> is found and removed
	 */
	public <S extends ScoringObject> boolean removeStatisticInstruction(
			StatisticInstruction<S, ? extends Statistic> instruction) {
		return this.statisticInstructions.remove(instruction);
	}

	/**
	 * <p>
	 * Updates this <tt>match</tt> object with the provided <tt>point</tt>.
	 * </p>
	 * 
	 * @param point
	 *            the point object to update within <tt>match</tt>.
	 * @return the match object
	 */
	public Match update(Point point) {
		return this.strategy.update(this.match, point);
	}

	/**
	 * <p>
	 * Updates this <tt>match</tt> object with the provided <tt>stroke</tt>.
	 * </p>
	 * 
	 * @param stroke
	 *            the stroke object to update within <tt>match</tt>.
	 * @return the match object
	 */
	public Match update(Stroke stroke) {
		return this.strategy.update(this.match, stroke);
	}
}
