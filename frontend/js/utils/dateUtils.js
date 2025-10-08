const DAY_MS = 86_400_000;

export function getWeekStart(date = new Date()) {
  const d = new Date(date);
  d.setHours(0, 0, 0, 0);      
  const weekday = d.getDay() || 7;
  d.setDate(d.getDate() - (weekday - 1));
  return d;
}

export function startOfCurrentWeek() {
  return getWeekStart(new Date());
}

export function moveWeek(date, offset) {
  const d = new Date(date);
  d.setDate(d.getDate() + offset * 7);
  return getWeekStart(d);
}

export function formatWeekday(date) {
  return date.toLocaleDateString(undefined, { weekday: "short" });
}

export function formatWeekRange(weekStart) {
  const start = getWeekStart(weekStart);
  const end = new Date(start);
  end.setDate(end.getDate() + 6);
  const displayFormat = { month: "short", day: "numeric" };
  return `${start.toLocaleDateString(undefined, displayFormat)} – ${end.toLocaleDateString(undefined, displayFormat)}`;
}

export function prepareWeekData(raw, weekStart) {
  const start = getWeekStart(weekStart);
  const days = Array.from({ length: 7 }, (_, i) => {
    const date = new Date(start);
    date.setDate(start.getDate() + i);
    return {
      date,
      key: toLocalKey(date),
      label: formatWeekday(date),
      shifts: []
    };
  });

  const shifts = normalise(raw);
  shifts.forEach((shift) => {
    const shiftStart = new Date(shift.startTime);
    const dayIndex = Math.floor((startOfDay(shiftStart) - startOfDay(start)) / DAY_MS);
    if (dayIndex >= 0 && dayIndex < 7) {
      days[dayIndex].shifts.push({
        ...shift,
        startTime: shiftStart,
        endTime: new Date(shift.endTime)
      });
    }
  });

  days.forEach((day) => day.shifts.sort((a, b) => a.startTime - b.startTime));
  return days;
}

// ----- internal helpers -----
function normalise(raw) {
  if (!raw) return [];
  if (Array.isArray(raw)) return raw;
  if (Array.isArray(raw.shifts)) return raw.shifts;

  if (Array.isArray(raw.days)) {
    const allShifts = [];
    for (const day of raw.days) {
      const dayShifts = day.shifts || [];
      for (const shift of dayShifts) {
        allShifts.push({
          ...shift,
          startTime: shift.startTime ?? day.date,
          endTime: shift.endTime ?? day.date
        });
      }
    }
    return allShifts;
  }

  return [];
}


function startOfDay(date) {
  const d = new Date(date);
  d.setHours(0, 0, 0, 0);
  return d;
}

function toLocalKey(date) {
  const y = date.getFullYear();
  const m = String(date.getMonth() + 1).padStart(2, "0");
  const d = String(date.getDate()).padStart(2, "0");
  return `${y}-${m}-${d}`;
}
