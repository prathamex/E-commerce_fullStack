function loadUserAvatar() {
    const userData = JSON.parse(
        localStorage.getItem("userData")
    );

    if (!userData || !userData.name) return;

    const parts = userData.name.trim().split(" ");

    let initials = "";

    if (parts.length >= 2) {
        initials =
            parts[0].charAt(0).toUpperCase() +
            parts[1].charAt(0).toUpperCase();
    } else {
        initials =
            parts[0].charAt(0).toUpperCase();
    }

    const avatar = document.getElementById("userAvatar");

    if (avatar) {
        avatar.textContent = initials;
    }
}


document.addEventListener("DOMContentLoaded", loadUserAvatar);