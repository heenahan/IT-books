package modern_java_in_action.chap05;

import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Fibonacci {
	
	public static void main(String[] args) {
		Stream.iterate(new int[]{0, 1}, arr -> new int[]{arr[1], arr[0] + arr[1]})
			.limit(20)
			.map(arr -> arr[0]) // 첫번째 숫자만 남김
			.forEach(t -> System.out.print(t + " "));
		System.out.println("");
		// 익명 클래스를 이용해서 상태 필드를 정의했다. Supplier가 가변 상태를 가졌다.
		IntSupplier fib = new IntSupplier() {
			private int prev = 0;
			private int current = 1;
			@Override
			public int getAsInt() {
				int result = prev;
				int temp = current;
				current += prev;
				prev = temp;
				return result;
			}
		};
		IntStream.generate(fib).limit(10).forEach(System.out::println);
	}
	
}
