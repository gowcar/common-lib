package com.jiaxintec.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Class Name:  HttpUtils
 * Author:      Jacky Zhang
 * Create Time: 2020-03-11 上午12:59
 * Description:
 */
@Slf4j
public class HttpUtils
{
    public static RestTemplate restTemplate;
    public final static HttpHeaders headers = new HttpHeaders();
    public final static HttpHeaders multipartFormHeaders = new HttpHeaders();
    public final static HttpHeaders formHeaders = new HttpHeaders();

    private static final int CONNECTION_TIMEOUT = 10 * 1000;
    private static final int READ_TIMEOUT = 10 * 1000;

    static {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(CONNECTION_TIMEOUT);
        httpRequestFactory.setConnectTimeout(CONNECTION_TIMEOUT);
        httpRequestFactory.setReadTimeout(READ_TIMEOUT);
        restTemplate = new RestTemplate(httpRequestFactory);

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        formHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        formHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        multipartFormHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        multipartFormHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        multipartFormHeaders.setConnection("Keep-Alive");
        multipartFormHeaders.set("Charset", "UTF-8");
    }

    public static <M> M getRequest(String url, Class<M> responseType) {
        HttpEntity<Object> entity = new HttpEntity((Object) null, headers);
        return request(url, HttpMethod.GET, entity, responseType);
    }

    public static <M> M getRequest(String url, Map<String, String> cookies, Class<M> responseType) {
        if (cookies != null && cookies.size() > 0) {
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> cookie : cookies.entrySet()) {
                buf.append(cookie.getKey()).append("=").append(cookie.getValue()).append(";");
            }
            headers.set("Cookie", buf.toString());
        }
        HttpEntity<Object> entity = new HttpEntity((Object) null, headers);
        return request(url, HttpMethod.GET, entity, responseType);
    }

    public static <M> M getRequest(String url, ParameterizedTypeReference<M> responseType) {
        HttpEntity<Object> entity = new HttpEntity((Object) null, headers);
        return request(url, HttpMethod.GET, entity, responseType);
    }

    public static <M> M getJsonRequest(String url, Class<M> responseType, String json) {
        return restTemplate.getForObject(url, responseType, new Object[]{json});
    }

    public static <M> ArrayList<M> getListRequest(String url, ParameterizedTypeReference<ArrayList<M>> reference) {
        HttpEntity<Object> entity = new HttpEntity((Object) null, headers);
        return (ArrayList) request(url, HttpMethod.GET, entity, reference);
    }

    public static <M> M postRequest(String url, Object body, ParameterizedTypeReference<M> responseType) {
        HttpEntity<Object> entity = new HttpEntity(body, headers);
        return request(url, HttpMethod.POST, entity, responseType);
    }

    public static <M> ArrayList<M> postListRequest(String url, Object body, ParameterizedTypeReference<ArrayList<M>> reference) {
        HttpEntity<Object> entity = new HttpEntity(body, headers);
        return (ArrayList) request(url, HttpMethod.POST, entity, reference);
    }

    public static <M> M postRequest(String url, Class<M> responseType) {
        HttpEntity<Object> entity = new HttpEntity((Object) null, headers);
        return request(url, HttpMethod.POST, entity, responseType);
    }

    public static <M> M postRequest(String url, Object body, Class<M> responseType) {
        HttpEntity<Object> entity = new HttpEntity(body, headers);
        return request(url, HttpMethod.POST, entity, responseType);
    }

    public static <M> ResponseEntity postRequestWithEntity(String url, Object body, Map<String, String> cookies, Class<M> responseType) {
        HttpHeaders newHeaders = new HttpHeaders();
        newHeaders.putAll(headers);
        if (cookies != null && cookies.size() > 0) {
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> cookie : cookies.entrySet()) {
                buf.append(cookie.getKey()).append("=").append(cookie.getValue()).append(";");
            }
            newHeaders.set("Cookie", buf.toString());
        }
        newHeaders.set("Content-Type", "application/x-www-form-urlencoded");
        HttpEntity<Object> entity = new HttpEntity(body, newHeaders);
        return requestWithEntity(url, HttpMethod.POST, entity, responseType);
    }

    public static <M> M deleteRequest(String url, Object body, Class<M> responseType) {
        HttpEntity<Object> entity = new HttpEntity(body, headers);
        return request(url, HttpMethod.DELETE, entity, responseType);
    }

    public static void deleteRequest(String url) {
        HttpEntity<Object> entity = new HttpEntity((Object) null, headers);
        request(url, HttpMethod.DELETE, entity, Object.class);
    }

    public static <M> M patchRequest(String url, Object body, Class<M> responseType) {
        HttpEntity<Object> entity = new HttpEntity(body, headers);
        return request(url, HttpMethod.PATCH, entity, responseType);
    }

    public static <M> M putRequest(String url, Object body, Class<M> responseType) {
        HttpEntity<Object> entity = new HttpEntity(body, headers);
        return request(url, HttpMethod.PUT, entity, responseType);
    }

    public static <M> M formPostRequest(String url, MultiValueMap param, Class<M> responseType) {
        HttpEntity<MultiValueMap> entity = new HttpEntity(param, formHeaders);
        return request(url, HttpMethod.POST, entity, responseType);
    }

    public static <M> M multipartFormPostRequest(String url, MultiValueMap param, Class<M> responseType) {
        HttpEntity<MultiValueMap> entity = new HttpEntity(param, multipartFormHeaders);
        return request(url, HttpMethod.POST, entity, responseType);
    }

    private static <M> M request(String url, HttpMethod method, HttpEntity entity, Class<M> responseType) {
        ResponseEntity<M> result = restTemplate.exchange(url, method, entity, responseType, new Object[0]);
        return result.getBody();
    }

    private static <M> M request(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<M> responseType) {
        ResponseEntity<M> result = restTemplate.exchange(url, method, requestEntity, responseType, new Object[0]);
        return result.getBody();
    }

    private static <M> ResponseEntity<M> requestWithEntity(String url, HttpMethod method, HttpEntity entity, Class<M> responseType) {
        ResponseEntity<M> result = restTemplate.exchange(url, method, entity, responseType, new Object[0]);
        return result;
    }

    public static  <M> M upload(String url, byte[] data, Class<M> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setConnection("Keep-Alive");
        headers.setCacheControl("no-cache");
        ByteArrayResource resource = new ByteArrayResource(data);
        HttpEntity<ByteArrayResource> httpEntity = new HttpEntity<>(resource);
        ResponseEntity<M> result = restTemplate.postForEntity(url, httpEntity, responseType);
        return result.getBody();
    }

    public static byte[] exec(
            String uri,
            HttpMethod method,
            MediaType mediaType,
            String chartSet,
            Map<String, String> cookies,
            Map<String, List<String>> responseHeaders,
            byte[] params
    ) throws Exception {
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method.name());
        connection.setRequestProperty("Content-Type", mediaType.toString());
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Charset", chartSet);
        if (cookies != null && cookies.size() > 0) {
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> cookie : cookies.entrySet()) {
                buf.append(cookie.getKey()).append("=").append(cookie.getValue()).append(";");
            }
            connection.setRequestProperty("Cookie", buf.toString());
            log.debug("Cookie is -> {}", buf.toString());
        }
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);

        if (params != null) {
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(params);
            out.flush();
            out.close();
        }

        connection.connect();

        if (responseHeaders != null) {
            for (Map.Entry<String, List<String>> headers : connection.getHeaderFields().entrySet()) {
                String h = headers.getKey();
                List<String> v = headers.getValue();
                log.debug("Response Headers k -> {}, v -> {}", h, v);
                responseHeaders.put(h, v);
            }
        }

        InputStream inputStream = connection.getInputStream();
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.flush();
        bos.close();
        inputStream.close();
        return bos.toByteArray();
    }
}
