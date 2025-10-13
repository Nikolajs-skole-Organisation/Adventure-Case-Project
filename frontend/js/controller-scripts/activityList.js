// listActivities.js
import { getActivities } from "../services/activityApi.js";

const imageMap = {
  "paintball": "../../images/paintball.jpg",
  "gokart": "../../images/gokart.jpg",
  "bow combat": "../../images/bowcombat.jpg",
  "mini golf": "../../images/minigolf.jpg",
  "sumo wrestling": "../../images/sumowrestling.jpg",
  "laser tag": "../../images/lasertag.jpg"
};

async function loadActivities() {
  try {
    const activities = await getActivities();
    const container = document.querySelector(".card-list");
    container.innerHTML = ""; // ryd eksisterende kort

    activities.forEach(activity => {
      const imgKey = activity.name.trim().toLowerCase();
      const image = imageMap[imgKey] || "../../images/default.jpg";

      const html = `
        <div class="card">
          <img src="${image}" alt="${activity.name}" class="card-image">
          <div class="overlay">
            <h2><a href="../activity/activity.html?id=${activity.id}">${activity.name}</a></h2>
            <p>${activity.description}</p>
            <p><strong>Price:</strong> ${activity.price} EUR</p>
            <a href="../activity/activity.html?id=${activity.id}" class="btn">Learn more</a>
          </div>
        </div>
      `;
      container.insertAdjacentHTML("beforeend", html);
    });

  } catch (err) {
    console.error("Error loading activities:", err);
    document.querySelector(".card-list").innerHTML = "<p>Error loading activities.</p>";
  }
}

loadActivities();
