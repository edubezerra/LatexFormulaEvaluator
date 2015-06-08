package br.cefetrj.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import br.cefetrj.parser.FormulaEvaluator;
import br.cefetrj.parser.ParseException;

public class FormulaEvaluatorTest {

	private static final Log LOG = LogFactory.getLog(FormulaEvaluator.class);

	@Test
	public void testAdditionMustHaveTwoOperands() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"1 + 2 +\n".getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		try {
			eval.parse();
			fail();
		} catch (ParseException e) {
			// expected
		}
	}

	@Test
	public void testAddition() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"1 + 2\n".getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		double result = eval.parse();
		assertEquals(3.0, result, 0.01);
	}

	@Test
	public void testSubtractionMustHaveTwoOperands() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"1 + 2 -\n".getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		try {
			eval.parse();
			fail();
		} catch (ParseException e) {
			// expected
		}
	}

	@Test
	public void testMultiplication() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"12 * 2\n".getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		double result = eval.parse();
		LOG.debug("result: " + result);
		assertEquals(24.0, result, 0.01);
	}

	@Test
	public void testEvaluationPlusAndMinus() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"2 + 3.0 - 1.5".getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		double valor = eval.parse();
		assertEquals(valor, 3.5, 0.1);
	}

	@Test
	public void testPrecedencePlusTimesAndMinus() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"2 + 3.1 - 1.5 * 3 + 2 * 3".getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		double valor = eval.parse();
		assertEquals(valor, 6.6, 0.1);
	}

	@Test
	public void testPrecedenceWithParenthesis() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"2 + 3.1 - 1.5 * 3 - (1 + 2) * 3".getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		double valor = eval.parse();
		assertEquals(valor, -8.4, 0.1);
	}

	@Test
	public void testFracSyntax() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"\\frac{2}{3.1} - \\frac{1.5}{3} - \\frac{(1 + 2)}{3}"
						.getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		double valor = eval.parse();
		assertEquals(valor, -0.85, 0.01);
	}

	@Test
	public void testExponentiation() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"2^3.1 + 1.5^3 - 1^3".getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		double valor = eval.parse();
		assertEquals(valor, 10.94, 0.01);
	}

	@Test
	public void testNumberInScientificNotation() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"1.87796E16".getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		double valor = eval.parse();
		assertEquals(valor, 1.87796E16, 0.01);
	}
	
	@Test(expected=br.cefetrj.parser.TokenMgrError.class)
	public void testNumberInWrongScientificNotation() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"1.87796G16".getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		double valor = eval.parse();
		assertEquals(valor, 1.87796E16, 0.01);
	}
	
	@Test
	public void testSumWithOneOperandInScientificNotation() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"2.44949 + 1.87796E-16".getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		double valor = eval.parse();
		assertEquals(valor, 2.44949, 0.01);
	}
	
	@Test
	public void testFractionWithIdentifierBetweenParentheses() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"\\frac{(\\lambda_2 + q_1)}{(\\mu_1)}".getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		double valor = eval.parse();
		assertEquals(valor, 2.0, 0.01);
	}
	
	@Test
	public void testFractionWithIdentifier() throws Exception {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				"\\frac{\\lambda_2 + q_1}{\\mu_1}".getBytes());
		FormulaEvaluator eval = new FormulaEvaluator(inputStream);
		double valor = eval.parse();
		assertEquals(valor, 2.0, 0.01);
	}
}
