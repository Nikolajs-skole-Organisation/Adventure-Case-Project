import { post } from "../utils/fetchUtils.js";

const BASE_URL = "http://localhost:8080/api";
const LOGIN_URL = `${BASE_URL}/employees/login`;

export async function login(email, password) {
  return post(LOGIN_URL, { email, password });
}