package co.grandcircus.blackjack;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
	public ModelAndView index() {
		Deck deck = a.getDeck();
		ModelAndView m = new ModelAndView("index");
		m.addObject("deck", deck);
		return m;
	}
	
	@RequestMapping("/game")
	public ModelAndView game(
			@RequestParam(value="id", required=true) String id) {
		Long i = (long) 1;
		User user = userDao.findById(i).get();
		ModelAndView m = new ModelAndView("game");
		m.addObject("user", user);
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
	public ModelAndView deal(@RequestParam(value="id") String id,
			HttpSession session) {
		
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(a.getCard(id));
		dealerHand.add(a.getCard(id));
		List<Card> userHand = new ArrayList<>();
		userHand.add(a.getCard(id));
		userHand.add(a.getCard(id));
		session.setAttribute("userHand", userHand);
		session.setAttribute("dealerHand", dealerHand);
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
		
}