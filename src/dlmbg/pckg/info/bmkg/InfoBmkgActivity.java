package dlmbg.pckg.info.bmkg;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class InfoBmkgActivity extends ListActivity {

	static final String URL = "http://data.bmkg.go.id/gempaterkini.xml";
	static final String KEY_ITEM = "gempa";
	static final String KEY_ID = "Tanggal";
	static final String KEY_TANGGAL = "Tanggal";
	static final String KEY_JAM = "Jam";
	static final String KEY_POINT = "point";
	static final String KEY_KOORDINAT = "coordinates";
	static final String KEY_LINTANG = "lintang";
	static final String KEY_BUJUR = "bujur";
	static final String KEY_KEKUATAN_GEMPA = "Magnitude";
	static final String KEY_KEDALAMAN = "Kedalaman";
	static final String KEY_WILAYAH = "Wilayah";
	private ProgressDialog pDialog;
	ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		new AmbilData().execute();
	}
    
    class AmbilData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InfoBmkgActivity.this);
            pDialog.setMessage("Please Wait...\n" + "Make Sure Your Internet Connection is Working...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        
        protected String doInBackground(String... args) {

    		XMLParser parser = new XMLParser();
    		String xml = parser.getXmlFromUrl(URL);
    		Document doc = parser.getDomElement(xml);

    		NodeList nl = doc.getElementsByTagName(KEY_ITEM);
    		for (int i = 0; i < nl.getLength(); i++) {

            	HashMap<String, String> map = new HashMap<String, String>();
    			
    			Element e = (Element) nl.item(i);
    			
    			String koordinat = parser.getValue(e, KEY_KOORDINAT);
    			String[] koor = koordinat.split(",");
    			
    			map.put(KEY_ID, parser.getValue(e, KEY_ID));
    			map.put(KEY_TANGGAL, parser.getValue(e, KEY_TANGGAL));
    			map.put(KEY_JAM, parser.getValue(e, KEY_JAM));
    			map.put(KEY_LINTANG, "Garis Lintang : "+koor[1]);
    			map.put(KEY_BUJUR, "Garis Bujur : "+ koor[0]);
    			map.put(KEY_KEKUATAN_GEMPA, "Kekuatan Gempa : "+parser.getValue(e, KEY_KEKUATAN_GEMPA));
    			map.put(KEY_KEDALAMAN, "Kedalaman : "+ parser.getValue(e, KEY_KEDALAMAN));
    			map.put(KEY_WILAYAH, "Wilayah : "+parser.getValue(e, KEY_WILAYAH));

    			menuItems.add(map);
    		}
			return null;
        }
        
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
        			
            		ListAdapter adapter = new SimpleAdapter(InfoBmkgActivity.this, menuItems,
            				R.layout.daftar_item,
            				new String[] { KEY_TANGGAL, KEY_JAM, KEY_LINTANG, KEY_BUJUR, KEY_KEKUATAN_GEMPA, KEY_KEDALAMAN, KEY_WILAYAH }, 
            				new int[] {R.id.tanggal, R.id.jam, R.id.lintang, R.id.bujur,R.id.kekuatan,R.id.kedalaman,R.id.wilayah });

            		setListAdapter(adapter);
                }
            });
 
        }
    }
}