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
		m.addObject("deck", deck);
		session.setAttribute("deck", deck);
		session.setAttribute("stay", 0);
		return m;
	}

	@RequestMapping("/game")
	public ModelAndView game(@RequestParam(value = "id", required = false) String id,
			@SessionAttribute(name = "deck", required = false) Deck deck,
			@SessionAttribute(name = "user", required = false) User sessionUser, HttpSession session) {
		Long i = sessionUser.getId();
		User user = userDao.findById(i).get();
		ModelAndView m = new ModelAndView("game");
		m.addObject("user", user);
		m.addObject("deck", deck);
		session.setAttribute("deck", deck);
		return m;
	}

	@RequestMapping("/login-confirmation")
	public ModelAndView submitSignup(User user, HttpSession session) {
		Long id = user.getId();
		//User user = userDao.findById(id).get();
		user.setBankroll((long) 500);
		user.setWins((int) 0);
		user.setLosses((int) 0);
		userDao.save(user);
		session.setAttribute("user", user);
		ModelAndView mv = new ModelAndView("welcome");
		return mv;
	}

	@RequestMapping("/deal")
	public ModelAndView deal(HttpSession session, 
			@SessionAttribute(name = "deck", required = false) Deck deck,
			@SessionAttribute(name = "user", required = false) User sessionUser,
			@RequestParam("betDeal") Integer bet ){
			
			
		session.setAttribute("bet", bet);


		
		if(deck.getRemaining() <= 12) {
			deck = a.shuffle(deck);
		}
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(a.getCard(deck.getId()));
		dealerHand.add(a.getCard(deck.getId()));
		List<Card> userHand = new ArrayList<>();
		userHand.add(a.getCard(deck.getId()));
		userHand.add(a.getCard(deck.getId()));
		if(getHandValue(userHand) == 21) {
			Long id = sessionUser.getId();
			User user = userDao.findById(id).get();
			user.setBankroll((long) (user.getBankroll() + 1.5*bet));
			user.setWins(user.getWins()+1);
			userDao.save(user);
			session.setAttribute("user", user);
			stay(session, userHand, dealerHand, deck, 0, user);
		}  else {
			Long id = sessionUser.getId();
			User user = userDao.findById(id).get();
			user.setBankroll((user.getBankroll() - bet));
			user.setLosses(user.getLosses() + 1);
			userDao.save(user);
			session.setAttribute("user", user);
		}
		session.setAttribute("userHand", userHand);
		session.setAttribute("userHandValue", getHandValue(userHand));
		session.setAttribute("dealerHand", dealerHand);
		session.setAttribute("stay", 0);
		if(userHand.get(0).getValue().equalsIgnoreCase(userHand.get(1).getValue())) {
			session.setAttribute("stay", 3);
		} else if(getHandValue(userHand) == 21){
			session.setAttribute("stay", 5);
		} else {
			session.setAttribute("stay", 2);			
		}
		return new ModelAndView("redirect:/game");
	}

	@RequestMapping("/hit")
	public ModelAndView hit(HttpSession session, @SessionAttribute(name = "deck") Deck deck,
			@SessionAttribute(name = "userHand") List<Card> userHand,
			@SessionAttribute(name = "dealerHand") List<Card> dealerHand, 
			@SessionAttribute(name = "bet") Integer bet,
			@SessionAttribute(name = "user", required = false) User sessionUser) {
		if(deck.getRemaining() <= 12) {
			deck = a.shuffle(deck);
		}
		userHand.add(a.getCard(deck.getId()));
		session.setAttribute("userHand", userHand);
		if (bust(userHand) == false) {
			session.setAttribute("userHandValue", getHandValue(userHand));
			session.setAttribute("stay", 1);
			if(getHandValue(userHand)== 21) {
				session.setAttribute("stay", 4);
			}
		} else {
			session.setAttribute("userHandValue", "BUST!");
			session.setAttribute("stay", 5);
		}
		return new ModelAndView("redirect:/game");
	}

	@RequestMapping("/stay")
	public ModelAndView stay(HttpSession session, 
			@SessionAttribute(name = "userHand") List<Card> userHand,
			@SessionAttribute(name = "dealerHand") List<Card> dealerHand, 
			@SessionAttribute(name = "deck") Deck deck,
			@SessionAttribute(name = "bet") Integer bet,
			@SessionAttribute(name = "user", required = false) User sessionUser) {

		session.setAttribute("userHand", userHand);
		session.setAttribute("stay", 5);
		while (dealerHit(dealerHand) == true) {
			dealerHand.add(a.getCard(deck.getId()));
		}
		if (bust(dealerHand) == true && bust(userHand) == false) {
			Long id = sessionUser.getId();
			User user = userDao.findById(id).get();
			user.setBankroll(user.getBankroll() + 2*bet);
			user.setWins(user.getWins()+ 1);
			userDao.save(user);
			session.setAttribute("user", user);
			session.setAttribute("dealerHandValue", "BUST");
		} else if(bust(dealerHand) == false && bust(userHand) == false) {
			session.setAttribute("dealerHandValue", getHandValue(dealerHand));
			if(getHandValue(dealerHand) < getHandValue(userHand)) {
				Long id = sessionUser.getId();
				User user = userDao.findById(id).get();
				user.setBankroll(user.getBankroll() + 2*bet);
				user.setWins(user.getWins()+ 1);
				userDao.save(user);
				session.setAttribute("user", user);
			} else if(getHandValue(dealerHand) == getHandValue(userHand)) {
				Long id = sessionUser.getId();
				User user = userDao.findById(id).get();
				user.setBankroll(user.getBankroll() + bet);
				userDao.save(user);
				session.setAttribute("user", user);
			}
		}
		List<Card> testHand = new ArrayList<>();//New hand from db
		String str = userHand.toString();// turns hand into a single string
//		after pulling hand from db
		str = (str.substring(1, str.length()-1));//removes square brackets
		String[] newStr = str.split(", ");//splits string from db into a list of strings each being a card
		for (int i = 0; i < newStr.length; i++){//loops turning each string into a card and adding it to a haND
			Card newC = new Card();//temp card to add to hand
			String[] tempCard = newStr[i].split(",");//splitting card/string into a string array each string a card value
			newC.setValue(tempCard[0]);
			newC.setImage(tempCard[1]);
			newC.setSuit(tempCard[2]);
			testHand.add(newC);
		}
		saveToDB(testHand, sessionUser);
		return new ModelAndView("redirect:/game");
	}
	
	@RequestMapping("/softStay")
	public ModelAndView softStay(HttpSession session, 
			@SessionAttribute(name = "userHand") List<Card> userHand,
			@SessionAttribute(name = "dealerHand") List<Card> dealerHand, 
			@SessionAttribute(name = "deck") Deck deck,
			@SessionAttribute(name = "bet") Integer bet,
			@SessionAttribute(name = "user", required = false) User sessionUser) {

		session.setAttribute("stay", 5);
		if (bust(dealerHand) == true && bust(userHand) == false) {
			Long id = sessionUser.getId();
			User user = userDao.findById(id).get();
			user.setBankroll(user.getBankroll() + 2*bet);
			userDao.save(user);
			session.setAttribute("user", user);
			session.setAttribute("dealerHandValue", "BUST");
		} else if(bust(dealerHand) == false && bust(userHand) == false) {
			session.setAttribute("dealerHandValue", getHandValue(dealerHand));
			if(getHandValue(dealerHand) < getHandValue(userHand)) {
				Long id = sessionUser.getId();
				User user = userDao.findById(id).get();
				user.setBankroll(user.getBankroll() + 2*bet);
				user.setWins(user.getWins()+ 1);
				userDao.save(user);
				session.setAttribute("user", user);
			} else if(getHandValue(dealerHand) == getHandValue(userHand)) {
				Long id = sessionUser.getId();
				User user = userDao.findById(id).get();
				user.setBankroll(user.getBankroll() + bet);
				userDao.save(user);
				session.setAttribute("user", user);
			}
		}
		List<Card> testHand = new ArrayList<>();//New hand from db
		String str = userHand.toString();// turns hand into a single string
//		after pulling hand from db
		str = (str.substring(1, str.length()-1));//removes square brackets
		String[] newStr = str.split(", ");//splits string from db into a list of strings each being a card
		for (int i = 0; i < newStr.length; i++){//loops turning each string into a card and adding it to a haND
			Card newC = new Card();//temp card to add to hand
			String[] tempCard = newStr[i].split(",");//splitting card/string into a string array each string a card value
			newC.setValue(tempCard[0]);
			newC.setImage(tempCard[1]);
			newC.setSuit(tempCard[2]);
			testHand.add(newC);
		}
		saveToDB(testHand, sessionUser);
		return new ModelAndView("redirect:/game");
	}
	
	@RequestMapping("/double")
	public ModelAndView doubleDown(HttpSession session,
			@SessionAttribute(name = "user", required = false) User sessionUser,
			@SessionAttribute(name="deck") Deck deck,
			@SessionAttribute(name="bet") Integer bet,
			@SessionAttribute(name="userHand") List<Card> userHand,
			@SessionAttribute(name="dealerHand") List<Card> dealerHand) {
		if(deck.getRemaining() <= 12) { 
			deck = a.shuffle(deck);
		}
		userHand.add(a.getCard(deck.getId()));
		session.setAttribute("userHand", userHand);
		Integer oldBet = (Integer) session.getAttribute("bet");
		session.setAttribute("bet", oldBet*2);
		Long id = sessionUser.getId();
		User user = userDao.findById(id).get();
		user.setBankroll(user.getBankroll() - oldBet);
		userDao.save(user);
		session.setAttribute("user", user);
		if (bust(userHand) == false) {
			session.setAttribute("userHandValue", getHandValue(userHand));
			session.setAttribute("stay", 4);
			
			if(getHandValue(userHand)== 21) {
				session.setAttribute("stay", 4);
			}
		} else {
			session.setAttribute("userHandValue", "BUST!");
			session.setAttribute("stay", 5);
		}
		stay(session, userHand, dealerHand, deck, 2*bet, user);
		return new ModelAndView("redirect:/game");
	}
	
	@RequestMapping("/surrender")
	public ModelAndView surrender(HttpSession session,
			@SessionAttribute(name="dealerHand") List<Card> dealerHand,
			@SessionAttribute(name="userHand") List<Card> userHand,
			@SessionAttribute(name="deck") Deck deck,
			@SessionAttribute(name = "user", required = false) User sessionUser,
			@SessionAttribute(name="bet") Integer bet) {
			Integer oldBet = (Integer) session.getAttribute("bet");
			session.setAttribute("bet", oldBet/2);
			Long id = sessionUser.getId();
			User user = userDao.findById(id).get();
			user.setBankroll(user.getBankroll() + oldBet/2);
			user.setLosses(user.getLosses() + 1);
			userDao.save(user);
			session.setAttribute("user", user);
			stay(session, userHand, dealerHand, deck, 0, user);
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
	public ModelAndView viewInstructions()	{
		ModelAndView m = new ModelAndView("instructions");
		return m;
	}
	
	@RequestMapping("/lastHands")
	public ModelAndView lastfive(HttpSession session,
			@SessionAttribute(name = "user", required = false) User sessionUser) {
		ModelAndView mv = new ModelAndView("lastfive");
		Long id = sessionUser.getId();//grabs user id from the session
		List<Card> hand1 = handToCardList(getLastHand5(id));//creates the list of cards of the most recent hand
		mv.addObject("hand1", hand1);//adds it to the model and view
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
		String str = userHand.toString();//Changes the list of cards to a single string
		Long id = user.getId();//gets the users id number
		Hand hand = new Hand();//creates a hand class this is needed to read and write to the table
		hand.setUserId(id);
		hand.setHand(str);
		handDao.save(hand);//saves the userhand to table with user id And automatically assigns a hand id
	}
	public List<Card> handToCardList(Hand hand) {
		List<Card> testHand = new ArrayList<>();
		String str = hand.getHand();// turns hand into a single string
//		after pulling hand from db
		str = (str.substring(1, str.length()-1));//removes square brackets
		String[] newStr = str.split(", ");//splits string from db into a list of strings each being a card
		for (int i = 0; i < newStr.length; i++){//loops turning each string into a card and adding it to a haND
			Card newC = new Card();//temp card to add to hand
			String[] tempCard = newStr[i].split(",");//splitting card/string into a string array each string a card value
			newC.setValue(tempCard[0]);
			newC.setImage(tempCard[1]);
			newC.setSuit(tempCard[2]);
			testHand.add(newC);
			System.out.println(i + ": " + newC);
		}
		return testHand;
	}
	

	public Hand getLastHand5(Long id){
		List<Hand> hands = handDao.findFirst5ByUserIdOrderByHandIdDesc(id);//grabs the last 5 hands from table
		return hands.get(0);//returns the most recent hand
	}
	
	public Hand getLastHand4(Long id){
		List<Hand> hands = handDao.findFirst5ByUserIdOrderByHandIdDesc(id);
		return hands.get(1);//returns second most recent hand
	}
	
	public Hand getLastHand3(Long id){
		List<Hand> hands = handDao.findFirst5ByUserIdOrderByHandIdDesc(id);
		return hands.get(2);
	}
	
	public Hand getLastHand2(Long id){
		List<Hand> hands = handDao.findFirst5ByUserIdOrderByHandIdDesc(id);
		return hands.get(3);
	}
	
	public Hand getLastHand1(Long id){
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