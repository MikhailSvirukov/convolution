# Convolution Filters

Приложение для применения фильтров свёртки ([convolution](https://lodev.org/cgtutor/filtering.html)) к изображениям с различными стратегиями параллельной обработки 

## Quick Start

Сборка проекта:
```bash
./gradlew build
```

### Commands

#### sequential
Последовательная обработка изображения
```bash
./gradlew run --args "sequential <filename> <filter>"
```
- `filename` - путь к изображению (требуется)
- `filter` - тип фильтра (требуется)

#### coroutines_rows
Параллельная обработка по строкам
```bash
./gradlew run --args "coroutines_rows <filename> <filter> [tasks]"
```
- `filename` - путь к изображению (требуется)
- `filter` - тип фильтра (требуется)
- `tasks` - количество потоков (опционально - по умолчанию по числу строк)

#### coroutines_columns
Параллельная обработка по столбцам
```bash
./gradlew run --args "coroutines_columns <filename> <filter> [tasks]"
```
- `filename` - путь к изображению (требуется)
- `filter` - тип фильтра (требуется)
- `tasks` - количество потоков (опционально - по умолчанию по числу столбцов)

#### coroutines_segment
Параллельная обработка сегментами
```bash
./gradlew run --args "coroutines_segment <filename> <filter> [tasks]"
```
- `filename` - путь к изображению (требуется)
- `filter` - тип фильтра (требуется)
- `tasks` - количество сегментов (опционально - по умолчанию 8 сегментов)

#### coroutines_chunks
Параллельная обработка 2D блоками
```bash
./gradlew run --args "coroutines_chunks <filename> <filter> [tasksX] [tasksY]"
```
- `filename` - путь к изображению (требуется)
- `filter` - тип фильтра (требуется)
- `tasksX` - число разбиения по горизонтали (опционально - по умолчанию 8)
- `tasksY` - количество блоков по вертикали (опционально - по умолчанию 8)

## Benchmarks

### Single Image: snow.jpg (8160×6144)

![Benchmark Results - Snow](img/graphics/plot_snow.png)

| Algorithm | Time (ms) | Error (ms) |
|-----------|-----------|-----------|
| coroutinesByPixel | 84608 | 3054 |
| coroutinesChunks | 552 | 10 |
| coroutinesColumns | 591 | 6 |
| coroutinesRows | 532 | 7 |
| coroutinesSegment | 641 | 15 |
| sequential | 2546 | 56 |

### Multiple Images: img/test (5 images)

![Benchmark Results - MultIO](img/graphics/mult_io.png)

| Algorithm | Time (ms) | Error (ms) |
|-----------|-----------|-----------|
| coroutinesByPixel | 26148 | 2232 |
| coroutinesChunks | 207 | 4 |
| coroutinesColumns | 233 | 9 |
| coroutinesRows | 255 | 9 |
| coroutinesSegment | 281 | 13 |
| sequential | 1499 | 24 |

## Conclusions


---





