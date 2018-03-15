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
 * @author Tony Lopez
 *
 */
public class SimplePercentageResult implements PercentageResult {

	public static final DecimalFormat FORMATTER = new DecimalFormat("##%");
	
	private final int numerator;
	private final int denominator;
	
	public SimplePercentageResult(int numerator, int denominator) {
		// TODO no negatives
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.statistics.PercentageResult#getNumerator()
	 */
	@Override
	public int getNumerator() {
		return this.numerator;
	}

	/* (non-Javadoc)
	 * @see com.tonelope.tennis.scoreprocessor.processor.statistics.PercentageResult#getDenominator()
	 */
	@Override
	public int getDenominator() {
		return this.denominator;
	}

	public double getPercentage() {
		if (0 == this.denominator) {
			return 0;
		}
		return (double) this.numerator / this.denominator;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append(this.numerator)
				.append("/")
				.append(this.denominator)
				.append(" (")
				.append(FORMATTER.format(this.getPercentage()))
				.append(")")
				.toString();
	}
}
