package br.cefetrj.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class FormulaEvaluatorTest {

	private static final Log LOG = LogFactory.getLog(FormulaEvaluator.class);

	@Test
	public void testAdditionMustHaveTwoOperands() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator("1 + 2 +\n");
		try {
			eval.parse();
			fail();
		} catch (ParseException e) {
			// expected
		}
	}

	@Test
	public void testAddition() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator("1 + 2\n");
		double result = eval.evaluate();
		assertEquals(3.0, result, 0.01);
	}

	@Test
	public void testSubtractionMustHaveTwoOperands() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator("1 + 2 -\n");
		try {
			eval.parse();
			fail();
		} catch (ParseException e) {
			// expected
		}
	}

	@Test
	public void testMultiplication() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator("12 * 2\n");
		double result = eval.evaluate();
		LOG.debug("result: " + result);
		assertEquals(24.0, result, 0.01);
	}

	@Test
	public void testEvaluationPlusAndMinus() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator("2 + 3.0 - 1.5");
		double valor = eval.evaluate();
		assertEquals(valor, 3.5, 0.1);
	}

	@Test
	public void testPrecedencePlusTimesAndMinus() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator(
				"2 + 3.1 - 1.5 * 3 + 2 * 3");
		double valor = eval.evaluate();
		assertEquals(valor, 6.6, 0.1);
	}

	@Test
	public void testPrecedenceWithParenthesis() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator(
				"2 + 3.1 - 1.5 * 3 - (1 + 2) * 3");
		double valor = eval.evaluate();
		assertEquals(valor, -8.4, 0.1);
	}

	@Test
	public void testFracSyntax() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator(
				"\\frac{2}{3.1} - \\frac{1.5}{3} - \\frac{(1 + 2)}{3}");
		double valor = eval.evaluate();
		assertEquals(valor, -0.85, 0.01);
	}

	@Test
	public void testExponentiation() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator("2^3.1 + 1.5^3 - 1^3");
		double valor = eval.evaluate();
		assertEquals(valor, 10.94, 0.01);
	}

	@Test
	public void testNumberInScientificNotation() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator("1.87796E16");
		double valor = eval.evaluate();
		assertEquals(valor, 1.87796E16, 0.01);
	}

	@Test(expected = br.cefetrj.parser.TokenMgrError.class)
	public void testNumberInWrongScientificNotation() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator("1.87796G16");
		double valor = eval.evaluate();
		assertEquals(valor, 1.87796E16, 0.01);
	}

	@Test
	public void testSumWithOneOperandInScientificNotation() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator("2.44949 + 1.87796E-16");
		double valor = eval.evaluate();
		assertEquals(valor, 2.44949, 0.01);
	}

	@Test
	public void testFractionWithIdentifierBetweenParentheses() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator(
				"\\frac{(\\lambda_2 + q_1)}{(\\mu_1)}");
		eval.parse();
	}

	@Test
	public void testFractionWithIdentifier() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator(
				"\\frac{\\lambda_2 + q_1}{\\mu_1}");
		eval.parse();
	}

	/**
	 * Note the wrong subscript in the identifier "q".
	 */
	@Test(expected = br.cefetrj.parser.TokenMgrError.class)
	public void testIdentifierWithWrongSubscript() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator(
				"\\frac{\\lambda_n + q_1}{\\mu_1}");
		eval.parse();
	}

	/**
	 * Note the invalid identifier "asdsad".
	 */
	@Test(expected = br.cefetrj.parser.TokenMgrError.class)
	public void testInvalidIdentifier() throws Exception {
		FormulaEvaluator eval = new FormulaEvaluator("\\lambda_1 + asdsad");
		eval.parse();
	}
}
