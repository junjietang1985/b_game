package bowling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import bowling.visitor.Toss;

/**
 * The 'UI' of the bowling application
 * @author Junjie
 *
 */
public class App {

	public Optional<Integer> gameStart(String command, ScoreGenerator generator) {
		FrameManager frameManager = new FrameManager(generator);
		while (frameManager.hasNextToss()) {
			if (!"all".equals(command)) {
				System.out.println("press enter to generate next toss:");
				BufferedReader br = new BufferedReader(new InputStreamReader(
						System.in));
				try {
					br.readLine();
				} catch (IOException e) {
					System.out.println("IO exception, please try again later");
					e.printStackTrace();
				}
			}
			frameManager.updateCurrentFrameForNextToss();
			Toss toss = new Toss(frameManager.getCurrentFrame());
			frameManager.getFramesWithoutDeterminedScores().forEach(
					f -> toss.accept(f));
			frameManager.display();
		}
		return frameManager.getTotalScore();

	}

	public static void main(String[] args) throws Exception {
		System.out.println("------------------");
		System.out.println("-- bowling game --");
		System.out.println("------------------");
		System.out.println();
		System.out
				.println("type 'all' to see the auto-generated report for ALL tosses OR else, to generate one toss by one:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String command = br.readLine();
		new App().gameStart(command, new ScoreGenerator());
	}

}
