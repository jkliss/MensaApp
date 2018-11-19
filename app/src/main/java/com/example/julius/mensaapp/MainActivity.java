package com.example.julius.mensaapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensa);
        final TextView editText = (TextView) findViewById(R.id.textView);
        editText.setMovementMethod(new ScrollingMovementMethod());
        LongOperation op = new LongOperation();
        op.execute();
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            URL url;
            String string = "";
            try {
                url = new URL("https://www.stw-d.de/gastronomie/speiseplaene/essenausgabe-sued-duesseldorf/");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                url.openStream()));

                String inputLine;

                while ((inputLine = in.readLine()) != null){
                    if(inputLine.contains(" data-date=")){
                        inputLine = inputLine.substring(inputLine.indexOf(" data-date=\'") + 12);
                        inputLine = inputLine.substring(0, inputLine.indexOf("\'><div class="));
                        if(inputLine.length() > 4) {
                            string += "--------------  " + inputLine + "  --------------\n";
                        }
                    }
                    if(inputLine.contains("<li data-info=")){
                        inputLine = inputLine.substring(inputLine.indexOf("\">") + 2);
                        inputLine = inputLine.substring(0, inputLine.indexOf("</li>"));
                        if(inputLine.length() > 4 && !inputLine.contains("Preis ab") && !inputLine.contains("Theke geschlossen")){
                            string += inputLine + "\n";
                        } else if(inputLine.contains("Theke geschlossen")){
                            string += "Theke geschlossen\n";
                        }
                    }
                    if(inputLine.contains("<div class=\"counter-table\">") && (!string.endsWith("----\n"))){
                        string += "\n";
                    }
                }

                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            string = string.replaceAll("\\[.*?\\]", "");
            string = string.replaceAll("\\(.*?\\)", "");
            return string;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView txt = (TextView) findViewById(R.id.textView);
            txt.setText(result); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
