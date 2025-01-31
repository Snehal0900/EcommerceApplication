console.log("Login Page");

// Function to retrieve a cookie by name
function getCookie(name) {
	let cookieArr = document.cookie.split(';');
	for (let i = 0; i < cookieArr.length; i++) {
		let cookie = cookieArr[i].trim();
		if (cookie.startsWith(name + "=")) {
			return cookie.substring(name.length + 1);
		}
	}
	return null;
}

// Check if the token exists in cookies
let token = getCookie("authToken");
if (token) {
    console.log("Token found in cookies:", token);
    // Make an authenticated request to /home
    fetch('http://localhost:8080/home', {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + token
        },
        credentials: 'include' // Include cookies in the request
    })
    .then(response => response.json())
    .then(data => console.log("Home response:", data))
    .catch(error => console.error("Error accessing /home:", error));
} 
else {
    console.log("No token found in cookies");
}

function updatePlaceholder() {
	const searchType = document.getElementById("searchType").value;
	const input = document.getElementById("searchValue");

	if (searchType === "name") {
		input.placeholder = "Enter product name";
		input.type = "text";
	} else if (searchType === "minPrice") {
		input.placeholder = "Enter minimum price";
		input.type = "number";
	} else if (searchType === "maxPrice") {
		input.placeholder = "Enter maximum price";
		input.type = "number";
	}
}

document.addEventListener("DOMContentLoaded", function () {
    const quantityInputs = document.querySelectorAll(".quantity-input");

    quantityInputs.forEach(input => {
        input.addEventListener("change", function () {
            const productId = this.dataset.productId;
            const newQuantity = this.value;

            fetch(`/home/buyer/cart/update/${productId}?quantity=${newQuantity}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                }
            })
            .then(response => response.text()) // Or response.json() if your backend sends JSON
            .then(data => {
                if (data.includes("error")) {  // Check for errors in response data
                    console.error("Error updating cart:", data); // You can customize this message
                } else {
                    // Dynamically update the cart on the page with new quantity or success message
                    console.log("Cart updated successfully");
                    location.reload(); // This can reload the page or update part of the DOM as needed
                }
            })
            .catch(error => {
                console.error("Error updating cart:", error);  // Handle fetch errors
            });
        });
    });
});
