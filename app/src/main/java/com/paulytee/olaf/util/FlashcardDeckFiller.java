package com.paulytee.olaf.util;

import com.google.gson.Gson;
import com.paulytee.olaf.data.Flashcard;
import com.paulytee.olaf.data.FlashcardDeck;
import com.paulytee.olaf.data.FlashcardMoiety;
import com.paulytee.olaf.util.LanguageConstants.Language;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Paul T. on 12/16/2015.
 */
public class FlashcardDeckFiller
{
	private Map<Language, Map<Integer, List<FlashcardMoiety>>> fullMapByLanguage = new HashMap<>();
	private Map<String, Map<Language, FlashcardMoiety>> phraseMap = new HashMap<>();

	public Map<Language, Map<Integer, List<FlashcardMoiety>>> getFullMapByLanguage() {
		return fullMapByLanguage;
	}

	public Map<String, Map<Language, FlashcardMoiety>> getPhraseMap() {
		return phraseMap;
	}

	public void initMaps() {
		List<FlashcardMoiety> list = getFlashcardList();
		for (FlashcardMoiety fcm : list) {
			// Add to fullMapByLanguage
			Language lang = Language.fromString(fcm.get_id().split("#")[0]);
			if (!fullMapByLanguage.containsKey(lang)) {
				fullMapByLanguage.put(lang, new HashMap<Integer, List<FlashcardMoiety>>());
			}
			Map<Integer, List<FlashcardMoiety>> languageMap = fullMapByLanguage.get(lang);
			if (!languageMap.containsKey(fcm.getLevel())) {
				languageMap.put(fcm.getLevel(), new ArrayList<FlashcardMoiety>());
			}
			languageMap.get(fcm.getLevel()).add(fcm);

			// Add to phraseMap
			String phrase = fcm.get_id().split("#")[1];
			if (!phraseMap.containsKey(phrase)) {
				phraseMap.put(phrase, new HashMap<Language, FlashcardMoiety>());
			}
			phraseMap.get(phrase).put(lang, fcm);
		}
	}

	public List<FlashcardMoiety> getFlashcardList() {
		String[] lines = DATA.split(";");
		List<FlashcardMoiety> l = new ArrayList<>();
		for (String line : lines) {
			FlashcardMoiety fcm = new Gson().fromJson(line, FlashcardMoiety.class);
			l.add(fcm);
		}
		return l;
	}

	public FlashcardDeck populateFlashcardDeck(Language nativeLanguage, Language foreignLanguage) {
		FlashcardDeck fcDeck = new FlashcardDeck();
		initMaps();
		List<FlashcardMoiety> nativeCards = getFullMapByLanguage().get(nativeLanguage).get(1);
		for (FlashcardMoiety fcm : nativeCards) {
			FlashcardMoiety foreignCard =
					getPhraseMap().get(fcm.get_id().split("#")[1]).get(foreignLanguage);
			if (foreignCard != null) {
				fcDeck.addCard(new Flashcard(fcm.getScript(), foreignCard.getScript(), foreignCard.getRoman(), foreignCard.getLevel()));
			}
		}
		return fcDeck;
	}

	public FlashcardDeck updateSavedDecks(FlashcardDeck fcDeckMaster, FlashcardDeck fcDeckSaved) {
		Language nativeLanguage = fcDeckSaved.getNativeLanguage();
		// Count the cards in each level
		Map<Integer, List<Flashcard>> savedMap = new HashMap<>();
		for (int i = fcDeckSaved.getCardCount(); i > 0; i--) {
			Flashcard fc = fcDeckSaved.getActiveCard();
			if (!savedMap.containsKey(fc.getLevel())) {
				savedMap.put(fc.getLevel(), new ArrayList<Flashcard>());
			}
			savedMap.get(fc.getLevel()).add(fc);
			fcDeckSaved.getNextCard();
		}
		// Count the cards in each level - Master
		Map<Integer, List<Flashcard>> masterMap = new HashMap<>();
		for (int i = fcDeckMaster.getCardCount(); i > 0; i--) {
			Flashcard fc = fcDeckMaster.getActiveCard();
			if (!masterMap.containsKey(fc.getLevel())) {
				masterMap.put(fc.getLevel(), new ArrayList<Flashcard>());
			}
			masterMap.get(fc.getLevel()).add(fc);
			fcDeckMaster.getNextCard();
		}
		for (Integer i : savedMap.keySet()) {
			if (savedMap.get(i).size() != masterMap.get(i).size()) {
				// Lists are different size; find missing elements in savedMap and add them
				List<Flashcard> masterList = masterMap.get(i);
				for (Flashcard fcM : masterList) {
					boolean found = false;
					List<Flashcard> savedList = savedMap.get(i);
					for (Flashcard fcS : savedList) {
						if (fcS.getText().equals(fcM.getText())) {
							found = true;
							break;
						}
					}
					if (!found) {
						fcDeckSaved.addCard(fcM);
					}
				}
			}
		}
		return fcDeckSaved;
	}

	public static final String DATA =
			"{\"_id\":\"US#hello\", \"_ver\":\"\", \"level\":1, \"script\":\"Hello\", \"type\":\"word\"};" +
			"{\"_id\":\"spa-MEX#hello\", \"_ver\":\"\", \"level\":1, \"script\":\"Hola\", \"type\":\"word\"};" +
			"{\"_id\":\"US#good_morning\", \"_ver\":\"\", \"level\":1, \"script\":\"Good morning.\", \"type\":\"word\"};" +
			"{\"_id\":\"spa-MEX#good_morning\", \"_ver\":\"\", \"level\":1, \"script\":\"Buenos dias.\", \"type\":\"word\"};" +
			"{\"_id\":\"US#good_afternoon\", \"_ver\":\"\", \"level\":1, \"script\":\"Good afternoon.\", \"type\":\"word\"};" +
			"{\"_id\":\"spa-MEX#good_afternoon\", \"_ver\":\"\", \"level\":1, \"script\":\"Buenas tardes.\", \"type\":\"word\"};" +
			"{\"_id\":\"US#good_night\", \"_ver\":\"\", \"level\":1, \"script\":\"Good night.\", \"type\":\"word\"};" +
			"{\"_id\":\"spa-MEX#good_night\", \"_ver\":\"\", \"level\":1, \"script\":\"Buenas noches.\", \"type\":\"word\"};" +
			"{\"_id\":\"CHINA#hello\", \"_ver\":\"\", \"level\":1, \"script\":\"您好\", \"roman\":\"Nín hǎo\", \"type\":\"word\"};" +
			"{\"_id\":\"hi-IN#hello\", \"_ver\":\"\", \"level\":1, \"script\":\"नमस्ते\", \"roman\":\"Namastee\", \"type\":\"word\"}";
}
