import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Tests {
    public static void main(String[] args) throws IOException {
        URL url = new URL("http://localhost:8888");
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

        Map<String, List<String>> hdrs = httpCon.getHeaderFields();
        Set<String> hdrKeys = hdrs.keySet();

        for (String k : hdrKeys)
            System.out.println(k + hdrs.get(k));

    }
}
