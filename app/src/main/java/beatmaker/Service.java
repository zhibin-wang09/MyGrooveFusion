package beatmaker;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.apache.hc.client5.http.utils.Base64;

import java.net.http.HttpClient;



public class Service {

    public static boolean sendRequest(String name, byte[] data){
        try {
            String base64encode = Base64.encodeBase64String(data);
            String body = String.format("{\"producerName\": \"%s\", \"data\": \"%s\"}", name, base64encode);
            HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI("http://localhost:8080/api/v1/mixed_audios"))
            .headers("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build();

            HttpResponse<String> response = HttpClient.newBuilder().build().send(request,BodyHandlers.ofString());
            System.out.println(response);
            return true;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
}
