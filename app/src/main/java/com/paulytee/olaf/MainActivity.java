package com.paulytee.olaf;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paulytee.olaf.data.Flashcard;
import com.paulytee.olaf.data.FlashcardDeck;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity
{
	private FlashcardDeck fcDeck = new FlashcardDeck();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		fcDeck.addCard(new Flashcard("Hello", "Hola"));
		fcDeck.addCard(new Flashcard("Good morning", "Buenos dias"));
		fcDeck.addCard(new Flashcard("Good afternoon", "Buenas tardes"));
		((TextView)findViewById(R.id.txtClue)).setText(fcDeck.getActiveCard().getText());

		FloatingActionButton fabReveal = (FloatingActionButton) findViewById(R.id.fabReveal);
		fabReveal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String s = fcDeck.getActiveCardBack();
				Snackbar.make(view, s, Snackbar.LENGTH_LONG).setAction("Action", null).show();
			}
		});

		FloatingActionButton fabRemove = (FloatingActionButton) findViewById(R.id.fabRemove);
		fabRemove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				fcDeck.removeActiveCard();
				String s = fcDeck.getActiveCardFront();
				((TextView)findViewById(R.id.txtClue)).setText(fcDeck.getActiveCardFront());
			}
		});

		FloatingActionButton fabFlip = (FloatingActionButton) findViewById(R.id.fabFlip);
		fabFlip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				fcDeck.flipActiveCard();
				((TextView)findViewById(R.id.txtClue)).setText(fcDeck.getActiveCardFront());
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Loading
		try{
			FileInputStream fis = this.getApplicationContext().openFileInput("UserData.data");
			ObjectInputStream is = new ObjectInputStream(fis);
			//fcDeck = (FlashcardDeck) is.readObject();
			String jsonString = (String) is.readObject();
			is.close();
			fis.close();
			Log.d("MainActivity", jsonString);
			fcDeck = new Gson().fromJson(jsonString, FlashcardDeck.class);
			((TextView)findViewById(R.id.txtClue)).setText(fcDeck.getActiveCardFront());
		} catch (Exception e) {
			Log.e("MainActivity", "Error: loading from the internal storage failed - \n" + e.toString());
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	//Saving
		try {
			String json = new GsonBuilder().create().toJson(fcDeck);
			FileOutputStream fos = this.getApplicationContext().openFileOutput("UserData.data", Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(json);
			os.close();
			fos.close();
		} catch (Exception e) {
			Log.e("MainActivity", "Error: Failed to save User into internal storage - \n" + e.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
