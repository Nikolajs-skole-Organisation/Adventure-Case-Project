import { getAllReservations } from "../services/reservationsApi.js";
import { clearLogin } from "../utils/authUtils.js";

const dom = {};
let cache = [];

window.addEventListener("DOMContentLoaded", init);

async function init() {
  dom.tableBody = document.querySelector("#reservationsTable tbody");
  dom.error = document.querySelector("#error");
  dom.search = document.querySelector("#searchInput");
  dom.logout = document.querySelector("#logoutBtn");

  dom.logout?.addEventListener("click", () => {
    clearLogin();
    window.location.assign("login.html");
  });

  dom.search?.addEventListener("input", handleFilter);

  await loadReservations();
}

async function loadReservations() {
  try {
    dom.error.hidden = true;
    const data = await getAllReservations();

    if (typeof data === "string" || data == null) {
      cache = [];
      return render(cache, "No reservations today");
    }


    const list = Array.isArray(data) ? data : (data?.content ?? []);

    cache = list.map(r => ({
      id: r.bookingCode ?? "",
      name: r.contactName ?? "",
      email: r.contactEmail ?? "",
      start: r.startTime ?? "",
      end: r.endTime ?? "",
      status: r.confirmed === true ? "Confirmed"
        : r.confirmed === false ? "Pending"
          : "",
      activity: r.activityName ?? ""
    }));

    cache.sort((a, b) => new Date(b.start) - new Date(a.start));
    render(cache);
  } catch (err) {
    console.error(err);
    showError("Kunne ikke hente reservationer. Prøv at opdatere siden.");
  }
}

function render(list, emptyMsg = "No reservations found") {
  if (!list.length) {
    dom.tableBody.innerHTML = `
      <tr>
        <td colspan="6" style="text-align:center; opacity:.8; padding:14px 0;">
          ${emptyMsg}
        </td>
      </tr>`;
    return;
  }

  dom.tableBody.innerHTML = list.map(r => `
    <tr>
      <td>${safe(r.id)}</td>
      <td>${safe(r.name)}</td>
      <td>${safe(r.email)}</td>
      <td>${formatDateTime(r.start)}</td>
      <td>${formatDateTime(r.end)}</td>
      <td>${safe(r.status)}</td>
    </tr>`).join("");
}

/* ---------------- SEARCH ---------------- */
function handleFilter() {
  const q = dom.search.value.trim().toLowerCase();

  if (!q) {
    render(cache);
    return;
  }

  const filtered = cache.filter(r => {
    const haystack = `
      ${r.id} ${r.name} ${r.email} ${r.activity} ${r.status}
    `.toLowerCase();

    return haystack.includes(q);
  });

  if (!filtered.length) {
    render([], "No matching reservations");
  } else {
    render(filtered);
  }
}

/* ---------------- HELPERS ---------------- */
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
    "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
  }[s]));
}
