package bowling;

import java.util.Random;

/**
 * A random number generator, making UT easier
 * 
 * @author Junjie
 *
 */
public class ScoreGenerator {

	public int generateScore(int max) {
		return new Random().nextInt(max + 1);
	}
}
