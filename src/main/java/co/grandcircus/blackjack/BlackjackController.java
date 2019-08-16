package co.grandcircus.blackjack;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import co.grandcircus.blackjack.ApiService;
import co.grandcircus.blackjack.dao.UserRepository;
import co.grandcircus.blackjack.entity.User;



//Note: need to change bet method when pulling in multiple users, currently only takes in one user
@RestController
public class BlackjackController {
	
	@Autowired
	ApiService a;
	
	@Autowired
	private UserRepository userDao;


	@RequestMapping("/login")
	public ModelAndView showSignup() {
		return new ModelAndView("login-form");
	}
	
	@RequestMapping("/login-confirmation")
	public ModelAndView submitSignup(User user, HttpSession session) {
		
		
		
		userDao.save(user);
		
		session.setAttribute("user", user);
		
		ModelAndView mv = new ModelAndView("welcome");
		return mv;
	}
	
	@RequestMapping("/logout")
	public ModelAndView logout(HttpSession session) {
		session.invalidate();
		
		return new ModelAndView("redirect:/");
	}
		
	@RequestMapping("/bet")
	public ModelAndView bet(@RequestParam(value="bankroll", required=true) Long bet) {
		ModelAndView m = new ModelAndView("index");
		Long id = (long) 1;
		User user = userDao.findById(id).get();
		user.setBankroll(user.getBankroll() - bet);
		userDao.save(user);
		
		
		
	}
		
}