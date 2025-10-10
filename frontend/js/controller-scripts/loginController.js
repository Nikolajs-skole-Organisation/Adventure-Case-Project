import { login } from "../services/authApi.js";
import { setLoggedIn } from "../utils/authUtils.js";

const dom = {};

window.addEventListener("DOMContentLoaded", () => {
  dom.form = document.querySelector("#loginForm");
  dom.error = document.querySelector("#loginError");
  dom.form.addEventListener("submit", handleLoginSubmit);
});

async function handleLoginSubmit(e) {
  e.preventDefault();
  dom.error.hidden = true;

  const form = new FormData(dom.form);
  const email = form.get("email");
  const password = form.get("password");

  try {
    const user = await login(email, password); 
    setLoggedIn(user);                         
    const params = new URLSearchParams(window.location.search);
    const returnUrl = params.get("returnUrl") || "reservations.html"; 
    window.location.assign(returnUrl);
  } catch (err) {
    dom.error.textContent = "Forkert email eller password.";
    dom.error.hidden = false;
    console.error(err);
  }
}