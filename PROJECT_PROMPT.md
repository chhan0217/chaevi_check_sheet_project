# 프로젝트 구현 프롬프트

아래 기준으로 Android 앱을 개발해 주세요.

## 프로젝트

전기차 충전기 정기점검표 앱을 만든다. 이 앱은 채비관제 운영 충전기의 운영 수익 극대화를 위해 집중관리 대상 충전기를 관리하고, 매월 1회 정기점검 결과를 현장에서 즉시 기록하기 위한 앱이다.

집중관리 대상은 상위 매출 약 45%에 해당하는 충전기를 우선으로 한다. 사용자는 충전기 목록을 확인하고, 충전기를 선택해 해당 월의 정기점검표를 작성하며, 점검 결과를 충전기와 점검월 기준으로 기록하고 조회할 수 있어야 한다.

## 개발 목표

- 현장에서 종이 점검표나 별도 사후 입력 없이 앱에서 즉시 기록할 수 있게 한다.
- 정기점검 항목별 정상, 이상, 해당 없음 상태를 빠르게 입력할 수 있게 한다.
- 이상 사항은 메모나 증빙 정보를 남길 수 있게 한다.
- 점검 완료, 이상 있음, 미점검 상태를 명확히 구분한다.
- 점검 기록은 충전기, 점검월, 점검자 기준으로 정확히 관리한다.

## 기술 기준

- Kotlin 기반 Android 앱으로 개발한다.
- 아키텍처는 클린 아키텍처를 따른다.
- 의존성 방향은 `Presentation -> Domain <- Data`를 지킨다.
- View는 Jetpack Compose로 작성한다.
- 화면 이동은 Navigation 3를 사용한다.
- 네트워크는 Retrofit, OkHttp, Kotlinx Serialization 조합을 사용한다.
- 의존성 주입은 Dagger Hilt를 사용한다.
- 테스트 코드 중심으로 개발한다.
- Play Store에는 배포하지 않고 내부 APK로만 배포한다.
- release APK는 내부 배포용 서명 키와 버전 이력 관리 기준을 따른다.
- 점검항목 데이터는 설정 화면에서 다운로드한 고정 형식 엑셀 파일을 앱 내부 저장소에 보관한 뒤 파싱해서 사용한다.

## 계층 기준

- `Presentation` 계층은 Compose 화면, Navigation 3, ViewModel, UI 상태, 사용자 이벤트를 담당한다.
- `Domain` 계층은 Entity, Repository 인터페이스, UseCase, 업무 규칙을 담당한다.
- `Data` 계층은 Retrofit API, DTO, DataSource, Repository 구현체, Mapper를 담당한다.
- Repository 인터페이스는 `Domain`에 두고 구현체는 `Data`에 둔다.
- Composable과 ViewModel은 Retrofit API, DB, DataSource를 직접 호출하지 않는다.
- Data 모델과 Domain 모델은 Mapper로 분리한다.
- ViewModel은 `@HiltViewModel`과 `@Inject constructor`를 기본으로 작성한다.
- Repository 구현체, DataSource, Retrofit, OkHttp는 Hilt Module을 통해 주입한다.
- Domain 계층은 Hilt에 직접 의존하지 않는다.
- 엑셀 다운로드, 파일 저장, 엑셀 파싱은 `Data` 계층에서 처리한다.
- 엑셀 파싱 결과는 Repository와 UseCase를 거쳐 ViewModel에 전달한다.

## 패키지 기준

```text
app/src/main/java/.../
  presentation/
    navigation/
    checklist/
    charger/
    common/
  domain/
    model/
    repository/
    usecase/
  data/
    local/
    remote/
    repository/
    mapper/

app/src/test/java/.../
  domain/
  presentation/
  data/

app/src/androidTest/java/.../
  presentation/
  data/
```

## 테스트 기준

- 업무 규칙은 `Domain` 계층 단위 테스트로 먼저 검증한다.
- UseCase는 입력, 정상 결과, 예외 상황을 테스트한다.
- ViewModel은 UI 상태 변경과 사용자 이벤트 처리 결과를 테스트한다.
- Repository 구현체는 원격 API, 로컬 저장소, 캐시 동작을 분리해서 테스트한다.
- 버그 수정 시에는 재현 테스트를 먼저 작성한 뒤 수정한다.
- Android 프레임워크가 필요 없는 로직은 JVM 단위 테스트로 작성한다.

## 구현 방식

- 한 번에 큰 기능을 만들지 말고 작은 단위로 나누어 구현한다.
- 기능 구현 전 필요한 테스트를 먼저 작성하거나 구현과 함께 작성한다.
- 구현 후 빌드와 관련 테스트를 별도 확인 없이 실행하고, 실패하면 수정 후 다시 검증한다.
- Hilt/KAPT 생성 코드 충돌을 피하기 위해 Gradle 빌드와 테스트 검증은 순차 실행한다.
- 기존 코드 구조와 네이밍을 우선 따른다.
- 신규 화면은 XML layout이 아니라 Compose로 작성한다.
- 각 View는 `Route`와 `Screen`을 구분한다.
- `Route`는 Navigation 인자, ViewModel 연결, 상태 수집, 화면 이동 이벤트 연결을 담당한다.
- `Screen`은 순수 UI로 작성하고 상태와 이벤트 콜백을 파라미터로 전달받는다.
- 각 View의 UI 업데이트는 ViewModel이 제공하는 `StateFlow` 기반 `UiState`로 처리한다.
- `Route`는 `StateFlow`를 lifecycle-aware 방식으로 수집해서 `Screen`에 전달한다.
- `Screen` 내부에서 장기 유지되어야 하는 화면 상태를 직접 `remember`로 관리하지 않는다.
- 화면 간 데이터 전달은 식별자 중심으로 최소화한다.
- 상세 데이터는 전달받은 식별자를 기준으로 ViewModel에서 조회한다.
- 사용자에게 보이는 문구는 짧고 명확하게 작성한다.
- API 통신, 초기 데이터 로딩, 저장 처리 중에는 공통 로딩 View를 사용한다.
- 임시 코드, 미사용 파일, 불필요한 디버그 로그는 남기지 않는다.
- 내부 APK 배포를 고려해 versionCode, versionName, release signing, APK 파일명 규칙을 관리한다.

## 우선 구현 후보

1. 프로젝트 Gradle 설정을 Compose, Navigation 3, Retrofit, OkHttp, Kotlinx Serialization, 테스트 라이브러리 기준으로 정리한다.
2. 클린 아키텍처 패키지 구조를 만든다.
3. 충전기 Domain 모델과 정기점검 Domain 모델을 정의한다.
4. 집중관리 대상 충전기 목록 UseCase 테스트를 작성한다.
5. 충전기 목록 화면과 Navigation 기본 구조를 Compose로 만든다.
6. 정기점검표 작성 화면과 ViewModel 상태 관리를 구현한다.
7. 내부 APK 배포를 위한 release 빌드, 서명, 파일명 규칙을 정리한다.
8. 설정 화면에서 엑셀 파일 다운로드, 내부 저장, 고정 양식 파싱 구조를 만든다.
