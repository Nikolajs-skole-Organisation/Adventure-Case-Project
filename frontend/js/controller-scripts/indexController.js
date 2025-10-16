import { getActivities } from "../services/activityApi.js";

window.addEventListener("DOMContentLoaded", init);

const dom = {};

async function init() {
  dom.container = document.getElementById("activities-container");
  await loadActivities();
}

async function loadActivities() {
  try {
    const activities = await getActivities();
    render(activities || []);
  } catch (err) {
    console.error("Failed to load activities:", err);
    render([]);
  }
}

function toFileBase(name) {
  return String(name || "")
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
    .trim()
    .replace(/\s+/g, " ")
    .toLowerCase();
}

function setImageWithFallback(imgEl, basePathNoExt, exts, placeholderSrc) {
  let i = 0;
  const tryNext = () => {
    if (i >= exts.length) {
      imgEl.onerror = null;
      imgEl.src = placeholderSrc;
      return;
    }
    const ext = exts[i++];
    imgEl.src = `${basePathNoExt}.${ext}`;
  };
  imgEl.onerror = tryNext;
  tryNext();
}

function buildCard(activity) {
  const name = activity?.name ?? "Untitled Activity";
  const fileBase = toFileBase(name);
  const imgBasePath = `../images/${encodeURIComponent(fileBase)}`;
  const tryExts = ["webp", "jpg", "jpeg", "png"];

  const card = document.createElement("div");
  card.classList.add("activity-card");

  const img = document.createElement("img");
  img.alt = name;
  img.loading = "lazy";
  setImageWithFallback(img, imgBasePath, tryExts, "../images/default.png");

  const title = document.createElement("p");
  title.textContent = name;

  card.appendChild(img);
  card.appendChild(title);

  return card;
}

function render(list) {
  dom.container.innerHTML = "";

  if (!Array.isArray(list) || list.length === 0) {
    const msg = document.createElement("p");
    msg.textContent = "No activities found.";
    dom.container.appendChild(msg);
    return;
  }

  for (const activity of list) {
    dom.container.appendChild(buildCard(activity));
  }
}
