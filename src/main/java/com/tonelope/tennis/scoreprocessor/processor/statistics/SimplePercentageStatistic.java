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

import java.text.DecimalFormat;

/**
 * <p>
 * Delivers a statistic of the format:
 * </p>
 * 
 * <pre>
 * SimplePercentageStatistic statistic = new SimplePercentageStatistic(3, 4);
 * </pre>
 * 
 * <p>
 * Calling <tt>statistic.toString</tt> would give:
 * </p>
 * 
 * <pre>
 * 3/4 (75%)
 * </pre>
 * 
 * @author Tony Lopez
 *
 */
public class SimplePercentageStatistic implements PercentageStatistic {

	public static final DecimalFormat FORMATTER = new DecimalFormat("##%");

	private final int numerator;
	private final int denominator;

	public SimplePercentageStatistic(int numerator, int denominator) {
		if (numerator < 0) {
			throw new IllegalArgumentException("numerator must be greater than 0 but was " + numerator);
		}
		if (numerator < 0) {
			throw new IllegalArgumentException("denominator must be greater than 0 but was " + denominator);
		}
		this.numerator = numerator;
		this.denominator = denominator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tonelope.tennis.scoreprocessor.processor.statistics.
	 * PercentageStatistic#getNumerator()
	 */
	@Override
	public int getNumerator() {
		return this.numerator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.tonelope.tennis.scoreprocessor.processor.statistics.
	 * PercentageStatistic#getDenominator()
	 */
	@Override
	public int getDenominator() {
		return this.denominator;
	}

	/**
	 * <p>
	 * Returns the percentage calculated using the <tt>numerator</tt> /
	 * <tt>denominator</tt>.
	 * </p>
	 * 
	 * @return the percentage as a double
	 */
	public double getPercentage() {
		if (0 == this.denominator) {
			return 0;
		}
		return (double) this.numerator / this.denominator;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder().append(this.numerator).append("/").append(this.denominator).append(" (")
				.append(FORMATTER.format(this.getPercentage())).append(")").toString();
	}
}
