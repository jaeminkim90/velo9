package teamexpress.velo9.member.dto;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;

@Getter
public class RandomNumber {

	private static final int BOUND = 10;

	private static final RandomNumber RANDOM_NUMBER = new RandomNumber();

	private String number;

	private RandomNumber() {
	}
	public static RandomNumber get() {
		Random random = new Random();
		RANDOM_NUMBER.number = IntStream.range(0, 6)
			.map(i -> random.nextInt(BOUND))
			.mapToObj(String::valueOf)
			.collect(Collectors.joining());
		return RANDOM_NUMBER;
	}
}

