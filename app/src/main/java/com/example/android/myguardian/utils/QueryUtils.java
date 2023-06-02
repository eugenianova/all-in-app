package com.example.android.myguardian.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.android.myguardian.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Методы-помощники, относящиеся к созданию запроса и получению новостей из Guardian.
 */
public class QueryUtils {

    /** Теги для лог сообщений */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Создаём приватный конструктор, так как никто не должен создавать объект {@link QueryUtils}.
     */
    private QueryUtils() {
    }

    /**
     *Запросить набор данных Guardian и вернуть список объектов {@link News}.
     */
    public static List<News> fetchNewsData(String requestUrl) {
        // Создаём URL объект
        URL url = createUrl(requestUrl);

        // Выполняем HTTP запрос к URL и получаем JSON ответ
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Извлекаем релевантные поля из JSON ответа и создаём  список {@link News}
        List<News> newsList = extractFeatureFromJSON(jsonResponse);

        // Возвращаем список {@link News}
        return newsList;
    }

    /**
     * Возвращаем новый объект URL object из данной строки URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL.", e);
        }
        return url;
    }

    /**
     * Создаём HTTP запрос к данной URL и возвращаем String как ответ.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // Если URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(Constants.READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(Constants.CONNECT_TIMEOUT /* milliseconds */);
            urlConnection.setRequestMethod(Constants.REQUEST_METHOD_GET);
            urlConnection.connect();

            // Если запрос был успешен (response code 200),
            // тогда считываем поток ввода и парсим ответ.
            if (urlConnection.getResponseCode() == Constants.SUCCESS_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Закрытие потока ввода может вызвать IOException, поэтому сигнатура метода
                // makeHttpRequest(URL url) специфицирует, что исключение IOException
                // может быть вызвано.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * преобразуем {@link InputStream} в String которая содержит целый ответ JSON с сервера
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     *Возвращаем список объектов {@link News} которые были посмтроены из парсинга данного JSON ответа.
     */
    private static List<News> extractFeatureFromJSON(String newsJSON) {
        // Если строка JSON пустая или null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        // Создаём пустой ArrayList к которому мы можем начать добавлять новости
        List<News> newsList = new ArrayList<>();

        // Пытаемся парсить строку-ответ JSON. Если есть проблема с форматированием JSON
        // возникнет объект исключения JSONException.
        try {
            // Созадём JSONObject из строки ответа JSON
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Извлекаем JSONObject связанный с ключом, называющимся "response"
            JSONObject responseJsonObject = baseJsonResponse.getJSONObject(Constants.JSON_KEY_RESPONSE);

            // Извлекаемe JSONArray относящийся к ключу, называющемуся "results"
            JSONArray resultsArray = responseJsonObject.getJSONArray(Constants.JSON_KEY_RESULTS);

            // Для каждого элемента в resultsArray, создаём объект {@link News}
            for (int i = 0; i < resultsArray.length(); i++) {

                // Получаем одну новость с позицией i внутри списка новостей
                JSONObject currentNews = resultsArray.getJSONObject(i);
                // Для данной новости, извлекаем значение для ключа с названием "webTitle"
                String webTitle = currentNews.getString(Constants.JSON_KEY_WEB_TITLE);
                // Для этой новости извлекаем значение ключа с названием "sectionName"
                String sectionName = currentNews.getString(Constants.JSON_KEY_SECTION_NAME);
                // Для этой новости извлекаем значение ключа с названием "webPublicationDate"
                String webPublicationDate = currentNews.getString(Constants.JSON_KEY_WEB_PUBLICATION_DATE);
                // Для этой новости извлекаем значение ключа с названием "webUrl"
                String webUrl = currentNews.getString(Constants.JSON_KEY_WEB_URL);

                // Для данной новости, если она содержит ключ с названием "tags", извлекаем JSONArray
                // aсвязанный с ключом "tags"
                String author = null;
                if (currentNews.has(Constants.JSON_KEY_TAGS)) {
                    // Извлекаем JSONArray связанный с ключом с названием "tags"
                    JSONArray tagsArray = currentNews.getJSONArray(Constants.JSON_KEY_TAGS);
                    if (tagsArray.length() != 0) {
                        // извлекаем первый JSONObject в массиве tagsArray
                        JSONObject firstTagsItem = tagsArray.getJSONObject(0);
                        // извлекаем значение для ключа с названием "webTitle"
                        author = firstTagsItem.getString(Constants.JSON_KEY_WEB_TITLE);
                    }
                }

                // Для данной новости, если она содержит ключ с названием "fields", извлекаем JSONObject
                // связанный с ключом "fields"
                String thumbnail = null;
                String trailText = null;
                if (currentNews.has(Constants.JSON_KEY_FIELDS)) {
                    // Извлекаем JSONObject связанный с ключом "fields"
                    JSONObject fieldsObject = currentNews.getJSONObject(Constants.JSON_KEY_FIELDS);
                    // Если есть ключ назваеющийся "thumbnail", извлекаем значение для ключа "thumbnail"
                    if (fieldsObject.has(Constants.JSON_KEY_THUMBNAIL)) {
                        thumbnail = fieldsObject.getString(Constants.JSON_KEY_THUMBNAIL);
                    }
                    // Если есть ключ с названием "trailText", извлекаем значение "trailText"
                    if (fieldsObject.has(Constants.JSON_KEY_TRAIL_TEXT)) {
                        trailText = fieldsObject.getString(Constants.JSON_KEY_TRAIL_TEXT);
                    }
                }

                // Создаём новый объект {@link News} с заголовком и url из ответа JSON.
                News news = new News(webTitle, sectionName, author, webPublicationDate, webUrl, thumbnail, trailText);

                // Добавляем новый {@link News} в список newsList.
                newsList.add(news);
            }
        } catch (JSONException e) {
            // Если возникает ошибка во время исполнения какого-дибо выражения выше в блоке "try",
            // проверяем исключение здесь, чтобы приложение не крашилось. Выводим лог сообщение с информацией об исключении
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        // Врзвращаем список новостей.
        return newsList;
    }
}
