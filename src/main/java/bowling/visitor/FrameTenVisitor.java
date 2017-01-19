package bowling.visitor;

import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.commons.lang.StringUtils;

import bowling.FrameManager;
import bowling.ScoreGenerator;
import bowling.strategy.TenthFrameBonusStrategy;

/**
 * Represents a frame 10 visitor which implements
 * {@link AbstractFrameVisitor#isFinished()}. The initial bonus strategy is
 * {@link TenthFrameBonusStrategy}
 * 
 * @author Junjie
 *
 */
public class FrameTenVisitor extends AbstractFrameVisitor implements Visitor {

	public FrameTenVisitor(int id, ScoreGenerator generator) {
		super(id);
		super.setBonusStrategy(new TenthFrameBonusStrategy(generator));
	}

	@Override
	public boolean isFinished() {
		TenthFrameBonusStrategy tenthFrameBonusStrategy = (TenthFrameBonusStrategy) bonusStrategy;
		// there must be the first toss
		if (!tenthFrameBonusStrategy.getFirstToss().isPresent()) {
			return false;
		}
		// there must be the second toss
		else if (!tenthFrameBonusStrategy.getSecondToss().isPresent()) {
			return false;
		}
		// there must be the third toss when the first toss makes it a strike or
		// the first toss and the second make it as a spare
		else if ((tenthFrameBonusStrategy.getFirstToss().get().intValue() == Toss.FULL_SCORE || tenthFrameBonusStrategy
				.getFirstToss().get().intValue()
				+ tenthFrameBonusStrategy.getSecondToss().get().intValue() == Toss.FULL_SCORE)
				&& !tenthFrameBonusStrategy.getThirdToss().isPresent()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void displayFirstRow() {
		String spaced = String.format("%" + FrameManager.SIZE_HALF_CELL * 1.5
				+ "s", "");
		System.out.print(spaced + id + spaced);
	}

	@Override
	public void displaySecondRow() {
		IntStream.rangeClosed(1, (FrameManager.SIZE_HALF_CELL * 3 + 1))
				.forEach(i -> System.out.print("-"));
	}

	@Override
	public void displayThirdRowWithFrameScores() {
		this.bonusStrategy.displayThirdRowWithFrameScores();
	}

	@Override
	public void displayForthRow() {
		String spaces = String.format("%" + (FrameManager.SIZE_HALF_CELL - 1)
				+ "s", "");
		System.out.print("|" + spaces);
		IntStream.rangeClosed(1, FrameManager.SIZE_HALF_CELL * 2).forEach(
				i -> System.out.print("-"));
		System.out.print("|");
	}

	@Override
	public void displayFifthRowWithFrameScore(Optional<Integer> totalScore) {
		System.out.print("|"
				+ StringUtils.center(
						totalScore.isPresent() ? String.valueOf(totalScore
								.get()) : "",
						3 * FrameManager.SIZE_HALF_CELL - 1, " ") + "|");
	}

}
