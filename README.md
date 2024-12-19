# Решатель 7 лабораторной работы по информатике

Программа разработана на языке Kotlin с использованием фреймворка Compose Multiplatform.

[Демо-версия](http://s.pank.su/)

## Структура проекта (чистая архитектура)

### 1. **Слой данных (data)**
Отвечает за кэширование вычислений и хранение данных.  
[Исходный код](composeApp/src/commonMain/kotlin/su/pank/solver/data/)

- **[calculation](composeApp/src/commonMain/kotlin/su/pank/solver/data/calculation/):** хранение вычислений для первой таблицы.
- **[di](composeApp/src/commonMain/kotlin/su/pank/solver/data/di/):** настройки dependency injection.
- **[fano](composeApp/src/commonMain/kotlin/su/pank/solver/data/fano/):** вычисления кодировки по Шеннону-Фано.
- **[huffman](composeApp/src/commonMain/kotlin/su/pank/solver/data/huffman/):** вычисления кодировки по Хаффману.
- **[model](composeApp/src/commonMain/kotlin/su/pank/solver/data/model/):** общие модели данных.

---

### 2. **Слой бизнес-логики (domain)**
Содержит основные алгоритмы и вычисления.  
[Исходный код](composeApp/src/commonMain/kotlin/su/pank/solver/domain/)

- **[di](composeApp/src/commonMain/kotlin/su/pank/solver/domain/di/):** настройки dependency injection.
- **[HuffmanCodingUseCase](composeApp/src/commonMain/kotlin/su/pank/solver/domain/HuffmanCodingUseCase.kt):** алгоритм кодировки по Хаффману.
- **[ProbabilityTableUseCase](composeApp/src/commonMain/kotlin/su/pank/solver/domain/ProbabilityTableUseCase.kt):** расчёт вероятности и энтропии.
- **[ShanonFanoCodingUseCase](composeApp/src/commonMain/kotlin/su/pank/solver/domain/ShanonFanoCodingUseCase.kt):** алгоритм кодировки по Шеннону-Фано.

---

### 3. **Слой пользовательского интерфейса (ui)**
Реализован с использованием паттерна MVVM.  
[Исходный код](composeApp/src/commonMain/kotlin/su/pank/solver/ui/)

- **[di](composeApp/src/commonMain/kotlin/su/pank/solver/ui/di/):** настройки dependency injection.
- **[main](composeApp/src/commonMain/kotlin/su/pank/solver/ui/main/):** главный экран.
  - **[another_table](composeApp/src/commonMain/kotlin/su/pank/solver/ui/main/another_table/):** таблица сравнения ASCII.
  - **[entropy_table](composeApp/src/commonMain/kotlin/su/pank/solver/ui/main/entropy_table/):** таблица вероятности и энтропии.
  - **[huffman](composeApp/src/commonMain/kotlin/su/pank/solver/ui/main/huffman/):** отображение данных кодировки Хаффмана.
  - **[shanon_fano](composeApp/src/commonMain/kotlin/su/pank/solver/ui/main/shanon_fano/):** отображение данных кодировки Шеннона-Фано.
  - **[Main](composeApp/src/commonMain/kotlin/su/pank/solver/ui/main/Main.kt):** реализация главного экрана.
  - **[MainNavigation](composeApp/src/commonMain/kotlin/su/pank/solver/ui/main/MainNavigation.kt):** навигация главного экрана.

---

## Известные проблемы

1. В data-слое используются списки вместо словарей, что может снижать производительность.
2. Слои `domain` и `data` не синхронизированы в одном потоке данных.
3. Алгоритм построения графа частично разработан с помощью нейросети, что может повлиять на читаемость и предсказуемость кода.

---

### Описание

Приложение предназначено для решения задач 7 лабораторной работы по информатике, включая кодирование данных алгоритмами Хаффмана и Шеннона-Фано, а также расчёт вероятности и энтропии.