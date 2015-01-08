package nz.co.rivertech.miniwagenz;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefActivity extends PreferenceActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
//        addPreferencesFromResource(R.xml.other);
    }
	
	
	
//	   @Override
//	    public void onCreate(Bundle savedInstanceState) {
//	        super.onCreate(savedInstanceState);
//	        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFragments()).commit();
//	        
//	        }
//	   
}
