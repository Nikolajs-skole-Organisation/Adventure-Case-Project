import { isLoggedIn } from "./authUtils.js";

if (!isLoggedIn()) {
  const here = encodeURIComponent(window.location.pathname);
  window.location.assign(`/webpages/login.html?returnUrl=${here}`);
}