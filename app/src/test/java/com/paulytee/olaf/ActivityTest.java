package com.paulytee.olaf;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paulytee.olaf.data.Flashcard;
import com.paulytee.olaf.data.FlashcardDeck;
import com.paulytee.olaf.data.FlashcardMoiety;
import com.paulytee.olaf.util.FlashcardDeckFiller;
import com.paulytee.olaf.util.LanguageConstants;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.junit.Rule;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.paulytee.olaf.util.LanguageConstants.*;

/**
 * Created by Paul T. on 12/15/2015.
 */
public class ActivityTest extends TestCase {

	public void testDataRead() throws Exception {
		InputStream is = getClass().getResourceAsStream("./testData.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		List<FlashcardMoiety> l = new ArrayList<>();
		while ((line = br.readLine()) != null) {
			FlashcardMoiety fcm = new Gson().fromJson(line, FlashcardMoiety.class);
			l.add(fcm);
		}
		Log.d("ActivityTest", l.toString());
	}

	public void testDataReadLocal() throws Exception {
		List<FlashcardMoiety> l = new FlashcardDeckFiller().getFlashcardList();
		System.out.println(l.toString());
	}

	public void testInitFlashcardDeckFiller() throws Exception {
		FlashcardDeckFiller fdf = new FlashcardDeckFiller();
		fdf.initMaps();
		System.out.println(fdf.getFullMapByLanguage().toString());
	}

	public void testPopulateFlashcardDeck() throws Exception {
		FlashcardDeckFiller fdf = new FlashcardDeckFiller();
		FlashcardDeck fcDeck = fdf.populateFlashcardDeck(Language.US, Language.spa_MEX);
		String json = new GsonBuilder().create().toJson(fcDeck);
		System.out.println(json.toString());
	}

	public void testPopulateFlashcardDeckFillGaps() throws Exception {
		FlashcardDeckFiller fdf = new FlashcardDeckFiller();
		FlashcardDeck fcDeckSaved = fdf.populateFlashcardDeck(Language.US, Language.spa_MEX);
		FlashcardDeckFiller fdf2 = new FlashcardDeckFiller();
		FlashcardDeck fcDeckMaster = fdf2.populateFlashcardDeck(Language.US, Language.spa_MEX);
		Field field = FlashcardDeck.class.getDeclaredField("flashcards");
		field.setAccessible(true);
		List<Flashcard> fcListSaved = (List<Flashcard>) field.get(fcDeckSaved);
		fcListSaved.remove(fcListSaved.size()-1);
		fcDeckSaved = fdf2.updateSavedDecks(fcDeckMaster, fcDeckSaved);
		String json = new GsonBuilder().create().toJson(fcDeckSaved);
		System.out.println(json.toString());
	}
}
