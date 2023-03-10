# 옵저버 패턴
## 1. 문제
기상 모니터링 애플리케이션 Weather-O-Rama가 있다. 
애플리케이션의 WeatherData 클래스는 기상 관측값이 갱신될 때마다 디스플레이를 업데이트해야 한다.
그리고 디스플레이가 더 늘어날 수 있으므로 애플리케이션은 확장성을 가져야 한다.

## 2. 개념
옵저버 패턴은 **주제(subject)**와 **옵저버(observer)**가 있다. 
주제에서 중요한 데이터를 관리하고 있으며 주제 데이터가 바뀌면 옵저버에게 전달한다.
옵저버 객체는 주제를 구독하고 있으며 주제 데이터가 바뀌면 갱신 내용을 전달 받는다.

옵저버 객체는 구독을 취소하고 옵저버를 그만둘 수 있다. 
구독을 취소하면 주제 데이터가 바뀌어도 아무 연락을 받지 않는다.

옵저버 패턴은 한 객체의 상태가 바뀌면 그 객체에 의존하는 다른 객체에게 연락이 가고 자동으로 내용이 갱신된다.
따라서 일대다(one-to-many) 의존성을 정의한다.

옵저버 패턴에서 주제가 상태를 저장하고 제어한다. 
반대로 옵저버는 상태를 사용하지만 반드시 소유할 필요는 없다.
따라서 상태가 들어있는 주제 객체는 하나만 있을 수 있고 옵저버 객체는 여러개 있을 수 있다. 
하나의 주제와 여러 개의 옵저버가 연관된 일대다 관계가 성립된다.

옵저버는 데이터가 변경되었을 때 주제에서 갱신해 주기를 기다리는 입장으로 주제에 **의존성**을 가진다.

옵저버 패턴은 여러 객체가 동일한 데이터를 제어하는 방법보다 더 깔끔한 객체지향 디자인이다.

옵저버 패턴과 관계가 있는 출판-구독(Publish-Subscribe) 패턴이 존재한다. 
미들웨어 시스템에서 종종 쓰이는 복잡한 패턴이다.
책에서 잠깐 언급한 패턴으로 옵저버 패턴과는 어떻게 다른지 나중에 자세히 알아보고 싶다.

## 3. 느슨한 결합
느슨한 결합(Loose Coupling)은 **객체들이 상호작용할 수는 있지만 서로를 잘 모르는 관계**를 의미한다.
느슨한 결합을 활용하면 **변경 사항이 생겨도 유연하게 처리할 수 있는 객체지향 시스템을 구축**할 수 있다.

옵저버 패턴에서 주제와 옵저버 사이에 느슨한 결합이 나타난다.

주제는 옵저버가 특정 인터페이스, Observer 인터페이스를 구현한다는 사실만 안다.

주제는 Observer 인터페이스를 구현하는 객체의 목록에만 의존한다.
실행 중(Runtime)에 하나의 옵저버를 다른 옵저버로 바꿔도 주제는 계속해서 다른 옵저버에게 데이터를 보낸다.

그리고 새로운 형식의 옵저버를 추가할 때도 주제를 변경할 필요가 없다.
새로운 클래스가 옵저버가 되고 싶다면 Observer 인터페이스를 구현하고 옵저버로 등록하기만 하면 된다.
이 과정에서 주제는 신경 쓸 필요가 없다.

주제와 옵저버는 서로 독립적으로 재사용할 수 있다. 
주제나 옵저버는 다른 용도로 활용할 일이 있다고 해도 손쉽게 재사용 가능하다.
주제나 옵저버가 달라져도 주제나 옵저버 인터페이스를 구현한다는 조건만 만족한다면 서로에게 영향을 미치치 않는다.