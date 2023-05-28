/*
 * MIT License
 *
 * Copyright (c) 2018 Soojeong Shin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.android.newsfeed.utils;

/**
 * хранит константы для приложения.
 */

public class Constants {

    /**
     * Создаёт приватный конструктор, так как никто не должен создавать объект {@link Constants}.
     */
    private Constants() {
    }

    /**  Извлекает ключ, связанный с объектом JSONObject */
    static final String JSON_KEY_RESPONSE = "response";
    static final String JSON_KEY_RESULTS = "results";
    static final String JSON_KEY_WEB_TITLE = "webTitle";
    static final String JSON_KEY_SECTION_NAME = "sectionName";
    static final String JSON_KEY_WEB_PUBLICATION_DATE = "webPublicationDate";
    static final String JSON_KEY_WEB_URL = "webUrl";
    static final String JSON_KEY_TAGS = "tags";
    static final String JSON_KEY_FIELDS = "fields";
    static final String JSON_KEY_THUMBNAIL = "thumbnail";
    static final String JSON_KEY_TRAIL_TEXT = "trailText";

    /** Read timeout for setting up the HTTP request */
    static final int READ_TIMEOUT = 10000; /* milliseconds */

    /** Connect timeout for setting up the HTTP request */
    static final int CONNECT_TIMEOUT = 15000; /* milliseconds */

    /** Код ответа HTTP когда запрос успешен */
    static final int SUCCESS_RESPONSE_CODE = 200;

    /** Метод запроса типа  "GET" для считывания информации с сервера */
    static final String REQUEST_METHOD_GET = "GET";

    /** URL для информации о новости из the guardian data set */
    public static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search";

    /** Параметры */
    public static final String QUERY_PARAM = "q";
    public static final String ORDER_BY_PARAM = "order-by";
    public static final String PAGE_SIZE_PARAM = "page-size";
    public static final String ORDER_DATE_PARAM = "order-date";
    public static final String FROM_DATE_PARAM = "from-date";
    public static final String SHOW_FIELDS_PARAM = "show-fields";
    public static final String FORMAT_PARAM = "format";
    public static final String SHOW_TAGS_PARAM = "show-tags";
    public static final String API_KEY_PARAM = "api-key";
    public static final String SECTION_PARAM = "section";

    /** Показываемые поля, которые мы хотим, чтобы API вернул */
    public static final String SHOW_FIELDS = "thumbnail,trailText";

    /** Формат, который мы хотим, чтобы API вернул */
    public static final String FORMAT = "json";

    /** Показываемые теги, которые мы хотим, чтобы API вернул */
    public static final String SHOW_TAGS = "contributor";

    /** API Key */
    public static final String API_KEY = "61040c6c-ba7a-4869-b215-d4d21a013524"; // Use your API Key when API rate limit exceeded
    //else type "test" instead of an API key

    /** Default number to set the image on the top of the textView */
    public static final int DEFAULT_NUMBER = 0;

    /** Константы для каждого фрагмента */
    public static final int HOME = 0;
    public static final int WORLD = 1;
    public static final int SCIENCE = 2;
    public static final int SPORT = 3;
    public static final int ENVIRONMENT = 4;
    public static final int SOCIETY = 5;
    public static final int FASHION = 6;
    public static final int BUSINESS = 7;
    public static final int CULTURE = 8;

}
