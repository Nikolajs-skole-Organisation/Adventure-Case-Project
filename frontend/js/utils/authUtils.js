const KEY_LOGIN = "isLoggedIn";
const KEY_USER = "currentUser";

export function setLoggedIn(userObj) {
  localStorage.setItem(KEY_LOGIN, "true");
  if (userObj) localStorage.setItem(KEY_USER, JSON.stringify(userObj));
}

export function isLoggedIn() {
  return localStorage.getItem(KEY_LOGIN) === "true";
}

export function currentUser() {
  const raw = localStorage.getItem(KEY_USER);
  try { return raw ? JSON.parse(raw) : null; } catch { return null; }
}

export function clearLogin() {
  localStorage.removeItem(KEY_LOGIN);
  localStorage.removeItem(KEY_USER);
}