import {
  createActivity,
  getActivities,
  updateActivity,
  deleteActivity,
} from "../services/activityApi.js";

window.addEventListener("DOMContentLoaded", initApp);

// === Entry Point ===
function initApp() {
  reloadAndRender();
  setupEventListeners();
}

// === Utility Functions ===
async function reloadAndRender() {
  const activities = await getActivities();
  renderTable(activities);
}

function setupEventListeners() {
  const form = document.getElementById("activity-form");
  form.addEventListener("submit", handleFormSubmit);

  const tableBody = document.getElementById("activity-table-body");
  tableBody.addEventListener("click", handleTableClick);
}

// === Handlers ===
async function handleFormSubmit(event) {
  event.preventDefault();
  const form = event.target;
  const fd = new FormData(form);

  const activity = {
    name: fd.get("name"),
    description: fd.get("description"),
    minAge: parseInt(fd.get("minAge")),
    minHeight: parseInt(fd.get("minHeight")),
    maxParticipants: parseInt(fd.get("maxParticipants")),
    price: parseFloat(fd.get("price")),
  };

  if (fd.get("id")) {
    await updateActivity(fd.get("id"), activity);
  } else {
    await createActivity(activity);
  }

  form.reset();
  await reloadAndRender();
}

async function handleTableClick(event) {
  const btn = event.target;

  // Edit
  if (btn.classList.contains("edit-button")) {
    const row = btn.closest("tr");
    fillEditForm({
      id: row.dataset.id,
      name: row.children[1].textContent,
      description: row.children[2].textContent,
      minAge: row.children[3].textContent,
      minHeight: row.children[4].textContent,
      maxParticipants: row.children[5].textContent,
      price: row.children[6].textContent,
    });
  }

  // Delete
  if (btn.classList.contains("delete-button")) {
    const row = btn.closest("tr");
    await deleteActivity(row.dataset.id);
    await reloadAndRender();
  }
}

// === Rendering ===
function renderTable(activities) {
  const tbody = document.getElementById("activity-table-body");
  tbody.innerHTML = "";

  activities.forEach((act) => {
    const html = `
      <tr data-id="${act.id}">
        <td>${act.id}</td>
        <td>${act.name}</td>
        <td>${act.description}</td>
        <td>${act.minAge}</td>
        <td>${act.minHeight}</td>
        <td>${act.maxParticipants}</td>
        <td>${act.price}</td>
        <td>
          <button class="edit-button" type="button">Edit</button>
          <button class="delete-button" type="button">Delete</button>
        </td>
      </tr>`;
    tbody.insertAdjacentHTML("beforeend", html);
  });
}

function fillEditForm(activity) {
  document.getElementById("id").value = activity.id;
  document.getElementById("name").value = activity.name;
  document.getElementById("description").value = activity.description;
  document.getElementById("minAge").value = activity.minAge;
  document.getElementById("minHeight").value = activity.minHeight;
  document.getElementById("maxParticipants").value = activity.maxParticipants;
  document.getElementById("price").value = activity.price;
}
