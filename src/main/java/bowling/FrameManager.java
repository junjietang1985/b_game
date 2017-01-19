package bowling;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import bowling.visitor.AbstractFrameVisitor;
import bowling.visitor.FrameOneToNineVisitor;
import bowling.visitor.FrameTenVisitor;

/**
 * The manager of the frame, containing all the 10 frames.
 * @author Junjie
 *
 */
public class FrameManager {
	private TreeSet<AbstractFrameVisitor> frames = new TreeSet<>();
	private AbstractFrameVisitor currentFrame;
	public static int SIZE_HALF_CELL = 4;

	public FrameManager(ScoreGenerator generator) {

		IntStream.rangeClosed(1, 9).forEach(
				i -> frames.add(new FrameOneToNineVisitor(i, generator)));
		frames.add(new FrameTenVisitor(10, generator));
		this.currentFrame = frames.first();
	}

	public TreeSet<AbstractFrameVisitor> getFrames() {
		return frames;
	}

	public AbstractFrameVisitor getCurrentFrame() {
		return currentFrame;
	}

	public Set<AbstractFrameVisitor> getFramesWithoutDeterminedScores() {
		return frames.subSet(frames.first(), true, currentFrame, true).stream()
				.filter(f -> !f.isFrameScoreDetermined())
				.collect(Collectors.toSet());
	}

	public boolean hasNextToss() {
		return !currentFrame.isFinished()
				|| frames.higher(currentFrame) != null;
	}

	public AbstractFrameVisitor updateCurrentFrameForNextToss() {
		if (!currentFrame.isFinished()) {
			return currentFrame;
		} else if (frames.higher(currentFrame) != null) {
			currentFrame = frames.higher(currentFrame);
			return currentFrame;
		} else {
			throw new IllegalStateException("Error! currentFrame id: "
					+ currentFrame.getId() + "currentFrame.isFinished(): "
					+ currentFrame.isFinished());
		}
	}

	public Optional<Integer> getTotalScore(
			AbstractFrameVisitor abstractFrameVisitor) {
		if (abstractFrameVisitor.isFrameScoreDetermined()) {
			return Optional.of(frames
					.subSet(frames.first(), true, abstractFrameVisitor, true)
					.stream().mapToInt(f -> f.getFrameScore().get()).sum());
		}
		return Optional.empty();
	}
	
	public Optional<Integer> getTotalScore(){
		return getTotalScore(frames.last());
	}
	
	public void display(){
		frames.forEach(f->f.displayFirstRow());
		System.out.println();
		frames.forEach(f->f.displaySecondRow());
		System.out.println();
		frames.forEach(f->f.displayThirdRowWithFrameScores());
		System.out.println();
		frames.forEach(f->f.displayForthRow());
		System.out.println();
		frames.forEach(f->f.displayFifthRowWithFrameScore(getTotalScore(f)));
		System.out.println();
		frames.forEach(f->f.displaySecondRow());
		System.out.println();
		System.out.println();
		System.out.println();
	}
}
