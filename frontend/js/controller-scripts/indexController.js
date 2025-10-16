import { getActivities } from "../services/activityApi.js";

window.addEventListener("DOMContentLoaded", init);

const dom = {};

async function init() {
  dom.container = document.getElementById("activities-container");
  dom.dialog = document.getElementById("activity-dialog")
  await loadActivities();

  dom.dialog.addEventListener("click", (e) => {
    const rect = dom.dialog.getBoundingClientRect();
    const inDialog =
        e.clientX >= rect.left && e.clientX <= rect.right &&
        e.clentY >= rect.top && e.clentY <= rect.bottom;
    if (!inDialog) dom.dialog.close();
  });
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
  card.className = "activity-card";
  card.tabIndex = 0;
  card.setAttribute("role", "button")
  card.setAttribute("aria-label", `Open details for ${name}`);

  const img = document.createElement("img");
  img.alt = name;
  img.loading = "lazy";
  setImageWithFallback(img, imgBasePath, tryExts, "../images/default.png");

  const title = document.createElement("p");
  title.textContent = name;

  card.appendChild(img);
  card.appendChild(title);

  const open = () => openDetail(activity, img.src, name);
  card.addEventListener("click", open);
  card.addEventListener("keydown", (e) => {
    if(e.key === "Enter" || e.key === " "){
        e.preventDefault();
        open();
    }
  })

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

function openDetail(activity, resolvedImgSrc, resolvedName){
    const {
        name = resolvedName || "Untitled Activity",
        description = "no description available.",
        price,
        minAge,
        minHeight,
        maxParticipant
    } = activity ?? {};

    const wrapper = document.createElement("div");
    wrapper.className = "detail-card";

    const media = document.createElement("div");
    media.className = "detail-media";
    const img = document.createElement("img");
    img.alt = name;
    img.src = resolvedImgSrc;
    media.appendChild(img);

    const body = document.createElement("div");
    body.className = "detail-body";

    const header = document.createElement("div");
    header.className = "detail-header";

    const title = document.createElement("h2");
    title.className = "detail-title";
    title.textContent = name;

    const closeBtn = document.createElement("button");
    closeBtn.className = "detail-close";
    closeBtn.type = "button";
    closeBtn.setAttribute("aria-label", "Close");
    closeBtn.textContent = "X";
    closeBtn.addEventListener("click", () => dom.dialog.close);

    header.appendChild(title);
    header.appendChild(closeBtn);

    const desc = document.createElement("p");
    desc.className = "detail-desc";
    desc.textContent = description;

    const list = document.createElement("ul");
    list.className = "detail-list";

    const addItem = (label, value, suffix = "") => {
        if(value !== undefined && value !== null && value !== "") {
            const li = document.createElement("li");
            li.textContent = `${label}: ${value}${suffix}`;
            list.appendChild(li);
        }
    };

    addItem("Price", formatPrice(price));
    addItem("Minimum age", minAge, " years");
    addItem("Minimum height", minHeight, " cm");
    addItem("Max participants", maxParticipant);

    body.appendChild(header);
    body.appendChild(desc);
    body.appendChild(list);

    wrapper.appendChild(media);
    wrapper.appendChild(body);

    dom.dialog.innerHTML = "";
    dom.dialog.appendChild(wrapper);
    dom.dialog.showModal();

    closeBtn.focus();
}

function formatPrice(value){
    if (value === undefined || value === null || isNaN(value)) return "-";
    try{
        return new Intl.NumberFormat("da-DK", { style: "currency", currency: "DKK", maximumFractionDigits: 0 }).format(value);
    }catch {
    return `${value} DKK`;
    }
}
