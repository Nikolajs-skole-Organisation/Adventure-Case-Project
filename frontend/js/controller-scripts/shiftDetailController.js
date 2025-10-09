import { getShiftDetail } from "../services/shiftApi.js";

let dom;

window.addEventListener("DOMContentLoaded", init);

async function init() {
  dom = mapDom();
  dom.backBtn?.addEventListener("click", handleBackClick);

  const shiftId = readShiftId();
  const shift = await getShiftDetail(shiftId);
  renderShift(shift);
}

function readShiftId() {
  const params = new URLSearchParams(window.location.search);
  return params.get("id");
}

function renderShift(shift) {
  dom.title.textContent = shift.name ?? "Shift";
  dom.time.textContent = `${shift.startTime} – ${shift.endTime}`;

  const employees = shift.employees || [];
  const employeeNames = employees.length
    ? employees.map(({ name }) => name).join(", ")
    : "No employees assigned.";
  dom.employees.textContent = employeeNames;
}

function mapDom() {
  return {
    title: document.querySelector("#shiftTitle"),
    time: document.querySelector("#shiftTime"),
    employees: document.querySelector("#shiftEmployees"),
    backBtn: document.querySelector("#backToScheduleBtn"),
  };
}

function handleBackClick() {
  if (window.history.length > 1) {
    window.history.back();
  } else {
    window.location.assign("/webpages/shift-list.html");
  }
}
