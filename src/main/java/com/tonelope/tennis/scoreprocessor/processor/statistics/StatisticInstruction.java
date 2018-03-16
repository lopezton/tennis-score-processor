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

import com.tonelope.tennis.scoreprocessor.model.ScoringObject;

/**
 * 
 * <p>
 * Base interface for all objects that wish to provide instruction on how to
 * build and generate <tt>Statistic</tt> results.
 * </p>
 * 
 * <p>
 * The owner of this <tt>StatisticInstruction</tt> implementation will ideally
 * have many implementations to execute against. For this reason, each
 * <tt>ScoringObject</tt> is processed through this instance's <tt>evaluate</tt>
 * method one-by-one so that this implementation can extract any necessary data
 * to keep and record metrics over. In other words, the <tt>evaluate</tt> method
 * will be called multiple times for as many <tt>scoringObject</tt> objects are
 * needing to be processed as a result of the caller.
 * </p>
 * 
 * <p>
 * Once all of the metrics have been captured, a call to <tt>getResult()</tt>
 * can be made to deliver the final resulting <tt>Statistic</tt> object.
 * </p>
 * 
 * <p>
 * </p>
 * 
 * @author Tony Lopez
 *
 * @param <S>
 *            The type of <tt>ScoringObject</tt> this instance will collect
 *            metrics over
 * @param <T>
 *            The type of <tt>Statistic</tt> this instance return
 * 
 * @see com.tonelope.tennis.scoreprocessor.processor.statistics
 */
public interface StatisticInstruction<S extends ScoringObject, T extends Statistic> {

	/**
	 * <p>
	 * Create and return a resulting <tt>Statistic</tt> object crafted from the
	 * results of this instance's <tt>evaluate(...)</tt> method.
	 * </p>
	 * 
	 * <p>
	 * <tt>createResult()</tt> can be called manually, or is called by the
	 * default implementation of <tt>getResult()</tt>.
	 * 
	 * @return the resulting <tt>Statistic</tt> object
	 */
	T createResult();

	/**
	 * <p>
	 * Processes the provided <tt>scoringObject</tt> according to this
	 * implementation's logical needs.
	 * </p>
	 * 
	 * <p>
	 * The <tt>evaluate</tt> method will be called multiple times for as many
	 * <tt>scoringObject</tt> objects are needing to be processed as a result of
	 * the caller. This will typically be several times. The general idea of
	 * <tt>evaluate</tt> is for implementations to collect metrics one-by-one
	 * and store all necessary data internally within instance variables.
	 * </p>
	 * 
	 * @param scoringObject
	 *            the <tt>ScoringObject</tt> to collect statistical data over
	 */
	void evaluate(S scoringObject);

	/**
	 * <p>
	 * This method returns the resulting <tt>Statistic</tt> object.
	 * </p>
	 * 
	 * <p>
	 * The default implementation of this method performs the following steps:
	 * </p>
	 * 
	 * <ul>
	 * <li>Creates the resulting <tt>Statistic</tt> object
	 * (<tt>createResult()</tt>)</li>
	 * <li>Resets this instance's state (<tt>reset()</tt>)</li>
	 * <li>Returns the <tt>Statistic</tt> object</li>
	 * </ul>
	 * 
	 * @return
	 */
	default T getResult() {
		T result = this.createResult();
		this.reset();
		return result;
	}

	/**
	 * <p>
	 * Performs any necessary reset logic for this instance. In most cases, this
	 * method should reset all of the instance variables to their original
	 * state. If not, the <tt>getResult</tt> call may produce undesirable
	 * results.
	 * </p>
	 * 
	 * <p>
	 * <tt>reset()</tt> can be called manually, or is called after the result
	 * set has been created within the default implementation of
	 * <tt>getResult()</tt>.
	 */
	void reset();

}
