package org.akerigan.askue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Vlad Vinichenko (akerigan@gmail.com)
 * @since 10.10.2010 20:09:11 (Europe/Moscow)
 */
public class AppConstans {

    public static final String PLATFORM_LOOK_AND_FEEL =
            "com.jgoodies.looks.plastic.PlasticXPLookAndFeel";

    public static final Pattern PATTERN_DBF_FILE_NAME =
            Pattern.compile("[Dd]{2}\\d{6}\\.[Dd][Bb][Ff]");

    public static final String ENCODING_DBF_DEFAULT =
            "CP866";

    public static final DateFormat DATE_FORMAT_DATES_LIST =
            new SimpleDateFormat("dd.MM.yyyy");

    public static final DateFormat DATE_FORMAT_SHORT =
            new SimpleDateFormat("dd.MM");

    public static final String MESSAGE_TITLE =
            "Отчеты АСКУЭ :: Менеджер данных";

    public static final String MESSAGE_MENU_FILE =
            "Файл";

    public static final String MESSAGE_MENU_IMPORT_DBF =
            "Импорт dbf";

    public static final String MESSAGE_MENU_CLOSE =
            "Закрыть";

    public static final String MESSAGE_IMPORT_ERROR =
            "Ошибка импорта файла";

    public static final String MESSAGE_EXPORT_TO_EXCEL =
            "Экспорт в Excel";

    public static final String MESSAGE_IMPORTED_DATES =
            "Импортированные даты:";

    public static final String MESSAGE_NO_DATES_SELECTED =
            "Не выбрано ни одной даты";

    public static final String MESSAGE_INFORMATION =
            "Информация";

    public static final String MESSAGE_ERROR =
            "Ошибка";

    public static final String MESSAGE_EXPORTED_SUCCESSFULLY =
            "Данные успешно экспортированы";

    public static final String MESSAGE_EXPORT_ERROR =
            "Ошибка импорта в excel: ";

    public static final String MESSAGE_FEEDER_ADD =
            "Добавить фидер";

    public static final String MESSAGE_FEEDER_REMOVE =
            "Удалить фидер";

    public static final String MESSAGE_FEEDERS =
            "Фидеры:";

    public static final String MESSAGE_FEEDER_REMOVE_CONFIRM =
            "Удалить фидер?";

    public static final String MESSAGE_CONFIRM =
            "Подтверждение";

    public static final String MESSAGE_FEEDER_REMOVAL_RESTRICTED =
            "Невозможно удалить фидер: есть ассоциированные с ним счетчики";

    public static final String MESSAGE_FEEDER_REMOVING =
            "Удаление фидера";

    public static final String MESSAGE_DEVICES =
            "Счетчики:";

    public static final String MESSAGE_INVALID_DBF_NAME =
            "Неправильный dbf файл, имя файла должно начинаться " +
                    "с dd, затем 6 цифр даты, затем расширение dbf: ";

    public static final String MESSAGE_INVALID_DBF_NAME_DATE =
            "Неправильная дата в имени файла: ";

    public static final String MESSAGE_INVALID_DBF_COLUMNS_COUNT =
            "Неправильный dbf файл: должно быть 50 колонок";

    public static final String MESSAGE_INVALID_DBF_COLUMNS_TYPE =
            "Неправильный dbf файл: все колонки должны быть числового типа";

    public static final String COLUMN_FEEDER_CODE =
            "Код фидера";

    public static final String COLUMN_FEEDER_NAME =
            "Фидер";

    public static final String DUBLICATE_FEEDER_NAME_PART_1 =
            " Копия(";

    public static final String DUBLICATE_FEEDER_NAME_PART_2 =
            " Копия(";

    public static final String COLUMN_DEVICE_CODE =
            "Код счетчика";

    public static final String COLUMN_DEVICE_FEEDER =
            "Фидер";

    public static final String COLUMN_DEVICE_REACTIVE =
            "Реактивный";

    public static final String COLUMN_DEVICE_ENABLED =
            "Включен";

    public static final String SHEET_ACTIVE =
            "Активные присоединения";

    public static final String SHEET_REACTIVE =
            "Реактивные присоединения";

    public static final String SHEET_COLUMN_PERIOD =
            "Дата, время";

    public static final String SUFFIX_ACTIVE =
            " АП, кВт*ч";

    public static final String SUFFIX_REACTIVE =
            " РП, кВАр*ч";

    // characters * 256
    public static final int WIDTH_COLUMN_PERIOD = 16 * 256;

    // characters count
    public static final int WIDTH_COLUMN_DEFAULT = 15;

    // points * 20
    public static final short HEIGHT_ROW_DEFAULT = 30 * 20;

    public static final Map<Integer, String> PERIOD_STRING;

    static {
        PERIOD_STRING = new HashMap<Integer, String>();
        for (int period = 0; period < 48; ++period) {
            PERIOD_STRING.put(period, ", " + getPeriodAsString(period) + ":" + getPeriodAsString(period + 1));
        }
    }

    private static String getPeriodAsString(int period) {
        int hours = period / 2;
        int minutes = period % 2 * 30;
        return String.format("%02d-%02d", hours, minutes);
    }
}
