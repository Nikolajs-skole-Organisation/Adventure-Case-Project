import { getAllEmployees } from "../services/employeeApi.js";

window.addEventListener("DOMContentLoaded", loadEmployees);

async function loadEmployees() {
  const tableBody = document.querySelector("#employeeTableBody");
  if (!tableBody) return;

  try {
    const result = await getAllEmployees();
    const employees = Array.isArray(result)
      ? result
      : result && Array.isArray(result.content)
      ? result.content
      : [];

    if (!employees.length) {
      tableBody.innerHTML =
        '<tr><td colspan="3">No employees found.</td></tr>';
      return;
    }

    tableBody.innerHTML = employees
      .map((employee) => {
        const item = employee || {};
        const name =
          item.name ||
          item.fullName ||
          [item.firstName || "", item.lastName || ""]
            .filter(Boolean)
            .join(" ") ||
          "Unnamed";
        const email = item.email || "-";
        const role = item.role || item.title || "-";
        return `<tr><td>${name}</td><td>${email}</td><td>${role}</td></tr>`;
      })
      .join("");
  } catch (error) {
    console.error(error);
    tableBody.innerHTML =
      '<tr><td colspan="3">Could not load employees.</td></tr>';
  }
}
