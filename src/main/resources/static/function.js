let betAmount = 0;
let back = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSVhzvnISkrvxN3Mg7uFbwKSmzg-EzcfNjnKKb5Sf90Rm8U5vd";
let front = "${item.image}";
let current = back;

function addFive() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 5;
	betDisplay.innerText = "$" + betAmount;
	if (document.getElementById("betDeal") != null
			&& typeof (document.getElementById("betDeal")) != 'undefined') {
		let betForm = document.getElementById("betDeal");
		betDeal.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\""
				+ betAmount
				+ "\"> <button type=\"submit\" value=\"Submit\">Deal</button>";
	} else {
		let nextBetForm = document.getElementById("nextBet");
		nextBetForm.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\""
				+ betAmount
				+ "\"> <button type=\"submit\" value=\"Submit\">Next</button>";
	}
}

function addTwentyFive() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 25;
	betDisplay.innerText = "$" + betAmount;
	if (document.getElementById("betDeal") != null
			&& typeof (document.getElementById("betDeal")) != 'undefined') {
		let betForm = document.getElementById("betDeal");
		betDeal.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\""
				+ betAmount
				+ "\"> <button type=\"submit\" value=\"Submit\">Deal</button>";
	} else {
		let nextBetForm = document.getElementById("nextBet");
		nextBetForm.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\""
				+ betAmount
				+ "\"> <button type=\"submit\" value=\"Submit\">Next</button>";
	}
}

function addHundred() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 100;
	betDisplay.innerText = "$" + betAmount;
	if (document.getElementById("betDeal") != null
			&& typeof (document.getElementById("betDeal")) != 'undefined') {
		let betForm = document.getElementById("betDeal");
		betDeal.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\""
				+ betAmount
				+ "\"> <button type=\"submit\" value=\"Submit\">Deal</button>";
	} else {
		let nextBetForm = document.getElementById("nextBet");
		nextBetForm.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\""
				+ betAmount
				+ "\"> <button type=\"submit\" value=\"Submit\">Next</button>";
	}
}

function addFiveHundred() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 500;
	betDisplay.innerText = "$" + betAmount;
	if (document.getElementById("betDeal") != null
			&& typeof (document.getElementById("betDeal")) != 'undefined') {
		let betForm = document.getElementById("betDeal");
		betDeal.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\""
				+ betAmount
				+ "\"> <button type=\"submit\" value=\"Submit\">Deal</button>";
	} else {
		let nextBetForm = document.getElementById("nextBet");
		nextBetForm.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\""
				+ betAmount
				+ "\"> <button type=\"submit\" value=\"Submit\">Next</button>";
	}
}

function addThousand() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 1000;
	betDisplay.innerText = "$" + betAmount;
	if (document.getElementById("betDeal") != null
			&& typeof (document.getElementById("betDeal")) != 'undefined') {
		let betForm = document.getElementById("betDeal");
		betDeal.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\""
				+ betAmount
				+ "\"> <button type=\"submit\" value=\"Submit\">Deal</button>";
	} else {
		let nextBetForm = document.getElementById("nextBet");
		nextBetForm.innerHTML = "<input type=\"hidden\" name=\"betDeal\" value=\""
				+ betAmount
				+ "\"> <button type=\"submit\" value=\"Submit\">Next</button>";
	}
}

function flipCard() {
	let facedown = document.getElementByClass("backside");
	facedown.innetHTML = "<th class=\"backside\">${item.image}</th>";
}
