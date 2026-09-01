(async function () {
  const token =
    localStorage.getItem("token") || sessionStorage.getItem("token");

  if (!token) {
    window.location.href = "login.html";
    return;
  }

  try {
    const response = await fetch("http://localhost:8081/auth/me", {
      headers: {
        Authorization: "Bearer " + token,
      },
    });

    console.log("Auth Me Status:", response.status);

    if (!response.ok) {
      window.location.href = "login.html";
      return;
    }

    const user = await response.json();

    if (user.role !== "ADMIN") {
      window.location.href = "403.html";
      return;
    }
  } catch (error) {
    console.error(error);
    window.location.href = "login.html";
  }
})();

/* ==========================
   Profile Dropdown
========================== */

function toggleProfileDropdown() {
  document.getElementById("profileDropdown").classList.toggle("show");
}

document.addEventListener("click", function (e) {
  const wrapper = document.querySelector(".profile-wrapper");

  if (wrapper && !wrapper.contains(e.target)) {
    document.getElementById("profileDropdown")?.classList.remove("show");
  }
});

/* ==========================
   Logout
========================== */

async function logout() {
  const token =
    localStorage.getItem("token") || sessionStorage.getItem("token");

  try {
    await fetch("http://localhost:8081/auth/logout", {
      method: "POST",
      headers: {
        Authorization: "Bearer " + token,
      },
    });
  } catch (error) {
    console.error("Logout Error:", error);
  }

  localStorage.removeItem("token");
  sessionStorage.removeItem("token");

  window.location.href = "login.html";
}
