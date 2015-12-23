package com.paulytee.olaf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.paulytee.olaf.data.FlashcardDeck;
import com.paulytee.olaf.util.FlashcardDeckFiller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.paulytee.olaf.util.LanguageConstants.*;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener
{
	private static final int TTS_DATA_CHECK_CODE = 1;
	private static final String CLASS_NAME = "MainActivity";

	private List<FlashcardDeck> userDecks = new ArrayList<>();
	private FlashcardDeck fcDeck = new FlashcardDeck();
	private FlashcardDeckFiller fdFiller = new FlashcardDeckFiller();
	private boolean savedDecksInitialized = false;

	private TextToSpeech ttsObj;
	private GestureDetectorCompat mDetector;
	private Locale localeNative = Locale.US;
	private Locale localeForeign = new Locale("spa", "MEX");
	private Language nativeLanguage = Language.US;
	private Language foreignLanguage = Language.spa_MEX;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		fcDeck = fdFiller.populateFlashcardDeck(nativeLanguage, foreignLanguage);
		//fcDeck.addCard(new Flashcard("Hello", "Hola"));
		//fcDeck.addCard(new Flashcard("Good morning", "Buenos dias"));
		//fcDeck.addCard(new Flashcard("Good afternoon", "Buenas tardes"));
		((TextView)findViewById(R.id.txtClue)).setText(fcDeck.getActiveCard().getText());

		FloatingActionButton fabReveal = (FloatingActionButton) findViewById(R.id.fabReveal);
		fabReveal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String s = fcDeck.getActiveCardBack();
				presentCurrentCard(s, !fcDeck.getActiveCard().getNativeFirst());
				Snackbar.make(view, s, Snackbar.LENGTH_LONG).setAction("Action", null).show();
			}
		});

		FloatingActionButton fabRemove = (FloatingActionButton) findViewById(R.id.fabRemove);
		fabRemove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				fcDeck.removeActiveCard();
				presentCurrentCard();
			}
		});

		FloatingActionButton fabFlip = (FloatingActionButton) findViewById(R.id.fabFlip);
		fabFlip.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				fcDeck.flipActiveCard();
				((TextView) findViewById(R.id.txtClue)).setText(fcDeck.getActiveCardFront());
			}
		});

		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, TTS_DATA_CHECK_CODE);
		mDetector = new GestureDetectorCompat(this, new MyGestureListener());
	}

	public void presentCurrentCard() {
		String s = fcDeck.getActiveCardFront();
		((TextView)findViewById(R.id.txtClue)).setText(fcDeck.getActiveCardFront());
		presentCurrentCard(s, fcDeck.getActiveCard().getNativeFirst());
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Loading
		try{
			FileInputStream fis = this.getApplicationContext().openFileInput("UserData.data");
			ObjectInputStream is = new ObjectInputStream(fis);
			String jsonString = (String) is.readObject();
			is.close();
			fis.close();
			Log.d(CLASS_NAME, jsonString);
			// The first time through, fcDeck will be the deck that's built from the set of all cards
			FlashcardDeck fcDeckOld = null;
			if (!savedDecksInitialized) {
				fcDeckOld = fcDeck;
			}
			// Retrieve the user's saved deck(s)
			try {
				fcDeck = new Gson().fromJson(jsonString, FlashcardDeck.class); // temporary
			} catch (JsonSyntaxException e) {
				userDecks = new Gson().fromJson(jsonString, new TypeToken<ArrayList<FlashcardDeck>>(){}.getType());
			}
			if (userDecks.isEmpty()) { // temporary
				userDecks.add(fcDeck);
			}
			// See if there are new cards in the latest deck(s) that need to be added to the saved deck(s)
			if (!savedDecksInitialized) {
				List<FlashcardDeck> tempDeckList = new ArrayList<>();
				tempDeckList.addAll(userDecks);
				userDecks.clear();
				for (FlashcardDeck deck : tempDeckList) {
					deck = fdFiller.updateSavedDecks(fcDeckOld, deck);
					userDecks.add(deck);
					if (deck.isActive()) {
						fcDeck = deck;
					}
				}
			}
			((TextView)findViewById(R.id.txtClue)).setText(fcDeck.getActiveCardFront());
			savedDecksInitialized = true;
		} catch (Exception e) {
			Log.e(CLASS_NAME, "Error: loading from the internal storage failed - \n" + e.toString());
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	//Saving
		try {
			String json = new GsonBuilder().create().toJson(userDecks);
			FileOutputStream fos = this.getApplicationContext().openFileOutput("UserData.data", Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(json);
			os.close();
			fos.close();
		} catch (Exception e) {
			Log.e(CLASS_NAME, "Error: Failed to save User into internal storage - \n" + e.toString());
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
		if (id == R.id.action_select) {
			startActivity(new Intent(this, SelectActivity.class));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == TTS_DATA_CHECK_CODE) {
			// Make sure the request was successful
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				Log.d(CLASS_NAME, "TTS ok");
				ttsObj = new TextToSpeech(this, this);
			} else {
				// missing data, install it
				Log.d(CLASS_NAME, "TTS not present -- installing");
				Intent installIntent = new Intent();
				installIntent.setAction(
						TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}

	private void presentCurrentCard(String text, boolean isNative) {
		ttsObj.setLanguage(isNative ? localeNative : localeForeign);
		ttsObj.speak(text, TextToSpeech.QUEUE_ADD, null);
	}

	private void initLanguages() {
		ttsObj.setLanguage(Locale.US);
		String myText1 = "Did you sleep well?";
		String myText2 = "I hope so, because it's time to wake up.";
		ttsObj.speak(myText1, TextToSpeech.QUEUE_FLUSH, null);
		ttsObj.speak(myText2, TextToSpeech.QUEUE_ADD, null);
		ttsObj.setLanguage(new Locale("spa", "MEX"));
		ttsObj.speak("Bien, empezamos.", TextToSpeech.QUEUE_ADD, null);

		ttsObj.setLanguage(Locale.CHINA);
		//ttsObj.speak("Dǎrǎo yīxià.", TextToSpeech.QUEUE_ADD, null);
		ttsObj.speak("打擾一下。", TextToSpeech.QUEUE_ADD, null);
	}

	@Override
	public void onInit(int status) {
		Log.d(CLASS_NAME, "onInit");

		ttsObj.setLanguage(new Locale("spa", "MEX"));
		ttsObj.speak("Bien, empezamos.", TextToSpeech.QUEUE_ADD, null);
		ttsObj.setLanguage(new Locale("te", "IN"));
		ttsObj.speak("క్షమించండి", TextToSpeech.QUEUE_ADD, null);


		//Locale loc = new Locale("te");
		//Log.i("-------------", Arrays.toString(loc.getAvailableLocales()));
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}


	class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
		private static final String DEBUG_TAG = "Gestures";

		@Override
		public boolean onDown(MotionEvent event) {
			//Log.d(DEBUG_TAG,"onDown: " + event.toString());
			return true;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2,
		                       float velocityX, float velocityY) {
			//Log.d(DEBUG_TAG, "onFling:  X = " + velocityX + ", Y = " + velocityY);
			if (velocityX > 0 && velocityX > velocityY) {
				fcDeck.getNextCard();
				presentCurrentCard();
			} else if (velocityX < 0 && velocityX < velocityY) {
				fcDeck.getPreviousCard();
				presentCurrentCard();
			}
			return true;
		}
	}
}
