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

/**
 * <p>
 * An interface of <tt>Statistic</tt> that requires all subclasses provide both
 * a numerator and a denominator for processing.
 * </p>
 * 
 * @author Tony Lopez
 *
 */
public interface PercentageStatistic extends Statistic {

	/**
	 * Retrieve the denominator value for this percentage statistic.
	 * 
	 * @return the denominator value
	 */
	int getDenominator();

	/**
	 * Retrieve the numerator value for this percentage statistic.
	 * 
	 * @return the numerator value
	 */
	int getNumerator();
}
