import { getActivities } from "../services/activityApi.js";

window.addEventListener("DOMContentLoaded", init);

const dom = {};
let allActivities = [];

async function init() {
  dom.container = document.getElementById("activities-container");
  dom.detailDialog = document.getElementById("activity-dialog");
  dom.resDialog = document.getElementById("reservation-dialog");

  dom.detailDialog.addEventListener("click", (e) => {
    if(e.target == dom.detailDialog) dom.detailDialog.close();
  });

  dom.resDialog.addEventListener("click", (e) => {
    if(e.target === dom.resDialog) dom.resDialog.close();
  });

  await loadActivities();
}

async function loadActivities() {
  try {
    const activities = await getActivities();
    allActivities = Array.isArray(activities) ? activities : [];
    render(allActivities);
  } catch (err) {
    console.error("Failed to load activities:", err);
    allActivities = [];
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
        id,
        name = resolvedName || "Untitled Activity",
        description = "no description available.",
        price,
        minAge,
        minHeight,
        maxParticipants
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
    closeBtn.addEventListener("click", () => dom.detailDialog.close());

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
    addItem("Max participants", maxParticipants);

    const reserveBtn = document.createElement("button");
    reserveBtn.type = "button";
    reserveBtn.textContent = "reserve";
    reserveBtn.className = "res-submit";
    reserveBtn.addEventListener("click", () => {
        dom.detailDialog.close();
        openReservation(activity);
    })

    body.appendChild(header);
    body.appendChild(desc);
    body.appendChild(list);

    const reserveBar = document.createElement("div");
    reserveBar.style.marginTop = "1rem";
    reserveBar.style.display = "flex";
    reserveBar.style.justifyContent = "flex-end";
    reserveBar.appendChild(reserveBtn);
    body.appendChild(reserveBar);

    wrapper.appendChild(media);
    wrapper.appendChild(body);

    dom.detailDialog.innerHTML = "";
    dom.detailDialog.appendChild(wrapper);
    dom.detailDialog.showModal();

    closeBtn.focus();
}

function openReservation(presetActivity){
    const wrapper = document.createElement("div");
    wrapper.className = "res-card";

    const header = document.createElement("div");
    header.className = "res-header";

    const title = document.createElement("h3");
    title.className = "res-title";
    title.textContent = "Reserve Activity";

    const closeBtn = document.createElement("button");
    closeBtn.className = "res-close";
    closeBtn.type = "button";
    closeBtn.setAttribute("aria-label", "close");
    closeBtn.textContent = "X";
    closeBtn.addEventListener("click", () => dom.resDialog.close());

    header.appendChild(title);
    header.appendChild(closeBtn);

    const body = document.createElement("div");
    body.className = "res-body";

    const row1 = document.createElement("div");
    row1.className = "res-row res-row--two";

    const fieldActivity = document.createElement("div");
    fieldActivity.className = "res-field";
    const labelActivity = document.createElement("label");
    labelActivity.textContent = "Activity";
    const selectActivity = document.createElement("select");
    selectActivity.required = true;

    allActivities.forEach(a => {
        const opt = document.createElement("option");
        opt.value = String(a.id ?? a.name);
        opt.textContent = a.name ?? `Activity ${a.id}`;
        selectActivity.appendChild(opt);
    });

    const presetValue = String(presetActivity?.id ?? presetActivity?.name);
    if(presetValue) selectActivity.value = presetValue

    fieldActivity.appendChild(labelActivity);
    fieldActivity.appendChild(selectActivity);

    // Participants dropdown. Can max select the amount allocated to an activity.
    const fieldParticipants = document.createElement("div");
    fieldParticipants.className = "res-field";
    const labelParticipants = document.createElement("label");
    labelParticipants.textContent = "Participants";
    const selectParticipants = document.createElement("select");
    selectParticipants.required = true;
    fieldParticipants.appendChild(labelParticipants);
    fieldParticipants.appendChild(selectParticipants);

    row1.appendChild(fieldActivity);
    row1.appendChild(fieldParticipants);

    // Dates and Times (24-hour format)
    const row2 = document.createElement("div");
    row2.className = "res-row res-row--two";

    // ---- START ----
    const fieldStart = document.createElement("div");
    fieldStart.className = "res-field";
    const labelStart = document.createElement("label");
    labelStart.textContent = "Start date & time";

    // Create separate date and time inputs
    const inputStartDate = document.createElement("input");
    inputStartDate.lang = "da";
    inputStartDate.type = "date";
    inputStartDate.required = true;

    const inputStartTime = document.createElement("input");
    inputStartTime.lang = "da";
    inputStartTime.type = "time";
    inputStartTime.required = true;
    inputStartTime.step = 60; // minute precision

    // Add both inputs to field
    fieldStart.appendChild(labelStart);
    fieldStart.appendChild(inputStartDate);
    fieldStart.appendChild(inputStartTime);

    // ---- END ----
    const fieldEnd = document.createElement("div");
    fieldEnd.className = "res-field";
    const labelEnd = document.createElement("label");
    labelEnd.textContent = "End date & time";

    const inputEndDate = document.createElement("input");
    inputEndDate.lang = "da";
    inputEndDate.type = "date";
    inputEndDate.required = true;

    const inputEndTime = document.createElement("input");
    inputEndTime.lang = "da";
    inputEndTime.type = "time";
    inputEndTime.required = true;
    inputEndTime.step = 60;

    fieldEnd.appendChild(labelEnd);
    fieldEnd.appendChild(inputEndDate);
    fieldEnd.appendChild(inputEndTime);

    row2.appendChild(fieldStart);
    row2.appendChild(fieldEnd);
    
    // Contact information
    const row3 = document.createElement("div");
    row3.className = "res-row res-row--three";

    const fieldName = document.createElement("div");
    fieldName.className = "res-field";
    const labelName = document.createElement("label");
    labelName.textContent = "Contact name";
    const inputName = document.createElement("input");
    inputName.type = "text";
    inputName.required = true;
    fieldName.appendChild(labelName);
    fieldName.appendChild(inputName);

    const fieldPhone = document.createElement("div");
    fieldPhone.className = "res-field";
    const labelPhone = document.createElement("label");
    labelPhone.textContent = "Contact phone";
    const inputPhone = document.createElement("input");
    inputPhone.type = "tel";
    inputPhone.required = true;
    fieldPhone.appendChild(labelPhone);
    fieldPhone.appendChild(inputPhone);

    const fieldEmail = document.createElement("div");
    fieldEmail.className = "res-field";
    const labelEmail = document.createElement("label");
    labelEmail.textContent = "Contact email";
    const inputEmail = document.createElement("input");
    inputEmail.type = "email";
    inputEmail.required = true;
    fieldEmail.appendChild(labelEmail);
    fieldEmail.appendChild(inputEmail);

    row3.appendChild(fieldName);
    row3.appendChild(fieldPhone);
    row3.appendChild(fieldEmail);

    body.appendChild(row1);
    body.appendChild(row2);
    body.appendChild(row3);

   // Footer with total + submit
    const actions = document.createElement("div");
    actions.className = "res-actions";

    const totalEl = document.createElement("div");
    totalEl.className = "res-total";
    totalEl.textContent = "Total: —";

    const submitBtn = document.createElement("button");
    submitBtn.type = "button";
    submitBtn.className = "res-submit";
    submitBtn.textContent = "Confirm reservation";

    actions.appendChild(totalEl);
    actions.appendChild(submitBtn);

   // Assemble
    wrapper.appendChild(header);
    wrapper.appendChild(body);
    wrapper.appendChild(actions);

    dom.resDialog.innerHTML = "";
    dom.resDialog.appendChild(wrapper);
    dom.resDialog.showModal();

    function getSelectedActivity(){
        const val = selectActivity.value;

        let act = allActivities.find(a => String(a.id) === val);
        if(!act) act = allActivities.find(a => (a.name && String(a.name) === val));
        return act;
    }

    function populateParticipants(){
        const act = getSelectedActivity();
        const max = Math.max(1, Number(act?.maxParticipants ?? 1));
        const current = Number(selectParticipants.value || 1);

        selectParticipants.innerHTML = "";
        for (let i = 1; i <= max; i++){
            const opt = document.createElement("option");
            opt.value = String(i);
            opt.textContent = String(i);
            selectParticipants.appendChild(opt);
        }

        selectParticipants.value = String(Math.min(current || 1, max));
    }

    function updateTotal(){
        const act = getSelectedActivity();
        const price = Number(act?.price ?? 0);
        const qty = Number(selectParticipants.value || 0);
        const total = price * qty;
        totalEl.textContent = `Total: ${formatPrice(total)}`;
    }

    selectActivity.addEventListener("change", () => {
        populateParticipants();
        updateTotal();
    });
    selectParticipants.addEventListener("change", updateTotal);

    populateParticipants();
    updateTotal();

    // Set defaults for start/end date and time
    const pad = (n) => String(n).padStart(2, "0");
    const toDateValue = (d) => `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}`;
    const toTimeValue = (d) => `${pad(d.getHours())}:${pad(d.getMinutes())}`;

    const now = new Date();
    now.setMinutes(0, 0, 0);
    now.setHours(now.getHours() + 1);
    const twoHoursLater = new Date(now.getTime() + 2 * 60 * 60 * 1000);

    inputStartDate.value = toDateValue(now);
    inputEndDate.value = toDateValue(twoHoursLater);
    inputStartTime.value = toTimeValue(now);
    inputEndTime.value = toTimeValue(twoHoursLater);
    

    submitBtn.addEventListener("click", async () => {
        const act = getSelectedActivity();
        if(!act){
            alert("Please select an activity.");
            return;
        }
        const payload = {
            startTime: `${inputStartDate.value}T${inputStartTime.value}`,
            endTime: `${inputEndDate.value}T${inputEndTime.value}`,
            participants: Number(selectParticipants.value),
            contactName: inputName.value.trim(),
            contactPhone: inputPhone.value.trim(),
            contactEmail: inputEmail.value.trim(),
            activityId: act.id ?? null
        };

        if(!payload.startTime || !payload.endTime){
            alert("Please choose start and end time.");
            return;
        }
        if(!payload.contactName || !payload.contactPhone || !payload.contactEmail){
            alert("Please fill in contact details.");
            return;
        }
        if(!payload.activityId){
            alert("Selected activity is missing an id.");
            return;
        }

        try{
            submitBtn.disabled = true;
            const res= await fetch("http://localhost:8080/api/reservation", {
                method: "POST",
                headers: {"Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });

            if(!res.ok){
                const text = await res.text().catch(() => "");
                throw new Error(`Reservation failed (${res.status}): ${text || res.statusText}`);
            }

            const data = await res.json();
            dom.resDialog.close();

            alert(`Reservation created successfully!\n\nYour booking code: ${data.bookingCode}`);
        } catch (err) {
            console.error(err);
            alert("Could not create reservation. Please try again");
        } finally {
            submitBtn.disabled = false;
        }
    });
}

function formatPrice(value){
    if (value === undefined || value === null || isNaN(value)) return "-";
    try{
        return new Intl.NumberFormat("da-DK", { style: "currency", currency: "DKK", maximumFractionDigits: 0 }).format(value);
    }catch {
    return `${value} DKK`;
    }
}

function toLocalDateTimeValue(d){
    const pad = (n) => String(n).padStart(2, "0");
    const yyyy = d.getFullYear();
    const mm = pad(d.getMonth() + 1);
    const dd = pad(d.getDate());
    const hh = pad(d.getHours());
    const mi = pad(d.getMinutes());
    return `${yyyy}-${mm}-${dd}T${hh}:${mi}`;
}
