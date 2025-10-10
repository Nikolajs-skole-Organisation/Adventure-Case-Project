import { getAllReservations } from "../services/reservationApi.js";
import { clearToken } from "../utils/auth.js";

const dom = {};

window.addEventListener("DOMContentLoaded", init);

async function init() {
  dom.tableBody = document.querySelector("#reservationsTable tbody");
  dom.error = document.querySelector("#error");
  dom.search = document.querySelector("#searchInput");
  dom.logout = document.querySelector("#logoutBtn");

  dom.logout?.addEventListener("click", () => {
    clearToken();
    window.location.assign("login.html");
  });

  dom.search?.addEventListener("input", handleFilter);

  await loadReservations();
}

let cache = [];

async function loadReservations() {
  try {
    dom.error.hidden = true;
    const data = await getAllReservations();
    cache = Array.isArray(data) ? data : (data?.content ?? []); 
    render(cache);
  } catch (err) {
    console.error(err);
    showError("Kunne ikke hente reservationer. Prøv at opdatere siden.");
  }
}

function render(list) {
  dom.tableBody.innerHTML = list.map(r => `
    <tr>
      <td>${safe(r.id)}</td>
      <td>${safe(r.customerName ?? r.name ?? "")}</td>
      <td>${safe(r.email ?? "")}</td>
      <td>${formatDateTime(r.startTime ?? r.start ?? r.from)}</td>
      <td>${formatDateTime(r.endTime ?? r.end ?? r.to)}</td>
      <td>${safe(r.status ?? "")}</td>
    </tr>
  `).join("");
}

function handleFilter() {
  const q = dom.search.value.trim().toLowerCase();
  if (!q) return render(cache);
  const filtered = cache.filter(r => {
    const hay = `${r.id ?? ""} ${r.customerName ?? r.name ?? ""} ${r.email ?? ""}`.toLowerCase();
    return hay.includes(q);
  });
  render(filtered);
}

function formatDateTime(v) {
  if (!v) return "";
  try {
    const d = new Date(v);
    if (isNaN(d.getTime())) return safe(v);
    return d.toLocaleString();
  } catch {
    return safe(v);
  }
}

function showError(msg) {
  dom.error.textContent = msg;
  dom.error.hidden = false;
}

function safe(x) {
  return String(x ?? "").replace(/[&<>"']/g, s => ({
    "&":"&amp;","<":"&lt;",">":"&gt;",'"':"&quot;","'":"&#39;"
  }[s]));
}