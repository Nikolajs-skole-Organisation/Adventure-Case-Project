import { get } from "../utils/fetchUtils.js";

const BASE_URL = "http://localhost:8080/api";
const EMPLOYEES_URL = `${BASE_URL}/employees`;

export async function getAllEmployees() {
  return get(EMPLOYEES_URL);
}