package org.plafue.cucumber.confluence.uploader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.codehaus.jackson.map.ObjectMapper;
import org.plafue.cucumber.confluence.uploader.dataobjects.NewPage;
import org.plafue.cucumber.confluence.uploader.dataobjects.UpdatablePage;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Future;

public class Uploader {

    public static String CONTENT_URL = "/confluence/rest/api/content/";

    private static final ObjectMapper mapper = new ObjectMapper();
    private final CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();

    private final URI contentResource;
    private final String updateContentUrl;

    public Uploader(String baseUrl) throws MalformedURLException, URISyntaxException {
        this.contentResource = new URI(baseUrl + CONTENT_URL);
        this.updateContentUrl = baseUrl + CONTENT_URL;
        httpClient.start();
    }

    public Future<HttpResponse> createPage(NewPage page) throws IOException {
        return executeJsonRequest(new HttpPost(contentResource), new CreatePageCallbackHandler(), page);
    }

    public Future<HttpResponse> updatePage(UpdatablePage page) throws IOException {
        return executeJsonRequest(new HttpPut(updateContentUrl + page.id), new UpdatePageCallbackHandler(), page);
    }

    private Future<HttpResponse> executeJsonRequest(HttpEntityEnclosingRequestBase requestBase,
                                                    FutureCallback<HttpResponse> callbackHandler,
                                                    Object object) throws IOException {
        StringEntity entity = new StringEntity(mapper.writeValueAsString(object));
        entity.setContentType(ContentType.APPLICATION_JSON.toString());
        requestBase.setEntity(entity);
        return httpClient.execute(requestBase,callbackHandler);
    }

    private class CreatePageCallbackHandler implements FutureCallback<HttpResponse> {
        @Override
        public void completed(HttpResponse result) {
            throw new NotImplementedException();
        }

        @Override
        public void failed(Exception ex) {
            throw new NotImplementedException();
        }

        @Override
        public void cancelled() {
            throw new NotImplementedException();
        }
    }

    private class UpdatePageCallbackHandler implements FutureCallback<HttpResponse> {
        @Override
        public void completed(HttpResponse result) {
            throw new NotImplementedException();
        }

        @Override
        public void failed(Exception ex) {
            throw new NotImplementedException();
        }

        @Override
        public void cancelled() {
            throw new NotImplementedException();
        }
    }
}