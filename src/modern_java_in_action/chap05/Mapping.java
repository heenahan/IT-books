package modern_java_in_action.chap05;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Mapping {
	
	public static void main(String[] args) {
		Mapping T = new Mapping();
		
		T.solution1();
		T.solution2();
		T.solution3();
	}
	// 숫자 리스트가 주어지면 각 숫자의 제곱근으로 이루어진 리스트 반환
	public void solution1() {
		List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> square = nums.stream().map(i -> i * i).collect(Collectors.toList());
		
		System.out.println(square);
	}
	// 두 개의 숫자 리스트가 주어지면 모든 숫자 쌍의 리스트 반환
	public void solution2() {
		List<Integer> nums1 = Arrays.asList(1, 2, 3);
		List<Integer> nums2 = Arrays.asList(3, 4, 5);
		
		List<Integer[]> pair = nums1.stream()
				// Stream<Integer>을 반환한다. 따라서 그냥 map을 사용하면 Stream<Stream<Integer[]>>가 된다.
				.flatMap(i -> nums2.stream().map(j -> new Integer[]{ i, j }))
				.collect(Collectors.toList());
		
		for (Integer[] arr : pair) {
			for (Integer i : arr) System.out.print(i + " ");
			System.out.println("");
		}
	}
	// 2번 예제에서 합이 3으로 나누어떨어지는 쌍만 반환
	public void solution3() {
		List<Integer> nums1 = Arrays.asList(1, 2, 3);
		List<Integer> nums2 = Arrays.asList(3, 4, 5);
		
		List<Integer[]> div = nums1.stream()
				.flatMap(i -> nums2.stream().map(j -> new Integer[]{i, j}))
				.filter(pair -> (pair[0] + pair[1]) % 3 == 0)
				.collect(Collectors.toList());
		
		div = nums1.stream()
				.flatMap(i -> nums2.stream()
								.filter(j -> (i + j) % 3 == 0)
								.map(j -> new Integer[]{ i, j }))
				.collect(Collectors.toList());
		
		for (Integer[] arr : div) {
			for (Integer i : arr) System.out.print(i + " ");
			System.out.println("");
		}
	}

}
