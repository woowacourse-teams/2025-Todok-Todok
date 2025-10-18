### 🔍 코드 리뷰 체크리스트

#### 📱 리소스 네이밍
- [ ] Layout 파일명이 `<WHAT>_<WHERE>` 패턴을 따르는가?
- [ ] View ID가 `<WHAT>_<DESCRIPTION>` 패턴을 따르는가?
- [ ] Drawable 파일명이 적절한 접두사를 사용하는가?
- [ ] Color 이름이 Material Design 가이드를 따르는가?
- [ ] Dimension 값이 공통 space 값을 사용하는가?

#### 🏗️ 아키텍처
- [ ] ViewModel이 Android Framework 의존성을 가지지 않는가?
- [ ] Repository Pattern이 올바르게 구현되었는가?
- [ ] UseCase가 단일 책임 원칙을 따르는가?
- [ ] 네트워크 모델이 Request/Response 접미사를 사용하는가?

#### 🎭 UI 이벤트
- [ ] UiEvent sealed interface가 적절히 정의되었는가?
- [ ] 이벤트 클래스명이 동작 접두사를 사용하는가?
- [ ] ViewModel에서 이벤트 발생 로직이 분리되었는가?

#### 📝 Kotlin 코드
- [ ] 패키지명이 소문자만 사용하는가?
- [ ] 함수명이 적절한 동사/명사를 사용하는가?
- [ ] Boolean 함수가 is/has/can/should 접두사를 사용하는가?
- [ ] 널 안전성이 고려되었는가?
- [ ] 확장 함수가 적절히 활용되었는가?

#### 🔒 보안 및 성능
- [ ] API Key가 하드코딩되지 않았는가?
- [ ] 민감한 정보가 로그에 출력되지 않는가?
- [ ] ViewBinding이 올바르게 해제되는가?

#### 🧪 테스트
- [ ] 테스트 케이스명이 명확한가?
- [ ] Given-When-Then 구조를 따르는가?
- [ ] 테스트 픽스처가 활용되었는가?

### 🎯 핵심 원칙
1. **일관성**: 동일한 상황에서는 동일한 네이밍 사용
2. **명확성**: 이름만 보고도 역할과 기능을 파악 가능
3. **간결성**: 불필요한 단어 사용 지양
4. **확장성**: 새로운 기능 추가# Android 코드 컨벤션 가이드

## 📖 목차
1. [리소스 명명 규칙](#리소스-명명-규칙)
2. [아키텍처 규칙](#아키텍처-규칙)
3. [UI 이벤트 컨벤션](#ui-이벤트-컨벤션)
4. [Kotlin 코드 컨벤션](#kotlin-코드-컨벤션)
5. [보안 및 성능 가이드라인](#보안-및-성능-가이드라인)
6. [테스트 코드 컨벤션](#테스트-코드-컨벤션)
7. [개발 도구 및 설정](#개발-도구-및-설정)

### 테스트 케이스 구조
```kotlin
class UserRepositoryTest {

    @Test
    fun `사용자 정보를 정상적으로 가져온다`() {
        // Given (준비)
        val userId = "12345"
        val expectedUser = User(id = userId, name = "John")
        every { mockApi.getUser(userId) } returns expectedUser

        // When (실행)
        val result = userRepository.getUser(userId)

        // Then (검증)
        assertEquals(expectedUser, result)
        verify { mockApi.getUser(userId) }
    }
}
```

### 테스트 유틸리티
```kotlin
// TestFixtures.kt
object TestFixtures {
    fun createUser(
        id: String = "test_id",
        name: String = "Test User",
        email: String = "test@example.com"
    ) = User(id = id, name = name, email = email)

    fun createUserList(count: Int = 3): List<User> {
        return (1..count).map { 
            createUser(id = "user_$it", name = "User $it")
        }
    }
}

// 사용 예시
@Test
fun `사용자 목록을 정상적으로 반환한다`() {
    // Given
    val users = TestFixtures.createUserList(5)
    every { mockRepository.getUsers() } returns users

    // When & Then
    val result = useCase.getUsers()
    assertEquals(5, result.size)
}
```
---

## 🔒 보안 및 성능 가이드라인

### 보안 가이드라인

#### API Key 관리
```kotlin
// ❌ 코드에 직접 하드코딩 금지
class ApiService {
    private val apiKey = "your_api_key_here" // 절대 금지!
}

// ✅ BuildConfig 또는 환경변수 사용
class ApiService {
    private val apiKey = BuildConfig.API_KEY
}
```

#### 민감한 데이터 처리
```kotlin
// ✅ 로그에 민감한 정보 출력 금지
class UserRepository {
    fun login(email: String, password: String) {
        // ❌ 절대 금지
        Log.d("Login", "Password: $password")

        // ✅ 안전한 로깅
        Log.d("Login", "Attempting login for user: ${email.take(3)}***")
    }
}
```

### 성능 가이드라인

#### 메모리 최적화
```kotlin
// ✅ ViewBinding을 Fragment에서 올바르게 해제
class MyFragment : Fragment() {
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지
    }
}
```

#### RecyclerView 최적화
```kotlin
class MyAdapter : ListAdapter<Item, MyViewHolder>(DiffCallback()) {

    // ✅ DiffUtil 사용으로 효율적인 업데이트
    class DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // ✅ ViewBinding 사용
        val binding = ItemMyBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }
}
```

---

## 🛠️ 개발 도구 및 설정

## 🎨 리소스 명명 규칙

### Layout
**규칙**: `<WHAT>_<WHERE>`

#### WHAT 접두사

| Prefix | 설명 |
| --- | --- |
| `activity_` | Activity에서 쓰이는 layout |
| `fragment_` | Fragment에서 쓰이는 layout |
| `dialog_` | Dialog에서 쓰이는 layout |
| `view_` | CustomView에서 쓰이는 layout |
| `item_` | RecyclerView, GridView, ListView등에서 ViewHolder에 쓰이는 layout |
| `layout_` | `<include/>`로 재사용되는 공통의 layout |

#### 예시
- `activity_main`: MainActivity의 layout
- `fragment_request`: RequestFragment의 layout
- `dialog_contact`: 문의안내 Dialog의 layout
- `view_rating`: 커스텀으로 만든 RatingView의 layout
- `item_my_car`: 내차량 목록에서 사용되는 각각의 item의 layout
- `layout_dealer_review`: 재사용되는 딜러리뷰 layout

### ID
**규칙**: `<WHAT>_<DESCRIPTION>`

#### View Prefix 규칙
1. Android View는 CamelCase의 대문자를 축약: `TextView -> tv_`
2. 대문자가 1개만 있는 경우 모두 소문자: `Switch -> switch_`
3. CustomView는 전체 이름을 snake_case: `MyCustomView -> my_custom_view`
4. 정의되지 않은 View는 팀에서 상의 후 추가

#### View Prefix 표

| View | Prefix |
| --- | --- |
| TextView | `tv_` |
| ImageView | `iv_` |
| CheckBox | `cb_` |
| RecyclerView | `rv_` |
| EditText | `et_` |
| ProgressBar | `pb_` |
| FrameLayout | `fl_` |
| NestedScrollView | `nsv_` |
| Space | `space_` |
| Switch | `switch_` |
| AbcDeFgh | `adf_` |
| Abcdef | `abcdef_` |
| MyCustomView | `my_custom_view` |

#### 기타 규칙
- VISIBLE/GONE 용도로만 사용하는 View는 `view_xxx` 허용
- 버튼 기능은 `ImageView`, `TextView`만 사용

#### 예시
- `iv_close`: 닫기 ImageView
- `tv_select`: 선택 TextView
- `rv_car_list`: 자동차 목록 RecyclerView
- `view_etc_model`: 기타 모델 화면 LinearLayout

### Drawable
**규칙**: `<WHAT>(_<WHERE>)_<DESCRIPTION>(_<SIZE>)`

#### WHAT 접두사

| Prefix | 설명 |
| --- | --- |
| `btn_` | 버튼으로 쓰이는 이미지 |
| `ic_` | 버튼이 아닌 화면에 보여지는 아이콘 |
| `bg_` | 배경으로 쓰이는 이미지 |
| `img_` | 실제사진이거나 일러스트형태의 이미지 |
| `div_` | divider로 활용되는 이미지 |

#### Selector 상태

| 상태 | Suffix |
| --- | --- |
| Normal | `_normal` |
| Pressed | `_pressed` |
| Focused | `_focused` |
| Disabled | `_disabled` |
| Selected | `_selected` |

#### Background 규칙
- 색상 변화: `bg_white_to_sky_blue.xml`
- 테두리: `bg_white_radius_24dp.xml`
- 선 테두리: `bg_stroke_sky_blue_radius_8dp.xml`

#### 예시
- `btn_call_normal.png`: 전화걸기 버튼 이미지
- `btn_call_pressed.png`: 전화걸기 버튼 눌렸을때의 이미지
- `btn_call.xml`: 전화걸기 버튼 이미지의 selector xml
- `ic_dealer_gift.png`: 딜러가 보내준 기프티콘 아이콘
- `img_splash_chart.png`: 스플래시 화면 차트 이미지

### Dimension
**규칙**: `<WHERE>_<DESCRIPTION>_<WHAT>`

#### 공통 Margin/Padding
```xml
<dimen name="space_x_small">8dp</dimen>
<dimen name="space_small">12dp</dimen>
<dimen name="space_median">16dp</dimen>
<dimen name="space_s_large">18dp</dimen>
<dimen name="space_large">20dp</dimen>
<dimen name="space_x_large">24dp</dimen>
```

#### 특정 화면용
```xml
<dimen name="register_car_item_car_model_start_padding">40dp</dimen>
<dimen name="register_car_item_grade_start_padding">56dp</dimen>
<dimen name="register_car_item_car_detail_start_padding">72dp</dimen>
```

#### Height/Size
- 높이만 지정: `height`
- 1:1 비율: `size`

```xml
<dimen name="toolbar_height">56dp</dimen>
<dimen name="dealer_profile_image_size">48dp</dimen>
```

**중요**: 2번 이상 사용되는 경우는 dimen 정의 필수

### String
**규칙**: `<WHERE>_<DESCRIPTION>`

#### 공통 텍스트
- 여러 곳에서 재사용: `all_<DESCRIPTION>`

#### 예시
- `permission_dialog_camera_title`: 카메라권한 Dialog 제목
- `permission_dialog_camera_description`: 카메라권한 Dialog 설명
- `all_yes`: 네
- `all_ok_understand`: 네, 알겠습니다

#### 문단 처리
```xml
<string name="sample">문단 첫번째줄
        \n문단 두번째줄
        \n문단 세번째줄</string>
```

### Theme/Style

#### 파일 구분
- Theme: `theme.xml`
- Style: `style.xml`

#### 명명 규칙
- parent 이름 패턴에 맞춤
- 수정 사항을 이름에 포함

```xml
<style name="Widget.HeyDealer.Button" parent="@style/Widget.AppCompat.Button">
...
</style>

<style name="Theme.HeyDealer.Transparent" parent="Theme.HeyDealer">
...
</style>
```

#### Base Style
```xml
<style name="Base.Theme" parent="..." />
<style name="Base.Theme.Transparent">...</style>
<style name="HeyDealerTheme" parent="Base.Theme">...</style>
```

### Attribute
```xml
<attr name="numStars" format="integer" />
```

- 이름: `camelCase`
- 기존 `android:xxx` 재사용 권장

### 기타 규칙
- `android:xxxStart/android:xxxEnd` 사용 (Left/Right 대신)
- content_description: `content_description_<화면>_<내용>`

---

## 🏗️ 아키텍처 규칙

### 1. ViewModel 제약사항
- **AAC를 제외한 안드로이드 프레임워크 관련 코드 금지**
- 플랫폼 독립적인 코드 작성 필수

### 2. 도메인 모듈 분리
> **목적**: 도메인에 Framework 의존성이 주입되는 실수를 방지
### 3. Repository Pattern
> 데이터 접근 로직을 추상화하여 데이터 소스와 비즈니스 로직을 분리하는 패턴
- ViewModel은 UI 상태만 관리
- 데이터 소스 접근은 Repository로 분리
- DataSource 추상화로 관심사 분리

### 4. DataSource
> 실제 데이터에 접근하는 구체적인 구현체
- 특정 데이터 소스(DB, API, 파일 등)에 대한 직접적인 접근 로직

### 5. UseCase
- 명확한 비즈니스 로직을 가지는 Repository는 UseCase로 분리
- 클래스명: `xxxUseCase`
- List 가져오는 UseCase: `GetXXXListUseCase`

### 6. 네트워크 모델
- 요청 모델: `xxxRequest`
- 응답 모델: `xxxResponse`

```kotlin
data class [도메인]Response(
    // 필드들
)
```

---

## 🎭 UI 이벤트 컨벤션

### 기본 구조
```kotlin
sealed interface [화면명]UiEvent {
    // 이벤트 클래스들
}
```

### 명명 규칙
- Interface명: `{Screen}UiEvent` (예: `MainUiEvent`, `ProfileUiEvent`)
- UiEvent 접미사 필수

### Event Class 명명 규칙

#### 동작별 접두사
```kotlin
sealed interface MainUiEvent {
    // 네비게이션 이벤트
    data class NavigateTo[Destination](val param: Type) : MainUiEvent

    // 메시지 표시 이벤트
    data class Show[MessageType](val param: Type) : MainUiEvent

    // 데이터 처리 이벤트
    data class Update[DataType](val param: Type) : MainUiEvent

    // 에러 처리 이벤트
    data class ShowError[Context](val throwable: Throwable) : MainUiEvent
}
```

#### 구체적인 예시
```kotlin
sealed interface MainUiEvent {
    // ✅ 올바른 명명
    data class NavigateToDetail(val productId: Long) : MainUiEvent
    data class NavigateToCart(val lastSeenCategory: String?) : MainUiEvent
    data class ShowCannotIncrease(val quantity: Int) : MainUiEvent
    data class ShowErrorMessage(val throwable: Throwable) : MainUiEvent
    data class UpdateCartBadge(val count: Int) : MainUiEvent

    // ❌ 잘못된 명명
    data class GoToDetail(val productId: Long) : MainUiEvent // Navigate 대신 Go
    data class Error(val throwable: Throwable) : MainUiEvent // Show 접두사 누락
    data class CannotIncrease(val quantity: Int) : MainUiEvent // Show 접두사 누락
}
```

### ViewModel 구현 패턴
```kotlin
class [Screen]ViewModel(
    private val dependencies: Dependencies
) : ViewModel() {

    private val _uiEvent = MutableSingleLiveData<[Screen]UiEvent>()
    val uiEvent: SingleLiveData<[Screen]UiEvent> get() = _uiEvent

    private fun onUiEvent(event: [Screen]UiEvent) {
        _uiEvent.value = event
    }
}
```

---

## 📝 Kotlin 코드 컨벤션

### 개념 (Concepts)

#### 비교 (Comparison)
- Boolean 비교: `if (a?.b?.isTraded == true)` 사용
- `if (a?.b?.isTraded ?: false)` 보다 명확함

#### Custom Accessor VS Function VS Property
- **행동을 나타내는 개념**: `function` 사용
- **상태나 값 등 정보를 가져오는 개념**: `custom getter/setter` 사용
- **고정된 값**: 매번 계산하지 않도록 `property` 사용

### 네이밍 규칙

#### Package 명명 규칙
```kotlin
// ✅ 올바른 형태
package kr.co.prnd.domain

// ❌ 잘못된 형태 - underscore 사용 금지
package kr.co.prnd.domain_module

// ✅ 불가피한 경우 camelCase 허용
package com.example.myProject
```

### 함수 이름 규칙

#### View 관련
- 뷰 바인딩 초기화: `initXXX()`
- 시스템바 초기화: `initSystemBar()`
- ViewModel observe: `setupXXX()`
- 이벤트 리스너 설정: `setUpListeners()`

#### 데이터 처리 관련
- 데이터 소스에서 데이터 불러오기: `fetchXXX()`
- 데이터 소스에 데이터 저장: `saveXXX()`
- Return이 있는 데이터 불러오기: `getXXX()`
- 특정 객체 찾기: `findXXX()`
- 데이터 변환: `toXXX()`, `asXXX()`
- 데이터 검증: `validateXXX()`, `isValidXXX()`

#### 화면 이동 관련
- `navigateTo[목적지]`
- `openXXX()` (다이얼로그, 바텀시트 등)
- `closeXXX()` (화면 종료)

### 확장 함수 (Extension Functions)
```kotlin
// ✅ 명확한 용도의 확장 함수
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

// ✅ Context 확장 함수
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}
```

### 상수 및 열거형
```kotlin
// ✅ 상수는 companion object 내에 정의
class ApiService {
    companion object {
        private const val BASE_URL = "https://api.example.com/"
        private const val TIMEOUT_SECONDS = 30L
        const val API_VERSION = "v1" // public 상수
    }
}

// ✅ 열거형 사용
enum class UserType(val displayName: String) {
    ADMIN("관리자"),
    USER("일반사용자"),
    GUEST("게스트");

    companion object {
        fun fromString(value: String): UserType? {
            return values().find { it.name.equals(value, ignoreCase = true) }
        }
    }
}
```

### 람다 표현식
```kotlin
// ✅ 간단한 람다는 it 사용
items.filter { it.isActive }
    .map { it.name }

// ✅ 복잡한 람다는 명시적 파라미터 사용
items.filter { user -> 
    user.isActive && user.hasPermission("read")
}

// ✅ 후행 람다 문법 활용
button.setOnClickListener {
    // 클릭 처리
}

// ✅ 여러 람다가 있는 경우
LaunchedEffect(key1 = userId) {
    // 코루틴 코드
}
```

### 널 안전성 (Null Safety)
```kotlin
// ✅ 안전한 호출 연산자 사용
val length = text?.length ?: 0

// ✅ let 함수 활용
user?.let { user ->
    updateUserProfile(user)
}

// ✅ 엘비스 연산자 활용
fun getUserName(): String {
    return user?.name ?: "Unknown"
}

// ❌ 강제 언래핑 사용 금지 (특별한 경우 제외)
val name = user!!.name // 가능한 피할 것
```
```kotlin
getBrands() // ✅ 올바름
getBrandList() // ❌ 잘못됨

getUsers() // ✅ 올바름
getUserArray() // ❌ 잘못됨
```

#### 팩토리 메서드
```kotlin
class Foo private constructor() {
    companion object {
        fun Foo(): Foo // 클래스 네이밍을 그대로 사용
        fun newInstance(): Foo // 또는 newInstance 사용
    }
}
```

#### Boolean 함수 명명
```kotlin
// ✅ is/has/can/should 접두사 사용
fun isValid(): Boolean
fun hasPermission(): Boolean
fun canProceed(): Boolean
fun shouldUpdate(): Boolean

// ❌ 동사형 사용 금지
fun checkValid(): Boolean
fun validateInput(): Boolean
```

### Listener 규칙

#### Listener 이름
- function 1개: `fun interface On[행위]Listener`
- function 2개 이상: `interface XXXListener`

#### 함수 네이밍 패턴

##### `on[명사][동사]()` 패턴
Publisher가 이벤트만 전달하고 listener가 전적인 책임 처리
```kotlin
fun onClick()
fun onFocusChange()
fun onScrollChange()
fun onAnimationStart()
fun onTextChange()
```

##### `on[명사][동사 과거형]()` 패턴
Publisher가 무언가를 처리하고 listener에게 알려줄 때
```kotlin
fun onScrollStateChanged()
fun onTextChanged()
```

### 포맷팅 (Formatting)

#### 개행 규칙
- 생성자, 함수의 Parameter 정의:
  - 한 줄로 정의 가능하면 한 줄로 작성
  - 불가능하면 각 parameter별로 개행

#### When Statement 규칙
```kotlin
// 한 줄 분기 - 중괄호 사용 안 함
when (value) {
    0 -> return
    // ...
}

// 여러 조건 동시 사용
when (value) {
    foo -> // ...
    bar,
    baz
    -> return
}
```

---

## 🧪 테스트 코드 컨벤션

### 테스트 케이스 이름

#### JUnit5 사용 시
```kotlin
// ✅ 권장
@DisplayName("1 더하기 2는 3이어야 한다")
@Test
fun add() { /* Test Code */ }

@DisplayName("1 더하기 2는 3이어야 한다")
@Test
fun test1() { /* Test Code */ }

// ❌ 비권장
@DisplayName("1 더하기 2는 3이어야 한다")
@Test
fun `1 더하기 2는 3이어야 한다`() { /* Test Code */ }
```

#### JUnit4 사용 시
```kotlin
@Test
fun `1 더하기 2는 3이어야 한다`() { /* Test Code */ }
```

#### 주의사항
- 클래스명이나 함수명은 변경될 수 있으므로 테스트 케이스 이름에 포함 지양

```kotlin
// ✅ 권장
@Test
fun `1 더하기 2는 3이어야 한다`() { /* Test Code */ }

// ⚠️ 가능하지만 비권장
@Test
fun `Calculator에서 add(1, 2)를 호출하면 3을 반환해야 한다`() { /* Test Code */ }
```

### 테스트 클래스 이름

#### 일반 클래스
```kotlin
// Calculator.kt
class Calculator { /* ... */ }

// CalculatorTest.kt
class CalculatorTest { /* ... */ }
```

#### Top-level 함수 파일
```kotlin
// CalculatorExtension.kt
fun Calculator.foo() { /* ... */ }
fun Calculator.bar() { /* ... */ }

// CalculatorExtensionTest.kt (not CalculatorExtensionKtTest.kt)
class CalculatorExtensionTest { /* ... */ }
```

**이유**: 
- IDE 기능 때문에 `Kt`를 붙이는 것이 code smell로 여겨짐
- IDE 기능을 쓰지 못해도 테스트 코드 작성에 치명적 이슈 없음

---

## 📋 요약

### 핵심 원칙
1. **일관성**: 동일한 상황에서는 동일한 네이밍 사용
2. **명확성**: 이름만 보고도 역할과 기능을 파악 가능
3. **간결성**: 불필요한 단어 사용 지양
4. **확장성**: 새로운 기능 추가 시에도 일관된 패턴 유지
