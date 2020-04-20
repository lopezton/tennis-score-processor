package com.tonelope.tennis.scoreprocessor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import com.tonelope.tennis.scoreprocessor.TennisScoreProcessor.TennisScoreProcessorBuilder;

/**
 *
 *
 * @author Tony Lopez
 *
 */
public class TennisScoreProcessorTest {

	private TennisScoreProcessor testee;
	
	@ParameterizedTest
	@CsvFileSource(resources = "/standard-scores-test.csv")
	public void testStandardScoring(String pointsString, String expected) {
		this.testee = new TennisScoreProcessorBuilder().build();
		AbstractPoint[] points = ConversionUtils.binaryStringToPoints(pointsString);
		this.testee.update(points);
		String actual = this.testee.getScore();
		Assertions.assertThat(actual).isEqualTo(expected);
	}
	
	@ParameterizedTest
	@CsvSource({
		"'0001110', '1-0'",
		"'0001111', '0-1'"
	})
	public void testNoAdScoring(String pointsString, String expected) {
		this.testee = new TennisScoreProcessorBuilder().build();
		AbstractPoint[] points = ConversionUtils.binaryStringToPoints(pointsString);
		this.testee.update(points);
		String actual = this.testee.getScore();
		Assertions.assertThat(actual).isEqualTo(expected);
	}
}
