package beatmaker;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.io.IOException;

public class Service {

    public static <T> void sendData(String endpoint, T data){
        try(CloseableHttpClient httpClient  = HttpClients.createDefault()){
            byte[] binaryData = (byte[]) data;
            ClassicHttpRequest httpPost = ClassicRequestBuilder.post(endpoint)
                    .setEntity(new ByteArrayEntity(binaryData, ContentType.APPLICATION_OCTET_STREAM))
                    .build();
            httpClient.execute(httpPost, response -> {
                System.out.println(response.getCode() + " " + response.getReasonPhrase());

                final HttpEntity entity2 = response.getEntity();
                EntityUtils.consume(entity2);
                return null;
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
