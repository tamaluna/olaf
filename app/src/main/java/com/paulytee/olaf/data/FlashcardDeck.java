package com.paulytee.olaf.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul T. on 11/30/2015.
 * Contains a list of Flashcards and all the methods necessary to manipulate them.
 */
public class FlashcardDeck {
	public static final Flashcard nullCard = new Flashcard("[No cards selected]", "[No cards selected]");
	private List<Flashcard> flashcards = new ArrayList<>();
	private int cardIndex = 0;
	private int activeCards = 0;
	private transient Flashcard activeCard = nullCard;

	public void addCard(Flashcard card) {
		if (activeCards == 0) { activeCard = card; }
		flashcards.add(card);
		activeCards++;
	}

	public void removeActiveCard() {
		if (activeCards == 0) { return; }
		getActiveCard().setActive(false);
		activeCards--;
		if (activeCards == 0) {
			activeCard = nullCard;
		} else {
			activeCard = getNextCard();
		}
	}

	public Flashcard getNextCard() {
		if (hasActiveCards()) {
			while (true) {
				cardIndex++;
				if (cardIndex > flashcards.size()) { cardIndex = 0; }
				activeCard = flashcards.get(cardIndex);
				if (activeCard.getActive()) {
					return activeCard;
				}
			}
		}
		return activeCard;
	}

	public Flashcard getActiveCard() {
		if (activeCard.equals(nullCard) && activeCards > 0) {
			activeCard = flashcards.get(cardIndex);
		}
		return activeCard;
	}

	public String getActiveCardFront() {
		return getActiveCard().getNativeFirst() ? activeCard.getText() : activeCard.getForeign();
	}

	public String getActiveCardBack() {
		return getActiveCard().getNativeFirst() ? activeCard.getForeign() : activeCard.getText();
	}

	public void flipActiveCard() {
		getActiveCard().setNativeFirst(!activeCard.getNativeFirst());
	}

	public boolean hasActiveCards() { return activeCards > 0; }
}
