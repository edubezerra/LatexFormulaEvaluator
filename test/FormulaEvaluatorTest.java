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
				"\\frac{2}{3.1} - \\frac{1.5}{3} - \\frac{(1 + 2)}{3}".getBytes());
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
}
