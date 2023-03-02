package modern_java_in_action.chap05;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Pythagoras {
	
	public static void main(String[] args) {
		IntStream.rangeClosed(1, 100) // 1부터 100까지 a 생성
				.boxed()
				// Stream<Stream<int[]>> -> Stream<int[]>로 변환
				.flatMap(a -> IntStream.rangeClosed(a, 100) // (3, 4, 5) (4, 3, 5)와 같은 중복 방지, a부터 시작
								  // 제곱근이 자연수인 경우만 필터링
									.filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
								  // Stream<int[]>를 반환
									.mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)})
				).forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));
		// Math.sqrt 계산 두 번 안하도록
		Stream<double[]> pythagoras2 = IntStream.rangeClosed(1, 100)
												.boxed()
												.flatMap(a -> IntStream.rangeClosed(a, 100)
														              .mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)})
														              .filter(t -> t[2] % 1 == 0)
												);
		pythagoras2.forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));
	}

}
