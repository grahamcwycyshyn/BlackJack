package co.grandcircus.blackjack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
		gamestate.setNumUser(gamestate.getUsers().size());
		session.setAttribute("gamestate", gamestate);
		User user = userDao.findById(i).get();
		if(gamestate.getPhase() != 0 && gamestate.getUsers().get(gamestate.getUserIndex()).getHands().size() < 2) {
			if(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).getCards().get(0).getValue().equalsIgnoreCase(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).getCards().get(1).getValue())) {
				gamestate.setPhase(2);
				session.setAttribute("gamestate", gamestate);
			}
		}
		ModelAndView m = new ModelAndView("game");
		m.addObject("users", gamestate.getUsers());
		m.addObject("user", user);
		m.addObject("deck", gamestate.getDeck());
		m.addObject("gamestate", gamestate);
		return m;
	}

	@RequestMapping("/login-confirmation")
	public ModelAndView submitSignup(User user, HttpSession session,
			@SessionAttribute(name = "gamestate") GameState gamestate) {
		user.setBankroll((long) 500);
		user.setWins((int) 0);
		user.setLosses((int) 0);
		user.setWinLoss((double) 0);
		userDao.save(user);
		user.setUserIndex(0);
		List<User> users = new ArrayList<>();
		users.add(user);
//		gamestate.getTurn().set(0, false);
		gamestate.setUsers(users);
		gamestate.setUserIndex(0);
		List<Integer> bets = new ArrayList<>();
		bets.add(5);
		gamestate.setBets(bets);
		gamestate.setPhase(0);
		session.setAttribute("gamestate", gamestate);
		ModelAndView mv = new ModelAndView("welcome");
		return mv;
	}

	@RequestMapping("/join")
	public ModelAndView join(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate, User user,
			@RequestParam(name = "name") String name) {
		Long id = user.getId();
		// User user = userDao.findById(id).get();
		user.setName(name);
		user.setBankroll((long) 500);
		user.setWins((int) 0);
		user.setLosses((int) 0);
		user.setUserIndex(gamestate.getUserIndex() + 1);
		userDao.save(user);
		gamestate.getUsers().add(user);
		gamestate.getBets().add(5);
		gamestate.setUserIndex(gamestate.getUserIndex() + 1);
		session.setAttribute("gamestate", gamestate);
		System.out.println(gamestate.getUserIndex());
		return new ModelAndView("redirect:/game");
	}

	public List<Card> dealHand(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate) {
		List<Card> userHand = new ArrayList<>();
		userHand.add(a.getCard(gamestate.getDeck().getId()));
		userHand.add(a.getCard(gamestate.getDeck().getId()));
		return userHand;
	}

	@RequestMapping("/next")
	public ModelAndView next(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate,
			@RequestParam("betDeal") Integer bet) {
		gamestate.getBets().set(gamestate.getUserIndex(), bet);
		System.out.println(bet);
//		gamestate.getBets().set(gamestate.getUsers().size() - gamestate.getUserIndex() -1, bet);
		System.out.println(gamestate.getUserIndex());
		System.out.println(gamestate.getBets().get(gamestate.getUserIndex()));
//		Long id = gamestate.getUsers().get(gamestate.getUserIndex()).getId();
//		User user = userDao.findById(id).get();
//		user.setBankroll((user.getBankroll() - bet));
//		userDao.save(user);
		gamestate.setUserIndex(gamestate.getUserIndex() - 1);
		session.setAttribute("gamestate", gamestate);
		return new ModelAndView("redirect:/game");
	}

	@RequestMapping("/deal")
	public ModelAndView deal(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate,
			@RequestParam("betDeal") Integer bet) {
		gamestate.setUserIndex(gamestate.getUsers().size() - 1);
		gamestate.getBets().set(0, bet);
		gamestate.setPhase(1);
		session.setAttribute("gamestate", gamestate);
		if (gamestate.getDeck().getRemaining() <= 40) {
			Deck deck = gamestate.getDeck();
			a.shuffle(deck);
			gamestate.setDeck(deck);
			session.setAttribute("gamestate", gamestate);
		}
		List<Card> dealerHand = new ArrayList<>();
		dealerHand.add(a.getCard(gamestate.getDeck().getId()));
		dealerHand.add(a.getCard(gamestate.getDeck().getId()));
		gamestate.setDealerHand(dealerHand);
		List<User> users = gamestate.getUsers();
		for (int i = 0; i < gamestate.getUsers().size(); i++) {
//			if(gamestate.getHands() == null) {
			Hand hand = new Hand();// new hand made
			hand.setCards(dealHand(session, gamestate));// deals hand
			hand.setHandValue(getHandValue(hand.getCards()));
			List<Hand> hands = new ArrayList<Hand>();// new empty list of hands
			hands.add(hand);// adds fresh dealt hand to list
			users.get(i).setHands(hands);// Adds list of hands to the first user
			gamestate.setUsers(users);
//				List<Hand> list = new ArrayList<>();
			gamestate.setHands(hands);
//			} else if(gamestate.getHands().size() < gamestate.getUsers().size()) {
//				Hand hand = new Hand();//new hand made
//				hand.setCards(dealHand(session, gamestate));//deals hand
//				hand.setHandValue(getHandValue(hand.getCards()));
//				List<Hand> hands = new ArrayList<Hand>();//new empty list of hands
//				hands.add(hand);//adds fresh dealt hand to list
//				users.get(i).setHands(hands);//Adds list of hands to the first user
//				List<Hand> list = new ArrayList<>();
//				gamestate.setHands(hands);
//			} else {
//				List<Card> list = dealHand(session, gamestate);
//				gamestate.getHands().set(i, list);
//				gamestate.getHandValues().set(i, getHandValue(list));
//			}
			if (getHandValue(users.get(i).getHands().get(0).getCards()) == 21) {
				Long id = gamestate.getUsers().get(i).getId();
				User user = userDao.findById(id).get();
				user.setBankroll((long) (user.getBankroll() + 1.5 * gamestate.getBets().get(i)));
				user.setWins(user.getWins() + 1);
				userDao.save(user);
				gamestate.getUsers().get(i).setBankroll(user.getBankroll());
//				stay(session, gamestate);
			} else {
				Long id = gamestate.getUsers().get(i).getId();
				User user = userDao.findById(id).get();
				user.setBankroll((user.getBankroll() - gamestate.getBets().get(i)));
//				user.setLosses(user.getLosses() + 1);
				userDao.save(user);
				gamestate.getUsers().get(i).setBankroll(user.getBankroll());
			}
//		List<List<Card>> hands = new ArrayList<>();
//		hands.add(userHand);
//		gamestate.setHands(hands);

			session.setAttribute("gamestate", gamestate);
//			session.setAttribute("userHandValue", getHandValue(users.get(i).getHands().get(0).getCards()));
//			session.setAttribute("stay", 0);
			if (users.get(i).getHands().get(0).getCards().get(0).getValue()
					.equalsIgnoreCase(users.get(i).getHands().get(0).getCards().get(1).getValue())) {
//				gamestate.setPhase(2);
				session.setAttribute("gamestate", gamestate);
			} else if (getHandValue(users.get(i).getHands().get(0).getCards()) == 21) {
				session.setAttribute("stay", 3);
			} else {
				session.setAttribute("stay", 3);
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
		List<User> users = gamestate.getUsers();
		List<Hand> userHands = users.get(gamestate.getUserIndex()).getHands();
		Hand userHand = userHands.get(0);// get is set to zero because there is only one hand
		userHand.getCards().add(a.getCard(gamestate.getDeck().getId()));
		userHand.setHandValue(getHandValue(userHand.getCards()));
		userHands.set(0, userHand);
		users.get(gamestate.getUserIndex()).setHands(userHands);
		gamestate.getUsers().get(gamestate.getUserIndex()).getHands().set(0, userHand);
		gamestate.setPhase(3);
		session.setAttribute("gamestate", gamestate);
		User user = gamestate.getUsers().get(gamestate.getUserIndex());
		if (bust(userHand.getCards()) == true || getHandValue(userHand.getCards()) == 21) {
			stay(session, gamestate);
			}
			
		if (bust(userHand.getCards()) == true) {
			gamestate.getUsers().get(gamestate.getUserIndex()).setLosses(gamestate.getUserIndex() + 1);
			user.setLosses(user.getLosses() + 1);
			gamestate.getUsers().get(gamestate.getUserIndex()).setBankroll(user.getBankroll());;
			user.setBankroll((user.getBankroll() - gamestate.getBets().get(0)));
			gamestate.getUsers().get(gamestate.getUserIndex()).setBankroll(user.getBankroll());;
			userDao.save(user);
		}
		
		if ( getHandValue(userHand.getCards()) == 21) {
			user.setWins(user.getWins() + 1);
			user.setBankroll((user.getBankroll() + gamestate.getBets().get(0)));
			gamestate.getUsers().get(gamestate.getUserIndex()).setBankroll(user.getBankroll());;
			userDao.save(user);
		}

//			session.setAttribute("stay", 1);
//		}
//			if (getHandValue(userHand) == 21) {
//				session.setAttribute("stay", 4);
//			}
//		} else {
//			session.setAttribute("stay", 5);
//		}
		
		return new ModelAndView("redirect:/game");
	}
	
	@RequestMapping("/hitTop")
	public ModelAndView hitTop(HttpSession session, @SessionAttribute(name="gamestate") GameState gamestate,
			@SessionAttribute(name="doubleState") boolean[] doubleState) {
		if (gamestate.getDeck().getRemaining() <= 12) {
			Deck deck = gamestate.getDeck();
			a.shuffle(deck);
			gamestate.setDeck(deck);
			session.setAttribute("gamestate", gamestate);
		}
		List<User> users = gamestate.getUsers();
		List<Hand> userHands = users.get(gamestate.getUserIndex()).getHands();
		Hand userHand = userHands.get(0);// get is set to zero because there is only one hand
		userHand.getCards().add(a.getCard(gamestate.getDeck().getId()));
		userHand.setHandValue(getHandValue(userHand.getCards()));
		users.get(gamestate.getUserIndex()).getHands().set(0, userHand);
		gamestate.getUsers().get(gamestate.getUserIndex()).getHands().set(0, userHand);
		session.setAttribute("doubleState", doubleState);
		session.setAttribute("gamestate", gamestate);
		if((bust(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).getCards()) == true) 
				|| (getHandValue(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).getCards()) == 21)) {
			doubleState[0] = true;
		}
		if(doubleState[0] == true && doubleState[1] == true) {
			stay(session, gamestate);
		}
		return new ModelAndView("redirect:/game");
	}
	
	@RequestMapping("/hitBottom")
	public ModelAndView hitBottom(HttpSession session, @SessionAttribute(name="gamestate") GameState gamestate,
			@SessionAttribute(name="doubleState") boolean[] doubleState) {
		if (gamestate.getDeck().getRemaining() <= 12) {
			Deck deck = gamestate.getDeck();
			a.shuffle(deck);
			gamestate.setDeck(deck);
			session.setAttribute("gamestate", gamestate);
		}
		List<User> users = gamestate.getUsers();
		List<Hand> userHands = users.get(gamestate.getUserIndex()).getHands();
		Hand userHand = userHands.get(1);// get is set to zero because there is only one hand
		userHand.getCards().add(a.getCard(gamestate.getDeck().getId()));
		userHand.setHandValue(getHandValue(userHand.getCards()));
		users.get(gamestate.getUserIndex()).getHands().set(1, userHand);
		gamestate.getUsers().get(gamestate.getUserIndex()).getHands().set(1, userHand);
		session.setAttribute("doubleState", doubleState);
		session.setAttribute("gamestate", gamestate);
		if((bust(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(1).getCards()) == true) 
				|| (getHandValue(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(1).getCards()) == 21)) {
			doubleState[1] = true;
		}
		if(doubleState[0] == true && doubleState[1] == true) {
			stay(session, gamestate);
		}
		return new ModelAndView("redirect:/game");
	}

	@RequestMapping("/stay")
	public ModelAndView stay(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate) {
		gamestate.setPhase(1);
		if (gamestate.getUserIndex() == 0) {
			gamestate.setPhase(0);
			session.setAttribute("gamestate", gamestate);
			for(int j = 0; j < gamestate.getUsers().get(gamestate.getUserIndex()).getHands().size(); j++) {
				saveToDB(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(j).getCards(), gamestate.getUsers().get(gamestate.getUserIndex()));
			}
			gamestate.setUserIndex(gamestate.getUsers().size() - 1);
			while (dealerHit(gamestate.getDealerHand()) == true) {
				gamestate.getDealerHand().add(a.getCard(gamestate.getDeck().getId()));
			}
			session.setAttribute("dealerHandValue", getHandValue(gamestate.getDealerHand()));
			for (int i = 0; i < gamestate.getUsers().size(); i++) {
				if (gamestate.getUsers().get(i).getHands().size() == 1) {
					if (bust(gamestate.getDealerHand()) == true
							&& bust(gamestate.getUsers().get(i).getHands().get(0).getCards()) == false) {
						Long id = gamestate.getUsers().get(i).getId();
						User user = userDao.findById(id).get();
						user.setBankroll(user.getBankroll() + 2 * gamestate.getBets().get(i));
						user.setWins(user.getWins() + 1);
						userDao.save(user);
						gamestate.getUsers().get(i).setBankroll(user.getBankroll());
//					gamestate.getUsers().set(i, user);
						session.setAttribute("gamestate", gamestate);
					} else if (bust(gamestate.getDealerHand()) == false
							&& bust(gamestate.getUsers().get(i).getHands().get(0).getCards()) == false) {
						if (getHandValue(gamestate.getDealerHand()) < getHandValue(
								gamestate.getUsers().get(i).getHands().get(0).getCards())) {
							Long id = gamestate.getUsers().get(i).getId();
							User user = userDao.findById(id).get();
							user.setBankroll(user.getBankroll() + 2 * gamestate.getBets().get(i));
							user.setWins(user.getWins() + 1);
							userDao.save(user);
							gamestate.getUsers().get(i).setBankroll(user.getBankroll());
//						gamestate.getUsers().set(i, user);
							session.setAttribute("gamestate", gamestate);
						} else if (getHandValue(gamestate.getDealerHand()) == getHandValue(
								gamestate.getUsers().get(i).getHands().get(0).getCards())) {
							Long id = gamestate.getUsers().get(i).getId();
							User user = userDao.findById(id).get();
							user.setBankroll(user.getBankroll() + gamestate.getBets().get(i));
							user.setLosses(user.getLosses() + 1);
							userDao.save(user);
							gamestate.getUsers().get(i).setBankroll(user.getBankroll());
//						gamestate.getUsers().set(i, user);
							session.setAttribute("gamestate", gamestate);
						}
					}
				} else {
					if ((bust(gamestate.getDealerHand()) == true
							&& bust(gamestate.getUsers().get(i).getHands().get(0).getCards()) == false
							&& bust(gamestate.getUsers().get(i).getHands().get(1).getCards()) == false)
							|| ((bust(gamestate.getDealerHand()) == false 
							&& bust(gamestate.getUsers().get(i).getHands().get(0).getCards()) == false
							&& bust(gamestate.getUsers().get(i).getHands().get(1).getCards()) == false)
							&& (getHandValue(gamestate.getUsers().get(i).getHands().get(0).getCards()) > getHandValue(gamestate.getDealerHand()))
							&& (getHandValue(gamestate.getUsers().get(i).getHands().get(1).getCards()) > getHandValue(gamestate.getDealerHand())))) {
						Long id = gamestate.getUsers().get(i).getId();
						User user = userDao.findById(id).get();
						user.setBankroll(user.getBankroll() + 2 * gamestate.getBets().get(i));
						user.setWins(user.getWins() + 2);
						userDao.save(user);
						gamestate.getUsers().get(i).setBankroll(user.getBankroll());
//					gamestate.getUsers().set(i, user);
						session.setAttribute("gamestate", gamestate);
					} else if ((bust(gamestate.getUsers().get(i).getHands().get(0).getCards()) == true
							&& bust(gamestate.getUsers().get(i).getHands().get(1).getCards()) == true)
							|| 
							(bust(gamestate.getDealerHand()) == false 
							&&  (getHandValue(gamestate.getUsers().get(i).getHands().get(0).getCards()) < getHandValue(gamestate.getDealerHand())))
							&&  (getHandValue(gamestate.getUsers().get(i).getHands().get(1).getCards()) < getHandValue(gamestate.getDealerHand()))
							) {
						Long id = gamestate.getUsers().get(i).getId();
						User user = userDao.findById(id).get();
						user.setLosses(user.getLosses() + 2);
						userDao.save(user);
						session.setAttribute("gamestate", gamestate);
					} else {
						Long id = gamestate.getUsers().get(i).getId();
						User user = userDao.findById(id).get();
						user.setBankroll(user.getBankroll() + gamestate.getBets().get(i));
						user.setWins(user.getWins() + 1);
						user.setLosses(user.getLosses() + 1);
						userDao.save(user);
						gamestate.getUsers().get(i).setBankroll(user.getBankroll());
					}
				}
			}
		} else {
			User user = gamestate.getUsers().get(gamestate.getUserIndex());
			for(int i = 0; i < user.getHands().size(); i++) {
				saveToDB(user.getHands().get(i).getCards(), user);
			}
			gamestate.setUserIndex(gamestate.getUserIndex()-1);
			session.setAttribute("gamestate", gamestate);
		}
//		if(gamestate.getUserIndex() == 0) {
//			gamestate.setPhase(0);
//			session.setAttribute("gamestate", gamestate);
//		}
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
		
		gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).getCards()
				.add(a.getCard(gamestate.getDeck().getId()));
		gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).setHandValue(
				getHandValue(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).getCards()));
		Integer oldBet = gamestate.getBets().get(gamestate.getUserIndex());
		gamestate.getBets().set(gamestate.getUserIndex(), oldBet * 2);
		Long id = gamestate.getUsers().get(gamestate.getUserIndex()).getId();
		User user = userDao.findById(id).get();
		user.setBankroll(user.getBankroll() - oldBet);
		userDao.save(user);
		gamestate.getUsers().get(gamestate.getUserIndex()).setBankroll(user.getBankroll());
		session.setAttribute("gamestate", gamestate);
		if (bust(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).getCards()) == false) {
			session.setAttribute("stay", 4);
						
			if (getHandValue(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).getCards()) == 21) {
				session.setAttribute("stay", 4);
			}
		} else {
//			session.setAttribute("userHandValue", "BUST!");
			session.setAttribute("stay", 5);
		}
		stay(session, gamestate);
		return new ModelAndView("redirect:/game");
	}
	
	@RequestMapping("/doubleTop")
	public ModelAndView doubleTop(HttpSession session, @SessionAttribute(name="gamestate") GameState gamestate,
			@SessionAttribute(name="doubleState") boolean[] doubleState) {
		if (gamestate.getDeck().getRemaining() <= 12) {
			Deck deck = gamestate.getDeck();
			a.shuffle(deck);
			gamestate.setDeck(deck);
			session.setAttribute("gamestate", gamestate);
		}
		doubleState[0] = true;
		gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).getCards()
			.add(a.getCard(gamestate.getDeck().getId()));
		gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).setHandValue(
				getHandValue(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).getCards()));
		Integer oldBet = gamestate.getBets().get(gamestate.getUserIndex());
		gamestate.getBets().set(gamestate.getUserIndex(), oldBet * (3/2));
		Long id = gamestate.getUsers().get(gamestate.getUserIndex()).getId();
		User user = userDao.findById(id).get();
		user.setBankroll(user.getBankroll() - (oldBet/2));
		userDao.save(user);
		gamestate.getUsers().get(gamestate.getUserIndex()).setBankroll(user.getBankroll());
		session.setAttribute("doubleState", doubleState);
		gamestate.setPhase(7);
		session.setAttribute("gamestate", gamestate);
		if(doubleState[0] == true && doubleState[1] == true) {
			stay(session, gamestate);
		}
		return new ModelAndView("redirect:/game");
	}
	
	@RequestMapping("/doubleBottom")
	public ModelAndView doubleBottom(HttpSession session, @SessionAttribute(name="gamestate") GameState gamestate,
			@SessionAttribute(name="doubleState") boolean[] doubleState) {
		if (gamestate.getDeck().getRemaining() <= 12) {
			Deck deck = gamestate.getDeck();
			a.shuffle(deck);
			gamestate.setDeck(deck);
			session.setAttribute("gamestate", gamestate);
		}
		doubleState[1] = true;
		gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(1).getCards()
			.add(a.getCard(gamestate.getDeck().getId()));
		gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(1).setHandValue(
				getHandValue(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(1).getCards()));
		Integer oldBet = gamestate.getBets().get(gamestate.getUserIndex());
		gamestate.getBets().set(gamestate.getUserIndex(), oldBet * (3/2));
		Long id = gamestate.getUsers().get(gamestate.getUserIndex()).getId();
		User user = userDao.findById(id).get();
		user.setBankroll(user.getBankroll() - (oldBet/2));
		userDao.save(user);
		gamestate.getUsers().get(gamestate.getUserIndex()).setBankroll(user.getBankroll());
		session.setAttribute("doubleState", doubleState);
		gamestate.setPhase(6);
		session.setAttribute("gamestate", gamestate);
		if(doubleState[0] == true && doubleState[1] == true) {
			stay(session, gamestate);
		}
		
		return new ModelAndView("redirect:/game");
	}

	@RequestMapping("/surrender")
	public ModelAndView surrender(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate) {
		Integer oldBet = gamestate.getBets().get(gamestate.getUserIndex());
		gamestate.getBets().set(gamestate.getUserIndex(), oldBet / 2);
		Long id = gamestate.getUsers().get(gamestate.getUserIndex()).getId();
		User user = userDao.findById(id).get();
		user.setBankroll(user.getBankroll() + oldBet / 2);
		user.setLosses(user.getLosses() + 1);
		userDao.save(user);
		gamestate.getUsers().get(gamestate.getUserIndex()).setBankroll(user.getBankroll());
//		gamestate.getUsers().set(0, user);
		session.setAttribute("gamestate", gamestate);
		stay(session, gamestate);
		return new ModelAndView("redirect:/game");
	}
	
	@RequestMapping("/surrenderTop")
	public ModelAndView surrenderTop(HttpSession session, @SessionAttribute(name="gamestate") GameState gamestate,
			@SessionAttribute(name="doubleState") boolean[] doubleState) {
		Integer oldBet = gamestate.getBets().get(gamestate.getUserIndex());
		gamestate.getBets().set(gamestate.getUserIndex(), oldBet * (3 / 4));
		Long id = gamestate.getUsers().get(gamestate.getUserIndex()).getId();
		User user = userDao.findById(id).get();
		user.setBankroll(user.getBankroll() + oldBet * (1 / 4));
		user.setLosses(user.getLosses() + 1);
		userDao.save(user);
		gamestate.getUsers().get(gamestate.getUserIndex()).setBankroll(user.getBankroll());
		doubleState[0] = true;
		session.setAttribute("doubleState", doubleState);
		session.setAttribute("gamestate", gamestate);
		if(doubleState[0] == true && doubleState[1] == true) {
			stay(session, gamestate);
		}
		return new ModelAndView("redirect:/game");
	}
	
	@RequestMapping("/surrenderBottom")
	public ModelAndView surrenderBottom(HttpSession session, @SessionAttribute(name="gamestate") GameState gamestate,
			@SessionAttribute(name="doubleState") boolean[] doubleState) {
		Integer oldBet = gamestate.getBets().get(gamestate.getUserIndex());
		gamestate.getBets().set(gamestate.getUserIndex(), oldBet * (3 / 4));
		Long id = gamestate.getUsers().get(gamestate.getUserIndex()).getId();
		User user = userDao.findById(id).get();
		user.setBankroll(user.getBankroll() + oldBet * (1 / 4));
		user.setLosses(user.getLosses() + 1);
		userDao.save(user);
		gamestate.getUsers().get(gamestate.getUserIndex()).setBankroll(user.getBankroll());
		doubleState[1] = true;
		session.setAttribute("doubleState", doubleState);
		session.setAttribute("gamestate", gamestate);
		if(doubleState[0] == true && doubleState[1] == true) {
			stay(session, gamestate);
		}
		return new ModelAndView("redirect:/game");
	}

	@RequestMapping("/split")
	public ModelAndView split(HttpSession session, @SessionAttribute(name = "gamestate") GameState gamestate) {
		boolean[] doubleState = {false, false};
		session.setAttribute("doubleState", doubleState);
		List<Card> newUserHand = new ArrayList<>();
		Hand hand = new Hand();
		// this line gets the first card of the user's first and only hand
		newUserHand.add(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).getCards().get(1));
		newUserHand.add(a.getCard(gamestate.getDeck().getId()));
		hand.setCards(newUserHand);
		hand.setHandValue(getHandValue(hand.getCards()));
		gamestate.getUsers().get(gamestate.getUserIndex()).getHands().add(hand);
		gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).getCards().set(1,
				a.getCard(gamestate.getDeck().getId()));
		gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).setHandValue(
				getHandValue(gamestate.getUsers().get(gamestate.getUserIndex()).getHands().get(0).getCards()));
		Integer oldBet = gamestate.getBets().get(gamestate.getUserIndex());
		gamestate.getBets().set(gamestate.getUserIndex(), oldBet * 2);
		Long id = gamestate.getUsers().get(gamestate.getUserIndex()).getId();
		User user = userDao.findById(id).get();
		user.setBankroll(user.getBankroll() - oldBet);
		userDao.save(user);
		gamestate.getUsers().get(gamestate.getUserIndex()).setBankroll(user.getBankroll());
		gamestate.setPhase(5);
		session.setAttribute("gamestate", gamestate);
		return new ModelAndView("redirect:/game");
	}

	@RequestMapping("/instructions")
	public ModelAndView viewInstructions() {
		ModelAndView m = new ModelAndView("instructions");
		return m;
	}

	
	
	@RequestMapping("/leaderBoard")
	public ModelAndView winleader() {
		List<User> mostWins = userDao.findAll();
		List<User> highBankroll = userDao.findAll();
		Collections.sort(mostWins, new User());
		Collections.reverse(mostWins);
		Collections.sort(highBankroll, new User());
		Collections.reverse(highBankroll);
		ModelAndView mv = new ModelAndView("leaderboard");
		mv.addObject("mostwins", mostWins);
		mv.addObject("highBankroll", highBankroll);
        return mv;
	}

	@RequestMapping("/lastHands")
	public ModelAndView lastfive(HttpSession session,
			@SessionAttribute(name = "gamestate", required = false) GameState gamestate) {
		ModelAndView mv = new ModelAndView("lastfive");
		Long id = gamestate.getUsers().get(gamestate.getUserIndex()).getId();// grabs user id from the session
		List<Card> hand1 = handToCardList(getLastHand5(id));// creates the list of cards of the most recent hand
		mv.addObject("hand1", hand1);// adds it to the model and view
		List<Card> hand2 = handToCardList(getLastHand4(id));
		if(hand2 != null) {
			mv.addObject("hand2", hand2);			
		}
		List<Card> hand3 = handToCardList(getLastHand3(id));
		if(hand3 != null) {			
			mv.addObject("hand3", hand3);
		}
		List<Card> hand4 = handToCardList(getLastHand2(id));
		if(hand4 != null) {
			mv.addObject("hand4", hand4);
		}
		List<Card> hand5 = handToCardList(getLastHand1(id));
		if(hand5 != null) {
			mv.addObject("hand5", hand5);
		}
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
		if(hand == null) {
			return null;
		}
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
		List<Hand> hands = handDao.findTopByUserIdOrderByHandIdDesc(id);// grabs the last 5 hands from table
		if(hands.size() > 0) {
			return hands.get(0);// returns the most recent hand
		} else {
			return null;
		}
	}

	public Hand getLastHand4(Long id) {
		List<Hand> hands = handDao.findFirst5ByUserIdOrderByHandIdDesc(id);
		if(hands.size() > 1) {
			return hands.get(1);// returns second most recent hand
		} else {
			return null;
		}
		
	}

	public Hand getLastHand3(Long id) {
		List<Hand> hands = handDao.findFirst5ByUserIdOrderByHandIdDesc(id);
		if(hands.size() > 2) {
			return hands.get(2);// returns second most recent hand
		} else {
			return null;
		}
	}

	public Hand getLastHand2(Long id) {
		List<Hand> hands = handDao.findFirst5ByUserIdOrderByHandIdDesc(id);
		if(hands.size() > 3) {
			return hands.get(3);// returns second most recent hand
		} else {
			return null;
		}
	}

	public Hand getLastHand1(Long id) {
		List<Hand> hands = handDao.findFirst5ByUserIdOrderByHandIdDesc(id);
		if(hands.size() > 4) {
			return hands.get(4);// returns second most recent hand
		} else {
			return null;
		}
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