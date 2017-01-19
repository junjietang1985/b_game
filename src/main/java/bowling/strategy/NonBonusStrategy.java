package bowling.strategy;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import bowling.FrameManager;
import bowling.FrameState;
import bowling.ScoreGenerator;
import bowling.visitor.Toss;

/**
 * By using this non-bonus strategy, a frame cannot obtain any bonus scores out of the frame.
 * Once strike or spare happens, it can be converted to {@link StrikeBonusStrategy} or {@link SpareBonusStrategy}
 * @author Junjie
 *
 */
public class NonBonusStrategy extends AbstractBonusStrategy implements
		BonusStrategy {
	private ScoreGenerator generator;

	public NonBonusStrategy(ScoreGenerator generator) {
		super.setFrameState(FrameState.WAIT_FOR_FIRST_TOSS);
		this.generator = generator;
	}

	@Override
	public BonusStrategy update(int score) {
		switch (this.getFrameState()) {
		case WAIT_FOR_FIRST_TOSS:
			// if the score of the first toss is full score, then it's strike
			if (score == Toss.FULL_SCORE) {
				return new StrikeBonusStrategy();
			}
			// otherwise try second toss
			this.firstToss = Optional.of(score);
			this.setFrameState(FrameState.WAIT_FOR_SECOND_TOSS);
			return this;
		case WAIT_FOR_SECOND_TOSS:
			// if the sum of the first toss and the second toss is full score,
			// then it's spare
			if (this.getFirstToss().get() + score == Toss.FULL_SCORE) {
				return new SpareBonusStrategy(this.getFirstToss().get(), score);
			}
			// otherwise no bonus is applied, and the score of the frame is
			// determined
			this.secondToss = Optional.of(score);
			this.setFrameState(FrameState.FRAME_SCORE_DETERMINED);
			return this;

		default:
			throw new IllegalStateException(
					"Error! Wrong state of NonBonusStrategy: "
							+ this.getFrameState());
		}
	}

	@Override
	public Optional<Integer> getFrameScore() {
		return this.getFrameState() == FrameState.FRAME_SCORE_DETERMINED ? Optional
				.of(this.firstToss.get() + this.secondToss.get()) : Optional
				.empty();
	}

	public int generateScore() {
		int score;
		switch (getFrameState()) {
		case WAIT_FOR_FIRST_TOSS:
			// 0 <= first toss <= full score
			score = generator.generateScore(Toss.FULL_SCORE);
			if (score <= Toss.FULL_SCORE && score >= 0) {
				return score;
			}
			throw new IllegalStateException("Error! first toss is invalid: "
					+ score);

		case WAIT_FOR_SECOND_TOSS:
			// 0 <= second toss <= full score - first toss
			int maxScore = Toss.FULL_SCORE - getFirstToss().get();
			score = generator.generateScore(maxScore);
			if (score >= 0 && score <= maxScore) {
				return score;
			}
			throw new IllegalStateException(
					"Error! second toss is invalid, firstToss: "
							+ getFirstToss().get() + " secondToss: " + score);
		default:
			throw new IllegalStateException("Error! getFrameState: "
					+ getFrameState());
		}
	}

	@Override
	public void displayThirdRowWithFrameScores() {
		System.out.print("|"
				+ StringUtils.center(
						this.getFirstToss().isPresent() ? String.valueOf(this
								.getFirstToss().get()) : "",
						FrameManager.SIZE_HALF_CELL - 1, "")
				+ "|"
				+ StringUtils.center(
						this.getSecondToss().isPresent() ? String.valueOf(this
								.getSecondToss().get()) : "",
						FrameManager.SIZE_HALF_CELL - 1, "") + "|");
	}

}
