function getToken() {
  return localStorage.getItem("token") || sessionStorage.getItem("token");
}

function getInitials(name) {
  if (!name) return "U";

  const words = name.trim().split(" ");

  if (words.length >= 2) {
    return (
      words[0][0] +
      words[words.length - 1][0]
    ).toUpperCase();
  }

  return words[0][0].toUpperCase();
}

document.addEventListener("DOMContentLoaded", () => {
  const token = getToken();

  // Protected page
  if (!token) {
    window.location.href = "login.html";
    return;
  }

  const fullName = localStorage.getItem("userName") || "User";
  const firstName = fullName.split(" ")[0];

  const profileName = document.getElementById("profileName");
  const profileInitials = document.getElementById("profileInitials");

  if (profileName) {
    profileName.innerText = firstName;
  }

  if (profileInitials) {
    profileInitials.innerText = getInitials(fullName);
  }
});

document.addEventListener("click", function (e) {
  const btn = document.getElementById("profileBtn");
  const dropdown = document.getElementById("profileDropdown");

  if (!btn || !dropdown) return;

  if (btn.contains(e.target)) {
    dropdown.classList.toggle("show");
  } else {
    dropdown.classList.remove("show");
  }
});

async function logout() {
  try {
    const token = getToken();

    await fetch("http://localhost:8081/auth/logout", {
      method: "POST",
      headers: {
        Authorization: "Bearer " + token,
        "Content-Type": "application/json"
      }
    });

  } catch (error) {
    console.error("Logout Error:", error);
  }

  localStorage.removeItem("token");
  sessionStorage.removeItem("token");
  localStorage.removeItem("userData");
  localStorage.removeItem("userName");

  window.location.href = "login.html";
}