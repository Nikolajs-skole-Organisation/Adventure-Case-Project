import { get, post, del } from "../utils/fetchUtils.js";

const BASE_URL = "http://localhost:8080/api";
const SHIFTS_URL = `${BASE_URL}/shifts`;

export async function getWeeklyShiftOverview(weekStartDate) {
  const iso = weekStartDate.toISOString().slice(0, 10);
  return get(`${SHIFTS_URL}/overview?weekStart=${iso}`);
}

export async function getShiftDetail(shiftId) {
  return get(`${SHIFTS_URL}/${shiftId}`);
}

export async function assignEmployeeToShift(shiftId, employeeId) {
  return post(`${SHIFTS_URL}/${encodeURIComponent(shiftId)}/employees`, {
    employeeId,
  });
}

export async function unassignEmployeeFromShift(shiftId, employeeId) {
  return del(
    `${SHIFTS_URL}/${encodeURIComponent(shiftId)}/employees/${encodeURIComponent(
      employeeId
    )}`
  );
}
