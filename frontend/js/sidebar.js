const sidebarHtml = `
  <nav class="app-sidebar">
    <h1 class="app-sidebar__logo">Adventure Case</h1>
    <ul class="app-sidebar__nav">
      <li><a href="/webpages/dashboard.html" data-route="dashboard">Dashboard</a></li>
      <li><a href="/webpages/shift-list.html" data-route="shift-list">Shifts</a></li>
      <li><a href="/webpages/employee-list.html" data-route="employee-list">Employees</a></li>
      <li><a href="/webpages/adminActivity.html" data-route="activities">Activities</a></li>
      <li><a href="/webpages/reservations.html" data-route="reservations">Reservations</a></li>
    </ul>
  </nav>
`;

export function mountSidebar(targetId, activeRoute) {
  const host = document.getElementById(targetId);
  if (!host) return;

  host.innerHTML = sidebarHtml;
  const activeLink = host.querySelector(`[data-route="${activeRoute}"]`);
  if (activeLink) activeLink.classList.add("is-active");
}
