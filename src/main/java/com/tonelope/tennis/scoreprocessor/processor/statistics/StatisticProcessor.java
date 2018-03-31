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
package com.tonelope.tennis.scoreprocessor.processor.statistics;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.tonelope.tennis.scoreprocessor.model.Game;
import com.tonelope.tennis.scoreprocessor.model.Match;
import com.tonelope.tennis.scoreprocessor.model.Point;
import com.tonelope.tennis.scoreprocessor.model.ScoringObject;
import com.tonelope.tennis.scoreprocessor.model.Set;
import com.tonelope.tennis.scoreprocessor.model.Stroke;

import lombok.Getter;

/**
 * @author Tony Lopez
 *
 */
@Getter
public class StatisticProcessor {

	private final Match match;
	private final List<StatisticInstruction<? extends ScoringObject, ? extends Statistic>> instructions = new ArrayList<>();

	public StatisticProcessor(Match match) {
		this.match = match;
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
	public <S extends ScoringObject> boolean addInstruction(StatisticInstruction<S, ? extends Statistic> instruction) {
		return this.instructions.add(instruction);
	}

	/**
	 * <p>
	 * Executes this instance's set of <tt>StatisticInstructions</tt> over the
	 * current <tt>match</tt> object and delivers a result set.
	 * </p>
	 * 
	 * @return the list containing the statistic results
	 */
	private List<StatisticInstruction<? extends ScoringObject, ? extends Statistic>> executeStatisticInstructions(
			List<StatisticInstruction<? extends ScoringObject, ? extends Statistic>> instructions) {

		// collect the instructions prior to building statistic data
		List<StatisticInstruction<Set, ? extends Statistic>> setInstructions = this
				.getStatisticInstructionsForType(instructions, Set.class);
		List<StatisticInstruction<Game, ? extends Statistic>> gameInstructions = this
				.getStatisticInstructionsForType(instructions, Game.class);
		List<StatisticInstruction<Point, ? extends Statistic>> pointInstructions = this
				.getStatisticInstructionsForType(instructions, Point.class);
		List<StatisticInstruction<Stroke, ? extends Statistic>> strokeInstructions = this
				.getStatisticInstructionsForType(instructions, Stroke.class);

		// evaluate the statistic data
		// TODO Refactor
		for (Set set : this.match.getSets()) {
			for (Game game : set.getGames()) {
				for (Point point : game.getPoints()) {
					for (Stroke stroke : point.getStrokes()) {
						strokeInstructions.forEach(s -> s.evaluate(stroke));
					}
					if (point.isCompleted()) {
						pointInstructions.forEach(s -> s.evaluate(point));
					}
				}
				if (game.isCompleted()) {
					gameInstructions.forEach(s -> s.evaluate(game));
				}
			}
			if (set.isCompleted()) {
				setInstructions.forEach(s -> s.evaluate(set));
			}
		}

		// user has access already, but return the instructions anyway.
		return instructions;
	}

	/**
	 * <p>
	 * Returns an unmodifiable list of the statistic instructions contained
	 * within this instance.
	 * </p>
	 * 
	 * @return the list of statistic instruction objects
	 */
	public List<StatisticInstruction<? extends ScoringObject, ? extends Statistic>> getInstructions() {
		return Collections.unmodifiableList(this.instructions);
	}

	/**
	 * <p>
	 * Executes a single statistic instruction and returns the result set.
	 * </p>
	 * 
	 * @param instruction
	 *            the statistic instruction to execute
	 * @return the statistic result
	 */
	public <S extends ScoringObject> Statistic getStatistic(StatisticInstruction<S, ? extends Statistic> instruction) {
		List<StatisticInstruction<?, ?>> instructions = new ArrayList<>();
		instructions.add(instruction);
		return this.executeStatisticInstructions(instructions).get(0).getResult();
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
			List<StatisticInstruction<? extends ScoringObject, ? extends Statistic>> instructions, Class<?> clazz) {
		List<StatisticInstruction<T, ? extends Statistic>> applicableInstructions = new ArrayList<>();
		for (StatisticInstruction<?, ? extends Statistic> instruction : instructions) {
			if (clazz.equals(((ParameterizedType) instruction.getClass().getGenericInterfaces()[0])
					.getActualTypeArguments()[0])) {
				applicableInstructions.add((StatisticInstruction<T, Statistic>) instruction);
			}
		}
		return applicableInstructions;
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
		return this.getStatistics(this.instructions);
	}

	/**
	 * <p>
	 * Executes the provided set of <tt>StatisticInstructions</tt> over the
	 * current <tt>match</tt> object and delivers a result set.
	 * </p>
	 * 
	 * @return the list containing the statistic results
	 */
	private List<Statistic> getStatistics(
			List<StatisticInstruction<? extends ScoringObject, ? extends Statistic>> instructions) {
		return this.executeStatisticInstructions(instructions).stream().map(StatisticInstruction::getResult)
				.collect(Collectors.toList());
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
	public boolean removeInstruction(StatisticInstruction<? extends ScoringObject, ? extends Statistic> instruction) {
		return this.instructions.remove(instruction);
	}
	
	public CommonStatisticProcessor toCommonStatisticProcessor() {
		return new CommonStatisticProcessor(this);
	}
}
