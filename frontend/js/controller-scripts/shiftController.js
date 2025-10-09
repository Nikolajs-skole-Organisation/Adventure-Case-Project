import { getWeeklyShiftOverview } from "../services/shiftApi.js";
import {
  getWeekStart,
  startOfCurrentWeek,
  moveWeek,
  formatWeekday,
  prepareWeekData,
  formatWeekRange,
} from "../utils/dateUtils.js";

let dom;
let currentWeekStart = getWeekStart(new Date());

window.addEventListener("DOMContentLoaded", initShiftController);

// ----- Entry Point -----
async function initShiftController() {
  dom = mapDomElements();
  //window.history.replaceState({}, "", "/shifts");
  setupEventListeners();
  await reloadAndRender();
}

// ----- Utility Functions -----
async function reloadAndRender() {
  const overview = await getWeeklyShiftOverview(currentWeekStart);

  const weekData = prepareWeekData(overview, currentWeekStart);
  renderWeek(weekData);
  updateWeekLabel();
}

function setupEventListeners() {
  dom.prevBtn.addEventListener("click", () => handleWeekChange(-1));
  dom.nextBtn.addEventListener("click", () => handleWeekChange(1));
  dom.todayBtn.addEventListener("click", handleTodayClick);
  dom.gridColumns.addEventListener("click", handleShiftClick);
}

// ----- Event Handlers -----
function handleWeekChange(offset) {
  currentWeekStart = moveWeek(currentWeekStart, offset);
  reloadAndRender();
}

function handleTodayClick() {
  currentWeekStart = startOfCurrentWeek();
  reloadAndRender();
}

function handleShiftClick(event){
  const card = event.target.closest(".shift-card");
  if(!card) return;

  navigateToShiftDetail(card.dataset.id);
}

// ----- Rendering -----
function renderWeek(days) {
  renderHeader(days);
  renderColumns(days);
}

function renderHeader(days) {
  dom.gridHeader.innerHTML = "";
  days.forEach((day) => {
    dom.gridHeader.insertAdjacentHTML(
      "beforeend",
      `
        <div class="day-header" data-date="${day.key}">
          <span class="day-name">${formatWeekday(day.date)}</span>
          <span class="day-date">${day.date.getDate()}</span>
        </div>
      `
    );
  });
}

function renderColumns(days) {
  dom.gridColumns.innerHTML = "";
  days.forEach((day) => {
    const shiftsHtml = day.shifts.length
      ? day.shifts.map(renderShiftCard).join("")
      : `<div class="no-shifts">No shifts</div>`;

    dom.gridColumns.insertAdjacentHTML(
      "beforeend",
      `
        <section class="day-column" data-date="${day.key}">
          <header class="day-column-title">${day.label}</header>
          <div class="day-column-body">${shiftsHtml}</div>
        </section>
      `
    );
  });
}

function renderShiftCard(shift) {
  const start = new Date(shift.startTime).toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
  });
  const end = new Date(shift.endTime).toLocaleTimeString([], {
    hour: "2-digit",
    minute: "2-digit",
  });
  const employees = (shift.employees ?? []).map((e) => e.name).join(", ");
  const statusClass = isUnderstaffed(shift) ? "shift-card--understaffed" : "";

  return `
    <article class="shift-card ${statusClass}" data-id="${shift.id}">
      <h4 class="shift-name">${getShiftLabel(shift)}</h4>
      <p class="shift-time">${start} – ${end}</p>
      <p class="shift-employees">${employees || "No employees"}</p>
    </article>
  `;
}

function navigateToShiftDetail(shiftId) {
  const params = new URLSearchParams({ id: shiftId });
  window.location.assign(`/webpages/shift-detail.html?${params}`);
}

function getShiftLabel(shift) {
  if (shift.name && shift.name.trim()) return shift.name;
  const hour = shift.startTime.getHours();
  return hour < 16 ? "Morning shift" : "Evening shift";
}

function isUnderstaffed(shift) {
  if (shift.staffed === false) return true;
  if (shift.staffed === true) return false;

  const required = shift.activities?.length ?? 0;
  const assigned = shift.employees?.length ?? 0;
  if (required > 0) return assigned < required;

  const capacity = Number.isFinite(shift.capacity) ? shift.capacity : 0;
  return capacity > 0 && assigned < capacity;
}

function updateWeekLabel() {
  dom.weekLabel.textContent = formatWeekRange(currentWeekStart);
}

// ----- DOM Helpers -----
function mapDomElements() {
  return {
    weekLabel: document.querySelector("#weekRangeLabel"),
    gridHeader: document.querySelector("#weekGridHeader"),
    gridColumns: document.querySelector("#weekGridColumns"),
    prevBtn: document.querySelector("#prevWeekBtn"),
    nextBtn: document.querySelector("#nextWeekBtn"),
    todayBtn: document.querySelector("#todayBtn"),
  };
}
