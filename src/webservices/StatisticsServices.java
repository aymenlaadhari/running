package webservices;

import java.util.HashMap;
import java.util.Map;

import model.Runner;
import model.RunnerStatistics;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import contentprovider.DatabaseHelper;

public class StatisticsServices extends Activity {
	private RequestQueue queue = Volley.newRequestQueue(this);
	final String urlGet = "http://httpbin.org/get?param1=hello";
	final String urlPost = "http://httpbin.org/post";
	final String urlPut = "http://httpbin.org/put";
	final String urlDelete = "http://httpbin.org/delete";
	DatabaseHelper helper;

	// prepare the Request

	private void get() {

		JsonObjectRequest getRequest = new JsonObjectRequest(
				Request.Method.GET, urlGet, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// display response
						Log.d("Response", response.toString());
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Log.d("Error.Response", response);
					}
				});
		queue.add(getRequest);
	}

	private void put() {
		StringRequest putRequest = new StringRequest(Request.Method.PUT,
				urlPut, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response
						Log.d("Response", response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// error
						// Log.d("Error.Response", response);
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				helper = new DatabaseHelper(getApplicationContext());
				Runner runner = (Runner) helper.findAllRunner().get(0);
				params.put("name", "Alif");
				params.put("domain", "http://itsalif.info");

				return params;
			}

		};

		queue.add(putRequest);
	}

	private void post() {
		StringRequest postRequest = new StringRequest(Request.Method.POST,
				urlPost, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response
						Log.d("Response", response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// error
						// Log.d("Error.Response", response);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				helper = new DatabaseHelper(getApplicationContext());
				Runner runner = (Runner) helper.findAllRunner().get(0);
				params.put("FbId", runner.getFbID());
				params.put("firstName", runner.getFirstName());
				params.put("lastName", runner.getLastName());
				params.put("birthDay", runner.getBirth());
				params.put("weight", Integer.toString(runner.getWeight()));
				params.put("height", Integer.toString(runner.getHeigh()));
				params.put("sexe", String.valueOf(runner.isMale()));
				return params;
			}
		};
		queue.add(postRequest);
	}

	private void delete() {
		StringRequest dr = new StringRequest(Request.Method.DELETE, urlDelete,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// response
						Log.d("Response", response);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// error.

					}
				});
		queue.add(dr);
	}
}
