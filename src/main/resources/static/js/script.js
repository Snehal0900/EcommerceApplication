// Check if the token exists in cookies
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

// Redirect based on token existence
let token = getCookie("authToken");
if (token) {
    // Redirect to protected page or homepage
    window.location.href = '/home';
} else {
    // Show error or stay on login page
    alert('Login failed, no token received.');
}
