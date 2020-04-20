package com.tonelope.tennis.scoreprocessor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.tonelope.tennis.scoreprocessor.AbstractPoint;
import com.tonelope.tennis.scoreprocessor.ConversionUtils;
import com.tonelope.tennis.scoreprocessor.TennisScoreProcessor;
import com.tonelope.tennis.scoreprocessor.TennisScoreProcessor.TennisScoreProcessorBuilder;

/**
 *
 *
 * @author Tony Lopez
 *
 */
public class TennisScoreProcessorTest {

	private TennisScoreProcessor testee = new TennisScoreProcessorBuilder().build();
	
	@ParameterizedTest
	@CsvFileSource(resources = "/standard-scores-test.csv")
	public void testStandardScoring(String pointsString, String expected) {
		AbstractPoint[] points = ConversionUtils.binaryStringToPoints(pointsString);
		this.testee.update(points);
		String actual = this.testee.getScore();
		Assertions.assertThat(actual).isEqualTo(expected);
	}
}
