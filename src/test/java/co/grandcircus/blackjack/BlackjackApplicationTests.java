package co.grandcircus.blackjack;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import co.grandcircus.blackjack.entity.Card;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlackjackApplicationTests {

	@Test
	public void contextLoads() {
	}
	@Test
	public void acesInHand() {
		ArrayList<Card> hand = new ArrayList<Card>();
		Card c1 = new Card("7");
		Card c2 = new Card("ACE");
		Card c3 = new Card("ACE");
		Card c4 = new Card("ACE");
		hand.add(c1);
		hand.add(c2);
		hand.add(c3);
		hand.add(c4);
		ArrayList<Card> hand2 = new ArrayList<Card>();
		Card c5 = new Card("7");
		Card c6 = new Card("ACE");
		hand2.add(c5);
		hand2.add(c6);
		ArrayList<Card> hand3 = new ArrayList<Card>();
		Card c7 = new Card("7");
		Card c8 = new Card("2");
		Card c9 = new Card("QUEEN");
		Card c10 = new Card("KING");
		hand3.add(c7);
		hand3.add(c8);
		hand3.add(c9);
		hand3.add(c10);
		ArrayList<Card> hand4 = new ArrayList<Card>();
		Card c11 = new Card("7");
		Card c12 = new Card("KING");
		Card c13 = new Card("ACE");
		Card c14 = new Card("ACE");
		hand4.add(c11);
		hand4.add(c12);
		hand4.add(c13);
		hand4.add(c14);
		assertEquals(3, Card.acesInHand(hand));
		assertEquals(1, Card.acesInHand(hand2));
		assertEquals(0, Card.acesInHand(hand3));
		assertEquals(2, Card.acesInHand(hand4));
	}
	@Test
	public void handValue() {
		ArrayList<Card> hand = new ArrayList<Card>();
		Card c1 = new Card("7");
		Card c2 = new Card("ACE");
		Card c3 = new Card("ACE");
		Card c4 = new Card("ACE");
		hand.add(c1);
		hand.add(c2);
		hand.add(c3);
		hand.add(c4);
		ArrayList<Card> hand2 = new ArrayList<Card>();
		Card c5 = new Card("7");
		Card c6 = new Card("ACE");
		hand2.add(c5);
		hand2.add(c6);
		ArrayList<Card> hand3 = new ArrayList<Card>();
		Card c7 = new Card("7");
		Card c8 = new Card("2");
		Card c9 = new Card("QUEEN");
		Card c10 = new Card("KING");
		hand3.add(c7);
		hand3.add(c8);
		hand3.add(c9);
		hand3.add(c10);
		ArrayList<Card> hand4 = new ArrayList<Card>();
		Card c11 = new Card("7");
		Card c12 = new Card("KING");
		Card c13 = new Card("ACE");
		Card c14 = new Card("ACE");
		hand4.add(c11);
		hand4.add(c12);
		hand4.add(c13);
		hand4.add(c14);
		assertEquals(20, Card.getHandValue(hand));
		assertEquals(18, Card.getHandValue(hand2));
		assertEquals(29, Card.getHandValue(hand3));
		assertEquals(19, Card.getHandValue(hand4));
	}
	@Test
	public void canSplit() {
		ArrayList<Card> hand = new ArrayList<Card>();
		Card c3 = new Card("ACE");
		Card c4 = new Card("ACE");
		hand.add(c3);
		hand.add(c4);
		ArrayList<Card> hand2 = new ArrayList<Card>();
		Card c5 = new Card("7");
		Card c6 = new Card("ACE");
		hand2.add(c5);
		hand2.add(c6);
		ArrayList<Card> hand3 = new ArrayList<Card>();
		Card c7 = new Card("7");
		Card c8 = new Card("7");
		hand3.add(c7);
		hand3.add(c8);
		ArrayList<Card> hand4 = new ArrayList<Card>();
		Card c11 = new Card("7");
		Card c12 = new Card("KING");
		Card c13 = new Card("ACE");
		Card c14 = new Card("ACE");
		hand4.add(c11);
		hand4.add(c12);
		hand4.add(c13);
		hand4.add(c14);
		assertEquals(true, Card.canSplit(hand));
		assertEquals(false, Card.canSplit(hand2));
		assertEquals(true, Card.canSplit(hand3));
		assertEquals(false, Card.canSplit(hand4));
	}
	@Test
	public void isSoftValue() {
		ArrayList<Card> hand = new ArrayList<Card>();
		Card c1 = new Card("7");
		Card c2 = new Card("ACE");
		Card c3 = new Card("ACE");
		Card c4 = new Card("ACE");
		hand.add(c1);
		hand.add(c2);
		hand.add(c3);
		hand.add(c4);
		ArrayList<Card> hand2 = new ArrayList<Card>();
		Card c5 = new Card("7");
		Card c6 = new Card("ACE");
		hand2.add(c5);
		hand2.add(c6);
		ArrayList<Card> hand3 = new ArrayList<Card>();
		Card c7 = new Card("7");
		Card c8 = new Card("2");
		Card c9 = new Card("QUEEN");
		Card c10 = new Card("KING");
		hand3.add(c7);
		hand3.add(c8);
		hand3.add(c9);
		hand3.add(c10);
		ArrayList<Card> hand4 = new ArrayList<Card>();
		Card c11 = new Card("7");
		Card c12 = new Card("KING");
		Card c13 = new Card("ACE");
		Card c14 = new Card("ACE");
		hand4.add(c11);
		hand4.add(c12);
		hand4.add(c13);
		hand4.add(c14);
		assertEquals(true, Card.isValueSoft(hand));
		assertEquals(true, Card.isValueSoft(hand2));
		assertEquals(false, Card.isValueSoft(hand3));
		assertEquals(false, Card.isValueSoft(hand4));
	}
	
	@Test
	public void canDouble() {
		ArrayList<Card> hand = new ArrayList<Card>();
		Card c1 = new Card("7");
		Card c2 = new Card("ACE");
		Card c3 = new Card("ACE");
		Card c4 = new Card("ACE");
		hand.add(c1);
		hand.add(c2);
		hand.add(c3);
		hand.add(c4);
		ArrayList<Card> hand2 = new ArrayList<Card>();
		Card c5 = new Card("7");
		Card c6 = new Card("ACE");
		hand2.add(c5);
		hand2.add(c6);
		ArrayList<Card> hand3 = new ArrayList<Card>();
		Card c7 = new Card("7");
		Card c8 = new Card("2");
		Card c10 = new Card("KING");
		hand3.add(c7);
		hand3.add(c8);
		hand3.add(c10);
		ArrayList<Card> hand4 = new ArrayList<Card>();
		Card c11 = new Card("7");
		Card c12 = new Card("KING");
		Card c13 = new Card("ACE");
		Card c14 = new Card("ACE");
		hand4.add(c11);
		hand4.add(c12);
		hand4.add(c13);
		hand4.add(c14);
		assertEquals(false, Card.canDouble(hand));
		assertEquals(true, Card.canDouble(hand2));
		assertEquals(false, Card.canDouble(hand3));
		assertEquals(false, Card.canDouble(hand4));
	}


}
