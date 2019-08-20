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
//		System.out.println(session.getAttribute("deck"));
		session.setAttribute("stay", 0);
		System.out.println("stay home:" + session.getAttribute("stay"));
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
//		System.out.println(session.getAttribute("deck"));
		System.out.println("stay game:" + session.getAttribute("stay"));
		return m;
	}

	@RequestMapping("/login-confirmation")
	public ModelAndView submitSignup(User user, HttpSession session) {
		userDao.save(user);
		session.setAttribute("user", user);
		ModelAndView mv = new ModelAndView("welcome");
		return mv;
	}

	@RequestMapping("/deal")
	public ModelAndView deal(HttpSession session, 
			@SessionAttribute(name = "deck", required = false) Deck deck,
			@RequestParam("betDeal") Integer bet) {
		session.setAttribute("bet", bet);
//		Object d = session.getAttribute("deck");
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
			Long id = (long) 1;
			User user = userDao.findById(id).get();
			user.setBankroll((long) (user.getBankroll() + 1.5*bet));
			userDao.save(user);
			session.setAttribute("user", user);
		}  else {
			Long id = (long) 1;
			User user = userDao.findById(id).get();
			user.setBankroll((user.getBankroll() - bet));
			userDao.save(user);
			session.setAttribute("user", user);
		}
		session.setAttribute("userHand", userHand);
		session.setAttribute("userHandValue", getHandValue(userHand));
		session.setAttribute("dealerHand", dealerHand);
		session.setAttribute("stay", 0);
//		System.out.println("stay before if: " + session.getAttribute("stay"));
		if(userHand.get(0).getValue().equalsIgnoreCase(userHand.get(1).getValue())) {
			session.setAttribute("stay", 3);
		} else if(getHandValue(userHand) == 21){
			session.setAttribute("stay", 4);
		} else {
			session.setAttribute("stay", 2);			
		}
		
		System.out.println("stay deal:" + session.getAttribute("stay"));
		return new ModelAndView("redirect:/game");
	}

	@RequestMapping("/hit")
	public ModelAndView hit(HttpSession session, @SessionAttribute(name = "deck") Deck deck,
			@SessionAttribute(name = "userHand") List<Card> userHand,
			@SessionAttribute(name = "dealerHand") List<Card> dealerHand, 
			@SessionAttribute(name = "bet") Integer bet) {
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
		System.out.println("stay hit:" + session.getAttribute("stay"));
		return new ModelAndView("redirect:/game");
	}

	@RequestMapping("/stay")
	public ModelAndView stay(HttpSession session, 
			@SessionAttribute(name = "userHand") List<Card> userHand,
			@SessionAttribute(name = "dealerHand") List<Card> dealerHand, 
			@SessionAttribute(name = "deck") Deck deck,
			@SessionAttribute(name = "bet") Integer bet) {

		session.setAttribute("userHand", userHand);
		session.setAttribute("stay", 5);
		while (dealerHit(dealerHand) == true) {
			dealerHand.add(a.getCard(deck.getId()));
		}
		if (bust(dealerHand) == true && bust(userHand) == false) {
			Long id = (long) 1;
			User user = userDao.findById(id).get();
			user.setBankroll(user.getBankroll() + 2*bet);
			userDao.save(user);
			session.setAttribute("user", user);
			session.setAttribute("dealerHandValue", "BUST");
		} else if(bust(dealerHand) == false && bust(userHand) == false) {
			session.setAttribute("dealerHandValue", getHandValue(dealerHand));
			if(getHandValue(dealerHand) < getHandValue(userHand)) {
				Long id = (long) 1;
				User user = userDao.findById(id).get();
				user.setBankroll(user.getBankroll() + 2*bet);
				userDao.save(user);
				session.setAttribute("user", user);
			} else if(getHandValue(dealerHand) == getHandValue(userHand)) {
				Long id = (long) 1;
				User user = userDao.findById(id).get();
				user.setBankroll(user.getBankroll() + bet);
				userDao.save(user);
				session.setAttribute("user", user);
			}
		}
		System.out.println("stay stay:" + session.getAttribute("stay"));
		return new ModelAndView("redirect:/game");
	}
	
	@RequestMapping("/double")
	public ModelAndView doubleDown(HttpSession session,
			@SessionAttribute(name="deck") Deck deck,
			@SessionAttribute(name="bet") Integer bet,
			@SessionAttribute(name="userHand") List<Card> userHand) {
		if(deck.getRemaining() <= 12) { 
			deck = a.shuffle(deck);
		}
		userHand.add(a.getCard(deck.getId()));
		
		session.setAttribute("userHand", userHand);
		Integer oldBet = (Integer) session.getAttribute("bet");
		session.setAttribute("bet", oldBet*2);
		Long id = (long) 1;
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
		System.out.println("stay hit:" + session.getAttribute("stay"));
		return new ModelAndView("redirect:/game");
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