package com.ingesup.android.projet.activites;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ingesup.android.projet.contact.Contact;
import com.ingesup.android.projet.json.FormatMessageEnvoi;
import com.ingesup.android.projet.json.GestionMessage;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.SearchView.OnQueryTextListener;
import android.app.Activity;
import android.content.Intent;

// Issue #14 : Ecran de recherche
public class RechercheContactsActivity extends Activity {

    private String _adresseServeur;
    private ListView _vueListeContacts = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recherche_contacts_layout);
		setTitle("Rechercher un contact");

        // parametrer l'actionbar
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
		_vueListeContacts = (ListView) findViewById(R.id.listView1);
		_vueListeContacts.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Contact vContact = (Contact) parent.getItemAtPosition(position);
				
				Intent vIntent = new Intent(RechercheContactsActivity.this, ProfilContactActivity.class);
				vIntent.putExtra("serveur", _adresseServeur);
				vIntent.putExtra("id", vContact.getId());
				startActivity(vIntent);

			}
		});

        Intent vIntent = getIntent();
        _adresseServeur = (String) vIntent.getExtras().get("serveur");
                
        listerTousLesContacts();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recherche, menu);
        MenuItem vItemMenu = menu.findItem(R.id.menu_champ_recherche);
        SearchView vSearchView = (SearchView) vItemMenu.getActionView();
        vSearchView.setQueryHint("Saisir nom du contact");
        
        vSearchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			public boolean onQueryTextSubmit(String query) {
		        return false;	// Utiliser plutot le filtre avec onQueryTextChange (optimal)
			}

			public boolean onQueryTextChange(String newText) {

				ArrayAdapter<Contact> vList = (ArrayAdapter<Contact>) _vueListeContacts.getAdapter();
				vList.getFilter().filter(newText);
				
				return true;
			}
		});

        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch(item.getItemId()) {
 			 
	    	case android.R.id.home : 
	    		onBackPressed(); break;
				
	    	default : 
	    		Log.e(RechercheContactsActivity.class.toString(), "Menu inconnu : " + item.getItemId());
    	}

    	return super.onOptionsItemSelected(item);
    }
    
	private void listerTousLesContacts() {
		JSONObject vMessageDemandeRecuperationUtilisateurs = FormatMessageEnvoi.formatterMessageRecuperationUtilisateurs();
        GestionMessage vGestionMessage = new GestionMessage();
        vGestionMessage.execute(
        		"http://" + _adresseServeur + "/ab_service_mgr/api/mobile/users",
        		vMessageDemandeRecuperationUtilisateurs.toString());
        
        try {
			JSONObject vMesssageReponse = vGestionMessage.get(10, TimeUnit.SECONDS);
			if(vMesssageReponse != null) {
				ArrayList<Contact> vListeContacts = new ArrayList<Contact>();
				JSONArray vContacts = (JSONArray) vMesssageReponse.getJSONArray("userDto");
				int vNbContacts = vContacts.length();
				for(int vIndex=0 ; vIndex<vNbContacts ; vIndex++) {
					JSONObject vInformationsContact = vContacts.getJSONObject(vIndex);
					Contact vContact = new Contact();
					vContact.setNom(vInformationsContact.getString("nom"));
					vContact.setPrenom(vInformationsContact.getString("prenom"));
					vContact.setId(Integer.parseInt(vInformationsContact.getString("identifiant")));
					vListeContacts.add(vContact);
				}
				
				_vueListeContacts.setAdapter(	// TODO : utiliser un adapter special pour afficher les infos du contact
						new ArrayAdapter<Contact>(RechercheContactsActivity.this, 
								android.R.layout.simple_list_item_1, android.R.id.text1, vListeContacts));
			}
			else {
				Toast.makeText(RechercheContactsActivity.this, "Aucun contact", Toast.LENGTH_LONG).show();
			}
			
		} catch (InterruptedException e) {
			Log.d(RechercheContactsActivity.class.toString(), e.getMessage());
		} catch (ExecutionException e) {
			Log.d(RechercheContactsActivity.class.toString(), e.getMessage());
		} catch (TimeoutException e) {
			Log.d(RechercheContactsActivity.class.toString(), e.getMessage());
		} catch (JSONException e) {
			Log.d(RechercheContactsActivity.class.toString(), e.getMessage());
		}
	}

}