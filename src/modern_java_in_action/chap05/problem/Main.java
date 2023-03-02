package modern_java_in_action.chap05.problem;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

public class Main {
	
	public static void main(String[] args) {
		Main T = new Main();
		System.out.println("========문 01========");
		System.out.println(T.solution1());
		System.out.println("========문 02========");
		System.out.println(T.solution2());
		System.out.println("========문 03========");
		System.out.println(T.solution3());
		System.out.println("========문 04========");
		System.out.println(T.solution4());
		System.out.println("========문 05========");
		System.out.println(T.solution5());
		System.out.println("========문 06========");
		T.solution6();
		System.out.println("========문 07========");
		System.out.println(T.solution7());
		System.out.println("========문 08========");
		System.out.println(T.solution8());
	}
	// 1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리
	public List<Transaction> solution1() {
		List<Transaction> transactions = getTransactions();
		
		return transactions.stream()
				       .filter(t -> t.getYear() == 2011)
				       .sorted(comparing(Transaction::getValue))
				       .collect(toList());
	}
	// 2. 거래자가 근무하는 모든 도시를 중복 없이 나열
	public List<String> solution2() {
		List<Transaction> transactions = getTransactions();
		
		return transactions.stream()
				       .map(Transaction::getTrader) // trader를 가져온다 Stream<Trader>
				       .map(Trader::getCity) // city를 가져온다 Stream<String>
				       .distinct() // 중복 없앰
				       .collect(toList());
	}
	// 3. 수원에서 근무하는 모든 거래자를 찾아 이름순으로 정렬
	public List<String> solution3() {
		List<Transaction> transactions = getTransactions();
		
		return transactions.stream()
				       .map(Transaction::getTrader)
				       .filter(t -> "Suwon".equals(t.getCity())) // 수원인 사람만 뽑음
				       .map(Trader::getName)
				       .distinct() // 중복 없앰
				       .sorted()
				       .collect(toList());
	}
	// 4. 모든 거래자의 이름을 알파벳 순으로 정렬
	public String solution4() {
		List<Transaction> transactions = getTransactions();
		transactions.stream()
				.map(t -> t.getTrader().getName())
				.distinct()
				.sorted()
				.reduce("", (a, b) -> a + b); // 매번 새로운 문자열 객체를 만들어내므로 비효율
		return transactions.stream()
				       .map(t -> t.getTrader().getName())
				       .distinct()
				       .sorted()
				       .collect(joining(" "));
	}
	// 5. 서울에 거래자가 존재?
	public boolean solution5() {
		List<Transaction> transactions = getTransactions();
		
		transactions.stream()
				.anyMatch(t -> "Seoul".equals(t.getTrader().getCity()));
		
		return transactions.stream()
				       .map(Transaction::getTrader)
				       .filter(t -> "Seoul".equals(t.getCity()))
				       .findAny()
				       .isPresent(); // 값이 있으면 true
	}
	// 6. 수원에 거주하는 거래자의 모든 트랜잭션값 출력
	public void solution6() {
		List<Transaction> transactions = getTransactions();
		
		transactions.stream()
					.filter(t -> "Suwon".equals(t.getTrader().getCity()))
					.map(Transaction::getValue)
					.forEach(System.out::println);
	}
	// 7. 전체 트랜젝션 중 최댓값
	public Optional<Integer> solution7() {
		List<Transaction> transactions = getTransactions();
		
		return transactions.stream()
				       .map(Transaction::getValue)
				       .reduce(Integer::max);
	}
	// 8. 전체 트랜젝션 중 최솟값
	public Optional<Integer> solution8() {
		List<Transaction> transactions = getTransactions();
		
		return transactions.stream()
				       .map(Transaction::getValue)
				       .reduce(Integer::min);
	}
	
	private List<Transaction> getTransactions() {
		Trader kim = new Trader("Kim", "Suwon");
		Trader choi = new Trader("Choi", "Seoul");
		Trader park = new Trader("Park", "Suwon");
		Trader lee = new Trader("Lee", "Suwon");
		
		return Arrays.asList(
				new Transaction(park, 2011, 300),
				new Transaction(choi, 2012, 1000),
				new Transaction(choi, 2011, 400),
				new Transaction(kim, 2011, 710),
				new Transaction(kim, 2012, 700),
				new Transaction(lee, 2012, 950)
		);
	}
}
