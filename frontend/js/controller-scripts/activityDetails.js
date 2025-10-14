
// Map of activity names
const imageMap = {
    "paintball": "../images/paintball.jpg",
    "gokart": "../images/gokart.jpg",
    "bow Combat": "../images/bowcombat.jpg",
    "mini Golf": "../images/minigolf.jpg",
    "sumo Wrestling": "../images/sumowrestling.jpg",
    "laser Tag": "../images/lasertag.jpg"
}



// Read id from the URL
const params = new URLSearchParams(window.location.search);
const id = params.get("id");

async function loadActivity() {
  try {
    const response = await fetch(`http://localhost:8080/api/activities/${id}`);
    if (!response.ok) throw new Error("Could not load activity");

    const activity = await response.json();

    // Fills out the HTML
    document.getElementById("activity-name").textContent = activity.name;
    document.getElementById("activity-description").textContent = activity.description;
    document.getElementById("activity-age").textContent = activity.minAge;
    document.getElementById("activity-height").textContent = activity.minHeight;
    document.getElementById("activity-max").textContent = activity.maxParticipants;
    document.getElementById("activity-price").textContent = activity.price + " EUR";

    // 👉 Set hero image
    const hero = document.getElementById("activity-hero");
    const imgKey = activity.name.trim().toLowerCase();
    hero.style.backgroundImage = `url(${imageMap[imgKey] || '../images/default.jpg'})`;

    hero.style.backgroundImage = `url(${imageMap[activity.name] || '../images/default.jpg'})`;
    hero.style.backgroundSize = "cover";
    hero.style.backgroundPosition = "center";

  } catch (err) {
    console.error(err);
    document.getElementById("activity-name").textContent = "Activity not found";
  }
}

loadActivity();
