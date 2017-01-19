package bowling.visitor;

import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.commons.lang.StringUtils;

import bowling.FrameManager;
import bowling.ScoreGenerator;
import bowling.strategy.NonBonusStrategy;

/**
 * Represents a frame 1-9 visitor which implements
 * {@link AbstractFrameVisitor#isFinished()}. The initial bonus strategy is
 * {@link NonBonusStrategy}
 * 
 * @author Junjie
 *
 */
public class FrameOneToNineVisitor extends AbstractFrameVisitor implements
		Visitor {

	public FrameOneToNineVisitor(int id, ScoreGenerator generator) {
		super(id);
		super.setBonusStrategy(new NonBonusStrategy(generator));
	}

	@Override
	public boolean isFinished() {
		// there must be the first toss
		if (!bonusStrategy.getFirstToss().isPresent()) {
			return false;
		}
		// strike means the frame is finished
		else if (bonusStrategy.getFirstToss().get().equals(Toss.FULL_SCORE)) {
			return true;
		}
		// there must be the second toss
		else if (!bonusStrategy.getSecondToss().isPresent()) {
			return false;
		}
		// the sum of the 2 tosses cannot greater than the full score
		else if (bonusStrategy.getFirstToss().get().intValue()
				+ bonusStrategy.getSecondToss().get().intValue() <= Toss.FULL_SCORE) {
			return true;
		} else {
			throw new IllegalStateException("Error! first toss: "
					+ bonusStrategy.getFirstToss().get() + " second toss: "
					+ bonusStrategy.getSecondToss().get());
		}
	}

	@Override
	public void displayFirstRow() {
		String spaced = String.format("%" + FrameManager.SIZE_HALF_CELL + "s",
				"");
		System.out.print(spaced + id + spaced);
	}

	@Override
	public void displaySecondRow() {
		IntStream.rangeClosed(1, (FrameManager.SIZE_HALF_CELL * 2 + 1))
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
		IntStream.rangeClosed(1, FrameManager.SIZE_HALF_CELL + 1).forEach(
				i -> System.out.print("-"));
	}

	@Override
	public void displayFifthRowWithFrameScore(Optional<Integer> totalScore) {
		System.out.print("|"
				+ StringUtils.center(
						totalScore.isPresent() ? String.valueOf(totalScore
								.get()) : "",
						2 * FrameManager.SIZE_HALF_CELL - 1, " ") + "|");
	}

}
