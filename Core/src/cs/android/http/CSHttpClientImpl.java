package cs.android.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cs.java.collections.List;
import cs.java.collections.Map;
import cs.java.collections.Mapped;
import cs.java.json.JSONArray;
import cs.java.json.JSONContainer;
import cs.java.json.JSONObject;
import cs.java.json.JSONType;
import cs.java.lang.Base;
import cs.java.model.Credentials;
import cs.java.net.Url;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.cache.CachingHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static cs.android.lang.AndroidLang.asArray;
import static cs.android.lang.AndroidLang.asString;
import static cs.java.lang.Lang.*;

public class CSHttpClientImpl extends Base implements CSHttpClient {

    // private class FlushedInputStream extends FilterInputStream {
    // public FlushedInputStream(InputStream inputStream) {
    // super(inputStream);
    // }
    //
    // @Override public long skip(long n) throws IOException {
    // long totalBytesSkipped = 0L;
    // while (totalBytesSkipped < n) {
    // long bytesSkipped = in.skip(n - totalBytesSkipped);
    // if (bytesSkipped == 0L) {
    // int oneByte = read();
    // if (oneByte < 0) break; // we reached EOF
    // bytesSkipped = 1; // we read one byte
    // }
    // totalBytesSkipped += bytesSkipped;
    // }
    // return totalBytesSkipped;
    // }
    // }
    private boolean showInfoOfResponseString;
    private static HttpClient client;
    private static DefaultHttpClient innerClient;
    private HttpResponse response;
    private HttpUriRequest request;
    private BasicCookieStore cookieStore;
    private Url url = new Url();
    private CredentialsProvider credentials;
    private int[] exceptionStatusCodes;

    public CSHttpClientImpl() {
        if (no(client)) {
            innerClient = new DefaultHttpClient();
            client = new CachingHttpClient(innerClient);
        }
    }

    public CSHttpClientImpl(String url) {
        this();
        this.url.setBaseUrl(url);
    }

    @Override
		public CSHttpClient add(String key, Object value) {
        url.add(key, value);
        return this;
    }

    @Override
		public CSHttpClient addArguments(Map<String, String> arguments) {
        for (Mapped<String, String> mapped : iterate(arguments))
            add(mapped.key(), mapped.value());
        return this;
    }

    @Override
		public CSHttpClient addCookie(Cookie cookie) {
        if (no(cookieStore)) cookieStore = new BasicCookieStore();
        cookieStore.addCookie(cookie);
        return this;
    }

    @Override
		public CSHttpClient debug(boolean showInfoOfResponseString) {
        this.showInfoOfResponseString = showInfoOfResponseString;
        return this;
    }

    // private int calculateInSampleSize(BitmapFactory.Options options, int
    // reqWidth, int reqHeight) {
    // int inSampleSize = 1;
    // if (options.outHeight > reqHeight || options.outWidth > reqWidth) if
    // (options.outWidth > options.outHeight) inSampleSize = Math
    // .round((float) options.outHeight / (float) reqHeight);
    // else inSampleSize = Math.round((float) options.outWidth / (float)
    // reqWidth);
    // return inSampleSize;
    // }

    @Override
		public CSHttpClient executeDelete() {
        HttpDelete post = new HttpDelete(getUrl());
        execute(post);
        return this;
    }

    @Override
		public CSHttpClient executeGet() {
        execute(new HttpGet(getUrl()));
        return this;
    }

    @Override
		public CSHttpClient executePatch(JSONObject data) {
        HttpPost post = new HttpPost(getUrl()) {
            @Override
            public String getMethod() {
                return "PATCH";
            }
        };
        post.setEntity(createEntity(data));
        execute(post);
        return this;
    }

    @Override
		public CSHttpClient executePost(File file, String filePartName, String mime) {
        MultipartEntity entity = new MultipartEntity();
        entity.addPart(filePartName, new FileBody(file));
        executePost(entity);
        return this;
    }

    @Override
		public CSHttpClient executePost(JSONType data) {
        executePost(createEntity(data));
        return this;
    }

    @Override
		public CSHttpClient executePost(Map<String, String> arguments) {
        executePost(createEntity(arguments));
        return this;
    }

    @Override
		public Cookie getCookie(int index) {
        if (getHttpCookies().size() > index) return getHttpCookies().get(index);
        return null;
    }

    @Override
		public Cookie getCookie(String name) {
        for (Cookie cookie : getCookies())
            if (cookie.getName().equals(name)) return cookie;
        return null;
    }

    @Override
		public List<Cookie> getCookies() {
        return list(getHttpCookies());
    }

    @Override
		public HttpResponse getResponse() {
        return response;
    }

    @Override
		public JSONArray getResponseArray() {
        JSONContainer json = getResponseJson();
        if (is(json)) return json.asArray();
        return null;
    }

    @Override
		public Bitmap getResponseBitmap(int requiredSize) {
        try {
            if (!isResponseOk()) return null;
            HttpEntity entity = getResponse().getEntity();
            if (is(entity))
                try {
                    byte[] data = asArray(entity.getContent());
                    BitmapFactory.Options decodeBoundsOptions = new BitmapFactory.Options();
                    decodeBoundsOptions.inJustDecodeBounds = true;
                    decodeBoundsOptions.inPurgeable = true;
                    decodeBoundsOptions.inInputShareable = true;

                    BitmapFactory.decodeStream(new ByteArrayInputStream(data), null, decodeBoundsOptions);
                    int scale = 1;
                    while (decodeBoundsOptions.outWidth / scale / 2 >= requiredSize
                            && decodeBoundsOptions.outHeight / scale / 2 >= requiredSize)
                        scale *= 2;

                    BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
                    decodeOptions.inSampleSize = scale;
                    decodeOptions.inPurgeable = true;
                    decodeOptions.inInputShareable = true;
                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data), null, decodeOptions);
                } finally {
                    entity.consumeContent();
                }
            throw exception("no entity");
        } catch (Exception e) {
            request.abort();
            throw exception(e);
        }
    }

    @Override
		public JSONObject getResponseObject() {
        JSONContainer response = getResponseJson();
        if (is(response)) return response.asObject();
        return null;
    }

    @Override
		public int getResponseStatus() {
        return response.getStatusLine().getStatusCode();
    }

    @Override
		public String getResponseString() {
        try {
            String content = asString(response.getEntity().getContent());
            if (showInfoOfResponseString) info("Retrived Content", content);
            return content;
        } catch (Exception e) {
            throw exception(e);
        }
    }

    @Override
		public String getUrl() {
        return url.toString();
    }

    @Override
		public boolean isResponseOk() {
        return between(getResponseStatus(), 200, 300);
    }

    @Override
		public Bitmap responseBitmap() {
        try {
            if (!isResponseOk()) return null;
            HttpEntity entity = getResponse().getEntity();
            if (is(entity)) try {
                ByteArrayInputStream input = new ByteArrayInputStream(asArray(entity.getContent()));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPurgeable = true;
                options.inInputShareable = true;
                return BitmapFactory.decodeStream(input, null, options);
            } finally {
                entity.consumeContent();
            }
            throw exception("no entity");
        } catch (Exception e) {
            request.abort();
            throw exception(e);
        }
    }

    @Override
		public CSHttpClient setCredentials(Credentials value) {
        return setCredentials(value.getUsername(), value.getPassword());
    }

    @Override
		public CSHttpClient setCredentials(CredentialsProvider provider) {
        credentials = provider;
        return null;
    }

    @Override
		public CSHttpClient setCredentials(String username, String password) {
        credentials = new BasicCredentialsProvider();
        credentials.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        return this;
    }

    @Override
		public CSHttpClient setUrl(String url) {
        this.url.setBaseUrl(url);
        return this;
    }

    public CSHttpClient setUrl(Url url) {
        this.url = url;
        return this;
    }

    @Override
		public void throwExceptionOn(int... statusCodes) {
        exceptionStatusCodes = statusCodes;
    }

    private HttpEntity createEntity(JSONType data) {
        info("Sending Entity:", data.toJSON());
        try {
            StringEntity entity = new StringEntity(data.toJSON(), HTTP.UTF_8);
            entity.setContentType("application/json");
            return entity;
        } catch (UnsupportedEncodingException e) {
            throw exception(e);
        }
    }

    private HttpEntity createEntity(Map<String, String> map) {
        try {
            List<NameValuePair> nameValuePairs = list();
            for (Mapped<String, String> item : iterate(map))
                nameValuePairs.add(new BasicNameValuePair(item.key(), item.value()));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs);
            info("Sending Entity:", asString(entity.getContent()));
            return entity;
        } catch (IOException e) {
            throw exception(e);
        }
    }

    private HttpResponse execute(HttpUriRequest request) {
        this.request = request;
        try {
            if (is(cookieStore)) innerClient.setCookieStore(cookieStore);
            HttpParams params = new BasicHttpParams();
            HttpClientParams.setRedirecting(params, true);
            innerClient.setParams(params);

            if (is(credentials)) innerClient.setCredentialsProvider(credentials);
            info("Sending", request.getURI(), request.getMethod());

            response = client.execute(request);

            info("Response Status Line", response.getStatusLine());
        } catch (Exception e) {
            throw exception(e);
        }
        for (int statusCode : iterate(exceptionStatusCodes))
            if (getResponseStatus() == statusCode) throw new HttpStatusException(statusCode);
        return response;
    }

    private CSHttpClient executePost(HttpEntity entity) {
        HttpPost post = new HttpPost(getUrl());
        post.setEntity(entity);
        execute(post);
        return this;
    }

    private java.util.List<Cookie> getHttpCookies() {
        return innerClient.getCookieStore().getCookies();
    }

    private JSONContainer getResponseJson() {
        return json(getResponseString());
    }

}
