package in.ac.iitk.dac;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class CS654DAC extends AppCompatActivity {

    private int flag = 0;
    private EditText editText;
    private EditText editText2;
    private Spinner spinner;
    private TextView textView2;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cs654_dac);

        //Adding reference variables to UI items
        editText = (EditText) findViewById(R.id.editText);
        spinner = (Spinner) findViewById(R.id.spinner);
        editText2 = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);
        textView2 = (TextView) findViewById(R.id.textView2);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.operator_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }


    public void calculate(View view) {
        // Do something in response to Calculate button being pressed
        if (flag ==0) {
            final String operand1 = editText.getText().toString();
            final String operand2 = editText2.getText().toString();
            final String operator = spinner.getSelectedItem().toString();

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "http://10.0.2.2:5000/";

            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Response", response);
                                textView2.setText("Result : " + response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error Response", error.getMessage());
                                textView2.setText("OOPs...that didn't work out");
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("operand1", operand1);
                        params.put("operand2", operand2);
                        params.put("operator", operator.charAt(1) + "");

                        return params;
                    }
                };
                queue.add(postRequest);
                textView2.setText("Calculating...");

                flag = 1;
                button.setText("RESET");
            } else {
                flag = 0;
                editText.setText(null);
                editText2.setText(null);
                spinner.setSelection(0);
                button.setText("CALCULATE");
                textView2.setText("Enter Operands above and Select Operator");

            }
        }
        else{
            textView2.setText("Not able to access Internet!! Try again after connecting.");
        }

    }
}
