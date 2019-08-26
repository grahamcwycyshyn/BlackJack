package co.grandcircus.blackjack;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import co.grandcircus.blackjack.dao.HandRepository;
import co.grandcircus.blackjack.dao.UserRepository;
import co.grandcircus.blackjack.entity.Card;
import co.grandcircus.blackjack.entity.Deck;
import co.grandcircus.blackjack.entity.GameState;
import co.grandcircus.blackjack.entity.Hand;
import co.grandcircus.blackjack.entity.User;

//Note: need to change bet method when pulling in multiple users, currently only takes in one user
@RestController
public class BlackjackController {

	@Autowired
	ApiService a;

	@Autowired
	UserRepository userDao;

	@Autowired
	HandRepository handDao;

	@RequestMapping("/")
	public ModelAndView index(HttpSession session) {
		Deck deck = a.getDeck();
		ModelAndView m = new ModelAndView("index");
		GameState gamestate = new GameState();
		gamestate.setDeck(deck);
		session.setAttribute("gamestate", gamestate);
		session.setAttribute("stay", 0);
		return m;
	}

	@RequestMapping("/game")
	public ModelAndView game(@RequestParam(value = "id", required = false) String id,
			@SessionAttribute(name = "gamestate") GameState gamestate, HttpSession session) {
		Long i = gamestate.getUsers().get(0).getId();
		session.setAttribute("gamestate", gamestate);
		User user = userDao.findById(i).get();
		ModelAndView m = new ModelAndView("game");
		m.addObject("user", user);
		m.addObject("deck", gamestate.getDeck());
		return m;
	}

	@RequestMapping("/login-confirmation")
	public ModelAndView submitSignup(User user, HttpSession session,
			@SessionAttribute(name = "gamestate") GameState gamestate) {
		Long id = user.getId();
		// User user = userDao.findById(id).get();
		user.setBankroll((long) 500);
		user.setWins((int) 0);
		user.setLosses((int) 0);
		user.setWinLoss((double) 0 );
		userDao.save(user);
		List<User> users = new ArrayList<>();
		users.add(user);
//		gamestate.getTurn().set(0, false);
		gamestate.setUsers(users);
		session.setAttribute("gamestate", gamestate);
		ModelAndView mv = new ModelAndView("welcome");
		return mv;
	}

	@RequestMapping("/join")
	public ModelAndView join(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate,
			User user) {
		Long id = user.getId();
		// User user = userDao.findById(id).get();
		user.setBankroll((long) 500);
		user.setWins((int) 0);
		user.setLosses((int) 0);
		userDao.save(user);
		gamestate.getUsers().add(user);
//		gamestate.getTurn().add(false);
		session.setAttribute("gamestate", gamestate);
		return new ModelAndView("redirect:/game");
	}

	public List<Card> dealHand(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate) {
		List<Card> userHand = new ArrayList<>();
		userHand.add(a.getCard(gamestate.getDeck().getId()));
		userHand.add(a.getCard(gamestate.getDeck().getId()));
		return userHand;
	}

	@RequestMapping("/deal")
	public ModelAndView deal(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate,
			@RequestParam("betDeal") Integer bet) {
		List<Integer> bets = new ArrayList<>();
		bets.add(bet);
		gamestate.setBets(bets);
		session.setAttribute("gamestate", gamestate);
		if (gamestate.getDeck().getRemaining() <= 12) {
			Deck deck = gamestate.getDeck();
			a.shuffle(deck);
			gamestate.setDeck(deck);
			session.setAttribute("gamestate", gamestate);
		}
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(a.getCard(gamestate.getDeck().getId()));
		dealerHand.add(a.getCard(gamestate.getDeck().getId()));
//<<<<<<< HEAD
//=======
//		List<Card> userHand = new ArrayList<>();
//		userHand.add(a.getCard(gamestate.getDeck().getId()));
//		userHand.add(a.getCard(gamestate.getDeck().getId()));
//		if(getHandValue(userHand) == 21) {
//			Long id = gamestate.getUsers().get(0).getId();
//			User user = userDao.findById(id).get();
//			user.setBankroll((long) (user.getBankroll() + 1.5*bet));
//			user.setWins(user.getWins()+1);
//			userDao.save(user);
//			stay(session, gamestate);
//		}  else {
//			Long id = gamestate.getUsers().get(0).getId();
//			User user = userDao.findById(id).get();
//			user.setBankroll((user.getBankroll() - bet));
//			user.setLosses(user.getLosses() + 1);
//			user.setWinLoss(user.getWinLoss()  );
//			userDao.save(user);
//			session.setAttribute("gamestate", gamestate.getUsers().set(0, user));
//		}
//		List<List<Card>> hands = new ArrayList<>();
//		hands.add(userHand);
//		gamestate.setHands(hands);
//		session.setAttribute("gamestate", gamestate);
//>>>>>>> e511ca10e40f6e7f314ac313a1f0c2dca2fde796
		gamestate.setDealerHand(dealerHand);
		for (int i = 0; i < gamestate.getUsers().size(); i++) {
			if(gamestate.getHands() == null) {
				List<List<Card>> list = new ArrayList<>();
				list.add(dealHand(session, gamestate));
				gamestate.setHands(list);
				List<Integer> values = new ArrayList<>();
				values.add(getHandValue(list.get(0)));
				gamestate.setHandValues(values);
			} else if(gamestate.getHands().size() < gamestate.getUsers().size()) {
				List<Card> list = dealHand(session, gamestate);
				gamestate.getHands().add(list);
				Integer value = getHandValue(list);
				gamestate.getHandValues().add(value);
			}
			else {
			List<Card> list = dealHand(session, gamestate);
			gamestate.getHands().set(i, list);
			gamestate.getHandValues().set(i, getHandValue(list));
			}
			if (getHandValue(gamestate.getHands().get(i)) == 21) {
				Long id = gamestate.getUsers().get(i).getId();
				User user = userDao.findById(id).get();
				user.setBankroll((long) (user.getBankroll() + 1.5 * bet));
				user.setWins(user.getWins() + 1);
				userDao.save(user);
				stay(session, gamestate);
			} else {
				Long id = gamestate.getUsers().get(0).getId();
				User user = userDao.findById(id).get();
				user.setBankroll((user.getBankroll() - bet));
				user.setLosses(user.getLosses() + 1);
				userDao.save(user);
				session.setAttribute("gamestate", gamestate.getUsers().set(0, user));
			}
//		List<List<Card>> hands = new ArrayList<>();
//		hands.add(userHand);
//		gamestate.setHands(hands);
			session.setAttribute("gamestate", gamestate);
			session.setAttribute("userHandValue", getHandValue(gamestate.getHands().get(0)));
			session.setAttribute("stay", 0);
			if (gamestate.getHands().get(i).get(0).getValue()
					.equalsIgnoreCase(gamestate.getHands().get(i).get(1).getValue())) {
				session.setAttribute("stay", 3);
			} else if (getHandValue(gamestate.getHands().get(i)) == 21) {
				session.setAttribute("stay", 5);
			} else {
				session.setAttribute("stay", 2);
			}
		}
		return new ModelAndView("redirect:/game");
	}

	@RequestMapping("/hit")
	public ModelAndView hit(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate) {
		if (gamestate.getDeck().getRemaining() <= 12) {
			Deck deck = gamestate.getDeck();
			a.shuffle(deck);
			gamestate.setDeck(deck);
			session.setAttribute("gamestate", gamestate);
		}
		List<Card> userHand = gamestate.getHands().get(0);
		userHand.add(a.getCard(gamestate.getDeck().getId()));
		List<List<Card>> hands = new ArrayList<>();
		hands.add(userHand);
		gamestate.setHands(hands);
		session.setAttribute("userHandValue", getHandValue(gamestate.getHands().get(0)));
		session.setAttribute("gamestate", gamestate);
		if (bust(userHand) == false) {
			session.setAttribute("stay", 1);
			if (getHandValue(userHand) == 21) {
				session.setAttribute("stay", 4);
			}
		} else {
			session.setAttribute("stay", 5);
		}
		return new ModelAndView("redirect:/game");
	}

	@RequestMapping("/stay")
	public ModelAndView stay(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate) {

		session.setAttribute("stay", 5);
		while (dealerHit(gamestate.getDealerHand()) == true) {
			gamestate.getDealerHand().add(a.getCard(gamestate.getDeck().getId()));
		}
		session.setAttribute("dealerHandValue", getHandValue(gamestate.getDealerHand()));
		if (bust(gamestate.getDealerHand()) == true && bust(gamestate.getHands().get(0)) == false) {
			Long id = gamestate.getUsers().get(0).getId();
			User user = userDao.findById(id).get();
			user.setBankroll(user.getBankroll() + 2 * gamestate.getBets().get(0));
			user.setWins(user.getWins() + 1);
			userDao.save(user);
			gamestate.getUsers().set(0, user);
			session.setAttribute("gamestate", gamestate);
		} else if (bust(gamestate.getDealerHand()) == false && bust(gamestate.getHands().get(0)) == false) {
			if (getHandValue(gamestate.getDealerHand()) < getHandValue(gamestate.getHands().get(0))) {
				Long id = gamestate.getUsers().get(0).getId();
				User user = userDao.findById(id).get();
				user.setBankroll(user.getBankroll() + 2 * gamestate.getBets().get(0));
				user.setWins(user.getWins() + 1);
				userDao.save(user);
				gamestate.getUsers().set(0, user);
				session.setAttribute("gamestate", gamestate);
			} else if (getHandValue(gamestate.getDealerHand()) == getHandValue(gamestate.getHands().get(0))) {
				Long id = gamestate.getUsers().get(0).getId();
				User user = userDao.findById(id).get();
				user.setBankroll(user.getBankroll() + gamestate.getBets().get(0));
				userDao.save(user);
				gamestate.getUsers().set(0, user);
				session.setAttribute("gamestate", gamestate);
			}
		}
		List<Card> testHand = new ArrayList<>();// New hand from db
		String str = gamestate.getHands().get(0).toString();// turns hand into a single string
//		after pulling hand from db
		str = (str.substring(1, str.length() - 1));// removes square brackets
		String[] newStr = str.split(", ");// splits string from db into a list of strings each being a card
		for (int i = 0; i < newStr.length; i++) {// loops turning each string into a card and adding it to a haND
			Card newC = new Card();// temp card to add to hand
			String[] tempCard = newStr[i].split(",");// splitting card/string into a string array each string a card
														// value
			newC.setValue(tempCard[0]);
			newC.setImage(tempCard[1]);
			newC.setSuit(tempCard[2]);
			testHand.add(newC);
		}
		saveToDB(testHand, gamestate.getUsers().get(0));
		return new ModelAndView("redirect:/game");
	}

	@RequestMapping("/double")
	public ModelAndView doubleDown(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate) {
		if (gamestate.getDeck().getRemaining() <= 12) {
			Deck deck = gamestate.getDeck();
			a.shuffle(deck);
			gamestate.setDeck(deck);
			session.setAttribute("gamestate", gamestate);
		}
		gamestate.getHands().get(0).add(a.getCard(gamestate.getDeck().getId()));
		session.setAttribute("gamestate", gamestate);
		session.setAttribute("userHandValue", getHandValue(gamestate.getHands().get(0)));
		Integer oldBet = gamestate.getBets().get(0);
		gamestate.getBets().set(0, oldBet * 2);
		Long id = gamestate.getUsers().get(0).getId();
		User user = userDao.findById(id).get();
		user.setBankroll(user.getBankroll() - oldBet);
		userDao.save(user);
		gamestate.getUsers().set(0, user);
		session.setAttribute("gamestate", gamestate);
		if (bust(gamestate.getHands().get(0)) == false) {
//			session.setAttribute("userHandValue", getHandValue(userHand));
			session.setAttribute("stay", 4);

			if (getHandValue(gamestate.getHands().get(0)) == 21) {
				session.setAttribute("stay", 4);
			}
		} else {
//			session.setAttribute("userHandValue", "BUST!");
			session.setAttribute("stay", 5);
		}
		stay(session, gamestate);
		return new ModelAndView("redirect:/game");
	}

	@RequestMapping("/surrender")
	public ModelAndView surrender(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate) {
		Integer oldBet = gamestate.getBets().get(0);
		gamestate.getBets().set(0, oldBet / 2);
		Long id = gamestate.getUsers().get(0).getId();
		User user = userDao.findById(id).get();
		user.setBankroll(user.getBankroll() + oldBet / 2);
		user.setLosses(user.getLosses() + 1);
		userDao.save(user);
		gamestate.getUsers().set(0, user);
		session.setAttribute("gamestate", gamestate);
		stay(session, gamestate);
		return new ModelAndView("redirect:/game");
	}

//	@RequestMapping("/split")
//	public ModelAndView split(HttpSession session,
//			@SessionAttribute(name="userHand") List<Card> userHand,
//			@SessionAttribute(name="deck") Deck deck) {
//		List<Card> newUserHand = new ArrayList<>();
//		newUserHand.add(userHand.get(0));
//		newUserHand.add(a.getCard(deck.getId()));
//		session.setAttribute("userHand", newUserHand);
//		List<Card> splitUserHand = new ArrayList<>();
//		splitUserHand.add(userHand.get(1));
//		splitUserHand.add(a.getCard(deck.getId()));
//		session.setAttribute("splitUserHand", splitUserHand);
//	}

	@RequestMapping("/instructions")
	public ModelAndView viewInstructions() {
		ModelAndView m = new ModelAndView("instructions");
		return m;
	}

	@RequestMapping("/lastHands")
	public ModelAndView lastfive(HttpSession session,
			@SessionAttribute(name = "user", required = false) User sessionUser) {
		ModelAndView mv = new ModelAndView("lastfive");
		Long id = sessionUser.getId();// grabs user id from the session
		List<Card> hand1 = handToCardList(getLastHand5(id));// creates the list of cards of the most recent hand
		mv.addObject("hand1", hand1);// adds it to the model and view
		List<Card> hand2 = handToCardList(getLastHand4(id));
		mv.addObject("hand2", hand2);
		List<Card> hand3 = handToCardList(getLastHand3(id));
		mv.addObject("hand3", hand3);
		List<Card> hand4 = handToCardList(getLastHand2(id));
		mv.addObject("hand4", hand4);
		List<Card> hand5 = handToCardList(getLastHand1(id));
		mv.addObject("hand5", hand5);
		return mv;
	}

	public void saveToDB(List<Card> userHand, User user) {
		String str = userHand.toString();// Changes the list of cards to a single string
		Long id = user.getId();// gets the users id number
		Hand hand = new Hand();// creates a hand class this is needed to read and write to the table
		hand.setUserId(id);
		hand.setHand(str);
		handDao.save(hand);// saves the userhand to table with user id And automatically assigns a hand id
	}

	public List<Card> handToCardList(Hand hand) {
		List<Card> testHand = new ArrayList<>();
		String str = hand.getHand();// turns hand into a single string
//		after pulling hand from db
		str = (str.substring(1, str.length() - 1));// removes square brackets
		String[] newStr = str.split(", ");// splits string from db into a list of strings each being a card
		for (int i = 0; i < newStr.length; i++) {// loops turning each string into a card and adding it to a haND
			Card newC = new Card();// temp card to add to hand
			String[] tempCard = newStr[i].split(",");// splitting card/string into a string array each string a card
														// value
			newC.setValue(tempCard[0]);
			newC.setImage(tempCard[1]);
			newC.setSuit(tempCard[2]);
			testHand.add(newC);
			System.out.println(i + ": " + newC);
		}
		return testHand;
	}

	public Hand getLastHand5(Long id) {
		List<Hand> hands = handDao.findFirst5ByUserIdOrderByHandIdDesc(id);// grabs the last 5 hands from table
		return hands.get(0);// returns the most recent hand
	}

	public Hand getLastHand4(Long id) {
		List<Hand> hands = handDao.findFirst5ByUserIdOrderByHandIdDesc(id);
		return hands.get(1);// returns second most recent hand
	}

	public Hand getLastHand3(Long id) {
		List<Hand> hands = handDao.findFirst5ByUserIdOrderByHandIdDesc(id);
		return hands.get(2);
	}

	public Hand getLastHand2(Long id) {
		List<Hand> hands = handDao.findFirst5ByUserIdOrderByHandIdDesc(id);
		return hands.get(3);
	}

	public Hand getLastHand1(Long id) {
		List<Hand> hands = handDao.findFirst5ByUserIdOrderByHandIdDesc(id);
		return hands.get(4);
	}

//	@RequestMapping("/lastHands")
//	public ModelAndView lastfive(HttpSession session,
//			@SessionAttribute(name = "user", required = false) User sessionUser,
//			@SessionAttribute(name = "hand", required = false) Hand sessionHand) {
//		ModelAndView mv = new ModelAndView("lastfive");
//		Long id = sessionUser.getId();//grabs user id from the session
//		Long dealer_id = sessionHand.getHandId(); //grabs hand id from the session
//		List<Card> hand1 = handToCardList(getLastHand5(id));//creates the list of cards of the most recent hand
//		List<Card> dealerhand1 = handToCardList(getLastHand5(dealer_id));
//		mv.addObject("hand1", hand1);//adds it to the model and view
//		mv.addObject("dealerhand1", dealerhand1);
//		List<Card> hand2 = handToCardList(getLastHand4(id));
//		List<Card> dealerhand2 = handToCardList(getLastHand4(dealer_id));
//		mv.addObject("hand2", hand2);
//		mv.addObject("dealerhand2", dealerhand2);
//		List<Card> hand3 = handToCardList(getLastHand3(id));
//		List<Card> dealerhand3 = handToCardList(getLastHand3(dealer_id));
//		mv.addObject("hand3", hand3);
//		mv.addObject("dealerhand3", dealerhand3);
//		List<Card> hand4 = handToCardList(getLastHand2(id));
//		List<Card> dealerhand4 = handToCardList(getLastHand2(dealer_id));
//		mv.addObject("hand4", hand4);
//		mv.addObject("dealerhand4", dealerhand4);
//		List<Card> hand5 = handToCardList(getLastHand1(id));
//		List<Card> dealerhand5 = handToCardList(getLastHand1(dealer_id));
//		mv.addObject("hand5", hand5);
//		mv.addObject("dealerhand5", dealerhand5);
//		return mv;
//	}
//	
//	
//	

	public boolean bust(List<Card> hand) {
		if (getHandValue(hand) > 21) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canHit(List<Card> hand) {
		if (!bust(hand)) {
			return true;
		} else {
			return false;
		}
	}

	public int getHandValue(List<Card> hand) {
		int score = 0;
		int aceCount = acesInHand(hand);
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i).getValue().equalsIgnoreCase("ACE")) {
				score += 1;
			} else if (hand.get(i).getValue().equalsIgnoreCase("KING")
					|| hand.get(i).getValue().equalsIgnoreCase("QUEEN")
					|| hand.get(i).getValue().equalsIgnoreCase("JACK")) {
				score += 10;
			} else {
				score += Integer.parseInt(hand.get(i).getValue());
			}
		}
		for (int i = aceCount; i > 0; i--) {
			if (score + (i * 10) < 22) {
				score += 10;
			}
		}
		return score;
	}

	public int acesInHand(List<Card> hand) {
		int aceCount = 0;
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i).getValue().equalsIgnoreCase("ACE")) {
				aceCount++;
			}
		}
		return aceCount;
	}

	public boolean isValueSoft(List<Card> hand) {
		int score = 0;
		int aceCount = acesInHand(hand);
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i).getValue().equalsIgnoreCase("ACE")) {
				score += 1;
			} else if (hand.get(i).getValue().equalsIgnoreCase("KING")
					|| hand.get(i).getValue().equalsIgnoreCase("QUEEN")
					|| hand.get(i).getValue().equalsIgnoreCase("JACK")) {
				score += 10;
			} else {
				score += Integer.parseInt(hand.get(i).getValue());
			}
		}
		for (int i = aceCount; i > 0; i--) {
			if (score + (i * 10) < 22) {
				return true;
			}
		}
		return false;
	}

	public boolean dealerHit(List<Card> hand) {
		boolean isSoft = isValueSoft(hand);
		int score = getHandValue(hand);
		if (isSoft && score < 18) {
			return true;
		} else if (score < 17) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canSplit(List<Card> hand) {
		if (hand.size() > 2) {
			return false;
		} else if (hand.get(0).getValue().equalsIgnoreCase(hand.get(1).getValue())) {
			return true;
		} else {
			return false;
		}
	}

	public boolean canDouble(List<Card> hand) {
		if (hand.size() > 2) {
			return false;
		} else {
			return true;
		}

	}

}