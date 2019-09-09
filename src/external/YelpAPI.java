package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Item;
import entity.Item.ItemBuilder;


public class YelpAPI {
	private static final String URL = "https://api.yelp.com/v3/businesses/search";
	private static final String DEFAULT_KEYWORD = ""; // no restriction
	private static final String API_KEY = "Bearer EftKbI9x7mpxYBmwoGnth3vwyjjEDnFfxF11-YZEdgen8LyunlwjkDasOIvpJiSnrMnI6M--0VQ-9HeyQsyrgMIKvovgUBQpyag7MSLHffU3jIQvMKXdbEjpi7ZtXXYx";
	private static final String term = "aquarium";

	public List<Item> search(double lat, double lon, String keyword) {
		
		//List<Item> items = new ArrayList<>();
		if (keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}


		try {
			keyword = URLEncoder.encode(keyword, "UTF-8"); //"Rick Sun" => "Rick%20Sun"
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//
//		// "apikey=qqPuP6n3ivMUoT9fPgLepkRMreBcbrjV&latlong=37,-120&keyword=event&radius=50"
//		String geoHash = GeoHash.encodeGeohash(lat, lon, 8);

		// apikey=abcde&geoPoint=xyz123&keyword=&radius=50
		String query = String.format("latitude=%s&longitude=%s&limit=%d&term=%s", lat, lon, 20, term);

		//String query = String.format("apikey=%s&latlong=%s,%s&keyword=%s&radius=%s", API_KEY, lat, lon, keyword, 50);
		String url = URL + "?" + query;

		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.addRequestProperty("Authorization", API_KEY);

			int responseCode = connection.getResponseCode();
			System.out.println("Sending request to url: " + url);
			System.out.println("Response code: " + responseCode);

			if (responseCode != 200) {
				return new ArrayList<>();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			JSONObject obj = new JSONObject(response.toString());

			if (!obj.isNull("businesses")) {
				return getItemList(obj.getJSONArray("businesses"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	private List<Item> getItemList(JSONArray businesses) throws JSONException {
		List<Item> items = new ArrayList<>();
		for (int i = 0; i < businesses.length(); ++i) {
			JSONObject event = businesses.getJSONObject(i);
			
			ItemBuilder builder = new ItemBuilder();
			if (!event.isNull("id")) {
				builder.setItemId(event.getString("id"));
			}
			if (!event.isNull("name")) {
				builder.setName(event.getString("name"));
			}
			if (!event.isNull("url")) {
				builder.setUrl(event.getString("url"));
			}
			if (!event.isNull("distance")) {
				builder.setDistance(event.getDouble("distance"));
			}
			
			builder.setAddress(getAddress(event)).setCategories(getCategories(event))
			       .setImageUrl(getImageUrl(event)).setLat(getLat(event)).setLng(getLon(event));
			
			items.add(builder.build());
		}

		return items;
	}
	
	private void queryAPI(double lat, double lon) {
		List<Item> events = search(lat, lon, null);

		try {
		    for (Item item: events) {
		    	System.out.println(item.toJSONObject());
		    }
		    
		} catch (Exception e) {
	                  e.printStackTrace();
		}
	}
	
	private double getLat(JSONObject business) throws JSONException {
		if (!business.isNull("coordinates")) {
			JSONObject location = business.getJSONObject("coordinates");
		    if (!location.isNull("latitude")) {
		    	return location.getDouble("latitude");
		    }
		}
		return 0;
	}
	
	private double getLon(JSONObject business) throws JSONException {
		if (!business.isNull("coordinates")) {
			JSONObject location = business.getJSONObject("coordinates");
		    if (!location.isNull("longitude")) {
		    	return location.getDouble("longitude");
		    }
		}
		return 0;
	}
	
	private String getAddress(JSONObject business) throws JSONException {
		if (!business.isNull("location")) {
			JSONObject address = business.getJSONObject("location");
			StringBuilder builder = new StringBuilder();
			
			if (!address.isNull("address1")) {
				builder.append(address.getString("address1"));
			}
			if (!address.isNull("address2")) {
				builder.append(",");
				builder.append(address.getString("address2"));
			}
			
			if (!address.isNull("address3")) {
				builder.append(",");
				builder.append(address.getString("address3"));
			}
		
			if (!address.isNull("city")) {
				builder.append(",");
				builder.append(address.getString("city"));
			}
			String result = builder.toString();
			if (!result.isEmpty()) {
				return result;
			}
		}
		return "";
	}
	
	private String getImageUrl(JSONObject business) throws JSONException {	
		if (!business.isNull("image_url")) {
			return business.getString("image_url");
		}
		
		return "";
	}
	
	private Set<String> getCategories(JSONObject event) throws JSONException {
		Set<String> categories = new HashSet<>();
		if (!event.isNull("classifications")) {
			JSONArray classifications = event.getJSONArray("classifications");
			for (int i = 0; i < classifications.length();i++) {
				JSONObject classification = classifications.getJSONObject(i);
				if (!classification.isNull("segment")) {
					JSONObject segment = classification.getJSONObject("segment");
					if (!segment.isNull("name")) {
						categories.add(segment.getString("name"));
					}
				}
			}
		}
		return categories;
	}
	/**
	 * Main entry for sample TicketMaster API requests.
	 */
	public static void main(String[] args) {
		YelpAPI tmApi = new YelpAPI();
		// Mountain View, CA
		// tmApi.queryAPI(37.38, -122.08);
		// London, UK
		// tmApi.queryAPI(51.503364, -0.12);
		// Houston, TX
		tmApi.queryAPI(29.682684, -95.295410);
	}



}