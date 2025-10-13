import {get, post, put, del} from "../utils/fetchUtilsActivity.js";

const BASE_URL = "http://localhost:8080/api";
const ACTIVITIES_URL = BASE_URL + "/activities";

export async function getActivities() {
    return get(ACTIVITIES_URL)
}

export async function createActivity(newActivity) {
    return post(ACTIVITIES_URL, newActivity);
}

export async function updateActivity(id, updatedActivity) {
    return put(`${ACTIVITIES_URL}/${id}`, updatedActivity)
}

export async function deleteActivity(id) {
    console.log("id " + id);

    return del(`${ACTIVITIES_URL}/${id}`);
}