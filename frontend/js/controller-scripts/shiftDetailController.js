import {
  getShiftDetail,
  assignEmployeeToShift,
  unassignEmployeeFromShift,
} from "../services/shiftApi.js";
import { getAllEmployees } from "../services/employeeApi.js";

let dom;
let currentShiftId;
let allEmployees = [];

window.addEventListener("DOMContentLoaded", init);

async function init() {
  dom = mapDom();
  dom.backBtn?.addEventListener("click", handleBackClick);
  dom.assignForm.addEventListener("submit", handleAssignSubmit);

  currentShiftId = readShiftId();
  await loadShiftAndEmployees();

  dom.employees.addEventListener("click", handleEmployeeListClick);
}

function readShiftId() {
  const params = new URLSearchParams(window.location.search);
  return params.get("id");
}

async function loadShiftAndEmployees() {
  const shift = await getShiftDetail(currentShiftId);
  const employees = await getAllEmployees();
  allEmployees = employees;
  renderShift(shift);
  populateEmployeeSelect(shift, employees);
}


function renderShift(shift) {
  dom.title.textContent = shift.name ?? "Shift";
  dom.time.textContent = `${shift.startTime} – ${shift.endTime}`;

  dom.employees.innerHTML = "";
  const employees = shift.employees ?? [];

  if (employees.length === 0) {
    const emptyItem = document.createElement("li");
    emptyItem.textContent = "No employees assigned.";
    dom.employees.appendChild(emptyItem);
    return;
  }

  employees.forEach(({ id, name }) => {
    const li = document.createElement("li");
    li.className = "shift-employee";
    li.dataset.employeeId = id;

    const label = document.createElement("p");
    label.className = "shift-employee__name";
    label.textContent = name;

    const removeBtn = document.createElement("button");
    removeBtn.type = "button";
    removeBtn.className = "shift-employee__remove";
    removeBtn.dataset.action = "remove";
    removeBtn.textContent = "Remove";

    li.append(label, removeBtn);
    dom.employees.appendChild(li);
  });
}

function populateEmployeeSelect(shift, employees) {
  dom.employeeSelect.innerHTML = `<option value="" disabled selected>Choose an employee</option>`;

  (employees ?? []).forEach(({ id, name }) => {
    const alreadyAssigned = (shift.employees ?? []).some((e) => e.id === id);
    if (alreadyAssigned) return;

    const option = document.createElement("option");
    option.value = id;
    option.textContent = name;
    dom.employeeSelect.appendChild(option);
  });
}

async function handleAssignSubmit(event) {
  event.preventDefault();
  dom.assignError.hidden = true;

  const employeeId = dom.employeeSelect.value;
  if (!employeeId) return;

  try {
    await assignEmployeeToShift(currentShiftId, employeeId);
    await loadShiftAndEmployees();
  } catch (error) {
    dom.assignError.textContent =
      error.message || "Could not assign employee.";
    dom.assignError.hidden = false;
  }
}

async function handleEmployeeListClick(event) {
  if (event.target.dataset.action !== "remove") return;

  const employeeItem = event.target.closest("[data-employee-id]");
  if (!employeeItem) return;

  const employeeId = employeeItem.dataset.employeeId;

  if (!confirm("Remove this employee from the shift?")) return;

  await unassignEmployeeFromShift(currentShiftId, employeeId);
  await loadShiftAndEmployees();
}

function mapDom() {
  return {
    title: document.querySelector("#shiftTitle"),
    time: document.querySelector("#shiftTime"),
    employees: document.querySelector("#shiftEmployees"),
    backBtn: document.querySelector("#backToScheduleBtn"),
    assignForm: document.querySelector("#assignEmployeeForm"),
    employeeSelect: document.querySelector("#employeeSelect"),
    assignError: document.querySelector("#assignError"),
  };
}

function handleBackClick() {
  window.location.assign("/webpages/shift-list.html");
}
