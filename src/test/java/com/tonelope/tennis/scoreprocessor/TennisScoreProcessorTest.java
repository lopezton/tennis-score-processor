package com.tonelope.tennis.scoreprocessor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

import com.tonelope.tennis.scoreprocessor.TennisScoreProcessor.FinalSet;
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
		this.testee = new TennisScoreProcessorBuilder().noAdScoring().build();
		AbstractPoint[] points = ConversionUtils.binaryStringToPoints(pointsString);
		this.testee.update(points);
		String actual = this.testee.getScore();
		Assertions.assertThat(actual).isEqualTo(expected);
	}
	
	@ParameterizedTest
	@CsvSource({
		"'0000000000', '6-0, 0-6, 1-0(0)'",
		"'10000000000', '6-0, 0-6, 1-0(1)'",
		"'00000000011111111100', '6-0, 0-6, 1-0(9)'",
		"'1111111111', '6-0, 0-6, 0-1(0)'",
		"'01111111111', '6-0, 0-6, 0-1(1)'",
		"'00000000011111111111', '6-0, 0-6, 0-1(9)'",
	})
	public void testFinalSetSuperTiebreak(String pointsString, String expected) {
		this.testee = new TennisScoreProcessorBuilder().finalSet(FinalSet.SUPER_TIEBREAK).build();
		String tiebreakPointsString = "000000000000000000000000111111111111111111111111";
		AbstractPoint[] points = ConversionUtils.binaryStringToPoints(tiebreakPointsString + pointsString);
		this.testee.update(points);
		String actual = this.testee.getScore();
		Assertions.assertThat(actual).isEqualTo(expected);
	}
	
	@ParameterizedTest
	@CsvSource({
		"'000000000000000000000000', '6-0, 0-6, 6-0'",
		"'0000000000000000000011111111111111111111111100000000', '6-0, 0-6, 8-6'",
		"'111111111111111111111111', '6-0, 0-6, 6-0'",
		"'1111111111111111111100000000000000000000000011111111', '6-0, 0-6, 6-8'",
	})
	public void testFinalSetWinBy2(String pointsString, String expected) {
		this.testee = new TennisScoreProcessorBuilder().finalSet(FinalSet.WIN_BY_2).build();
		String tiebreakPointsString = "000000000000000000000000111111111111111111111111";
		AbstractPoint[] points = ConversionUtils.binaryStringToPoints(tiebreakPointsString + pointsString);
		this.testee.update(points);
		String actual = this.testee.getScore();
		Assertions.assertThat(actual).isEqualTo(expected);
	}
	
	@ParameterizedTest
	@CsvSource({
		"'000000000000000000000000', '6-0, 0-6, 6-0'",
		"'0000000000000000000011111111111111111111111100000000', '6-0, 0-6, 8-6'",
		"'111111111111111111111111', '6-0, 0-6, 6-0'",
		"'1111111111111111111100000000000000000000000011111111', '6-0, 0-6, 6-8'",
		"'000000000000000000001111111111111111111111110000111100001111000011110000111100001111000000000000', '6-0, 0-6, 13-11'",
		"'000000000000000000001111111111111111111111110000111100001111000011110000111100001111000011111111', '6-0, 0-6, 11-13'",
		"'0000000000000000000011111111111111111111111100001111000011110000111100001111000011110000111100000000000', '6-0, 0-6, 13-12(0)'",
		"'0000000000000000000011111111111111111111111100001111000011110000111100001111000011110000111100001111111', '6-0, 0-6, 12-13(0)'",
		"'00000000000000000000111111111111111111111111000011110000111100001111000011110000111100001111000000000011111100', '6-0, 0-6, 13-12(6)'",
		"'00000000000000000000111111111111111111111111000011110000111100001111000011110000111100001111000000000011111111', '6-0, 0-6, 12-13(6)'",
	})
	public void testFinalSetTiebreakAt12All(String pointsString, String expected) {
		this.testee = new TennisScoreProcessorBuilder().finalSet(FinalSet.TIEBREAK_AT_12_ALL).build();
		String tiebreakPointsString = "000000000000000000000000111111111111111111111111";
		AbstractPoint[] points = ConversionUtils.binaryStringToPoints(tiebreakPointsString + pointsString);
		this.testee.update(points);
		String actual = this.testee.getScore();
		Assertions.assertThat(actual).isEqualTo(expected);
	}
	
	@ParameterizedTest
	@CsvSource({
		"'00000000', '6-0, 0-6, 7-5'",
		"'11111111', '6-0, 0-6, 5-7'",
		"'000011110000000000,' '6-0, 0-6, 7-6(0)'",
		"'000011111111111111,' '6-0, 0-6, 6-7(0)'",
		"'0000111110000000000,' '6-0, 0-6, 7-6(1)'",
		"'0000111101111111111,' '6-0, 0-6, 6-7(1)'",
		"'0000111100000000011111111100,' '6-0, 0-6, 7-6(9)'",
		"'0000111111111111100000000011,' '6-0, 0-6, 6-7(9)'",
	})
	public void testFinalSetTiebreakTenPointTiebreaker(String pointsString, String expected) {
		this.testee = new TennisScoreProcessorBuilder().finalSet(FinalSet.TEN_POINT_TIEBREAK).build();
		String tiebreakPointsString = "0000000000000000000000001111111111111111111111110000000000000000000011111111111111111111";
		AbstractPoint[] points = ConversionUtils.binaryStringToPoints(tiebreakPointsString + pointsString);
		this.testee.update(points);
		String actual = this.testee.getScore();
		Assertions.assertThat(actual).isEqualTo(expected);
	}
}
