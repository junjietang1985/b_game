package bowling;

import static org.assertj.core.api.Assertions.*;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AppTest {
	@Mock
	ScoreGenerator scoreGenerator;

	App app = new App();

	@Test
	public void testPerfectGame() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(10);
		assertThat(app.gameStart("all", scoreGenerator).get()).isEqualTo(300);
	}

	@Test
	public void testGameWithoutBonus() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(4);
		assertThat(app.gameStart("all", scoreGenerator).get()).isEqualTo(80);
	}

	@Test
	public void testGameWithSpareBonus() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(5);
		assertThat(app.gameStart("all", scoreGenerator).get()).isEqualTo(150);
	}

	@Test
	public void testGameWithoutBonusSoresOfTheTenthFrame() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(1, 1, 5, 5, 10, 2, 8, 2, 2, 1, 8, 10, 10, 10, 1, 1);
		assertThat(app.gameStart("all", scoreGenerator).get()).isEqualTo(132);
	}

	@Test
	public void testGameWithSpareOfTheTenthFrame() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(1, 1, 5, 5, 10, 2, 8, 2, 2, 1, 8, 10, 10, 10, 1, 9,
						10);
		assertThat(app.gameStart("all", scoreGenerator).get()).isEqualTo(158);
	}

	@Test
	public void testGameWithStrikeOfTheTenthFrame() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(1, 1, 5, 5, 10, 2, 8, 2, 2, 1, 8, 10, 10, 10, 10,
						9, 1);
		assertThat(app.gameStart("all", scoreGenerator).get()).isEqualTo(176);
	}

	@Test
	public void testFrameOneToNineWithInvalidFirstTossThatGreaterThanFullScore() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(11);
		assertThatThrownBy(() -> app.gameStart("all", scoreGenerator))
				.hasMessageContaining("first toss is invalid");
	}

	@Test
	public void testFrameOneToNineWithInvalidFirstTossThatLessThan0() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(-1);
		assertThatThrownBy(() -> app.gameStart("all", scoreGenerator))
				.hasMessageContaining("first toss is invalid");
	}

	@Test
	public void testFrameOneToNineWithInvalidSecondTossThatMakesFrameScoreGreaterThanFullScore() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(6);
		assertThatThrownBy(() -> app.gameStart("all", scoreGenerator))
				.hasMessageContaining("second toss is invalid");
	}

	@Test
	public void testFrameOneToNineWithInvalidSecondTossThatLessThan0() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(6, -1);
		assertThatThrownBy(() -> app.gameStart("all", scoreGenerator))
				.hasMessageContaining("second toss is invalid");
	}

	@Test
	public void testFrameTenWithInvalidFirstTossThatGreaterThanFullScore() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(1, 1, 5, 5, 10, 10, 10, 10, 10, 10, 10, 11);
		assertThatThrownBy(() -> app.gameStart("all", scoreGenerator))
				.hasMessageContaining("first toss is invalid");
	}

	@Test
	public void testFrameTenWithInvalidFirstTossThatLessThan0() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(1, 1, 5, 5, 10, 10, 10, 10, 10, 10, 10, -1);
		assertThatThrownBy(() -> app.gameStart("all", scoreGenerator))
				.hasMessageContaining("first toss is invalid");
	}

	@Test
	public void testFrameTenWithInvalidSecondTossThatMakesFrameScoreGreaterThanFullScore() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(1, 1, 5, 5, 10, 10, 10, 10, 10, 10, 10, 9, 9);
		assertThatThrownBy(() -> app.gameStart("all", scoreGenerator))
				.hasMessageContaining("second toss is invalid");
	}

	@Test
	public void testFrameTenWithInvalidSecondTossThatLessThan0() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(1, 1, 5, 5, 10, 10, 10, 10, 10, 10, 10, 9, -9);
		assertThatThrownBy(() -> app.gameStart("all", scoreGenerator))
				.hasMessageContaining("second toss is invalid");
	}

	@Test
	public void testFrameTenWithInvalidThirdTossThatGreaterThanFullScore() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(1, 1, 5, 5, 10, 10, 10, 10, 10, 10, 10, 1, 9, 11);
		assertThatThrownBy(() -> app.gameStart("all", scoreGenerator))
				.hasMessageContaining("third toss is invalid");
	}

	@Test
	public void testFrameTenWithInvalidThirdTossThatMakesTheSumOfTheSecondTossAndTheThirdGreaterThanFullScoreWhenTheFirstTossIsStrike() {
		Mockito.when(scoreGenerator.generateScore(Mockito.anyInt()))
				.thenReturn(1, 1, 5, 5, 10, 10, 10, 10, 10, 10, 10, 10, 9, 2);
		assertThatThrownBy(() -> app.gameStart("all", scoreGenerator))
				.hasMessageContaining("third toss is invalid");
	}
}
