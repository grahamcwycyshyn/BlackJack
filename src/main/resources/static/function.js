let betAmount = 0;


function addFive() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 5;
	betDisplay.innerText = betAmount;
}

function addTwentyFive() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 25;
	betDisplay.innerText = betAmount;
}

function addHundred() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 100;
	betDisplay.innerText = betAmount;
}

function addFiveHundred() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 500;
	betDisplay.innerText = betAmount;
}

function addThousand() {
	let betDisplay = document.getElementById("betAmount");
	betAmount += 1000;
	betDisplay.innerText = betAmount;
}