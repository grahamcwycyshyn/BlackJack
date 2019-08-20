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
import co.grandcircus.blackjack.entity.User;
import co.grandcircus.blackjack.entity.Hand;

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
		System.out.println(session.getAttribute("deck"));
		return m;
	}

	@RequestMapping("/game")
	public ModelAndView game(@RequestParam(value = "id", required = false) String id,
			@SessionAttribute(name = "deck", required = false) Deck deck, HttpSession session) {
		Long i = (long) 1;
		User user = userDao.findById(i).get();
		ModelAndView m = new ModelAndView("game");
		m.addObject("user", user);
		m.addObject("deck", deck);
		session.setAttribute("deck", deck);
		System.out.println(session.getAttribute("deck"));
		return m;
	}

	@RequestMapping("/signup")
	public ModelAndView showSignup() {
		return new ModelAndView("signup-form");
	}

	@RequestMapping("/signup-confirmation")
	public ModelAndView submitSignup(User user, HttpSession session) {
		userDao.save(user);
		session.setAttribute("user", user);
		ModelAndView mv = new ModelAndView("thanks");
		return mv;
	}

//	@RequestMapping("/login")
//	public ModelAndView showLogin() {
//		return new ModelAndView("login-form");
//	}
	@RequestMapping("/deal")
	public ModelAndView deal(HttpSession session, @SessionAttribute(name = "deck", required = false) Deck deck,
			@RequestParam("betDeal") Integer bet) {
		session.setAttribute("bet", bet);
		System.out.println(session.getAttribute("deck"));
		Object d = session.getAttribute("deck");
		System.out.println(d);
		List<Card> dealerHand = new ArrayList<>();
		System.out.println(deck.getId());
		System.out.println(a.getCard(deck.getId()));
		dealerHand.add(a.getCard(deck.getId()));
		dealerHand.add(a.getCard(deck.getId()));
		List<Card> userHand = new ArrayList<>();
		userHand.add(a.getCard(deck.getId()));
		userHand.add(a.getCard(deck.getId()));
		if(getHandValue(userHand) == 21) {
			Long id = (long) 1;
			User user = userDao.findById(id).get();
			user.setBankroll((long) (user.getBankroll() + 1.5*bet));
			userDao.save(user);
			session.setAttribute("user", user);
		}
		session.setAttribute("userHand", userHand);
		session.setAttribute("userHandValue", getHandValue(userHand));
		session.setAttribute("dealerHand", dealerHand);
		return new ModelAndView("redirect:/game");

	}

	@RequestMapping("/hit")
	public ModelAndView hit(HttpSession session, @SessionAttribute(name = "deck") Deck deck,
			@SessionAttribute(name = "userHand") List<Card> userHand,
			@SessionAttribute(name = "dealerHand") List<Card> dealerHand, @SessionAttribute(name = "bet") Integer bet) {
		userHand.add(a.getCard(deck.getId()));
		
		session.setAttribute("userHand", userHand);
		if (bust(userHand) == false) {
			session.setAttribute("userHandValue", getHandValue(userHand));
		} else {
			session.setAttribute("userHandValue", "BUST!");
			Long id = (long) 1;
			User user = userDao.findById(id).get();
			user.setBankroll(user.getBankroll() - bet);
			userDao.save(user);
			session.setAttribute("user", user);
		}
		return new ModelAndView("redirect:/game");

	}

	@RequestMapping("/stay")
	public ModelAndView stay(HttpSession session, @SessionAttribute(name = "userHand") List<Card> userHand,
			@SessionAttribute(name = "dealerHand") List<Card> dealerHand, @SessionAttribute(name = "deck") Deck deck,
			@SessionAttribute(name = "bet") Integer bet) {

		session.setAttribute("userHand", userHand);
		
		while (dealerHit(dealerHand) == true) {
			dealerHand.add(a.getCard(deck.getId()));
		}
		System.out.println(bust(dealerHand));
		System.out.println(bust(userHand));
		if (bust(dealerHand) == true && bust(userHand) == false) {
			Long id = (long) 1;
			User user = userDao.findById(id).get();
			user.setBankroll(user.getBankroll() + bet);
			userDao.save(user);
			session.setAttribute("user", user);
		} else if(bust(dealerHand) == false && bust(userHand) == false) {
			if(getHandValue(dealerHand) < getHandValue(userHand)) {
				Long id = (long) 1;
				User user = userDao.findById(id).get();
				user.setBankroll(user.getBankroll() + bet);
				userDao.save(user);
				session.setAttribute("user", user);
			} else if(getHandValue(dealerHand) > getHandValue(userHand)) {
				Long id = (long) 1;
				User user = userDao.findById(id).get();
				user.setBankroll(user.getBankroll() - bet);
				userDao.save(user);
				session.setAttribute("user", user);
			}
		}
		return new ModelAndView("redirect:/game");
	}

//	@RequestMapping("/bet")
//	public ModelAndView bet(@RequestParam(value="bankroll", required=true) Long bet) {
//		ModelAndView m = new ModelAndView("index");
//		Long id = (long) 1;
//		User user = userDao.findById(id).get();
//		Hand hand = handDao.findTopByOrderByIdDesc();
//		user.setBankroll(user.getBankroll() - bet);
//		userDao.save(user);
//		return m;
//	}
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

}