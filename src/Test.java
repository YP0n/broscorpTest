
/**
 * Програма для обробки та сортування дат у вказаному форматі
 * залишковим періодом відносно поточної дати.
 *
 * @author ua.ypon 22.12.2023
 */
public class Test {
    public static void main(String[] args) {
        // Тестові дати у форматі "dd-MM-yyyy"
        String[] testArgs = {
                "20-12-2004",
                "14-05-2204",
                "30-11-2023",
                "15-01-2024"
        };

        // Створення масиву для обробки тестових дат
        String[] values = new String[testArgs.length];
        for (int i = 0; i < testArgs.length; i++) {
            values[i] = testArgs[i];
        }

        // Обробка та сортування тестових дат
        String[] sortedEvens = sorted(values);

        // Виведення відсортованих результатів
        for (String ev : sortedEvens) {
            System.out.println(ev);
        }
    }

    /**
     * Метод для обробки та сортування дат.
     *
     * @param events Масив рядків, представляючих дати у форматі "dd-MM-yyyy"
     * @return Відсортований масив рядків із залишковим періодом відносно поточної дати
     */
    public static String[] sorted(String[] events) {
        // Форматтер для розбору та форматування дат
        final java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Поточна дата
        final java.time.LocalDate now = java.time.LocalDate.of(2000, 1, 1);

        // Список для зберігання результатів
        java.util.List<String> listOfEvents = new java.util.ArrayList<>();

        // Обробка кожної тестової дати
        for (String event : events) {
            // Знаходження дати в рядку та конвертація в LocalDate
            java.util.Optional<java.time.LocalDate> date = findDateInLine(event, formatter);
            if (date.isEmpty()) continue;

            // Розрахунок залишкового періоду відносно поточної дати
            java.time.Period timeLeft = java.time.Period.between(now, date.get());

            // Визначення індексу дати в рядку та витягування заголовка
            int dateIndex = event.indexOf(date.get().format(formatter));
            String title = event.substring(dateIndex + 10);

            // Створення рядка з залишковим періодом та заголовком
            StringBuilder sb = new StringBuilder();
            sb.append(timeLeft.getYears()).append("роки;").append(timeLeft.getMonths()).append("місяці;")
                    .append(timeLeft.getDays()).append("дні-").append(title);

            // Додавання до списку результату
            listOfEvents.add(sb.toString());
        }

        // Сортування залишкових періодів
        listOfEvents.sort(String::compareTo);

        // Перетворення списку в масив та повернення результату
        return listOfEvents.toArray(new String[listOfEvents.size()]);
    }

    /**
     * Метод для знаходження дати в рядку за допомогою регулярного виразу.
     *
     * @param line      Рядок, в якому шукається дата
     * @param formatter Форматтер для конвертації рядка у LocalDate
     * @return Optional з LocalDate, якщо дата знайдена; інакше пустий Optional
     */
    private static java.util.Optional<java.time.LocalDate> findDateInLine(String line,
                                                                          java.time.format.DateTimeFormatter formatter) {
        // Регулярний вираз для знаходження дати
        /*
        d: Це символ для будь-якої цифри (0-9).
        {1,2}: Ця конструкція вказує на кількість попередніх символів. У цьому випадку,
         це означає від одного до двох цифр. Тобто, може бути одна цифра або дві цифри.
        -: Це просто тире, яке очікується між першою і другою групами цифр.
        d{1,2}: Ще раз, це одна або дві цифри, представляють місяць.
        -: Тире, що розділяє місяць та день.
        \\d{4}: Це чотири цифри, представляють рік.
        Отже, загальна структура регулярного виразу вказує на те, що він очікує рядки у форматі "день-місяць-рік",
         де день і місяць можуть бути однієї або двох цифр, а рік повинен бути чотиризначним числом.
         */
        String regex = "\\d{1,2}-\\d{1,2}-\\d{4}";
        java.util.regex.Matcher m = java.util.regex.Pattern.compile(regex).matcher(line);

        // Перевірка, чи дата знайдена
        //Метод find() в регулярних виразах є частиною інтерфейсу Matcher, який використовується
        // для виконання пошуку в рядках за допомогою регулярних виразів.
        if (m.find()) {
            // Конвертація рядка у LocalDate та повернення Optional
            java.time.LocalDate date = java.time.LocalDate.parse(m.group(), formatter);
            return java.util.Optional.of(date);
        }

        // Повернення пустого Optional, якщо дата не знайдена
        return java.util.Optional.empty();
    }
}
