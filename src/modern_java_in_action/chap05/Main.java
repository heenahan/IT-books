package modern_java_in_action.chap05;

import java.util.stream.Stream;

public class Main {
	
	public static void main(String[] args) {
		Stream.iterate(0, n -> n < 100, n -> n + 4).forEach(System.out::println);
	}
	
}
