import { get } from "../utils/fetchUtils.js";

const BASE_URL = "http://localhost:8080/api";
const RESERVATIONS_URL = `${BASE_URL}/reservations`;

export async function getAllReservations() {
  return get(RESERVATIONS_URL);
}

export async function getReservationById(id) {
  return get(`${RESERVATIONS_URL}/${id}`);
}