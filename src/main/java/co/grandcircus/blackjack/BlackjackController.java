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
		System.out.println(session.getAttribute("deck"));
		return m;
	}
	
	@RequestMapping("/game")
	public ModelAndView game(
			@RequestParam(value="id", required=false) String id,
			@SessionAttribute(name="deck", required = false)Deck deck, HttpSession session) {
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
	
	@RequestMapping("/login")
	public ModelAndView showLogin() {
		return new ModelAndView("login-form");
	}
	@RequestMapping("/deal")
	public ModelAndView deal(HttpSession session, @SessionAttribute(name="deck", required = false)Deck deck) {
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
		ModelAndView m = new ModelAndView("game");
		m.addObject("dealerHand", dealerHand);
		m.addObject("userHand", userHand);
//		session.setAttribute("userHand", userHand);
//		session.setAttribute("dealerHand", dealerHand);
//		return new ModelAndView("redirect:/game");
		return m;
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
		
}