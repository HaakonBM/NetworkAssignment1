import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailExtractor {
    URL url;
    StringBuilder html;
    String pattern = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    Set<String> emails = new HashSet<>();
    String response;
    EmailExtractor(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            response = "CODE 2";
        }
    }

    void extract() {
        if(readPage()) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(html);
            while (m.find())
                emails.add(m.group());
            if (emails.isEmpty())
                response = "CODE 1";
            else
                response = "CODE 0";
        }
    }

    public boolean readPage() {
        try(BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()))) {
            html = new StringBuilder();
            String in = "";
            while((in = read.readLine()) != null)
                html.append(in);
        } catch (Exception e) {
            response = "CODE 2";
            return false;
        }
        return true;
    }

    public void flushEmails() {
        this.emails.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String s : emails)
            sb.append(s).append("\n");
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}